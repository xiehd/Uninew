package com.uninew.maintanence.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uninew.maintanence.adapter.VersionDialogAdapter;
import com.uninew.maintanence.dialog.DialogManager;
import com.uninew.maintanence.dialog.VersionDialog;
import com.uninew.maintanence.model.DefineUpdate;
import com.uninew.maintanence.presenter.IResultCallBack;
import com.uninew.maintanence.presenter.IUpdatePresenter;
import com.uninew.maintanence.presenter.IUpdateResultCallBack;
import com.uninew.maintanence.presenter.IUpdateView;
import com.uninew.maintanence.presenter.UpdatePresenter;
import com.uninew.settings.R;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/8/30.
 */


public class VersionFragment extends Fragment implements View.OnClickListener,IUpdateView {
    private static final String TAG = "VersionFragment";
    private View view;

    private boolean isSDFind = false;
    // 交互相关
    private IUpdatePresenter mUpdatePresenter;
    private VersionDialog versionDialog;

    private TextView version_app,version_os,version_mcu,version_dvr,version_carsrceen,version_map,
            version_gaodemap;

    private DialogManager dialogManager = DialogManager.getInstance();

    private Context context;
    private ImageView version_os_image;
    private static class MsgWhat {
        /**
         * 无SD卡
         */
        public static final int noSdCard = 0;
        /**
         * 没有升级文件
         */
        public static final int noUpdateFile = 1;
        /**
         * 显示升级列表
         */
        public static final int showUpdateList = 2;
        /**
         * 查询中
         */
        public static final int updateRequestProgress = 3;
        /**
         * 升级中
         */
        public static final int updateProgress = 4;
        /**
         * 系统升级成功
         */
        public static final int OsSuccess = 5;
        /**
         * 系统升级失败
         */
        public static final int OsFailed = 6;

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MsgWhat.noSdCard:
                    showAlertDialog(R.string.msgNoSDCard);
                    break;
                case MsgWhat.noUpdateFile:
                    dialogManager.closeProgressDialog();
                    showAlertDialog(R.string.msgNoUpdateFile);
                    break;
                // 显示更新列表
                case MsgWhat.showUpdateList:
                    dialogManager.closeProgressDialog();
                    int type = msg.arg1;
                    List<Map<String, String>> datas = (List<Map<String, String>>) msg.obj;
                    versionDialog = new VersionDialog(context, datas, buttonClickListener, type);
                    if (!versionDialog.isShowing()) {
                        versionDialog.show();
                    }
                    break;
                case MsgWhat.updateRequestProgress:
                    dialogManager.showProgressDialog(context,
                            context.getResources().getString(R.string.msgQueryUpdate));
                    break;
                case MsgWhat.OsSuccess:
                    dialogManager.closeProgressDialog();
                    Toast.makeText(context, "升级成功", Toast.LENGTH_SHORT).show();
                    break;
                case MsgWhat.OsFailed:
                    dialogManager.closeProgressDialog();
                    Toast.makeText(context, "升级失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void showAlertDialog(int msgId) {
        String title = context.getResources().getString(R.string.tipTitle);
        String tipMsg = context.getResources().getString(msgId);
        dialogManager.showOneButtonDialog(context, title, tipMsg,
                new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });
    }

