package com.uninew.car.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.uninew.car.R;
import com.uninew.car.db.revenue.Revenue;
import com.uninew.net.Taximeter.bean.P_TaxiOperationDataReport;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public class RevenueAdapter extends BaseAdapter<Revenue> {

    private OnRevenueItemClickListener mListener;

    public RevenueAdapter(List<Revenue> datas, Context context) {
        super(datas, context, R.layout.revenue_item_view);
    }

    @Override
    public void bindData(final BaseViewHolder holder, final Revenue revenue) {
        byte[] buffers = revenue.getRevenueDatas();
        if (buffers != null && buffers.length > 0) {
            P_TaxiOperationDataReport dataReport = new P_TaxiOperationDataReport();
            dataReport.getDataPacket(buffers);
            holder.setText(R.id.tv_revenue_time, revenueTime(dataReport.getUpCarTime()));
            holder.setText(R.id.tv_revenue_amount, dataReport.getTransactionIncome() + "");
            holder.setText(R.id.tv_revenue_timer, getTime(dataReport.getUpCarTime()
                    , dataReport.getDownCarTime()) + " " + mContext.getString(R.string.minute));
            holder.setText(R.id.tv_revenue_mileage, dataReport.getMileage() + "");
        }
        holder.setOnClickListener(R.id.iv_revenue_into, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(revenue, v.getId(), holder.getPosition());
                }
            }
        });
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(revenue, v.getId(), holder.getPosition());
                }
            }
        });
    }

    public void setOnRevenueItemClickListener(OnRevenueItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnRevenueItemClickListener {
        /**
         * 点击事件分发
         *
         * @param item
         * @param viewId
         * @param position
         */
        void onItemClick(Revenue item, int viewId, int position);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    private String getTime(String startTime, String endTime) {
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            return null;
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
            sHour = Integer.parseInt(startTime.substring(6, 8));
            sMinute = Integer.parseInt(startTime.substring(8, 10));
        }
        if (endTime.length() >= 4) {
            eHour = Integer.parseInt(endTime.substring(0, 2));
            eMinute = Integer.parseInt(endTime.substring(2, 4));
        }
        Log.d("mm", "sHour:" + sHour + ",sMinute:" + sMinute + ",sSecond:" + sSecond);
        Log.d("mm", "eHour:" + eHour + ",eMinute:" + eMinute + ",eSecond:" + eSecond);
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
        Log.d("mm", "h:" + h + ",m:" + m + ",s:" + s);
        return h * 60 + m + "";
    }

    private String revenueTime(String startTime) {
        String sy = "01";
        String sm = "01";
        String sd = "01";
        String sHour = "00";
        String sMinute = "00";
        String sSecond = "00";
        if (startTime.length() >= 10) {
            sy = startTime.substring(0, 2);
            sm = startTime.substring(2, 4);
            sd = startTime.substring(4, 6);
            sHour = startTime.substring(6, 8);
            sMinute = startTime.substring(8, 10);
        }
        return "20" + sy + "_" + sm + "_" + sd + " " + sHour + ":" + sMinute + ":" + sSecond;
    }

}
