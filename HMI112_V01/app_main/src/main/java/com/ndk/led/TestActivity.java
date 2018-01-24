package com.ndk.led;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 用于调试RFID刷卡功能
 * @author Administrator
 *
 */
public class TestActivity extends Activity{
	private Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.test);
		int result=JniLed.LedInit();
		System.out.println("result="+result);
		//btn=(Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Thread(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						while(true){
							try {
								Thread.sleep(100);
								//String s=JniLed.SendRc522Data();
//								if (!s.equals("error")) {
//									Message msg=new Message();
//									msg.obj="读卡成功！";
//									mHandler.sendMessage(msg);
//								}
//								LogTool.logE("yzb", s);
//								LogTool.logBytes("yzb", s);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
				}.start();
			}
		});
	}
	private Handler mHandler =new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			//ToastUtil.show(TestActivity.this, msg.obj.toString());
		}
		
	};
}