    /**
     * 升级按钮点击事件
     */
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (null != versionDialog && versionDialog.isShowing()) {
                versionDialog.dismiss();
            }
            int type = (int) v.getTag(R.id.key_dialog_type);
            final String path = (String) v.getTag(R.id.key_filePath);
            if (type == VersionDialogAdapter.TYPE_APK) {
                mUpdatePresenter.updateApk(path);
            } else if (type == VersionDialogAdapter.TYPE_MCU) {
                mUpdatePresenter.updateMcu(path, null);
            } else if (type == VersionDialogAdapter.TYPE_OS) {
                dialogManager.showProgressDialog(context,
                        context.getResources().getString(R.string.msgUpdate));
                mUpdatePresenter.updateOS(path, new IResultCallBack() {

                    @Override
                    public void resultCallBack(boolean result) {
                        Log.e(TAG, "系统升级结果" + result);
                        if (result) {
                            handler.sendEmptyMessage(MsgWhat.OsSuccess);
                        } else {
                            handler.sendEmptyMessage(MsgWhat.OsFailed);
                        }
                    }
                });
            }

        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_version, container, false);
        context = this.getActivity();
        initView();
        initListener();
        mUpdatePresenter = new UpdatePresenter(context,this);
        return view;
    }

    private void initView() {

        version_app = (TextView) view.findViewById(R.id.version_app);
        version_os = (TextView) view.findViewById(R.id.version_os);
        version_mcu = (TextView) view.findViewById(R.id.version_mcu);
        version_dvr = (TextView) view.findViewById(R.id.version_dvr);
        version_carsrceen = (TextView) view.findViewById(R.id.version_carsrceen);
        version_map = (TextView) view.findViewById(R.id.version_map);
        version_gaodemap = (TextView) view.findViewById(R.id.version_gaodemap);

        version_os_image = (ImageView) view.findViewById(R.id.version_os_image);
    }

    private void initListener(){
        version_os_image.setOnClickListener(this);
        view.findViewById(R.id.version_app_image).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.version_os_image:
                updateOS();
                break;
            case R.id.version_app_image:
                updateApk();
                break;
        }
    }

    private void updateApk() {
        mUpdatePresenter.updateApkRequest(new IUpdateResultCallBack() {
            @Override
            public void updateResultCallBack(int result, List<Map<String, String>> updateMsgs) {
                if (result == DefineUpdate.RquestResult_NoSDCard) {
                    handler.sendEmptyMessage(MsgWhat.noSdCard);
                } else if (result == DefineUpdate.RquestResult_NoFile) {
                    handler.sendEmptyMessage(MsgWhat.noUpdateFile);
                } else {
                    if (updateMsgs != null && updateMsgs.size() > 0) {
                        Message msg = new Message();
                        msg.what = MsgWhat.showUpdateList;
                        msg.obj = updateMsgs;
                        msg.arg1 = VersionDialogAdapter.TYPE_APK;
                        handler.sendMessage(msg);
                    }
                }
            }
        });
    }

    private void updateOS() {
        mUpdatePresenter.updateOsRequest(new IUpdateResultCallBack() {
            @Override
            public void updateResultCallBack(int result, List<Map<String, String>> updateMsgs) {
                if (result == DefineUpdate.RquestResult_NoSDCard) {
                    handler.sendEmptyMessage(MsgWhat.noSdCard);
                } else if (result == DefineUpdate.RquestResult_NoFile) {
                    handler.sendEmptyMessage(MsgWhat.noUpdateFile);
                } else {
                    if (updateMsgs != null && updateMsgs.size() > 0) {
                        Message msg = new Message();
                        msg.what = MsgWhat.showUpdateList;
                        msg.obj = updateMsgs;
                        msg.arg1 = VersionDialogAdapter.TYPE_OS;
                        handler.sendMessage(msg);
                    }
                }

            }
        });
    }

    @Override
    public void SDChange(boolean isEnable) {
        this.isSDFind = isEnable;

    }

    @Override
    public void onApkInsatll(boolean isSuccess) {
        dialogManager.closeProgressDialog();
        if (isSuccess) {
            if (null != context) {
                Toast.makeText(context,
                        context.getResources().getString(R.string.updateApkSuccess), Toast.LENGTH_SHORT).show();
            }

            Log.e(TAG, "apk升级成功...");
        } else {
            if (null != context) {
                Toast.makeText(context, context.getResources().getString(R.string.updateApkFailed),
                        Toast.LENGTH_SHORT).show();
            }

            Log.e(TAG, "apk升级失败");
        }

    }

    @Override
    public void ShowAppVersion(String version) {
        Log.i("xhd","version ="+version);
        version_app.setText(version);
    }

    @Override
    public void ShowOSVersion(String version) {
        version_os.setText(version);
    }

    @Override
    public void ShowMcuVersion(String version) {
        version_mcu.setText(version);
    }

    @Override
    public void ShowDvrVersion(String version) {
        version_dvr.setText(version);
    }

    @Override
    public void ShowCarSrceenVersion(String version) {
        version_carsrceen.setText(version);
    }

    @Override
    public void ShowMapVersion(String version) {
        version_map.setText(version);
    }

    @Override
    public void ShowGaoDeVersion(String version) {
        version_gaodemap.setText(version);
    }

    @Override
    public void onUpdateState(int state, String apk, boolean openState, boolean setLauncherState) {
        File file = new File(apk);
        switch (state) {
            case -1:
                dialogManager.closeProgressDialog();
                Toast.makeText(context,file.getName()+","+context.getString(R.string.updateApkFailed),Toast.LENGTH_SHORT).show();
                break;
            case 0:
                dialogManager.showProgressDialog(context,file.getName()+","+
                        context.getResources().getString(R.string.msgUpdate));
                break;
            case 1:
                dialogManager.closeProgressDialog();
                Toast.makeText(context,file.getName()+","+context.getString(R.string.updateApkSuccess),Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
