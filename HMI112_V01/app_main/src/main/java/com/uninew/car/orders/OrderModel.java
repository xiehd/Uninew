package com.uninew.car.orders;

import android.content.Context;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.uninew.car.R;
import com.uninew.car.audio.TtsUtil;
import com.uninew.car.db.order.Order;
import com.uninew.car.db.order.OrderKey;
import com.uninew.car.db.order.OrderLocalDataSource;
import com.uninew.car.db.order.OrderLocalSource;
import com.uninew.car.dialog.PromptDialog;
import com.uninew.net.JT905.bean.P_DriverAnswerOrderAns;
import com.uninew.net.JT905.bean.P_OrderSendDown;
import com.uninew.net.JT905.comm.client.IClientReceiveListener;
import com.uninew.net.JT905.comm.client.IClientReceiveManage;
import com.uninew.net.JT905.comm.client.IClientSendManage;

import java.text.ParseException;
import java.util.Date;

import tools.TimeTool;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class OrderModel implements IClientReceiveListener.ICallListener {

    private static final String TAG = "OrderModel";
    private IClientReceiveManage mReceiveManage = null;
    private IClientSendManage mSendManage = null;
    private Context mContext = null;
    private PromptDialog mDialog = null;
    private Order mOrder;
    private OrderLocalSource mDBOrder;

    public OrderModel(Context context) {
        this.mContext = context;
        mDBOrder = OrderLocalDataSource.getInstance(mContext);
    }

    public void initReceive(IClientReceiveManage receiveManage) {
        this.mReceiveManage = receiveManage;
        mReceiveManage.registerCallListener(this);
    }

    public void initSendManage(IClientSendManage sendManage) {
        this.mSendManage = sendManage;
    }

    public void unRegister() {
        if (mReceiveManage != null) {
            mReceiveManage.unRegisterCallListener();
            mReceiveManage = null;
        }
    }

    @Override
    public void orderSendDown(P_OrderSendDown orderMsg) {
        if (orderMsg == null) {
            return;
        }
        Log.d(TAG,"收到订单任务下发指令，orderMsg:"+orderMsg.toString());
        TtsUtil.getInstance(mContext).speak(mContext.getString(R.string.order_new_receive)
                +orderMsg.getBusinessDescription());
        showOrder(orderMsg);
        Order order = new Order();
        order.setBusinessId(orderMsg.getBusinessId());
        order.setBusinessDescription(orderMsg.getBusinessDescription());
        try {
            long time = TimeTool.parseToLong("20"+
                    orderMsg.getNeedTime(),"yyyyMMDDhhmmss");
            String srtTime = TimeTool.formatDate(new Date(time));
            Log.d(TAG,"time:"+time+",srtTime:"+srtTime);
            order.setNeedTime(srtTime);
            order.setReceiveTime(TimeTool.formatDate(new Date(System.currentTimeMillis())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        order.setBusinessType(orderMsg.getBusinessType());
        order.setOrderState(OrderKey.OrderStateKey.FAILURE_STATE);
        Log.d(TAG,"收到订单任务下发指令，order:"+order.toString());
        mDBOrder.saveDBData(order);
    }

    private void showOrder(final P_OrderSendDown orderMsg) {
        if (mDialog == null || mDialog.isCancel()) {
            mDialog = new PromptDialog(mContext);
        }
        mDialog.isSystemAlert();
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);
        int type = orderMsg.getBusinessType();
        switch (type) {
            case OrderKey.OrderTypeKey.ASSIGN_TYPE:
                mDialog.setTitle(R.string.orders_assign_type);
                break;
            case OrderKey.OrderTypeKey.BESPEAK_TYPE:
                mDialog.setTitle(R.string.orders_bespeak_type);
                break;
            case OrderKey.OrderTypeKey.TEMPORARY_TYPE:
                mDialog.setTitle(R.string.orders_temporary_type);
                break;
        }
        String content = orderMsg.getBusinessDescription();
        if (TextUtils.isEmpty(content)) {
            mDialog.isContentCenter();
            mDialog.setContent(R.string.error);
        } else {
            mDialog.setContent(content);
        }
        mDialog.showTimerButton(10, new PromptDialog.OnDialogTimerListener() {
            @Override
            public void onTimer(PromptDialog dialog, int time) {

            }

            @Override
            public void onFinish(PromptDialog dialog) {
                dialog.cancel();
            }


            @Override
            public void onClick(PromptDialog dialog) {
                if (mSendManage != null) {
                    mSendManage.driverAnswerOrder(orderMsg.getBusinessId());
                }
                dialog.cancel();
            }
        });
    }

    @Override
    public void answerOrderResponse(int result) {
        if (result == 0x01) {
            if (mDialog == null || mDialog.isCancel()) {
                mDialog = new PromptDialog(mContext);
            }
            mDialog.isSystemAlert();
            mDialog.show();
            mDialog.showToast(R.string.order_remind_grab_failure, 1000);
            TtsUtil.getInstance(mContext).speak(mContext.getString(R.string.order_remind_grab_failure));
        }
    }

    @Override
    public void answerOrderMsg(P_DriverAnswerOrderAns answerResult) {
        if (answerResult == null) {
            return;
        }
        showAnswerOrder(answerResult);
    }

    private void showAnswerOrder(final P_DriverAnswerOrderAns answerResult) {
        if(answerResult == null){
            return;
        }
        TtsUtil.getInstance(mContext).speak(mContext.getString(R.string.orders_success_state));
        add(answerResult);
        Log.d(TAG,"P_DriverAnswerOrderAns:"+answerResult.toString());
        if (mDialog == null || mDialog.isCancel()) {
            mDialog = new PromptDialog(mContext);
        }
        mDialog.isSystemAlert();
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_order_answer, null);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_order_answer_time);
        TextView tv_phone = (TextView) view.findViewById(R.id.tv_order_answer_phone);
        TextView tv_details = (TextView) view.findViewById(R.id.tv_order_answer_details);
        TextView tv_surcharge = (TextView) view.findViewById(R.id.tv_order_answer_surcharge);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        try {
            long time = TimeTool.parseToLong("20"+answerResult.getNeedTime(),"yyyyMMDDhhmmss");
            String srtTime = TimeTool.formatDate(new Date(time));
            Log.d(TAG,"time:"+time+",srtTime:"+srtTime);
            tv_time.setText(srtTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_details.setText(answerResult.getBusinessDescription());
        tv_phone.setText(answerResult.getPassengerPhoneNumber());
        double serviceCharge = answerResult.getServiceCharge();
        if (serviceCharge > 0) {
            tv_surcharge.setText(serviceCharge + mContext.getString(R.string.yuan));
        }
        mDialog.setTitle(R.string.orders_success_state);
        mDialog.setLeft(R.string.order_button_confirm);
        mDialog.setRight(R.string.order_button_cancel);
        mDialog.addContentView(view, lp, Gravity.LEFT);
        mDialog.setOnDialogClickListener(new PromptDialog.OnDialogClickListener() {
            @Override
            public void onLeft(PromptDialog dialog) {
                dialog.cancel();
            }

            @Override
            public void onRight(PromptDialog dialog) {
                showCancelOrder(answerResult.getBusinessId());
            }
        });
    }

    @Override
    public void platformcancelOrder(int businessId) {
        if (mDBOrder == null) {
            mDBOrder = OrderLocalDataSource.getInstance(mContext);
        }
        mDBOrder.getOrderByWorkId(businessId, new OrderLocalSource.GetOrderCallback() {
            @Override
            public void onDBBaseDataLoaded(Order order) {
                showCancelAnsOrder(order, -1);
            }

            @Override
            public void onDataNotAailable() {
            }
        });
    }

    @Override
    public void driverCancelOrderAns(int cancelResult) {
        if (mOrder != null)
            showCancelAnsOrder(mOrder, cancelResult);
    }

    @Override
    public void orderFinishEnsureAns(int ensureReuslt) {
        if (mDialog == null || mDialog.isCancel()) {
            mDialog = new PromptDialog(mContext);
        }
        mDialog.isSystemAlert();
        mDialog.show();
        mDialog.showToast(R.string.order_dialog_finish_success, 1000);
    }

    private void add(P_DriverAnswerOrderAns answerResult) {
        if (mDBOrder == null) {
            mDBOrder = OrderLocalDataSource.getInstance(mContext);
        }
        mOrder = new Order();
        mOrder.setTargatLongitude(answerResult.getTargetLongitude());
        mOrder.setTargatLatitude(answerResult.getTargetLatitude());
        mOrder.setPassengerPhoneNumber(answerResult.getPassengerPhoneNumber());
        mOrder.setPassengerLongitude(answerResult.getPassengerLongitude());
        mOrder.setBusinessDescription(answerResult.getBusinessDescription());
        mOrder.setBusinessId(answerResult.getBusinessId());
        mOrder.setBusinessType(answerResult.getBusinessType());
        try {
            long time = TimeTool.parseToLong("20"+answerResult.getNeedTime(),"yyyyMMDDhhmmss");
            String srtTime = TimeTool.formatDate(new Date(time));
            Log.d(TAG,"time:"+time+",srtTime:"+srtTime);
            mOrder.setNeedTime(srtTime);
            mOrder.setReceiveTime(TimeTool.formatDate(new Date(System.currentTimeMillis())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mOrder.setOrderState(OrderKey.OrderStateKey.BADE_STATE);
        mOrder.setServiceCharge(answerResult.getServiceCharge());
        mOrder.setPassengerLatitude(answerResult.getPassengerLongitude());
        mDBOrder.saveDBData(mOrder);
    }

    private void showCancelAnsOrder(Order order, int cancelResult) {
        if (mDBOrder == null) {
            mDBOrder = OrderLocalDataSource.getInstance(mContext);
        }
        String time = "";
        try {
            time = TimeTool.formatDate(new Date(System.currentTimeMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (mDialog == null || mDialog.isCancel()) {
            mDialog = new PromptDialog(mContext);
        }
        mDialog.isSystemAlert();
        mDialog.show();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_order_cancel_answer, null);
        TextView tv_number = (TextView) view.findViewById(R.id.tv_order_title_number);
        TextView tv_surcharge = (TextView) view.findViewById(R.id.tv_order_answer_surcharge);
        tv_number.setText(order.getBusinessId() + "");
        tv_surcharge.setText(order.getBusinessDescription());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        if (cancelResult == 0) {
            mDialog.setTitle(R.string.order_dialog_cancel_success);
            mDBOrder.changeState(order.getBusinessId(), OrderKey.OrderStateKey.DRIVER_CANCEL_STATE, time);
            mOrder = null;
        } else if (cancelResult == 1) {
            mDialog.setTitle(R.string.order_dialog_cancel_failure);
        } else {
            mDialog.setTitle(R.string.orders_passenger_cancel_state);
            mDBOrder.changeState(order.getBusinessId(), OrderKey.OrderStateKey.PASSENGER_CANCEL_STATE, time);
        }
        mDialog.addContentView(view, lp, Gravity.LEFT);
        mDialog.setOnDialogClickListener(R.string.back, null, new PromptDialog.OnDialogClickListener() {
            @Override
            public void onLeft(PromptDialog dialog) {
                dialog.cancel();
            }

            @Override
            public void onRight(PromptDialog dialog) {
                dialog.cancel();
            }
        });
    }

    private int reason = 0x02;

    public void showCancelOrder(final int businessId) {
        if (mOrder == null || mOrder.getBusinessId() != businessId) {
            if (mDBOrder == null) {
                mDBOrder = OrderLocalDataSource.getInstance(mContext);
            }
            mDBOrder.getOrderByWorkId(businessId, new OrderLocalSource.GetOrderCallback() {
                @Override
                public void onDBBaseDataLoaded(Order order) {
                    mOrder = order;
                }

                @Override
                public void onDataNotAailable() {

                }
            });
        }
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_order_cancel, null);
        RadioGroup rg_reason = (RadioGroup) view.findViewById(R.id.rg_order_reason);
        final RadioButton rb_accident = (RadioButton) view.findViewById(R.id.rb_order_accident);
        final RadioButton rb_block = (RadioButton) view.findViewById(R.id.rb_order_block);
        final RadioButton rb_other = (RadioButton) view.findViewById(R.id.rb_orders_other);
        rg_reason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_order_accident:
                        if (rb_accident.isChecked()) {
                            reason = 0x00;
                        }
                        break;
                    case R.id.rb_order_block:
                        if (rb_block.isChecked()) {
                            reason = 0x01;
                        }
                        break;
                    case R.id.rb_orders_other:
                        if (rb_other.isChecked()) {
                            reason = 0x02;
                        }
                        break;
                }
            }
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        if (mDialog == null || mDialog.isCancel()) {
            mDialog = new PromptDialog(mContext);
        }
        mDialog.isSystemAlert();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        mDialog.addContentView(view, lp, Gravity.CENTER);
        mDialog.setOnDialogClickListener(R.string.confirm, R.string.back, new PromptDialog.OnDialogClickListener() {
            @Override
            public void onLeft(PromptDialog dialog) {
                if (mSendManage != null) {
                    mSendManage.driverCancelOrder(businessId, reason);
                }
                reason = 0x02;
                dialog.cancel();
            }

            @Override
            public void onRight(PromptDialog dialog) {
                dialog.cancel();
            }
        });
    }

    public void finishOrder(final int businessId) {
        if (mDialog == null || mDialog.isCancel()) {
            mDialog = new PromptDialog(mContext);
        }
        mDialog.isSystemAlert();
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setTitle(R.string.prompt);
        mDialog.isContentCenter();
        mDialog.setContent(R.string.order_dialog_finish);
        mDialog.setOnDialogClickListener(R.string.confirm, R.string.back, new PromptDialog.OnDialogClickListener() {
            @Override
            public void onLeft(PromptDialog dialog) {
                if (mSendManage != null) {
                    mSendManage.orderFinishEnsure(businessId);
                }
                dialog.cancel();
            }

            @Override
            public void onRight(PromptDialog dialog) {
                dialog.cancel();
            }
        });
    }
}
