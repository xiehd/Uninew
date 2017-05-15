package com.uninew.auto.phone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.uninew.adpater.AbstractSpinerAdapter;
import com.uninew.adpater.AbstractSpinerAdapter.IOnItemSelectListener;
import com.uninew.adpater.ShowListAdapter;
import com.uninew.adpater.SpinerPopWindow;
import com.uninew.ftp.FTP;
import com.uninew.ftp.FTP.DownLoadProgressListener;
import com.uninew.ftp.FTP.UploadProgressListener;
import com.uninew.json.AutoState;
import com.uninew.json.JsonFileContants;
import com.uninew.json.JsonParse;
import com.uninew.utils.SPTools;
import com.uninew.utils.TimeTools;
import com.uninew.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LineActivity extends Activity implements IOnItemSelectListener,
        OnCheckedChangeListener, OnClickListener {

    private static AutoState mAutoState;

    private TextView tv_station_name_line, tv_station_imgleft,
            tv_station_imgright, top_title_txt;
    private ImageView img_back;

    private RadioGroup collect_radiogroup;
    private RadioButton radiobtn_up;
    private ListView line_listview;

    private JsonParse jsonParse;
    private ShowListAdapter listAdapter;
    private SPTools sp;
    private Button line_btn_download, line_btn_upload;
    private FTP mFTP;
    private ProgressDialog progressDialog;
    private int felieNumber = 0;
    private boolean isDown = true;
    private Switch switch_text;
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            // TODO Auto-generated method stub
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    String s = (String) msg.obj.toString();
                    String str[] = s.split("_");

                    if (str[0].equals(JsonFileContants.FTP_CONNECT_SUCCESSS)) {
                        progressDialogShow(str[0]);
                    } else if (str[0]
                            .equals(JsonFileContants.FTP_DISCONNECT_SUCCESS)) {
                        if (isDown) {
                            Toast.makeText(getApplicationContext(), "下载成功",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "上传成功",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        felieNumber = 0;
                    } else if (str[0].equals(JsonFileContants.FTP_UPLOAD_LOADING)
                            || str[0].equals(JsonFileContants.FTP_DOWN_LOADING)) {

                        if (progressDialog != null) {
                            if (str.length >= 2) {
                                if (str[0]
                                        .equals(JsonFileContants.FTP_UPLOAD_LOADING)) {
                                    progressDialog.setMessage("正在上传第 "
                                            + felieNumber + " 个文件：" + str[1] + "%");
                                } else {
                                    progressDialog.setMessage("正在下载第 "
                                            + felieNumber + " 个文件：" + str[1] + "%");
                                }
                            } else {
                                progressDialog.setMessage(str[0]);
                            }
                        }
                    } else if (str[0].equals(JsonFileContants.FTP_UPLOAD_SUCCESS)
                            || str[0].equals(JsonFileContants.FTP_DOWN_SUCCESS)) {
                        felieNumber++;
                        // Toast.makeText(getApplicationContext(),
                        // str[0],Toast.LENGTH_SHORT).show();
                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), str[0],
                                Toast.LENGTH_SHORT).show();
                        felieNumber = 0;
                    }

                    break;
                case 1:
                    listAdapter.setList(mAutoState.getUpstation());
                    line_listview.setAdapter(listAdapter);
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        setContentView(R.layout.activity_line);

        sp = new SPTools(this);
        jsonParse = new JsonParse();

        mAutoState = MainApplication.getInstance().getmAutoState();
        mFTP = new FTP(mAutoState.getFTP_id(), mAutoState.getFTP_user(),
                mAutoState.getFTP_password());

        mSpinerPopWindow = new SpinerPopWindow(getApplicationContext());
        mAdapter = new AbstractSpinerAdapter(this);
        mSpinerPopWindow.setItemListener(this);
        initView();
        init();
        setPos(mAutoState.getmCurrenlineIndex());

    }

    /**
     * progressDialog显示
     */
    private void progressDialogShow(String str) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(LineActivity.this);
        }
        progressDialog.setTitle("请稍后...");
        progressDialog.setCancelable(false);
        progressDialog.setMessage(str);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条对话框//样式（水平，旋转）

        progressDialog.show();
        felieNumber++;
    }

    private void init() {
        // TODO Auto-generated method stub
        for (String linename : mAutoState.getLineFilelist()) {
            windowDatas.add(linename);
        }

    }

    private void initView() {
        // TODO Auto-generated method stub
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        top_title_txt = (TextView) findViewById(R.id.top_title_txt);
        top_title_txt.setText("线路");
        tv_station_name_line = (TextView) findViewById(R.id.tv_station_name_line);
        tv_station_name_line.setOnClickListener(this);
        tv_station_imgright = (TextView) findViewById(R.id.tv_station_imgright);
        tv_station_imgright.setOnClickListener(this);
        tv_station_imgleft = (TextView) findViewById(R.id.tv_station_imgleft);
        tv_station_imgleft.setOnClickListener(this);

        collect_radiogroup = (RadioGroup) findViewById(R.id.collect_radiogroup);
        collect_radiogroup.setOnCheckedChangeListener(this);
        radiobtn_up = (RadioButton) findViewById(R.id.radiobtn_up);

        line_listview = (ListView) findViewById(R.id.line_listview);
        listAdapter = new ShowListAdapter(this, mAutoState.getUpstation());

        line_btn_download = (Button) findViewById(R.id.line_btn_download);
        line_btn_download.setOnClickListener(this);

        line_btn_upload = (Button) findViewById(R.id.line_btn_upload);
        line_btn_upload.setOnClickListener(this);

        title_switch = (TextView) findViewById(R.id.title_switch_txtmode);
        title_switch.setVisibility(View.INVISIBLE);

        switch_text = (Switch) findViewById(R.id.switch_text);
        switch_text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "开启轨迹点采集", Toast.LENGTH_SHORT).show();
                    mAutoState.setTrackRecord(true);
                } else {
                    Toast.makeText(getApplicationContext(), "关闭轨迹点采集", Toast.LENGTH_SHORT).show();
                    mAutoState.setTrackRecord(false);
                }
            }
        });
        if(mAutoState.getTrackRecord()){
            switch_text.setChecked(true);
        }else{
            switch_text.setChecked(false);
        }


    }

    private TextView title_switch;
    private SpinerPopWindow mSpinerPopWindow;
    private AbstractSpinerAdapter mAdapter;
    private ArrayList<String> windowDatas = new ArrayList<>();
    private static int index = 0;

    private void showSpinWindowstate() {
        // windowDatas.clear();
        Log.e("xhd", "showSpinWindowstate");
        if (mSpinerPopWindow != null) {
            mAdapter.refreshData(windowDatas, 0);
            mSpinerPopWindow.setAdatper(mAdapter);
            mSpinerPopWindow.setWidth(tv_station_name_line.getWidth());
            mSpinerPopWindow.showAsDropDown(tv_station_name_line);
        }
    }

    @Override
    public void onItemClick(int pos) {
        // TODO Auto-generated method stub
        index = pos;
        setPos(index);
    }

    private void setPos(int pos) {
        if (pos >= mAutoState.getLineFilelist().size()) {
            return;
        }
        mAutoState.setmCurrenlineIndex(pos);
        mAutoState.setmCurrenlineName(mAutoState.getLineFilelist().get(pos));
        tv_station_name_line.setText(mAutoState.getmCurrenlineName());
        radiobtn_up.setChecked(true);
        // listAdapter.setList(mAutoState.getUpstation());
        // line_listview.setAdapter(listAdapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                mAutoState.setUpstation(jsonParse.getStations(mAutoState
                        .getmCurrenlineName() + "上行"));
                mAutoState.setDownstation(jsonParse.getStations(mAutoState
                        .getmCurrenlineName() + "下行"));
                mAutoState.setUpMarkerDaolist(jsonParse.getCorners(mAutoState
                        .getmCurrenlineName() + "上行"));
                mAutoState.setDownMarkerDaolist(jsonParse.getCorners(mAutoState
                        .getmCurrenlineName() + "下行"));
                Utils.readGPSFile(mAutoState.getmCurrenlineName() + "gps.txt",
                        mAutoState.getCurrentPointlist());
                mHandler.sendEmptyMessageDelayed(1, 1);
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_station_name_line:
                showSpinWindowstate();
                break;
            case R.id.tv_station_imgleft:
                if (index <= 0) {
                    return;
                }
                index--;
                setPos(index);
                break;
            case R.id.tv_station_imgright:
                if (index >= windowDatas.size()) {
                    return;
                }
                index++;
                setPos(index);
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.line_btn_download:// 下载
                isDown = true;
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String fileName = mAutoState.getmCurrenlineName()
                                + "gps.txt";
                        // File downfile = new
                        // File(sdpath1+JsonFileContants.FILE_POINT);
                        try {
                            // ZipUtils.upZipFile(new
                            // File(JsonFileContants.FTP_FILE_PHONE+"/"+"myziP.zip"),
                            // JsonFileContants.FTP_FILE_PHONE+"/"+"my");
                            mFTP.downloadFile(
                                    JsonFileContants.FTP_FILE_DOWNREMOTEPATH, null,
                                    JsonFileContants.FTP_FILE_LINE, null,
                                    new DownLoadProgressListener() {

                                        @Override
                                        public void onDownLoadProgress(
                                                String currentStep,
                                                long downProcess, File file) {
                                            Message msgdown = new Message();
                                            msgdown.obj = currentStep + "_"
                                                    + downProcess;
                                            msgdown.what = 0;
                                            mHandler.removeMessages(0);
                                            mHandler.sendMessage(msgdown);
                                            // Log.i("LineActivity_down",
                                            // currentStep);

                                        }
                                    });

                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.line_btn_upload:// 上传
                isDown = false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String fileName2 = mAutoState.getmCurrenlineName()
                                + "gps.txt";
                        try {

                            File upfile = new File(JsonFileContants.FTP_FILE_LINE);

                            if (!upfile.exists()) {
                                // Toast.makeText(this, "上传文件不存在："+fileName2,
                                // Toast.LENGTH_SHORT).show();
                                Message msgup = new Message();
                                msgup.obj = "上传文件不存在：" + fileName2;
                                msgup.what = 0;
                                mHandler.removeMessages(0);
                                mHandler.sendMessage(msgup);
                                Log.e("LineActivity", "上传文件不存在：" + fileName2);
                                return;
                            }
                            // 可以压缩后上传
                            // ZipUtils.zipSingFiles(upfile, new
                            // File(JsonFileContants.FTP_FILE_PHONE+"/"+"ftpziP.zip"),
                            // "压缩测试");
                            mFTP.uploadMultiFile(upfile,
                                    JsonFileContants.FTP_FILE_UPREMOTEPATH + TimeTools.getCurrentTime() + "/",
                                    new UploadProgressListener() {
                                        @Override
                                        public void onUploadProgress(
                                                String currentStep,
                                                long uploadSize, File file) {
                                            // TODO Auto-generated method stub
                                            // Log.i("LineActivity_up",
                                            // currentStep);
                                            Message msgup = new Message();
                                            msgup.obj = currentStep + "_"
                                                    + uploadSize;
                                            msgup.what = 0;
                                            mHandler.removeMessages(0);
                                            mHandler.sendMessage(msgup);
                                        }
                                    });
                            //关闭连接
                            mFTP.uploadAfterOperate(new UploadProgressListener() {

                                @Override
                                public void onUploadProgress(String currentStep,
                                                             long uploadSize, File file) {
                                    // TODO Auto-generated method stub
                                    Message msgdown = new Message();
                                    msgdown.obj = currentStep;
                                    msgdown.what = 0;
                                    mHandler.removeMessages(0);
                                    mHandler.sendMessage(msgdown);
                                }
                            });
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            default:
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // TODO Auto-generated method stub
        switch (checkedId) {
            case R.id.radiobtn_up:
                listAdapter.setList(mAutoState.getUpstation());
                line_listview.setAdapter(listAdapter);
                break;
            case R.id.radiobtn_down:
                listAdapter.setList(mAutoState.getDownstation());
                line_listview.setAdapter(listAdapter);
                break;
            default:
                break;
        }
    }
}
