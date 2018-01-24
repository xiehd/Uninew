package com.uninew.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.uninew.net.JT905.comm.client.ClientReceiveManage;
import com.uninew.net.JT905.comm.client.ClientSendManage;
import com.uninew.net.JT905.comm.client.IClientReceiveListener;
import com.uninew.net.JT905.comm.client.IClientReceiveManage;
import com.uninew.net.JT905.comm.client.IClientSendManage;
import com.uninew.net.JT905.common.IpInfo;
import com.uninew.settings.R;

public class MainActivity extends Activity implements View.OnClickListener{

    private IClientReceiveManage mClientReceiveManage;
    private IClientSendManage mClientSendManage;
    private TextView text_state;
    private EditText edi_text_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_test_layout);

        mClientReceiveManage = new ClientReceiveManage(this);
        mClientSendManage = new ClientSendManage(this);
        mClientReceiveManage.registerConnectStateListener(connectListener);
        text_state = (TextView) findViewById(R.id.text_state);
        edi_text_id = (EditText) findViewById(R.id.edi_text_id);
    }
private int tcpid = 0x00;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_connet1:
                tcpid = Integer.parseInt(edi_text_id.getText().toString().trim());
                IpInfo mIpInfo = new IpInfo(tcpid,"114.55.55.149",8905,"114.55.55.200",8888);
               // IpInfo mIpInfo = new IpInfo(tcpid,"114.55.55.200",8888,"114.55.55.149",8905);
                //Toast.makeText(this,"参数信息："+mIpInfo.toString(),Toast.LENGTH_SHORT).show();
                mClientSendManage.createLink(mIpInfo);
                break;
            case R.id.btn_connet2:
                tcpid = Integer.parseInt(edi_text_id.getText().toString().trim());
                IpInfo mIpInfo2 = new IpInfo(tcpid,"114.55.55.200",8888,"114.55.55.149",8905);
                //Toast.makeText(this,"参数信息："+mIpInfo.toString(),Toast.LENGTH_SHORT).show();
                mClientSendManage.createLink(mIpInfo2);
                break;
            case R.id.btn_disconnet:
                mClientSendManage.disConnectLink(tcpid);
                break;
            case R.id.btn_connet_state:
                tcpid = Integer.parseInt(edi_text_id.getText().toString().trim());
                mClientSendManage.queryLinkState(tcpid);
                break;
            case R.id.btn_setting:
                try {
                    Intent intent = new Intent("com.uninew.settings.SettingActivity");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(intent);
                } catch (Exception e) {
                }
                break;
            case R.id.btn_text_clear:
                text_state.setText("");
                break;
        }
    }

    private IClientReceiveListener.IConnectListener connectListener = new IClientReceiveListener.IConnectListener() {
        @Override
        public void linkStateNotify(IpInfo ipInfo) {
           // Toast.makeText(this,"连接监听："+ipInfo.linkState,Toast.LENGTH_SHORT).show();
            Log.i("xhd",ipInfo.toString());
            StateShow(ipInfo);
        }
    };
    private void StateShow(IpInfo ipInfo){
        String state = "连接监听："+ipInfo.tcpId+"\n状态："+ipInfo.linkState+"；主连接："+ipInfo.isLinkMain+"；主IP："+ipInfo.mainIp
                +"；主端口："+ipInfo.mainPort +"；从IP："+ipInfo.spareIp +"；从端口："+ipInfo.sparePort;
        text_state.setText(state);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mClientReceiveManage != null){
            mClientReceiveManage.unRegisterConnectStateListener();
            mClientReceiveManage = null;
        }
    }
}
