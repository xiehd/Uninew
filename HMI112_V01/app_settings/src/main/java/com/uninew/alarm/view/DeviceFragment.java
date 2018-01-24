package com.uninew.alarm.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.uninew.adapter.AlarmAdapter;
import com.uninew.settings.R;

/**
 * Created by Administrator on 2017/9/16.
 */

public class DeviceFragment extends Fragment {

    private View view;
    private Spinner speed_spinner;
    private TextView alarm_speed1, alarm_speed2;
    private ListView alarm_speed_list;
    private AlarmAdapter mAlarmAdapter;
    private AlarmActivity mAlarmActivity;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_device, container, false);
        mAlarmActivity = (AlarmActivity) this.getActivity();
        initView();
        SetListener();
        return view;
    }

    private  String[] spinnerText;
    private void initView(){
        spinnerText = this.getActivity().getResources().getStringArray(R.array.alarm_spinner);
        speed_spinner = (Spinner) view.findViewById(R.id.device_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, spinnerText);
        //adapter.setDropDownViewResource(R.layout.spinner_item);
        speed_spinner.setAdapter(adapter);
        alarm_speed1 = (TextView) view.findViewById(R.id.alarm_speed1);
       // alarm_speed2 = (TextView) view.findViewById(R.id.alarm_speed2);

        alarm_speed_list = (ListView) view.findViewById(R.id.alarm_device_list);
        mAlarmAdapter = mAlarmActivity.getAdapter(mAlarmActivity.getType());
        alarm_speed_list.setAdapter(mAlarmAdapter);
        alarm_speed1.setText(this.getActivity().getResources().getString(R.string.more_alarm_speed_text3)+mAlarmAdapter.getCount());
    }
    private void SetListener() {
    }
}
