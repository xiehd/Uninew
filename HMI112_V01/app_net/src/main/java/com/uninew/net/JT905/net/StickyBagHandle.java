package com.uninew.net.JT905.net;

import android.util.Log;

import com.uninew.net.JT905.common.ProtocolTool;
import com.uninew.net.JT905.util.ByteTools;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * JT905粘包处理类
 * Created by Administrator on 2017/8/17 0017.
 */

public class StickyBagHandle {
    private static final boolean D = true;
    private static final String TAG = "TaxiStickyBagHandle";
    private Thread mThread;// 数据处理线程；
    private IDatasCallBack mDatasCallBack;
    private int tcpId=-1;

    public StickyBagHandle(int tcpId, IDatasCallBack mDatasCallBack) {
        this.mDatasCallBack=mDatasCallBack;
        this.tcpId=tcpId;
        if (mThread == null || mThread.getState() == Thread.State.TERMINATED) {
            mThread = new Thread(dataHandleRunnable);
            mThread.setPriority(Thread.MAX_PRIORITY);
            mThread.setName("StickyBagHandle");
            mThread.start();
        }
    }

    private enum RecieverDataState {
        Start, Received, End
    }

    static byte[] mReceiveDataBuffer = new byte[10240]; // 接受数据缓存
    static int mReceiveBufferOffset = 0;//
    static RecieverDataState mRecieverState = RecieverDataState.Start;// 接收数据状态
    public DataRunnable dataHandleRunnable = new DataRunnable();

    public class DataRunnable implements Runnable {
        private DataRunnable() {
        }

        private Queue<Byte> mdataQueue = new LinkedList<Byte>();
        private Object dataQueueLock = new Object();
        private boolean isFinish = true;

        public void run() {
            while (true) {
                if (Thread.interrupted()) {
                    return;
                }
                if (!mdataQueue.isEmpty() && isFinish) {
                    dataHandles();
                } else {
                    synchronized (this) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private synchronized void dataHandles() {
            datahandle();
        }
        private void datahandle() {
            isFinish = false;
            byte x = 0;
            if (mdataQueue.size() > 0) {
                synchronized (dataQueueLock) {
                    x = mdataQueue.peek();
                    switch (mRecieverState) {
                        case Start:// 开始
                            if (x == 0x7e) {// 开始标记位
                                mReceiveBufferOffset = 0;
                                mReceiveDataBuffer[mReceiveBufferOffset++] = x;
                                mRecieverState = RecieverDataState.Received;
                                mdataQueue.remove();
                            } else {
                                if (x != 0x7e) {
                                    mdataQueue.remove();
                                }
                            }
                            break;
                        case Received:// 接收
                            // Log.v(TAG, "mReceiveBufferOffset:"
                            // + mReceiveBufferOffset);
                            if (x != 0x7e) {
                                mReceiveDataBuffer[mReceiveBufferOffset++] = x;
                                mdataQueue.remove();
                            } else {
                                if (mReceiveBufferOffset > 5) {// 结束了
                                    mReceiveDataBuffer[mReceiveBufferOffset++] = x;
                                    if (receiveProtocol()) {// 协议数据完整
                                        mdataQueue.remove();
                                    }
                                    mReceiveBufferOffset = 0;
                                    mRecieverState = RecieverDataState.Start;
                                } else {// 重新开始
                                    mRecieverState = RecieverDataState.Start;
                                }
                            }
                            break;
                        case End:// 结束
                            mReceiveBufferOffset = 0;
                            break;
                        default:
                            break;
                    }
                }
            }
            isFinish = true;
        }

        /**
         * 取出完整协议
         * @return 协议校验成功-true 校验失败-false
         */
        private boolean receiveProtocol() {
            byte[] protocolBytes = Arrays.copyOfRange(mReceiveDataBuffer, 0, mReceiveBufferOffset);
            // LogTool.logBytes("protocolBytes", protocolBytes);
            byte[] data2 = new byte[protocolBytes.length - 2];
            // 去标识位
            for (int i = 0; i < protocolBytes.length - 2; i++) {
                data2[i] = protocolBytes[i + 1];
            }
            byte[] escapeReductions = null;
            if(D)Log.v(TAG, "转义前:"+ ByteTools.logBytes(data2));
            // 转义
            try {
                escapeReductions = ProtocolTool.escapeReduction(data2, 0, data2.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(D)Log.v(TAG, "转义后:"+ByteTools.logBytes(data2));
            // 校验码
            byte checkSum = ProtocolTool.xor(escapeReductions, 0, escapeReductions.length - 1);
            if(D)Log.v(TAG, "校验码:"+checkSum);
            if(D)Log.v(TAG, "校验码2:"+escapeReductions[escapeReductions.length - 1]);
            // 验证校验码
            if (checkSum == escapeReductions[escapeReductions.length - 1]) { // 验证检验码
                 if(D)Log.d(TAG, "Check Success ！！！");
                mDatasCallBack.datasCallBack(tcpId,escapeReductions);
                return true;
            } else {
                if(D) Log.e(TAG, "Check Error！！！");
            }
            return false;
        }

        /**
         * 传入数据
         * @param buffer  数据
         * @param length 长度
         */
        public synchronized void dataput(byte[] buffer, int length) {
            for (int i = 0; i < length; i++) {
                    synchronized (dataQueueLock) {
                        mdataQueue.add(buffer[i]);
                        this.notify();
                    }
            }
        }
    }

    public interface IDatasCallBack{
        /**
         * 数据回调
         * @param datas
         */
        void datasCallBack(int tcpId,byte[] datas);
    }
}
