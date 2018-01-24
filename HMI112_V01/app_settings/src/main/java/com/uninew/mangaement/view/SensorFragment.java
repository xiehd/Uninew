package com.uninew.mangaement.view;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.uninew.settings.R;
import com.uninew.until.SPKey;
import com.uninew.until.SPTools;


/**
 * Created by Administrator on 2017/8/30.
 */


public class SensorFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private View view;
    private RadioGroup sensor_radioGroup;
    private LinearLayout sensor_layout_in, sensor_layout_out;
    private CheckBox sensor_chekbox1, sensor_chekbox2, sensor_chekbox3, sensor_chekbox4;
    private Spinner spinner_sensor_chekbox_a, spinner_sensor_chekbox_b, spinner_sensor_chekbox_c,
            spinner_sensor_chekbox2_a, spinner_sensor_chekbox2_b, spinner_sensor_chekbox2_c,
            spinner_sensor_chekbox3_a, spinner_sensor_chekbox3_b, spinner_sensor_chekbox3_c,
            spinner_sensor_chekbox4_a, spinner_sensor_chekbox4_b, spinner_sensor_chekbox4_c,
            spinner_sensor_out1, spinner_sensor_out2, spinner_sensor_out3, spinner_sensor_out4;
    private Button sensor_out_btn_save, sensor_in_btn_save;
    private SPTools mSPTools;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mangae_sensor, container, false);
        mSPTools = new SPTools(this.getActivity());
        initView();
        SetListener();
        init();
        return view;
    }

    private void initView() {
        sensor_radioGroup = (RadioGroup) view.findViewById(R.id.sensor_radioGroup);
        sensor_layout_in = (LinearLayout) view.findViewById(R.id.sensor_layout_in);
        sensor_layout_out = (LinearLayout) view.findViewById(R.id.sensor_layout_out);

        sensor_out_btn_save = (Button) view.findViewById(R.id.sensor_out_btn_save);
        sensor_in_btn_save = (Button) view.findViewById(R.id.sensor_in_btn_save);

        sensor_chekbox1 = (CheckBox) view.findViewById(R.id.sensor_chekbox1);
        sensor_chekbox2 = (CheckBox) view.findViewById(R.id.sensor_chekbox2);
        sensor_chekbox3 = (CheckBox) view.findViewById(R.id.sensor_chekbox3);
        sensor_chekbox4 = (CheckBox) view.findViewById(R.id.sensor_chekbox4);

        spinner_sensor_chekbox_a = (Spinner) view.findViewById(R.id.spinner_sensor_chekbox_a);
        spinner_sensor_chekbox_b = (Spinner) view.findViewById(R.id.spinner_sensor_chekbox_b);
        spinner_sensor_chekbox_c = (Spinner) view.findViewById(R.id.spinner_sensor_chekbox_c);

        spinner_sensor_chekbox2_a = (Spinner) view.findViewById(R.id.spinner_sensor_chekbox2_a);
        spinner_sensor_chekbox2_b = (Spinner) view.findViewById(R.id.spinner_sensor_chekbox2_b);
        spinner_sensor_chekbox2_c = (Spinner) view.findViewById(R.id.spinner_sensor_chekbox2_c);

        spinner_sensor_chekbox3_a = (Spinner) view.findViewById(R.id.spinner_sensor_chekbox3_a);
        spinner_sensor_chekbox3_b = (Spinner) view.findViewById(R.id.spinner_sensor_chekbox3_b);
        spinner_sensor_chekbox3_c = (Spinner) view.findViewById(R.id.spinner_sensor_chekbox3_c);

        spinner_sensor_chekbox4_a = (Spinner) view.findViewById(R.id.spinner_sensor_chekbox4_a);
        spinner_sensor_chekbox4_b = (Spinner) view.findViewById(R.id.spinner_sensor_chekbox4_b);
        spinner_sensor_chekbox4_c = (Spinner) view.findViewById(R.id.spinner_sensor_chekbox4_c);

        spinner_sensor_out1 = (Spinner) view.findViewById(R.id.spinner_sensor_out1);
        spinner_sensor_out2 = (Spinner) view.findViewById(R.id.spinner_sensor_out2);
        spinner_sensor_out3 = (Spinner) view.findViewById(R.id.spinner_sensor_out3);
        spinner_sensor_out4 = (Spinner) view.findViewById(R.id.spinner_sensor_out4);

    }

    private void SetListener() {
        sensor_radioGroup.setOnCheckedChangeListener(this);
        sensor_in_btn_save.setOnClickListener(this);
        sensor_out_btn_save.setOnClickListener(this);
        ArrayAdapter<String> adapter_a = new ArrayAdapter<String>(getContext(), R.layout.spinner_item,
                this.getActivity().getResources().getStringArray(R.array.sensor_a_spinner));
        spinner_sensor_chekbox_a.setAdapter(adapter_a);
        spinner_sensor_chekbox_a.setSelection(mSPTools.getSharedInt(SPKey.IOKey.IOKey_use1));

        spinner_sensor_chekbox2_a.setAdapter(adapter_a);
        spinner_sensor_chekbox2_a.setSelection(mSPTools.getSharedInt(SPKey.IOKey.IOKey_use2));

        spinner_sensor_chekbox3_a.setAdapter(adapter_a);
        spinner_sensor_chekbox3_a.setSelection(mSPTools.getSharedInt(SPKey.IOKey.IOKey_use3));

        spinner_sensor_chekbox4_a.setAdapter(adapter_a);
        spinner_sensor_chekbox4_a.setSelection(mSPTools.getSharedInt(SPKey.IOKey.IOKey_use4));

        ArrayAdapter<String> adapter_b = new ArrayAdapter<String>(getContext(), R.layout.spinner2_item,
                this.getActivity().getResources().getStringArray(R.array.sensor_b_spinner));
        spinner_sensor_chekbox_b.setAdapter(adapter_b);
        spinner_sensor_chekbox_b.setSelection(mSPTools.getSharedInt(SPKey.IOKey.IOKey_level1));

        spinner_sensor_chekbox2_b.setAdapter(adapter_b);
        spinner_sensor_chekbox2_b.setSelection(mSPTools.getSharedInt(SPKey.IOKey.IOKey_level2));

        spinner_sensor_chekbox3_b.setAdapter(adapter_b);
        spinner_sensor_chekbox3_b.setSelection(mSPTools.getSharedInt(SPKey.IOKey.IOKey_level3));

        spinner_sensor_chekbox4_b.setAdapter(adapter_b);
        spinner_sensor_chekbox4_b.setSelection(mSPTools.getSharedInt(SPKey.IOKey.IOKey_level4));
        ArrayAdapter<String> adapter_c = new ArrayAdapter<String>(getContext(), R.layout.spinner2_item,
                this.getActivity().getResources().getStringArray(R.array.sensor_c_spinner));
        spinner_sensor_chekbox_c.setAdapter(adapter_c);
        spinner_sensor_chekbox_c.setSelection(mSPTools.getSharedInt(SPKey.IOKey.IOKey_dvr1));

        spinner_sensor_chekbox2_c.setAdapter(adapter_c);
        spinner_sensor_chekbox2_c.setSelection(mSPTools.getSharedInt(SPKey.IOKey.IOKey_dvr2));

        spinner_sensor_chekbox3_c.setAdapter(adapter_c);
        spinner_sensor_chekbox3_c.setSelection(mSPTools.getSharedInt(SPKey.IOKey.IOKey_dvr3));

        spinner_sensor_chekbox4_c.setAdapter(adapter_c);
        spinner_sensor_chekbox4_c.setSelection(mSPTools.getSharedInt(SPKey.IOKey.IOKey_dvr4));
        ArrayAdapter<String> adapter_out = new ArrayAdapter<String>(getContext(), R.layout.spinner_item,
                this.getActivity().getResources().getStringArray(R.array.sensor_out_spinner));
        spinner_sensor_out1.setAdapter(adapter_out);
        spinner_sensor_out1.setSelection(mSPTools.getSharedInt(SPKey.IOKey.IOKey_out_use1));

        spinner_sensor_out2.setAdapter(adapter_out);
        spinner_sensor_out2.setSelection(mSPTools.getSharedInt(SPKey.IOKey.IOKey_out_use2));

        spinner_sensor_out3.setAdapter(adapter_out);
        spinner_sensor_out3.setSelection(mSPTools.getSharedInt(SPKey.IOKey.IOKey_out_use3));

        spinner_sensor_out4.setAdapter(adapter_out);
        spinner_sensor_out4.setSelection(mSPTools.getSharedInt(SPKey.IOKey.IOKey_out_use4));
    }

    private void init() {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.mangae_sensor_in:
                sensor_layout_in.setVisibility(View.VISIBLE);
                sensor_layout_out.setVisibility(View.GONE);
                break;
            case R.id.mangae_sensor_out:
                sensor_layout_in.setVisibility(View.GONE);
                sensor_layout_out.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sensor_in_btn_save:
                if (sensor_chekbox1.isChecked()) {
                    String string_a = spinner_sensor_chekbox_a.getSelectedItem().toString();
                    String string_b = spinner_sensor_chekbox_b.getSelectedItem().toString();
                    String string_c = spinner_sensor_chekbox_c.getSelectedItem().toString();
                    mSPTools.putSharedInt(SPKey.IOKey.IOKey_use1, spinner_sensor_chekbox_a.getSelectedItemPosition());
                    mSPTools.putSharedInt(SPKey.IOKey.IOKey_level1, spinner_sensor_chekbox_b.getSelectedItemPosition());
                    mSPTools.putSharedInt(SPKey.IOKey.IOKey_dvr1, spinner_sensor_chekbox_c.getSelectedItemPosition());
                }
                if (sensor_chekbox2.isChecked()) {
                    mSPTools.putSharedInt(SPKey.IOKey.IOKey_use2, spinner_sensor_chekbox2_a.getSelectedItemPosition());
                    mSPTools.putSharedInt(SPKey.IOKey.IOKey_level2, spinner_sensor_chekbox2_b.getSelectedItemPosition());
                    mSPTools.putSharedInt(SPKey.IOKey.IOKey_dvr2, spinner_sensor_chekbox2_c.getSelectedItemPosition());

                }
                if (sensor_chekbox3.isChecked()) {
                    mSPTools.putSharedInt(SPKey.IOKey.IOKey_use3, spinner_sensor_chekbox3_a.getSelectedItemPosition());
                    mSPTools.putSharedInt(SPKey.IOKey.IOKey_level3, spinner_sensor_chekbox3_b.getSelectedItemPosition());
                    mSPTools.putSharedInt(SPKey.IOKey.IOKey_dvr3, spinner_sensor_chekbox3_c.getSelectedItemPosition());
                }
                if (sensor_chekbox4.isChecked()) {
                    mSPTools.putSharedInt(SPKey.IOKey.IOKey_use4, spinner_sensor_chekbox4_a.getSelectedItemPosition());
                    mSPTools.putSharedInt(SPKey.IOKey.IOKey_level4, spinner_sensor_chekbox4_b.getSelectedItemPosition());
                    mSPTools.putSharedInt(SPKey.IOKey.IOKey_dvr4, spinner_sensor_chekbox4_c.getSelectedItemPosition());
                }
                break;
            case R.id.sensor_out_btn_save:
                mSPTools.putSharedInt(SPKey.IOKey.IOKey_out_use1, spinner_sensor_out1.getSelectedItemPosition());
                mSPTools.putSharedInt(SPKey.IOKey.IOKey_out_use2, spinner_sensor_out2.getSelectedItemPosition());
                mSPTools.putSharedInt(SPKey.IOKey.IOKey_out_use3, spinner_sensor_out3.getSelectedItemPosition());
                mSPTools.putSharedInt(SPKey.IOKey.IOKey_out_use4, spinner_sensor_out4.getSelectedItemPosition());
                break;
        }
    }
}
