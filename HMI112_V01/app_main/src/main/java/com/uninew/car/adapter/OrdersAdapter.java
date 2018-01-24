package com.uninew.car.adapter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.uninew.car.R;
import com.uninew.car.db.order.Order;
import com.uninew.car.db.order.OrderKey;

import java.util.List;

/**
 * Created by Administrator on 2017/9/13 0013.
 */

public class OrdersAdapter extends BaseAdapter<Order>{
    private Context mContext;
    private OnOrderItemClickListener mListener;

    public OrdersAdapter(List<Order> datas, Context context) {
        super(datas, context, R.layout.orders_item_view);
        this.mContext = context;
    }

    @Override
    public void bindData(final BaseViewHolder holder, final Order order) {
        if (order != null) {
            int type = order.getBusinessType();
            switch (type) {
                case OrderKey.OrderTypeKey.ASSIGN_TYPE:
                    holder.setText(R.id.tv_orders_type, R.string.orders_assign_type);
                    break;
                case OrderKey.OrderTypeKey.BESPEAK_TYPE:
                    holder.setText(R.id.tv_orders_type, R.string.orders_bespeak_type);
                    break;
                case OrderKey.OrderTypeKey.TEMPORARY_TYPE:
                    holder.setText(R.id.tv_orders_type, R.string.orders_temporary_type);
                    break;
            }
            int state = order.getOrderState();
            switch (state) {
                case OrderKey.OrderStateKey.BADE_STATE:
                    holder.setText(R.id.tv_orders_state, R.string.orders_bade_state);
                    holder.setImageResource(R.id.iv_orders_state, R.mipmap.bade);
                    break;
                case OrderKey.OrderStateKey.DRIVER_CANCEL_STATE:
                    holder.setText(R.id.tv_orders_state, R.string.orders_driver_cancel_state);
                    holder.setImageResource(R.id.iv_orders_state, R.mipmap.cancel);
                    break;
                case OrderKey.OrderStateKey.FAILURE_STATE:
                    holder.setText(R.id.tv_orders_state, R.string.orders_failure_state);
                    holder.setImageResource(R.id.iv_orders_state, R.mipmap.cancel);
                    break;
                case OrderKey.OrderStateKey.FINISH_STATE:
                    holder.setText(R.id.tv_orders_state, R.string.orders_finish_state);
                    holder.setImageResource(R.id.iv_orders_state, R.mipmap.finish);
                    break;
                case OrderKey.OrderStateKey.OTHER_STATE:
                    holder.setText(R.id.tv_orders_state, R.string.orders_other_state);
                    holder.setImageResource(R.id.iv_orders_state, R.mipmap.cancel);
                    break;
                case OrderKey.OrderStateKey.PASSENGER_CANCEL_STATE:
                    holder.setText(R.id.tv_orders_state, R.string.orders_passenger_cancel_state);
                    holder.setImageResource(R.id.iv_orders_state, R.mipmap.cancel);
                    break;
            }
            holder.setText(R.id.tv_orders_state_time,"（"+order.getReceiveTime()+"）");
            holder.setText(R.id.tv_orders_finish_time, order.getNeedTime());
            holder.setText(R.id.tv_orders_address, order.getBusinessDescription());
            holder.setOnClickListener(R.id.iv_into, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(order, v.getId(), holder.getPosition());
                    }
                }
            });
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(order, v.getId(), holder.getPosition());
                    }
                }
            });
        }
    }

    public void setOnOrderItemClickListener(OnOrderItemClickListener listener) {
        this.mListener = listener;
    }


    public interface OnOrderItemClickListener {
        /**
         * 点击事件分发
         *
         * @param item
         * @param viewId
         * @param position
         */
        void onItemClick(Order item, int viewId, int position);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
