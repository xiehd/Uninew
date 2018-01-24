package com.uninew.car.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.uninew.car.R;
import com.uninew.car.db.message.CarMessage;
import com.uninew.car.db.message.MessageKey;
import com.uninew.car.db.order.Order;
import com.uninew.car.db.order.OrderKey;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public class MessagesAdapter extends BaseAdapter<CarMessage> {

    private MessagesAdapter.OnMessageItemClickListener mListener;

    public MessagesAdapter(List<CarMessage> datas, Context context) {
        super(datas, context, R.layout.orders_item_view);
    }

    public void setOnMessageItemClickListener(MessagesAdapter.OnMessageItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void bindData(final BaseViewHolder holder, final CarMessage message) {
        int type = message.getMessageType();
        switch (type) {
            case MessageKey.MessageTypeKey.COMMUNIQUE_TYPE:
                holder.setText(R.id.tv_orders_type, R.string.msg_communique);
                break;
            case MessageKey.MessageTypeKey.EVENT_TYPE:
                holder.setText(R.id.tv_orders_type, R.string.msg_event);
                break;
            case MessageKey.MessageTypeKey.QUESTION_TYPE:
                holder.setText(R.id.tv_orders_type, R.string.msg_question);
                break;
        }
        if (type != MessageKey.MessageTypeKey.QUESTION_TYPE) {
            int readState = message.getReadState();
            switch (readState) {
                case MessageKey.MessageReadState.READED:
                    holder.setText(R.id.tv_orders_state, R.string.msg_readed);
                    holder.setImageResource(R.id.iv_orders_state, R.mipmap.finish);
                    break;
                case MessageKey.MessageReadState.UNREAD:
                    holder.setText(R.id.tv_orders_state, R.string.msg_unread);
                    holder.setImageResource(R.id.iv_orders_state, R.mipmap.cancel);
                    break;
            }
        } else {
            int answerState = message.getAnswerState();
            switch (answerState) {
                case MessageKey.MessageAnswerState.ANSWERED:
                    holder.setText(R.id.tv_orders_state, R.string.answered);
                    holder.setImageResource(R.id.iv_orders_state, R.mipmap.finish);
                    break;
                case MessageKey.MessageAnswerState.UNANSWERED:
                    holder.setText(R.id.tv_orders_state, R.string.unanswered);
                    holder.setImageResource(R.id.iv_orders_state, R.mipmap.cancel);
                    break;
            }
        }
        holder.setVisibility(R.id.tv_orders_state_time, View.GONE);
        holder.setText(R.id.tv_orders_finish_time, message.getTime());
        holder.setText(R.id.tv_orders_address, message.getContent());
        holder.setOnClickListener(R.id.iv_into, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(message, v.getId(), holder.getPosition());
                }
            }
        });
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(message, v.getId(), holder.getPosition());
                }
            }
        });
    }

    public interface OnMessageItemClickListener {
        /**
         * 点击事件分发
         *
         * @param item
         * @param viewId
         * @param position
         */
        void onItemClick(CarMessage item, int viewId, int position);
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
