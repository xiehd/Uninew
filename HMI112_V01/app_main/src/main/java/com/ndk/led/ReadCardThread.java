package com.ndk.led;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Arrays;

public class ReadCardThread extends Thread{
	private static final String TAG = "ReadCardThread";
	private static final boolean D = true;
	private static final String Code = "UTF-8";
	private int interval = 10*60*1000;
	// private static int setVehicleNumber=0;
	private long signTime;
	private long signOutTime;
	private Handler mHandler;

	public ReadCardThread(Handler mHandler) {
		super();
		this.mHandler = mHandler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int result = JniLed.LedInit();
		Log.d(TAG,"---result="+result);
		while (true) {
			try {
				Thread.sleep(500);
				byte[] data = JniLed.SendRc522Data();
				if(D)Log.v(TAG, Arrays.toString(data));
				RFIDDataHandle(data);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * RFID
	 * 
	 * @param data
	 */
	int state = 0;
	boolean isSet = true;
	private long unSinge;//

	public void RFIDDataHandle(byte[] data) {
		isSet = true;
		long carID = 0;
		long num = 0;
		byte[] nameData = new byte[12];
		byte[] nameData2 = null;
		carID = byteToInt(data);//
		num = byteToIntId(data);//
		String name = "";//
		boolean checkData = checkData(carID, num);
		Message msg = new Message();
//		msg.what = WHAT_TOAST;
		if (checkData) {
			for (int i = 0; i < nameData.length - 1; i++) {
				if (data[i + 8] != 0) {
					nameData[i] = data[i + 8];
				} else {
					nameData2 = new byte[i];
					for (int j = 0; j < nameData2.length; j++) {
						nameData2[j] = nameData[j];
					}
					break;
				}
			}
			if(nameData2!=null){
				try {
					name = new String(nameData2, Code);
					byte[] bs = name.getBytes("GBK");
					name = new String(bs,"GBK");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Log.d(TAG, "name:"+name+",num:"+num+",carID:"+carID);
			msg.what = 0;
			msg.obj = num+"";
			mHandler.sendMessage(msg);
			//((ReadCardService) service).Readok(name,carID,num);
		} else {
			msg.obj = "信息读取失败,请再次刷卡;";
			msg.what = 0;
//			mHandler.sendMessage(msg);
//			ToastUtils.show(service, ", 1);
			
			//service.startSpeech("信息读取失败,请再次刷卡", 0);
			return;
		}
	}

			
	/**
	 *
	 * @param type 0:
	 * @param driverID
	 */
	private void sendBroadCast(int type, String driverID) {
	
	}
	/**
	 * byte
	 * 
	 * @return
	 */
	private long byteToInt(byte[] b) {
		// return (int) (b[3] & 0xff | (b[2]<<8)|(b[1]<<16)|(b[0]<<24));
		return (long) (b[0] & 0xFF) << 24 | (b[1] & 0xFF) << 16
				| (b[2] & 0xFF) << 8 | (b[3] & 0xFF);//
	}

	private long byteToIntId(byte[] b) {
		// return (int) (b[3] & 0xff | (b[2]<<8)|(b[1]<<16)|(b[0]<<24));
		return (long) (b[4] & 0xFF) << 24 | (b[5] & 0xFF) << 16
				| (b[6] & 0xFF) << 8 | (b[7] & 0xFF);//
	}
	/**
	 *
	 * 
	 * @param carID
	 * @param num
	 */
	private boolean checkData(long carID, long num) {
		boolean result = true;
		if (carID == 0 || num == 0) {
			result = false;
		}
		return result;
	}
	/**
	 *
	 * @param num
	 * @param i
	 * @return
	 */
	private String setNum(long num,int i) {
		String a = String.valueOf(num);
		if (a.length()<=i) {
			int b = i-a.length();
			for (int j = 0; j < b; j++) {
				a = "0"+a;
			}
		}
		return a;
	}
	private void sendMessage(int what,String toast){
		Message message = new Message();
		message.what = what;
		message.obj = toast;
		mHandler.sendMessage(message);
	}
}
