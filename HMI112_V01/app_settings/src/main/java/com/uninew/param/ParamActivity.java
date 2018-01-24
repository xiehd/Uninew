package com.uninew.param;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.uninew.settings.R;


public class ParamActivity extends Activity implements ParamContrat.View {

    private ParamContrat.Presenter mPresenter;

    private TextView tv_hearbeat;
    private TextView tv_tcpTimeout;
    private TextView tv_tcpTimes;
    private TextView tv_mainIp;
    private TextView tv_mainPort;
    private TextView tv_spareIp;
    private TextView tv_sparePort;
    private TextView tv_locationStratege;
    private TextView tv_locationPlan;
    private TextView tv_unloginTime;
    private TextView tv_accOffTime;
    private TextView tv_accOnTime;
    private TextView tv_sleepTime;
    private TextView tv_emergenTime;
    private TextView tv_emptyTime;
    private TextView tv_noEmptyTime;
    private TextView tv_unloginDistance;
    private TextView tv_accOffDistance;
    private TextView tv_accOnDistance;
    private TextView tv_sleepDistance;
    private TextView tv_emergenDistance;
    private TextView tv_emptyDistance;
    private TextView tv_noEmptyDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_param);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.stop();
    }

    private void init() {
        mPresenter = new ParamPresenter(this, this.getApplicationContext());
        initView();
    }

    private void initView() {
        tv_hearbeat = (TextView) findViewById(R.id.tv_param_hearbeat);
        tv_tcpTimeout = (TextView) findViewById(R.id.tv_param_tcpTimeout);
        tv_tcpTimes = (TextView) findViewById(R.id.tv_param_tcpTimes);
        tv_mainIp = (TextView) findViewById(R.id.tv_param_mainIp);
        tv_mainPort = (TextView) findViewById(R.id.tv_param_mainPort);
        tv_spareIp = (TextView) findViewById(R.id.tv_param_spareIp);
        tv_sparePort = (TextView) findViewById(R.id.tv_param_sparePort);
        tv_locationStratege = (TextView) findViewById(R.id.tv_param_locationStratege);
        tv_locationPlan = (TextView) findViewById(R.id.tv_param_locationPlan);
        tv_unloginTime = (TextView) findViewById(R.id.tv_param_unloginTime);
        tv_accOffTime = (TextView) findViewById(R.id.tv_param_accOffTime);
        tv_accOnTime = (TextView) findViewById(R.id.tv_param_accOnTime);
        tv_sleepTime = (TextView) findViewById(R.id.tv_param_sleepTime);
        tv_emergenTime = (TextView) findViewById(R.id.tv_param_emergenTime);
        tv_emptyTime = (TextView) findViewById(R.id.tv_param_emptyTime);
        tv_noEmptyTime = (TextView) findViewById(R.id.tv_param_noEmptyTime);
        tv_unloginDistance = (TextView) findViewById(R.id.tv_param_unloginDistance);
        tv_accOffDistance = (TextView) findViewById(R.id.tv_param_accOffDistance);
        tv_accOnDistance = (TextView) findViewById(R.id.tv_param_accOnDistance);
        tv_sleepDistance = (TextView) findViewById(R.id.tv_param_sleepDistance);
        tv_emergenDistance = (TextView) findViewById(R.id.tv_param_emergenDistance);
        tv_emptyDistance = (TextView) findViewById(R.id.tv_param_emptyDistance);
        tv_noEmptyDistance = (TextView) findViewById(R.id.tv_param_noEmptyDistance);
    }

    @Override
    public void setPresenter(ParamContrat.Presenter presenter) {

    }

    @Override
    public void showHeartbeat(String time) {
        tv_hearbeat.setText(time);
    }

    @Override
    public void showTcpTimeOut(String time) {
        tv_tcpTimeout.setText(time);
    }

    @Override
    public void showTcpTimes(String times) {
        tv_tcpTimes.setText(times);
    }

    @Override
    public void showMainIp(String ip) {
        tv_mainIp.setText(ip);
    }

    @Override
    public void showMainPort(String port) {
        tv_mainPort.setText(port);
    }

    @Override
    public void showSpareIp(String ip) {
        tv_spareIp.setText(ip);
    }

    @Override
    public void showSparePort(String port) {
        tv_sparePort.setText(port);
    }

    @Override
    public void showReportStrategy(String reportStrategy) {
        tv_locationStratege.setText(reportStrategy);
    }

    @Override
    public void showReportPlan(String reportPlan) {
        tv_locationPlan.setText(reportPlan);
    }

    @Override
    public void showUnLoginReportIntervalTime(String unLoginReportIntervalTime) {
        tv_unloginTime.setText(unLoginReportIntervalTime);
    }

    @Override
    public void showAccOffReportIntervalTime(String accOffReportIntervalTime) {
        tv_accOffTime.setText(accOffReportIntervalTime);
    }

    @Override
    public void showAccOnReportIntervalTime(String accOnReportIntervalTime) {
        tv_accOnTime.setText(accOnReportIntervalTime);
    }

    @Override
    public void showEmptyReportIntervalTime(String emptyReportIntervalTime) {
        tv_emptyTime.setText(emptyReportIntervalTime);
    }

    @Override
    public void showNoEmptyReportIntervalTime(String noEmptyReportIntervalTime) {
        tv_noEmptyTime.setText(noEmptyReportIntervalTime);
    }

    @Override
    public void showSleepReportIntervalTime(String sleepReportIntervalTime) {
        tv_sleepTime.setText(sleepReportIntervalTime);
    }

    @Override
    public void showEmergencyReportIntervalTime(String emergencyReportIntervalTime) {
        tv_emergenTime.setText(emergencyReportIntervalTime);
    }

    @Override
    public void showUnLoginReportIntervalDistance(String unLoginReportIntervalDistance) {
        tv_unloginDistance.setText(unLoginReportIntervalDistance);
    }

    @Override
    public void showAccOffReportIntervalDistance(String accOffReportIntervalDistance) {
        tv_accOffDistance.setText(accOffReportIntervalDistance);
    }

    @Override
    public void showAccOnReportIntervalDistance(String accOnReportIntervalDistance) {
        tv_accOnDistance.setText(accOnReportIntervalDistance);
    }

    @Override
    public void showEmptyReportIntervalDistance(String emptyReportIntervalDistance) {
        tv_emptyDistance.setText(emptyReportIntervalDistance);
    }

    @Override
    public void showNoEmptyReportIntervalDistance(String noEmptyReportIntervalDistance) {
        tv_noEmptyDistance.setText(noEmptyReportIntervalDistance);
    }

    @Override
    public void showSleepReportIntervalDistance(String sleepReportIntervalDistance) {
        tv_sleepDistance.setText(sleepReportIntervalDistance);
    }

    @Override
    public void showEmergencyReportIntervalDistance(String emergencyReportIntervalDistance) {
        tv_emptyDistance.setText(emergencyReportIntervalDistance);
    }
}
