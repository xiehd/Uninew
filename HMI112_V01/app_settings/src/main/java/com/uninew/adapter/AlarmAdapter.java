package com.uninew.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uninew.car.db.alarm.AlarmMessage;
import com.uninew.settings.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/30.
 */

public class AlarmAdapter extends BaseAdapter {

    private List<AlarmMessage> speedAlarms = new ArrayList<>();//速度报警
    private List<AlarmMessage> deviceAlarms = new ArrayList<>();//设备故障
    private List<AlarmMessage> otherAlarms = new ArrayList<>();//其他
    private LayoutInflater mLayoutInflater;
    private int type = 0;

    public AlarmAdapter(Context context, int type) {
        this.type = type;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public synchronized void setAlarmDate(List<AlarmMessage> allAlarms) {
        speedAlarms.clear();
        deviceAlarms.clear();
        otherAlarms.clear();
        for (AlarmMessage alarm : allAlarms) {
            if (alarm.getAlarmType() == 0) {
                speedAlarms.add(alarm);
            } else if (alarm.getAlarmType() == 1) {
                deviceAlarms.add(alarm);
            } else if (alarm.getAlarmType() == 2) {
                otherAlarms.add(alarm);
            }
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<AlarmMessage> getCurrenDate() {
        if (type == 0) {
            return speedAlarms;
        } else if (type == 1) {
            return deviceAlarms;
        }
        return otherAlarms;
    }

    @Override
    public int getCount() {
        if (type == 0) {
            return speedAlarms.size();
        } else if (type == 1) {
            return deviceAlarms.size();
        }
        return otherAlarms.size();
    }

    @Override
    public Object getItem(int position) {

        if (type == 0) {
            return speedAlarms.get(position);
        } else if (type == 1) {
            return deviceAlarms.get(position);
        }
        return otherAlarms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolper mViewHolper = null;
        if (convertView == null) {
            mViewHolper = new ViewHolper();
            if (type == 0) {
                convertView = mLayoutInflater.inflate(R.layout.alarm_list_item, null);
                mViewHolper.text_msg = (TextView) convertView.findViewById(R.id.alram_type);
                mViewHolper.text_startTime = (TextView) convertView.findViewById(R.id.speed_alarm_starttime);
                mViewHolper.text_continudTime = (TextView) convertView.findViewById(R.id.speed_alarm_continuedtime);
                mViewHolper.text_endTime = (TextView) convertView.findViewById(R.id.speed_alarm_endtime);
                mViewHolper.alram_index = (TextView) convertView.findViewById(R.id.alram_index);

            } else {
                convertView = mLayoutInflater.inflate(R.layout.alarm_list_item2, null);
                mViewHolper.text_msg = (TextView) convertView.findViewById(R.id.alram_type);
                mViewHolper.text_startTime = (TextView) convertView.findViewById(R.id.speed_alarm_starttime);
                mViewHolper.alram_index = (TextView) convertView.findViewById(R.id.alram_index);
            }
            convertView.setTag(mViewHolper);
        } else {
            mViewHolper = (ViewHolper) convertView.getTag();
        }

        if (type == 0) {
            mViewHolper.text_msg.setText(speedAlarms.get(position).getContent());
            mViewHolper.text_startTime.setText(speedAlarms.get(position).getStartTime());
            mViewHolper.text_continudTime.setText(speedAlarms.get(position).getContinuedTime()+"");
            mViewHolper.text_endTime.setText(speedAlarms.get(position).getStopTime());
            mViewHolper.alram_index.setText("" + (position + 1));
        } else if(type == 1){
            mViewHolper.text_msg.setText(deviceAlarms.get(position).getContent());
            mViewHolper.text_startTime.setText(deviceAlarms.get(position).getStartTime());
            mViewHolper.alram_index.setText("" + (position + 1));
        }else {
            mViewHolper.text_msg.setText(otherAlarms.get(position).getContent());
            mViewHolper.text_startTime.setText(otherAlarms.get(position).getStartTime());
            mViewHolper.alram_index.setText("" + (position + 1));
        }
        return convertView;
    }

    class ViewHolper {
        TextView text_msg, text_startTime, text_endTime, text_continudTime, alram_index;
    }
}
