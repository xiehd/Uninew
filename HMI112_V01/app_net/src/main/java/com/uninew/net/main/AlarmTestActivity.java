package com.uninew.net.main;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.uninew.car.db.revenue.Revenue;
import com.uninew.car.db.revenue.RevenueLocalDataSource;
import com.uninew.net.Alarm.AlarmManager;
import com.uninew.net.Alarm.illegalalram.RegionInfo;
import com.uninew.net.JT905.bean.T_LocationReport;
import com.uninew.net.JT905.bean.T_OperationDataReport;
import com.uninew.net.JT905.comm.client.ClientSendManage;
import com.uninew.net.JT905.comm.client.IClientSendManage;
import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.R;
import com.uninew.net.Taximeter.bean.P_TaxiOperationDataReport;
import com.uninew.net.test.p_Alarm;

public class AlarmTestActivity extends Activity implements View.OnClickListener {

    private AlarmManager mAlarmManager;
    private IClientSendManage mClientSendManage;
    private int speech = 20;
    private EditText edi_speech,edi_carnumber,edi_money,edi_km,edi_lon,edi_lat;
    private TextView text_car,text_money,text_km;
    private P_TaxiOperationDataReport mOperationData;//运营数据
    private RegionInfo mRegionInfo;//区域结构体
    private RevenueLocalDataSource db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_test);
        initView();
        mClientSendManage = new ClientSendManage(this);
        mRegionInfo = new RegionInfo();

        db = RevenueLocalDataSource.getInstance(this);
        init();
        if(mService != null)
        mAlarmManager = new AlarmManager(mService);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent service=new Intent(this,LinkService.class);
        bindService(service,conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(conn);
    }

    private LinkService mService;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((LinkService.LinkBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    private void init() {
        mOperationData = new P_TaxiOperationDataReport();
        mOperationData.setBusinessLicense("123456789");
        mOperationData.setDriverCertificate("123123123456456456");
        mOperationData.setCarNumber("粤A520");
        mOperationData.setUpCarTime("1710201500");
        mOperationData.setDownCarTime("0800");
        mOperationData.setMileage(100.2f);
        mOperationData.setEmptyMileage(50.5f);
        mOperationData.setSurcharge(30.5f);
        mOperationData.setTransactionIncome(20.3f);
        mOperationData.setWaitTimingTime("1022");
        mOperationData.setTrips(11);
        mOperationData.setTransactionType(0x00);
        mOperationData.setCarData(new byte[2]);

        //区域数据模拟
        mRegionInfo.setRegoinID(0x00);
        mRegionInfo.setRegoinType(0);//矩形
        mRegionInfo.setPointNumber(4);//顶点数
        mRegionInfo.getLonlist().add(113.500000);
        mRegionInfo.getLatlist().add(22.5000);

        mRegionInfo.getLonlist().add(114.500000);
        mRegionInfo.getLatlist().add(22.50000);

        mRegionInfo.getLonlist().add(114.500000);
        mRegionInfo.getLatlist().add(21.500000);

        mRegionInfo.getLonlist().add(113.50000);
        mRegionInfo.getLatlist().add(21.5000);



    }

    private void initView() {
        edi_carnumber = (EditText) findViewById(R.id.edi_carnumber);
        edi_money = (EditText) findViewById(R.id.edi_money);
        edi_km = (EditText) findViewById(R.id.edi_km);
        edi_lon = (EditText) findViewById(R.id.edi_lon);
        edi_lat = (EditText) findViewById(R.id.edi_lat);

        text_car = (TextView) findViewById(R.id.text_car);
        text_money = (TextView) findViewById(R.id.text_money);
        text_km = (TextView) findViewById(R.id.text_km);

        edi_speech = (EditText) findViewById(R.id.btn_speech);
        edi_speech.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().equals(""))
                    speech = Integer.parseInt(s.toString().trim());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(mService != null && mAlarmManager == null)
            mAlarmManager = new AlarmManager(mService);
        switch (v.getId()) {
            case R.id.btn_operter1://采集运营数据
                if (!edi_carnumber.getText().toString().equals("")){
                    mOperationData.setCarNumber(edi_carnumber.getText().toString());
                }
                if (!edi_money.getText().toString().equals("")){
                    mOperationData.setTransactionIncome(Float.parseFloat(edi_money.getText().toString()));
                }
                if (!edi_km.getText().toString().equals("")){
                    mOperationData.setMileage(Float.parseFloat(edi_km.getText().toString()));
                }
                mOperationData.getDataPacket(mOperationData.getDataBytes());
                text_car.setText(mOperationData.getCarNumber());
                text_money.setText(mOperationData.getTransactionIncome()+"");
                text_km.setText(mOperationData.getMileage()+"");
                break;
            case R.id.btn_operter2://上传运营数据
                T_OperationDataReport operationDataReport = new T_OperationDataReport();
                operationDataReport.setDownCarLocation(new T_LocationReport());
                operationDataReport.setUpCarLocation(new T_LocationReport());
                operationDataReport.setEvaluationExtended(0x00);
                operationDataReport.setOperationDatas(mOperationData.getDataBytes());
                operationDataReport.setEvaluationId(00000);
                operationDataReport.setEvaluationOption(0000);
                operationDataReport.setOrderId(123);
                mClientSendManage.operationDataReport(operationDataReport);
//保存数据库
                Revenue mRevenue = new Revenue();
                mRevenue.setUpCarLocation(new byte[10]);
                mRevenue.setDownLocation(new byte[10]);
                mRevenue.setRevenueId(111);
                mRevenue.setEvaluationId(11111);
                mRevenue.setEvaluation(2);
                mRevenue.setEvaluationExtended(0);
                mRevenue.setOrderId(123);
                mRevenue.setRevenueDatas(operationDataReport.getDataBytes());
                db.saveDBData(mRevenue);

                //评价
                Intent i = new Intent();
                i.setAction("com.uninew.car.EvaluationActivity");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("revenue",mRevenue);
                this.startActivity(i);

                break;
            case R.id.btn_alarm1://紧急报警
                mAlarmManager.mDevicefailureJudge.sendfailure(1, 0);
                break;
            case R.id.btn_alarm2://超速报警
                Log.i("当前模拟速度：", speech + "");
                mAlarmManager.mOverSpeedJudge.sendSpeed(speech);
                mAlarmManager.mOverSpeedJudge.sendSpeed(speech);
                mAlarmManager.mOverSpeedJudge.sendSpeed(speech);
                mAlarmManager.mOverSpeedJudge.sendSpeed(speech);
                mAlarmManager.mOverSpeedJudge.sendSpeed(speech);
                break;
            case R.id.btn_alarm3://连续驾驶
                mAlarmManager.mOverTimeJudge.sendSingleTime(0);
                break;
            case R.id.btn_alarm10://连续驾驶结束
                mAlarmManager.mOverTimeJudge.sendSingleTime(1);
                break;
            case R.id.btn_alarm5://电子围栏
                // 113.5-114.5   21.5-22.5
                //测试区域内：22.553522 113.902522；区域外：21.552222,113.852000
                double lon = 113.0;
                double lat = 21.0;
                if (!edi_lon.getText().toString().equals("")){
                    lon = Double.parseDouble(edi_lon.getText().toString());
                }
                if (!edi_lat.getText().toString().equals("")){
                    lat = Double.parseDouble(edi_lat.getText().toString());
                }
                mAlarmManager.mRegoinJudge.sendRegoinInfo(20,lon,lat,mRegionInfo);
                break;
            case R.id.btn_alarm6://电压不足
                mAlarmManager.mDevicefailureJudge.sendfailure(5, 0);
                break;
            case R.id.btn_alarm7://主电源掉电
                mAlarmManager.mDevicefailureJudge.sendfailure(6, 0);
                break;
            case R.id.btn_alarm8://计价器故障
                mAlarmManager.mDevicefailureJudge.sendfailure(10, 0);
                break;
            case R.id.btn_alarm9://天线未连接
                mAlarmManager.mDevicefailureJudge.sendfailure(2, 0);
                break;
            case R.id.btn_alarm_ok://确认报警
                mService.sendToJT905(new p_Alarm(BaseMsgID.PLATFORM_CONFIRM_ALARM));
                break;
            case R.id.btn_alarm_no://解除报警
                mService.sendToJT905(new p_Alarm(BaseMsgID.PLATFORM_RELEASE_ALARM));
                break;
        }
    }
}
