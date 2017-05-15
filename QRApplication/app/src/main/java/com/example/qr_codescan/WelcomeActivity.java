package com.example.qr_codescan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class WelcomeActivity extends Activity implements OnClickListener {

	private TextView welcome_in;
	Handler hander = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (POSTTIME > 0) {
					POSTTIME--;
					welcome_in.setText("点击进入(" + POSTTIME + ")");
					hander.sendEmptyMessageDelayed(0, 1000);
					if (POSTTIME == 0) {
						hander.removeMessages(0);
						startActivity(new Intent(getApplication(),BusMoneyhomeActivity.class));
						WelcomeActivity.this.finish();
					}
				}
				break;

			default:
				break;
			}
		};
	};
	private int POSTTIME = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		welcome_in = (TextView) findViewById(R.id.welcome_in);
		// hander.postDelayed(new Loading(), 5000);
		welcome_in.setText("点击进入(" + POSTTIME + ")");
		hander.removeMessages(0);
		hander.sendEmptyMessageDelayed(0, 1000);
		welcome_in.setOnClickListener(this);
	}

	class Loading implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			POSTTIME = 5;
			hander.removeMessages(0);
			startActivity(new Intent(getApplication(),
					BusMoneyhomeActivity.class));
			WelcomeActivity.this.finish();

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.welcome_in:
			POSTTIME = -1;
			hander.removeMessages(0);
			startActivity(new Intent(getApplication(),
					BusMoneyhomeActivity.class));
			WelcomeActivity.this.finish();
			break;

		default:
			break;
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		hander.removeMessages(0);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		hander.removeMessages(0);
	}
}
