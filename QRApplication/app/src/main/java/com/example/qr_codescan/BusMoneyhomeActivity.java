package com.example.qr_codescan;

import java.text.DecimalFormat;

import com.qr.utils.AnimationTools;
import com.qr.utils.JsonContants;
import com.qr.utils.SPTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BusMoneyhomeActivity extends Activity implements OnClickListener {
	private Button title_button_camera, title_button_back;
	private TextView title_textview_title,bus_setting,bus_money,bus_singmoney;
	private EditText edi_bus_money,edi_bus_singmoney;
	private SPTools sp;
	private LinearLayout lin_home,lin_home_setting;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_money);
		sp = new SPTools(getApplicationContext());
		initView();
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		bus_money.setText(JsonContants.MONEY);
		
		edi_bus_money.setText(JsonContants.MONEY);
		edi_bus_money.setSelection(JsonContants.MONEY.length());
		bus_singmoney.setText(JsonContants.DEBIT);
		
		edi_bus_singmoney.setText(JsonContants.DEBIT);
		edi_bus_singmoney.setSelection(JsonContants.DEBIT.length());
	}

	private void initView() {
		// TODO Auto-generated method stub
		title_button_camera = (Button) findViewById(R.id.title_button_camera);
		title_button_back = (Button) findViewById(R.id.title_button_back);
		title_button_back.setVisibility(View.INVISIBLE);
		title_textview_title = (TextView) findViewById(R.id.title_textview_title);
		title_textview_title.setText("公交付费");
		bus_setting = (TextView) findViewById(R.id.bus_setting);
		bus_setting.setOnClickListener(this);
		title_button_back.setOnClickListener(this);
		title_button_camera.setOnClickListener(this);
		
		lin_home = (LinearLayout) findViewById(R.id.lin_home);
		lin_home_setting = (LinearLayout) findViewById(R.id.lin_home_setting);
		
		bus_money = (TextView) findViewById(R.id.bus_money);
		bus_singmoney = (TextView) findViewById(R.id.bus_singmoney);
		
		edi_bus_money = (EditText) findViewById(R.id.edi_bus_money);
		edi_bus_singmoney = (EditText) findViewById(R.id.edi_bus_singmoney);
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		switch (requestCode) {
//		case JsonContants.SCANNIN_GREQUEST_CODE:
//			if (resultCode == RESULT_OK) {
//				Bundle bundle = data.getExtras();
//				// 显示扫描到的内容
//				String[] result = bundle.getString("result").split("_");
//				if (result != null && result.length > 3) {
//					JsonContants.LINE = result[0];
//					JsonContants.SHIFT = result[1];
//					JsonContants.DEBIT = result[2];
//				} else {
//					Toast.makeText(getApplicationContext(), "支付出错",
//							Toast.LENGTH_SHORT).show();
//				}
//				// 显示图片
//				Bitmap bitmap = (Bitmap) data.getParcelableExtra("bitmap");
//			}
//			break;
//		}
//	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_button_camera:
			Intent intent = new Intent();
			intent.setClass(BusMoneyhomeActivity.this,
					MipcaActivityCapture.class);
			//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//startActivityForResult(intent, JsonContants.SCANNIN_GREQUEST_CODE);
			startActivity(intent);
			break;
		case R.id.bus_setting:
			if(bus_setting.getText().toString().equals(getResources().getString(R.string.bus_setting_set))){
				bus_setting.setText(getResources().getString(R.string.bus_setting_save));
				lin_home_setting.setVisibility(View.VISIBLE);
				lin_home.setVisibility(View.GONE);
				AnimationTools.StartSetAnimation(lin_home_setting, 0, 1, 0, 1);
				init();
			}else{//保存
				
				bus_setting.setText(getResources().getString(R.string.bus_setting_set));
				double  num = Double.valueOf(edi_bus_money.getText().toString()).doubleValue();
				DecimalFormat df = new DecimalFormat("0.00");//格式化小数   
				String s = df.format(num);//返回的是String类型 
				JsonContants.MONEY = s;
				
				
				num = Double.valueOf(edi_bus_singmoney.getText().toString()).doubleValue();
				s  = df.format(num);
				JsonContants.DEBIT = s;
				sp.putSharedString("money", JsonContants.MONEY);
				sp.putSharedString("debit", JsonContants.DEBIT);
				
				
				lin_home_setting.setVisibility(View.GONE);
				lin_home.setVisibility(View.VISIBLE);
				AnimationTools.StartSetAnimation(lin_home, 0, 1, 0, 1);
				init();
				Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JsonContants.MONEY = sp.getSharedString("money");
		JsonContants.DEBIT = sp.getSharedString("debit");
		init();
	}
}
