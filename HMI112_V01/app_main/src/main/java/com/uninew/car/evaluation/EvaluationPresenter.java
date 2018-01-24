package com.uninew.car.evaluation;

import android.content.Context;
import android.util.Log;

import com.uninew.car.R;
import com.uninew.car.db.order.Order;
import com.uninew.car.db.order.OrderKey;
import com.uninew.car.db.order.OrderLocalDataSource;
import com.uninew.car.db.order.OrderLocalSource;
import com.uninew.car.until.ToastCommon;
import com.uninew.net.JT905.bean.T_OperationDataReport;
import com.uninew.net.JT905.comm.client.ClientSendManage;
import com.uninew.net.JT905.common.ProtocolTool;
import com.uninew.net.JT905.common.TimeTool;
import com.uninew.net.Taximeter.bean.P_TaxiOperationDataReport;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/9/25 0025.
 */

public class EvaluationPresenter implements EvaluationContract.Presenter {

    private EvaluationContract.View mView;
    private Context mContext;
    private EvaluationModel mModel;
    private ClientSendManage mClientSendManage;
    private OrderLocalSource orderLocalSource;

    public EvaluationPresenter(EvaluationContract.View view, Context context) {
        this.mContext = context;
        this.mView = view;
        mModel = new EvaluationModel(context);
        mView.setPresenter(this);
        mClientSendManage = new ClientSendManage(mContext);
        orderLocalSource = OrderLocalDataSource.getInstance(context);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void evaluateStar(String time, int star) {
        mModel.changeEvaluateStar(time, star);
    }

    @Override
    public void setAmount(double amount) {
        DecimalFormat df   = new DecimalFormat("######0.00");
        mView.showAmount( df.format(amount) + "");
    }

    @Override
    public void setTime(String time) {
        mView.showTime(time);
    }

    @Override
    public void sendOperationDataReport(final T_OperationDataReport operationDataReport, int extended) {
        if (operationDataReport != null) {

            operationDataReport.setOrderId(0);
            final P_TaxiOperationDataReport taxiOperationDataReportt = new P_TaxiOperationDataReport();
            taxiOperationDataReportt.getDataPacket(operationDataReport.getOperationDatas());
            orderLocalSource.getOrderByStateAndType(OrderKey.OrderTypeKey.TEMPORARY_TYPE,
                    OrderKey.OrderStateKey.BADE_STATE, new OrderLocalSource.LoadOrdersCallback() {
                        @Override
                        public void onDBBaseDataLoaded(List<Order> buffers) {
                            for (Order order : buffers) {
                                if (isCurrentOrder(order.getNeedTime(), taxiOperationDataReportt.getUpCarTime())) {
                                    operationDataReport.setOrderId(order.getBusinessId());
                                    mClientSendManage.orderFinishEnsure(order.getBusinessId());
                                }
                            }
                        }

                        @Override
                        public void onDataNotAailable() {

                        }
                    });
            operationDataReport.setEvaluationExtended(extended);
            operationDataReport.setEvaluationId(ProtocolTool.setBit905(TimeTool.getCurrentTimestamp()));
            mClientSendManage.operationDataReport(operationDataReport);
        }
    }

    @Override
    public void setMileage(double mileage) {
        mView.showMileage(mileage + mContext.getString(R.string.kilometre));
    }

    @Override
    public void setSurcharge(double surcharge) {
        mView.showSurcharge(surcharge + mContext.getString(R.string.yuan));
    }

    @Override
    public void setOrder(float order) {
        mView.showOrder(order + mContext.getString(R.string.yuan));
    }

    private boolean isCurrentOrder(String needTime, String upTime) {
        boolean isFinish = false;
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(needTime);
        String[] temp = matcher.replaceAll(" ").split(" ");
        Log.d("mm", "temp:" + Arrays.toString(temp));
        int m = 0;
        int h = 0;
        if (temp != null && temp.length >= 6) {
            h = Integer.parseInt(temp[3]);
            m = Integer.parseInt(temp[4]);
        }
        int sHour = Integer.parseInt(upTime.substring(6, 8));
        int sMinute = Integer.parseInt(upTime.substring(8, 10));
        int s = -1;
        if (h == sHour) {
            s = Math.abs(m - sMinute);
        } else {
            s = 60 - Math.abs(m - sMinute);
        }
        if (s < 20 && s >= 0) {
            isFinish = true;
        }
        return isFinish;
    }
}
