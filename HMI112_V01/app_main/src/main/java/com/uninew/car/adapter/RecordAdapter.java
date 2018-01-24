package com.uninew.car.adapter;

import android.content.Context;
import android.provider.CallLog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.uninew.car.Main.MainContract;
import com.uninew.car.R;
import com.uninew.car.db.dialler.Dialer;
import com.uninew.car.db.dialler.DialerKey;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public class RecordAdapter extends BaseAdapter<Dialer> {

    private static final String TAG = "RecordAdapter";

    public RecordAdapter(List<Dialer> datas, Context context) {
        super(datas, context, R.layout.record_item_view);
    }

    @Override
    public void bindData(BaseViewHolder holder, final Dialer dialer) {
        if (dialer != null) {
            int state = dialer.getDiallerState();
            Log.d(TAG, "state:" + state);
            switch (state) {
                case CallLog.Calls.OUTGOING_TYPE:
                    holder.setImageResource(R.id.iv_call_state, R.mipmap.called_phone);
                    holder.setVisibility(R.id.iv_call_state, View.VISIBLE);
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    holder.setImageResource(R.id.iv_call_state, R.mipmap.missed_calls);
                    holder.setVisibility(R.id.iv_call_state, View.VISIBLE);
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    holder.setVisibility(R.id.iv_call_state, View.INVISIBLE);
                    break;
            }
            if(TextUtils.isEmpty(dialer.getContact())){
                holder.setText(R.id.tv_record_name, dialer.getPhone());
            }else {
                holder.setText(R.id.tv_record_name, dialer.getContact());
            }
            holder.setText(R.id.tv_record_time, dialer.getCallDate());
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallBack != null) {
                        mCallBack.onClick(v,dialer);
                    }
                }
            });
        }
    }

    private RecordsItemCallBack mCallBack;

    public void setRecordsItemCallBack(RecordsItemCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface RecordsItemCallBack {
        void onClick(View v, Dialer dialer);
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
