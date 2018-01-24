package com.uninew.car.revenue;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.uninew.car.db.revenue.Revenue;
import com.uninew.car.db.revenue.RevenueLocalDataSource;
import com.uninew.car.db.revenue.RevenueLocalSource;
import com.uninew.net.Taximeter.bean.P_TaxiOperationDataReport;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import tools.TimeTool;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public class RevenuesPresenter implements RevenueContrat.Presenter, RevenueLocalSource.LoadRevenueCallback {

    private RevenueContrat.View mView;
    private Context mContext;
    private RevenueLocalSource mDBRevenue;
    private float hAmount = 0;
    private int hWaitTime = 0;
    private float hMileage = 0;
    private float hBareMileage = 0;

    public RevenuesPresenter(RevenueContrat.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        mDBRevenue = RevenueLocalDataSource.getInstance(mContext);
//        P_TaxiOperationDataReport dataReport = new P_TaxiOperationDataReport();
//        dataReport.setCarNumber("B123155");
//        dataReport.setBusinessLicense("1111111111");
//        dataReport.setDownCarTime("170930153000");
//        dataReport.setDriverCertificate("1122200000");
//        dataReport.setUpCarTime("1530");
//        dataReport.setWaitTimingTime("40");
//        dataReport.setCarData(new byte[20]);
//        Revenue revenue = new Revenue();
//        revenue.setRevenueDatas(dataReport.getDataBytes());
//        revenue.setTime("2018-1-20 14:50:22");
//        try {
//            revenue.setTime(TimeTool.formatDate(new Date(System.currentTimeMillis())));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        mDBRevenue.saveDBData(revenue);
//        revenue.setWaitTime(20);
//        revenue.setUpCarTime("2017-09-30 15:30:00");
//        revenue.setAmount(55.00);
//        revenue.setBareMileage(20.00);
//        revenue.setDownCarTime("2107-09-30 15:50:00");
//        revenue.setDrivingMileage(50.00);
//        revenue.setDriverCertificate("12345678955");
//        revenue.setPlateNumber("粤B123155");
//        revenue.setSurcharge(0);
//        revenue.setBusinessLicense("1244545788754");
//        Revenue revenue1 = new Revenue();
//        revenue1.setWaitTime(20);
//        revenue1.setUpCarTime("2017-09-30 15:30:44");
//        revenue1.setAmount(55.00);
//        revenue1.setBareMileage(20.00);
//        revenue1.setDownCarTime("2107-09-30 15:50:00");
//        revenue1.setDrivingMileage(50.00);
//        revenue1.setDriverCertificate("12345678955");
//        revenue1.setPlateNumber("粤B123155");
//        revenue1.setSurcharge(0);
//        revenue1.setBusinessLicense("1244545788754");
//        Revenue revenue2 = new Revenue();
//        revenue2.setWaitTime(20);
//        revenue2.setUpCarTime("2017-09-30 15:30:55");
//        revenue2.setAmount(55.00);
//        revenue2.setBareMileage(20.00);
//        revenue2.setDownCarTime("2107-09-30 15:50:00");
//        revenue2.setDrivingMileage(50.00);
//        revenue2.setDriverCertificate("12345678955");
//        revenue2.setPlateNumber("粤B123155");
//        revenue2.setSurcharge(0);
//        revenue2.setBusinessLicense("1244545788754");
//        mDBRevenue.saveDBData(new Revenue[]{revenue, revenue1, revenue2});
    }

    @Override
    public void start() {
        mDBRevenue.getAllDBDatas(this);
    }

    @Override
    public void stop() {

    }

    @Override
    public void onDBBaseDataLoaded(List<Revenue> buffers) {
        mView.showRevenues(buffers);
        Revenue revenue = buffers.get(buffers.size() - 1);
        byte[] b = revenue.getRevenueDatas();
        if(b != null && b.length > 0) {
            P_TaxiOperationDataReport dataReport = new P_TaxiOperationDataReport();
            dataReport.getDataPacket(b);
            mView.showWaitTime(getTime(dataReport.getUpCarTime(),dataReport.getDownCarTime()));
            mView.showTimes(dataReport.getTrips());
            mView.setAmount(dataReport.getTransactionIncome());
            mView.showBareMileage(dataReport.getEmptyMileage());
            mView.showBareRate((int) (dataReport.getEmptyMileage()/dataReport.getMileage() * 100));
            mView.setDrivingMileage(dataReport.getMileage());
        }
        for (Revenue revenue1: buffers){
            byte[] b1 = revenue1.getRevenueDatas();
            if(b1 != null && b1.length > 0) {
                P_TaxiOperationDataReport dataReport = new P_TaxiOperationDataReport();
                dataReport.getDataPacket(b1);
                hAmount = hAmount + dataReport.getTransactionIncome();
                hBareMileage = hBareMileage + dataReport.getEmptyMileage();
                hMileage = hMileage + dataReport.getMileage();
            }
        }
    }

    @Override
    public void onDataNotAailable() {
        mView.showRevenues(null);
    }

    @Override
    public void setHistoryRevenue() {
        mView.setDrivingMileage(hMileage);
        mView.showBareMileage(hBareMileage);
        mView.setAmount(hAmount);
    }

    @Override
    public void setCurrentRevenue() {
        mDBRevenue.getAllDBDatas(this);
        hMileage = 0;
        hBareMileage = 0;
        hAmount = 0;
    }

    @Override
    public void showDetailsRevenue(Revenue revenue) {
        mView.showDetailsRevenue(revenue);
    }

    private int getTime(String startTime, String endTime) {
        if(TextUtils.isEmpty(startTime)||TextUtils.isEmpty(endTime)){
            return 0;
        }
        int sHour = 0;
        int sMinute = 0;
        int sSecond = 0;
        int eHour = 0;
        int eMinute = 0;
        int eSecond = 0;
//        Pattern pattern = Pattern.compile("[^0-9]");
//        Matcher matcher = pattern.matcher(startTime);
//        String[] temp = matcher.replaceAll(" ").split(" ");
//        Log.d("mm","temp:"+ Arrays.toString(temp));
        if (startTime.length() >= 10) {
            sHour = Integer.parseInt(startTime.substring(6,8));
            sMinute = Integer.parseInt(startTime.substring(8,10));
        }
        if (endTime.length() >= 4) {
            eHour = Integer.parseInt(endTime.substring(0,2));
            eMinute = Integer.parseInt(endTime.substring(2,4));
        }
        Log.d("mm","sHour:"+sHour+",sMinute:"+sMinute+",sSecond:"+sSecond);
        Log.d("mm","eHour:"+eHour+",eMinute:"+eMinute+",eSecond:"+eSecond);
        int s = eSecond - sSecond;
        int m = eMinute - sMinute;
        int h = eHour - sHour;
        if (s < 0) {
            s = s + 60;
            m = m - 1;
        }
        if (m < 0) {
            m = m + 60;
            h = h - 1;
        }
        if (h < 0) {
            h = 24 + h;
        }
        Log.d("mm","h:"+h+",m:"+m+",s:"+s);
        return h * 60 + m;
    }
}
