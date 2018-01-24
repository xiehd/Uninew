package com.uninew.net.JT905.tcp;

import android.content.Context;
import android.util.Log;

import com.uninew.net.JT905.util.ByteTools;
import com.uninew.net.JT905.util.TimeTool;
import com.uninew.net.utils.FileText;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class TCPLink {

	private static final boolean D = true;
	private static final String TAG="TCPLink";
	private TCPRunStateEnum SocketState = TCPRunStateEnum.OPEN_FAILURE;
	/** 待发送的指令 */
	private Queue<byte[]> msgBeans = new LinkedList<byte[]>();
	/** 链接的IP地址 */
	private String serverIP = "60.12.11.39";
	/** 链接的端口号 */
	private int serverPort = 15555;
	/** Socket客户端 */
	private Socket socket = null;
	/** Socket输出流 */
	private OutputStream outputStream = null;
	/** Socket输入流 */
	private InputStream inputStream = null;
	/** Socket写线程 */
//	private SocketWriteThread writeThread = null;
	/** Socket读线程 */
	private SocketReadThread readThread = null;
	/**TCP编号，用于区分平台*/
	private int tcpId;

	/*回调接口*/
	private ITcpCallBack tcpCallback;

	public void setTcpBackInterface(ITcpCallBack tcpCallback) {
		this.tcpCallback = tcpCallback;
		Log.e(TAG,"tcpCallback="+this.tcpCallback );
	}

	/**
	 * 构造方法
	 * 
	 */
	public TCPLink(Context mContext,int tcpId) {
//		writeThread = new SocketWriteThread();
//		writeThread.start();
		this.tcpId=tcpId;
//		readThread = new SocketReadThread();
//		readThread.start();
	}

	public void openSocket(String serverIP, int serverPort) {
		try {
			closeSocket();
			this.serverIP = serverIP;
			this.serverPort = serverPort;
			socket = new Socket();
			SocketAddress address = new InetSocketAddress(serverIP, serverPort);
			socket.connect(address,30*1000);
//			socket.setKeepAlive(true);
			socket.setSoTimeout(30* 1000);
			Log.d("yzb", "----------打开链接成功-------");
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			// 初始化线程流
//			writeThread.createStream();
            readThread = new SocketReadThread();
            readThread.start();
			readThread.createStream();
			SocketState = TCPRunStateEnum.OPEN_SUCCESS;
			tcpCallback.onTCPRunState(tcpId,TCPRunStateEnum.OPEN_SUCCESS, TCPLinkErrorEnum.NORMAL);
		} catch (UnknownHostException e) {
			SocketState = TCPRunStateEnum.OPEN_FAILURE;
			tcpCallback.onTCPRunState(tcpId,TCPRunStateEnum.OPEN_FAILURE, TCPLinkErrorEnum.ERROR_IPORPORT);
			e.printStackTrace();
		} catch (IOException e) {
			SocketState = TCPRunStateEnum.OPEN_FAILURE;
			tcpCallback.onTCPRunState(tcpId,TCPRunStateEnum.OPEN_FAILURE, TCPLinkErrorEnum.ERROR_OTHERS);
			e.printStackTrace();
		}
	}

	/**
	 * 关闭Socket链接
	 */
	public void closeSocket() {
		try {
			if (socket != null && socket.isConnected()) {
				socket.close();
			}
			if(readThread != null){
				readThread.stopRead();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送消息
	 * 
	 * @param datas
	 */
	public void sendMsg(byte[] datas) {
		if(D)Log.e(TAG, "socketState:" + SocketState);
		if(outputStream != null && datas != null){
			try {
				if(D)Log.d(TAG, "send:"+ ByteTools.logBytes(datas));
				String log = "send_service_buffer\r\n"+"send_message:"
						+ ByteTools.ByteToStr(datas) + "\t"
						+ TimeTool.timestampTormat2(TimeTool.getCurrentTimestamp()) + "\r\n";
				FileText.writeText(log,true);
				outputStream.write(datas);
				outputStream.flush();
			} catch (IOException e) {
				if(D)Log.e(TAG, "send:出错了");
				try {
					outputStream.close();
					outputStream = null;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * Socket客户端写线程
	 * 
	 * @author Macro
	 * 
	 */
	public class SocketWriteThread extends Thread {
		private byte[] msgBean = null;
		private DataOutputStream dataOutputStream = null;

		public void createStream() {
			if (outputStream != null) {
				dataOutputStream = new DataOutputStream(outputStream);
			}
		}

		public void addMsgBean(byte[] bean) {
			msgBeans.offer(bean);
			synchronized (this) {
				this.notify();
			}
		}

		@Override
		public void run() {
			while (!isInterrupted()) {
				while (dataOutputStream != null && (msgBean = msgBeans.poll()) != null) {
					try {
						if(D)Log.e(TAG, "send:"+ ByteTools.logBytes(msgBean));
						dataOutputStream.write(msgBean);
						dataOutputStream.flush();
					} catch (Exception e) {
						// SocketState = TcpOpenResultEnum.OPEN_FAILURE;
						// tcpCallback
						// .onTCPRunStateder(TcpOpenResultEnum.OPEN_FAILURE);
						try {
							dataOutputStream.close();
							dataOutputStream = null;
							outputStream.close();
							outputStream = null;
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						e.printStackTrace();
					}
					try {
						Thread.sleep(10);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				try {
					synchronized (SocketWriteThread.this) {
						SocketWriteThread.this.wait();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Socket客户端读线程
	 * 
	 * @author Macro
	 * 
	 */
	public class SocketReadThread extends Thread {
		private byte buffer[] = new byte[1024];
		private int length = -1;
		private byte[] datas = null;
		private boolean isRunning = true;

		private SocketReadThread(){
			isRunning = true;
		}

		public void createStream() {
			isRunning = true;
			if(this.getState() == State.WAITING) {
				synchronized (this) {
					this.notify();
				}
			}
		}

		public void stopRead(){
			isRunning = false;
			if(D)Log.d(TAG, "-------ReadThread>>>>>1>>>>>>-----stop:" + isRunning);
			if(this.getState() == State.WAITING) {
				synchronized (this) {
					this.notify();
				}
			}
		}
		@Override
		public void run() {
			while (isRunning) {
				while (inputStream != null) {
					try {
						length = inputStream.read(buffer);
						if(D)Log.d(TAG, "-------Read MSG-----lenght=" + length);
						if (length > 0) {
							datas = Arrays.copyOf(buffer, length);
							if (tcpCallback != null) {
								if(D)Log.e(TAG, "read:"+ByteTools.logBytes(datas));
								String log = "receive_service_buffer" + "\r\nread_message:"
										+ ByteTools.ByteToStr(datas) + "\t"
										+ TimeTool.timestampTormat2(TimeTool.getCurrentTimestamp()) + "\r\n";
								FileText.writeText(log,false);
								tcpCallback.onTCPReceiveDatas(tcpId,datas);
							}
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} else {
							SocketState = TCPRunStateEnum.RUN_STOPED;
							tcpCallback.onTCPRunState(tcpId,TCPRunStateEnum.RUN_STOPED,
									TCPLinkErrorEnum.ERROR_SERVER_CLOSED);
							try {
								inputStream.close();
								inputStream = null;
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					} catch (SocketTimeoutException e) {
//						if(D)Log.e(TAG, "-----SocketTimeoutException----");
//						e.printStackTrace();
					} catch (Exception e) {
						if(D)Log.e(TAG, "-------Read-----Exception");
						e.printStackTrace();
						SocketState = TCPRunStateEnum.RUN_STOPED;
						tcpCallback.onTCPRunState(tcpId,TCPRunStateEnum.RUN_STOPED, TCPLinkErrorEnum.ERROR_SERVER_CLOSED);
						try {
							isRunning = false;
							inputStream.close();
							inputStream = null;
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
				if(isRunning) {
					try {
						synchronized (this) {
							this.wait();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(D)Log.d(TAG, "-------ReadThread>>>>>2>>>>>>>-----stop:" + isRunning);
			SocketState = TCPRunStateEnum.RUN_STOPED;
			if(SocketState != TCPRunStateEnum.RUN_STOPED)
				tcpCallback.onTCPRunState(tcpId,TCPRunStateEnum.RUN_STOPED, TCPLinkErrorEnum.ERROR_HAND_CLOSED);
		}
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public boolean isConnected() {
		if (socket != null && socket.isConnected()) {
			return true;
		} else {
			return false;
		}
	}
}
