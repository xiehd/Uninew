package com.uninew.net.Taximeter.protocol;

import android.util.Log;

import com.uninew.net.JT905.common.LogTool;
import com.uninew.net.Taximeter.common.DefineID;
import com.uninew.net.Taximeter.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;


/**
 * 计价器沾包处理
 * Created by Administrator on 2017/11/9.
 */

public class TaxiStickyBagHandle {

    private static final boolean D = true;
    private static final String TAG = "TaxiStickyBagHandle";

    private enum RecieverDataState {
        Start, Received, End
    }

    static byte[] mReceiveDataBuffer; // 接受数据缓存
    static int mReceiveBufferOffset = 0;//
    static RecieverDataState mRecieverState = RecieverDataState.Start;// 接收数据状态
    private short start = 0x00;
    private int length = 0x00;

    private Queue<Byte> mdataQueue = new LinkedList<Byte>();
    private Object dataQueueLock = new Object();
    private boolean isFinish = true;
    private boolean threadWaite = false;

    private IDatasCallBack mDatasCallBack;
    private Thread mThread;// 数据处理线程；
    public DataRunnable dataHandleRunnable = new DataRunnable();

    public TaxiStickyBagHandle(IDatasCallBack mDatasCallBack) {
        this.mDatasCallBack = mDatasCallBack;
        if (mThread == null || mThread.getState() == Thread.State.TERMINATED) {
            mThread = new Thread(dataHandleRunnable);
            mThread.setPriority(Thread.MAX_PRIORITY);
            mThread.setName("TaxiStickyBagHandle");
            mThread.start();
        }
    }

    public class DataRunnable implements Runnable {
        private DataRunnable() {
        }

        public void run() {
            while (true) {
                if (Thread.interrupted()) {
                    return;
                }
                if (!mdataQueue.isEmpty() && isFinish) {
                    // Log.i("xhd", "22222222");
                    dataHandles();
                } else {
                    synchronized (TaxiStickyBagHandle.this) {
                        if (threadWaite) {
                            threadWaite = false;
                            // Log.i("xhd", "333333");
                            TaxiStickyBagHandle.this.notify();
                        }
                    }
                    synchronized (dataQueueLock) {
                        try {
                            // Log.i("xhd", "111111111");
                            dataQueueLock.wait();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
        while (mdataQueue.size() > 0) {
            synchronized (dataQueueLock) {
                x = mdataQueue.peek();
                switch (mRecieverState) {
                    case Start:// 开始
                        if (start == 0x55AA) {// 开始标记位
                            //Log.i("xhd","开始:"+length+","+mReceiveBufferOffset);
                            mReceiveBufferOffset = 0;
                            mReceiveDataBuffer[mReceiveBufferOffset++] = x;
                            mRecieverState = RecieverDataState.Received;
                            mdataQueue.remove();
                        } else {
                            mdataQueue.remove();
                        }
                        break;
                    case Received:// 接收
                        // Log.i("xhd","接收:"+length+","+mReceiveBufferOffset);
                        if (mReceiveBufferOffset < length - 1) {
                            mReceiveDataBuffer[mReceiveBufferOffset++] = x;
                            mdataQueue.remove();
                        } else {
                            if (mReceiveBufferOffset >= 7) {// 结束了
                                mReceiveDataBuffer[mReceiveBufferOffset++] = x;
                                if (receiveProtocol(mReceiveDataBuffer)) {// 协议数据完整
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
     *
     * @return 协议校验成功-true 校验失败-false
     */
    private boolean receiveProtocol(byte[] dates) {
       // Log.i(TAG, "单次数据：length = " + length + ",mReceiveBufferOffset = " + mReceiveBufferOffset);
        // 校验码
        byte checkSum = ProtocolTool.xor(dates, 2, dates.length - 5);
        if (D)
            Log.v(TAG, "校验码1:" + checkSum);
        if (D)
            Log.v(TAG, "校验码2:" + dates[dates.length - 3]);
        //验证校验码
        if (checkSum == dates[dates.length - 3]) { // 验证检验码
            if (D)
                Log.d(TAG, "Check Success ！！！");
            mDatasCallBack.datasCallBack(dates);
        }else {
            if (D)
                Log.d(TAG, "Check Fail ！！！");
            return false;
        }
        return true;
    }

    /**
     * 传入数据
     */

    public synchronized void dataput(byte[] buffer) {

        LogTool.logBytes(TAG, "收到数据:", buffer);
        try {
            if (mdataQueue.size() > 0) {
                Log.i(TAG, "等待");
                threadWaite = true;
                this.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ByteArrayInputStream stream = new ByteArrayInputStream(buffer);
        DataInputStream in = new DataInputStream(stream);
        try {
            start = (short) in.readUnsignedShort();
            if (start == DefineID.TAXI_FLAG) {//协议头
                length = in.readUnsignedShort() + 7;
                Log.i(TAG, "包开始：length = " + length + ",start = " + start);
                if (length <= 215) {//不用走分包流程，节省时间
                    receiveProtocol(buffer);
                    return;
                }
                mReceiveDataBuffer = new byte[length];
            }
            for (int i = 0; i < buffer.length; i++) {
                synchronized (dataQueueLock) {
                    mdataQueue.add(buffer[i]);
                    if (mThread != null) {
                        try {
                            dataQueueLock.notify();
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    public interface IDatasCallBack {
        /**
         * 数据回调
         *
         * @param datas
         */
        void datasCallBack(byte[] datas);
    }
}
