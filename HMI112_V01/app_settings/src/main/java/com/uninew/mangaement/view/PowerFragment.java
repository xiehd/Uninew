package com.uninew.mangaement.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.uninew.settings.R;


/**
 * Created by Administrator on 2017/8/30.
 */


public class PowerFragment extends Fragment implements View.OnClickListener{
    private View view;
    private Spinner spinner_mangae_power_acc,spinner_mangae_power_bootmodel;

    private EditText mangae_power_noboottime,mangae_power_acc_battery_low,mangae_power_recovery_power;
    private CheckBox mangae_power_lowpower,mangae_power_battery_alarm;
    private Button power_btn_defult,power_btn_save;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mangae_power, container, false);
        initView();
        SetListener();
        return view;
    }

    private void initView() {
        spinner_mangae_power_acc = (Spinner) view.findViewById(R.id.spinner_mangae_power_acc);
        spinner_mangae_power_bootmodel = (Spinner) view.findViewById(R.id.spinner_mangae_power_bootmodel);

        mangae_power_noboottime = (EditText) view.findViewById(R.id.mangae_power_noboottime);
        mangae_power_acc_battery_low = (EditText) view.findViewById(R.id.mangae_power_acc_battery_low);
        mangae_power_recovery_power = (EditText) view.findViewById(R.id.mangae_power_recovery_power);

        mangae_power_battery_alarm = (CheckBox) view.findViewById(R.id.mangae_power_battery_alarm);
        mangae_power_lowpower = (CheckBox) view.findViewById(R.id.mangae_power_lowpower);

        power_btn_defult = (Button) view.findViewById(R.id.power_btn_defult);
        power_btn_save = (Button) view.findViewById(R.id.power_btn_save);


    }

    private void SetListener(){

        ArrayAdapter<String> adapter_acc = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,
                this.getActivity().getResources().getStringArray(R.array.power_acc_spinner));
        spinner_mangae_power_acc.setAdapter(adapter_acc);
        ArrayAdapter<String> adapter_bootmodel = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,
                this.getActivity().getResources().getStringArray(R.array.power_bootmodel_spinner));
        spinner_mangae_power_bootmodel.setAdapter(adapter_bootmodel);

        power_btn_defult.setOnClickListener(this);
        power_btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.power_btn_defult:
                break;
            case R.id.power_btn_save:
                break;
        }
    }
}
