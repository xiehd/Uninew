package com.uninew.mms.protocol;

import android.os.Handler;
import android.os.Message;
import android.os.StatFs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class MCUUpdataManager {
	private static final int MCU_UPDATE=7;
	private static int MCU_UPDATE_SUCCESS=1;
	private static int MCU_UPDATE_FAILURE=0;
	private static int MCU_UPDATE_NOTEXIST=2;
	private static int MCU_UPDATE_LOwversion=3;
	private static int MCU_UPDATE_OVER=4;
	private Handler myhandler;
    private McuUpdatePacket prev_cmd;
    private McuUpdataThread  mThread;
    private static final String SDCARDPATH="/mnt/media_rw/extsd/";
	public MCUUpdataManager(Handler myhandler) {
        this.myhandler=myhandler;
	}
	/***
	 * sd 是否可用 判断容量小于等于0 表示不可以用
	 * 
	 * @return
	 */
	public static boolean isSDEnable() {
		File path = new File("/mnt/media_rw/extsd");
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return blockSize * totalBlocks <= 0 ? false : true;
	}
	public Map<Integer, McuUpdatePacket> readMCUBin(String fileName) {
		if(!isSDEnable()){
			return null;
		}
		FileInputStream fileInputStream=null ;

		Map<Integer, McuUpdatePacket> map = new HashMap<Integer, McuUpdatePacket>();
		try {
			File file = new File(fileName);
			File[] files = file.listFiles();
		
			boolean isFound = false;
			String mSDUpdatePath = "";
			if (files != null) {
				for (File f : files) {
					String name = f.getName();
					if (name.startsWith("Mcu_") && name.endsWith(".bin")) {
//						if(MainApplication.mcu_version==null){
//							MainApplication.mcu_version="";
//						}else{
//							MainApplication.mcu_version+=".bin";
//						}
						if(name.compareTo("")>0){
							isFound = true;
							mSDUpdatePath = f.getAbsolutePath();
							fileInputStream = new FileInputStream(f);
							int length = -1;
							byte[] readbytes = new byte[1024 * 1024];
							int pack_no = 0;
							int total_pack = 0;
							length = fileInputStream.read(readbytes);
							if (length != -1) {
								total_pack = (int) ((length % 224 == 0) ? (length / 224) : (length / 224) + 1);
								for (int i = 0; i < total_pack; i++) {
									pack_no++;
									if (i < total_pack) {
										byte[] per_data = new byte[224];
										System.arraycopy(readbytes, i * 224, per_data, 0, per_data.length);
										McuUpdatePacket packet = new McuUpdatePacket(total_pack, pack_no, per_data);
										//map.put(pack_no, packet);
										//LogTool.logBytes("meij_readBin", packet.tobytes());
									} else {
										byte[] last_data = new byte[length - (total_pack - 1) * 224];
										System.arraycopy(readbytes, i * 224, last_data, 0, last_data.length);
										McuUpdatePacket packet = new McuUpdatePacket(total_pack, pack_no, last_data);
										//map.put(pack_no, packet);
										// LogTool.logBytes("meij_readBin", packet.tobytes());
									}

								}
							}
						}else{
						sendMessage(MCU_UPDATE,MCU_UPDATE_LOwversion);
						mThread.interrupt();

						setMcuUpdateState(mcUpdataState.updataInit);
						}


					}
				}
			}
			//LogTool.logI("MCU", "总包书："+map.size());
			return map;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private boolean isUpdataRun = true;
	private McuUpdataState mcUpdataState;
	private Queue<byte[]> mcuUpdataQueue = new LinkedList<byte[]>();
	private long sendtime;
	private long receivedtime;
    
	public void setSendTime(long sendtime){
		this.sendtime=sendtime;
	}
	public long getSendTime(){
		return this.sendtime;
	}
	/**
	 * 设置接收命令的时间
	 * @param receivedTime 接收时间
	 */
	public void setReceivedTime(long receivedTime){
		this.receivedtime=receivedTime;
	}
	public long getReceivedTime(){
		return this.receivedtime;
	}
	public void setMcuUpdateState(McuUpdataState mcUpdataState) {
		//this.mcUpdataState = mcUpdataState;
	}
	/**
	 * 设置前一条发送的数据包
	 * @param cmd 前一条数据包
	 */
    public void setPreCmd(McuUpdatePacket cmd){
    	//this.prev_cmd=cmd;
    }
    public McuUpdatePacket getPreCmd(){
    	//return this.prev_cmd;
		return null;
    }
    /**
     * 将指令添加到queue中
     * @param datas mcu发来的反馈指令
     */
    public void addMcuUpdataQuene(byte[] datas){
    	synchronized (mcuUpdataQueue) {
    		mcuUpdataQueue.offer(datas);
		}
    	//LogTool.logBytes("---mcuUpdataQueue--", mcuUpdataQueue.peek());
    }
    /**
     * 创建MCU升级线程
     */
    public void createMcuUpdateThread(){
    	//LogTool.logE("meij", "createMcuUpdateThread");
    	if(this.mcUpdataState==McuUpdataState.updataInit){
    		if(mThread!=null){
    			//LogTool.logE("meij", "createMcuUpdateThread1");
    			isUpdataRun=false;
    			mThread.interrupt();
    			mThread=null;
    		}
    		isUpdataRun=true;
    		mThread=new McuUpdataThread();
    		mThread.start();
    		//LogTool.logE("meij", "createMcuUpdateThread2");
    	}else{
    		//LogTool.logE("meij", "createMcuUpdateThread3");
    	}

    	
    }
    
	private class McuUpdataThread extends Thread {
		private Map<Integer, McuUpdatePacket> mcuUpdataMap;
		private int pack_no;
		private int send_time = 0;
        
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (isUpdataRun) {
				switch (mcUpdataState) {
				case updataInit://更新初始化
					pack_no = 1;
					mcuUpdataMap = readMCUBin(SDCARDPATH);//读取mcu升级bin文件
					if(mcuUpdataMap!=null&&!mcuUpdataMap.isEmpty()){
						
						//McuUpdatePacket mcuPacket = mcuUpdataMap.get(pack_no);//发送首包
						//pm.sendMcuUpdataDate(mcuPacket);
						//setPreCmd(mcuPacket);
						setMcuUpdateState(McuUpdataState.updaataWorks);
						setSendTime(System.currentTimeMillis());
						sendMessage(MCU_UPDATE,MCU_UPDATE_SUCCESS);
					}else{
						setMcuUpdateState(McuUpdataState.updataEnd);
					}

					break;
				case updaataWorks:
					if (!mcuUpdataMap.isEmpty() && mcuUpdataQueue != null) {
						long laps=getReceivedTime()-getSendTime();
						//LogTool.logI("SerialPort", "时间间隔："+laps+","+mcuUpdataQueue.isEmpty());
						if (!mcuUpdataQueue.isEmpty()&&laps<1000&&laps>=0) {
							byte[] body =null;
							synchronized (mcuUpdataQueue) {
								body = mcuUpdataQueue.poll();
							}
							sendMcuUpdataData(body);
							setSendTime(System.currentTimeMillis());
						}else if(mcuUpdataQueue.isEmpty()&&laps<-1000&&send_time<=3){//当没收到数据包反馈指令时，主动发三次
							//pm.sendMcuUpdataDate(getPreCmd());
							send_time++;
							//LogTool.logI("SerialPort", "send_time："+send_time);
						}else{
							//LogTool.logI("map", "升级失败");
							//sendMessage(MCU_UPDATE,MCU_UPDATE_FAILURE);
						}
					} else {
					sendMessage(MCU_UPDATE,MCU_UPDATE_NOTEXIST);
					setMcuUpdateState(McuUpdataState.updataEnd);
					}
					break;
				case updataEnd:
					isUpdataRun = false;
					sendMessage(MCU_UPDATE,MCU_UPDATE_OVER);
					break;
				default:
					setMcuUpdateState(McuUpdataState.updataInit);
					isUpdataRun = false;
					break;
				}
			}

		}
/**
 * 发送mcu升级数据包
 * @param datas 接收到反馈的指令
 */
		private void sendMcuUpdataData(byte[] datas) {
			switch (datas[3]) {
			case 0x01:// 成功
				if (datas[2] == datas[1]) {//最后一包
					pack_no = 1;
					//LogTool.logI("map", "升级成功");
					setMcuUpdateState(McuUpdataState.updataEnd);
				
				} else {
					int pre = datas[2];
					pack_no = (int)(pre&0xff) + 1;
				//	LogTool.logI("map", "发送包1:"+pack_no);
					//McuUpdatePacket mcuPacket = mcuUpdataMap.get(pack_no);
					//pm.sendMcuUpdataDate(mcuPacket);
					//setPreCmd(mcuPacket);
				}
				send_time = 0;
				break;
			case 0x00:// 失败
				send_time++;
				if (send_time > 3) {
					send_time=0;
					sendMessage(MCU_UPDATE,MCU_UPDATE_FAILURE);
				} else {//数据包发送失败后，发送三次；
					pack_no = datas[2];
					//pm.sendMcuUpdataDate(mcuUpdataMap.get(pack_no));
					//setPreCmd(mcuUpdataMap.get(pack_no));
				}

				break;
			default:
				break;
			}
		}

	}
	private void sendMessage(int what,int arg1){
		Message message=Message.obtain();
		message.what=what;		message.arg1=arg1;
		myhandler.sendMessage(message);
	}
	public enum McuUpdataState {
		updataInit, updaataWorks, updataEnd
	}

}
