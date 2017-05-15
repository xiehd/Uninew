package com.uninew.auto.phone;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uninew.json.AutoState;
import com.uninew.utils.SPTools;

public class FtpActivity extends Activity implements OnClickListener {

	private TextView top_title_txt;
	private ImageView img_back;
	private SPTools sp;
	private AutoState mAutoState;
	private EditText tfp_edi_ID, tfp_edi_user, tfp_edi_password;
	private Button collect_btn_save, collect_btn_clear;
	private CheckBox ftp_checkbox;
	private TextView title_switch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		setContentView(R.layout.activity_ftp);
		sp = new SPTools(this);
		mAutoState = MainApplication.getmAutoState();
		initView();
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		if (sp != null) {
			tfp_edi_ID.setText(sp.getSharedString("ftp_id"));
			tfp_edi_user.setText(sp.getSharedString("ftp_user"));
			tfp_edi_password.setText(mAutoState.getFTP_password());
		}

	}

	private void initView() {
		// TODO Auto-generated method stub
		top_title_txt = (TextView) findViewById(R.id.top_title_txt);
		top_title_txt.setText("更多");
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		tfp_edi_ID = (EditText) findViewById(R.id.tfp_edi_ID);
		tfp_edi_user = (EditText) findViewById(R.id.tfp_edi_user);
		tfp_edi_password = (EditText) findViewById(R.id.tfp_edi_password);

		collect_btn_save = (Button) findViewById(R.id.collect_btn_save);
		collect_btn_save.setOnClickListener(this);
		collect_btn_clear = (Button) findViewById(R.id.collect_btn_clear);
		collect_btn_clear.setOnClickListener(this);

		ftp_checkbox = (CheckBox) findViewById(R.id.ftp_checkbox);
		ftp_checkbox.setChecked(sp.getSharBoolean("checkbox"));
		
		title_switch = (TextView) findViewById(R.id.title_switch_txtmode);
		title_switch.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.collect_btn_save:
			if (sp == null) {
				return;
			}
			sp.putSharedString("ftp_id", tfp_edi_ID.getText().toString().trim());
			sp.putSharedString("ftp_user", tfp_edi_user.getText().toString().trim());
			sp.putSharedString("ftp_password", tfp_edi_password.getText().toString().trim());
			if (ftp_checkbox.isChecked()) {
				sp.putSharBoolean("checkbox", true);
			} else {
//				sp.putSharBoolean("checkbox", false);
//				sp.putSharedString("ftp_password", "");
			}
			mAutoState.setFTP_id(tfp_edi_ID.getText().toString().trim());
			mAutoState.setFTP_password(tfp_edi_password.getText().toString()
					.trim());
			mAutoState.setFTP_user(tfp_edi_user.getText().toString().trim());
			Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.collect_btn_clear:
			tfp_edi_ID.setText("");
			tfp_edi_user.setText("");
			tfp_edi_password.setText("");
			break;
		default:
			break;
		}
	}

}
