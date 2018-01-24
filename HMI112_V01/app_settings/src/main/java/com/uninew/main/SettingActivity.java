package com.uninew.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

import com.uninew.adapter.MenuDialogAdapter;
import com.uninew.constant.DefineActivityAction;
import com.uninew.export.Utils;
import com.uninew.net.JT905.comm.client.ClientSendManage;
import com.uninew.net.JT905.common.TimeTool;
import com.uninew.settings.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/29.
 */

public class SettingActivity extends Activity {

    private GridView gridView;
    private List<MenuDialogAdapter.MenuItemData> itemDatas;
    private Context context;
    private MenuDialogAdapter adapter;
    private SettingsApplication app;
    private ClientSendManage mClientSendManage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.avtivity_main);
        context = this;
        app = (SettingsApplication) Utils.getApp();
        initView();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.menu_gridvew);
        itemDatas = new ArrayList<>();
        //系统设置
        MenuDialogAdapter.MenuItemData setting = new MenuDialogAdapter.MenuItemData(R.drawable.menu_setting_selector, context.getResources().getString(R.string.more_system_setting));
        itemDatas.add(setting);
        //通讯设置
        MenuDialogAdapter.MenuItemData communication = new MenuDialogAdapter.MenuItemData(R.drawable.menu_mainance_selector, context.getResources().getString(R.string.more_communtion_setting));
        itemDatas.add(communication);
        //基础设置
        MenuDialogAdapter.MenuItemData mainTain = new MenuDialogAdapter.MenuItemData(R.drawable.menu_manger_selector, context.getResources().getString(R.string.more_set_setting));
        itemDatas.add(mainTain);
        //设备维护
        MenuDialogAdapter.MenuItemData state = new MenuDialogAdapter.MenuItemData(R.drawable.menu_alarm_selector, context.getResources().getString(R.string.more_device_miantain));
        itemDatas.add(state);
        //参数查看
        MenuDialogAdapter.MenuItemData param = new MenuDialogAdapter.MenuItemData(R.drawable.menu_param_selector, context.getResources().getString(R.string.app_paramActivity));
        itemDatas.add(param);

        adapter = new MenuDialogAdapter(context, itemDatas);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
//                    //系统设置
                    case 0:
                        // startThisActivity(DefineActivityAction.Action_SystemActivity);
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setClassName("carnetapp.settings", "settings.view.activity.Main");
                            startActivity(intent);
                        } catch (Exception e) {
                        }
                        break;
                    //设备维护
                    case 1:
                        startThisActivity(DefineActivityAction.Action_MaintanceActivityy);
                        break;
                    //运维管理
                    case 2:
                        startThisActivity(DefineActivityAction.Action_MangaeActivity);
                        break;
                    //报警记录
                    case 3:
                        startThisActivity(DefineActivityAction.Action_AlarmActivity);
                        break;
                    //参数查看
                    case 4:
                        startThisActivity(DefineActivityAction.Action_ParamActivity);
                        break;
                }
            }
        });

    }

    private void startThisActivity(String action) {
        try {
            Intent intent = new Intent(action);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 杀死该应用进程
        if (app != null) {
            app.exit();
        }
    }
}
