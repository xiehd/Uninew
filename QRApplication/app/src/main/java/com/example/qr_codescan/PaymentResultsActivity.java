package com.example.qr_codescan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qr.utils.JsonContants;
import com.qr.utils.SPTools;

import java.text.DecimalFormat;

public class PaymentResultsActivity extends Activity implements OnClickListener {

	private TextView pay_results_line, pay_results_shift, pay_results_debit,
			pay_results_result, pay_results_faile,title_textview_title;
	private Button title_button_back,title_button_camera;
	private ImageView img_result;
	private SPTools sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_results);
		sp = new SPTools(getApplicationContext());
		Intent intent = this.getIntent();
		initView();
		init(intent.getExtras());
	}

	private void init(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// 显示扫描到的内容
		if(savedInstanceState == null){
//			Toast.makeText(getApplicationContext(), "支付出错",
//					 Toast.LENGTH_SHORT).show();
			Log.e("savedInstanceState:", "数据为空");
			return;
		}
		String[] result = savedInstanceState.getString("result").split("_");
		if (result != null && result.length >= 2) {
			JsonContants.LINE = result[0];// 线路
			JsonContants.SHIFT = result[1];// 班次
			
			//JsonContants.DEBIT = result[2];// 扣款金额
//			double  num2 = Double.valueOf(result[2]).doubleValue();
//			DecimalFormat df2 = new DecimalFormat("0.00");//格式化小数   
//			String s2 = df2.format(num2);//返回的是String类型 
//			JsonContants.DEBIT  = s2;
			
			pay_results_line.setText(JsonContants.LINE);
			pay_results_shift.setText(JsonContants.SHIFT);
			pay_results_debit.setText(JsonContants.DEBIT);

			if (Double.valueOf(JsonContants.DEBIT).doubleValue() > Double
					.valueOf(JsonContants.MONEY).doubleValue()) {
				pay_results_result.setText("缴费不成功!");
				pay_results_result.setTextColor(getResources().getColor(R.color.text_result_faile));
				pay_results_faile.setText("余额不足!");
				pay_results_faile.setVisibility(View.VISIBLE);
				//img_result.setImageDrawable(getResources().getDrawable(R.drawable.fai_imgl));
				img_result.setImageResource(R.drawable.fai_imgl);
			} else {
				pay_results_result.setText("缴费成功!");
				pay_results_result.setTextColor(getResources().getColor(R.color.text_result_success));
				pay_results_faile.setVisibility(View.INVISIBLE);
				double  num = Double.valueOf(JsonContants.MONEY).doubleValue() - Double.valueOf(JsonContants.DEBIT).doubleValue();
				DecimalFormat df = new DecimalFormat("0.00");//格式化小数   
				String s = df.format(num);//返回的是String类型 
				sp.putSharedString("money", String.valueOf(s));
				//img_result.setImageDrawable(getResources().getDrawable(R.drawable.ok_img));
				img_result.setImageResource(R.drawable.ok_img);
				
			}
		} else {
			// Toast.makeText(getApplicationContext(), "支付出错",
			// Toast.LENGTH_SHORT).show();
			pay_results_result.setText("缴费不成功!");
			pay_results_result.setTextColor(getResources().getColor(R.color.text_result_faile));
			pay_results_faile.setText("支付出错!");
			pay_results_faile.setVisibility(View.VISIBLE);
			//img_result.setImageDrawable(getResources().getDrawable(R.drawable.fai_imgl));
			img_result.setImageResource(R.drawable.fai_imgl);
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		pay_results_line = (TextView) findViewById(R.id.pay_results_line);
		
		pay_results_shift = (TextView) findViewById(R.id.pay_results_shift);
		pay_results_debit = (TextView) findViewById(R.id.pay_results_debit);
		pay_results_result = (TextView) findViewById(R.id.pay_results_result);
		pay_results_faile = (TextView) findViewById(R.id.pay_results_faile);
		title_textview_title = (TextView) findViewById(R.id.title_textview_title);
		
		title_button_back = (Button) findViewById(R.id.title_button_back);
		title_button_back.setOnClickListener(this);
		title_button_camera = (Button) findViewById(R.id.title_button_camera);
		title_button_camera.setOnClickListener(this);
		
		title_button_camera = (Button) findViewById(R.id.title_button_camera);
		title_button_camera.setVisibility(View.INVISIBLE);
		title_button_back = (Button) findViewById(R.id.title_button_back);
		title_textview_title = (TextView) findViewById(R.id.title_textview_title);
		title_textview_title.setText("付费结果");
		img_result = (ImageView) findViewById(R.id.img_result);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_button_back:
			finish();
			break;

		default:
			break;
		}
	}

}
