package com.uninew.net.main;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.uninew.net.JT905.bean.P_ParamSet;
import com.uninew.net.JT905.bean.ParamKey;
import com.uninew.net.JT905.comm.client.ClientSendManage;
import com.uninew.net.JT905.comm.client.IClientSendManage;
import com.uninew.net.JT905.comm.common.BaseMessageBean;
import com.uninew.net.R;

import java.util.ArrayList;
import java.util.List;

public class GenerateDataActivity extends Activity {
    private IClientSendManage sendManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_data);
        init();
    }

    private void init() {
        sendManage = new ClientSendManage(this.getApplicationContext());
    }

    private void send(BaseMessageBean msg){
        sendManage.send(msg);
    }




}
