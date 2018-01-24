package com.uninew.mms.aidl.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.cookoo.car_terminal.aidl.IMmsSend;
import com.cookoo.car_terminal.aidl.IUninewAppSend;
import com.uninew.mms.McuService;
import com.uninew.mms.aidl.interfaces.Initialize;


/**
 * 
 * @author: xiehd
 */
public class RecorderService extends Service {

	private static RecorderService recorderService = null;
	private static final String TAG = "RecorderService";
	public static byte[] arrs;
	private byte[] konzai;
	private byte[] banzai;
	private byte[] manzai;
	private int TIME = 1000;
	private RemoteCallbackList<IUninewAppSend> callbacklist = new RemoteCallbackList<IUninewAppSend>();

	Handler handler;
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// handler自带方法实现定时器
			try {
				handler.postDelayed(this, TIME);
				// refleshData();
			} catch (Exception e) {
				e.printStackTrace();


			}
		}

	};



	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "RecorderService  onCreate");
		recorderService = this;
		Log.d("xhd", "RecorderService start McuService!!!!");
		Intent mmsService = new Intent(this, McuService.class);
		this.startService(mmsService);
		initData();
	}

	public static RecorderService getService() {

		return recorderService;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "RecorderService  onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind");
		return iBinder;
	}

	private IMmsSend.Stub iBinder = new IMmsSend.Stub() {

		@Override
		public void registerCallback(IUninewAppSend irs) throws RemoteException {
			Log.i(TAG, "绑定aidl=" + irs);
			RecorderServicePolicy.getPolicy().setRecorderSender(irs);
			RecorderServicePolicy.getPolicy().setIsRegister(true);
			Log.i(TAG, "---registerCallback---" + irs);
			if (irs != null) {
				callbacklist.register(irs);
			}
		}

		@Override
		public void unregisterCallback(IUninewAppSend irs) throws RemoteException {
			RecorderServicePolicy.getPolicy().setRecorderSender(null);
			RecorderServicePolicy.getPolicy().setIsRegister(false);

			Log.i(TAG, "--unregisterCallback---" + irs);
			if (irs != null) {
				callbacklist.unregister(irs);
			}
		}

		@Override
		public boolean systemStateNotify(byte state) throws RemoteException {
			return Initialize.getMcuReceiveListener().systemStateNotify(state);
		}

		@Override
		public void mcuVersionNotify(String version) throws RemoteException {
			Initialize.getMcuReceiveListener().mcuVersionNotify(version);
		}


		@Override
		public boolean electricity(byte type, byte electricity) throws RemoteException {
			return Initialize.getMcuReceiveListener().electricity(type,electricity);
		}

		// 物理按键获取 只需要KEY值就行了
		@Override
		public void handleMcuKey(byte key, byte action) throws RemoteException {
			Initialize.getMcuReceiveListener().handleMcuKey(key,action);
		}

		@Override
		public boolean receiveCanDatas(byte[] canDatas) throws RemoteException {
			return Initialize.getMcuReceiveListener().receiveCanDatas(canDatas);
		}

		@Override
		public boolean receiveRS232(byte id, byte[] rs232Datas) throws RemoteException {
			return Initialize.getMcuReceiveListener().receiveRS232(id,rs232Datas);
		}

		@Override
		public boolean receiveRS485(byte id, byte[] rs485Datas) throws RemoteException {
			return Initialize.getMcuReceiveListener().receiveRS485(id,rs485Datas);
		}

		@Override
		public boolean receiveBaudRate(byte id, byte baudRate) throws RemoteException {
			return Initialize.getMcuReceiveListener().receiveBaudRate(id,baudRate);
		}

		@Override
		public boolean receiveIOState(byte id, byte state) throws RemoteException {
			return Initialize.getMcuReceiveListener().receiveIOState(id,state);
		}

		@Override
		public boolean receivePulseSignal(int speed) throws RemoteException {
			return Initialize.getMcuReceiveListener().receivePulseSignal(speed);
		}


	};

	


	private void initData() {

//		handler = new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				// do something
//
//				Message message = handler.obtainMessage(0);
//				// initUI();
//				sendMessageDelayed(message, 1000);
//			}
//		};
//
//		Message message = handler.obtainMessage(0);
//		handler.sendMessageDelayed(message, 1000);

	}

	// handler.removeMessages(0) 结束调用






}
