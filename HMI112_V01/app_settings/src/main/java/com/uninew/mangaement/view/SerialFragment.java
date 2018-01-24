package com.uninew.mangaement.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.uninew.net.JT905.comm.client.ClientSendManage;
import com.uninew.net.JT905.comm.client.IClientSendManage;
import com.uninew.settings.R;
import com.uninew.until.SPKey;
import com.uninew.until.SPTools;


/**
 * Created by Administrator on 2017/8/30.
 */


public class SerialFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private View view;

    private Spinner spinner_232_1_rate, spinner_232_2_rate, spinner_232_3_rate, spinner_485_1_rate,
            spinner_232_1_out, spinner_232_2_out, spinner_232_3_out, spinner_485_1_out;
    private Button serial_btn_defult, serial_btn_save;
    private SPTools mSPTools;
    private IClientSendManage mClientSendManage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mangae_serial, container, false);
        mSPTools = new SPTools(this.getActivity());
        mClientSendManage = new ClientSendManage(this.getActivity());
        initView();
        SetListener();
        return view;
    }

    private void initView() {
        spinner_232_1_rate = (Spinner) view.findViewById(R.id.spinner_232_1_rate);
        spinner_232_1_out = (Spinner) view.findViewById(R.id.spinner_232_1_out);
        spinner_232_2_rate = (Spinner) view.findViewById(R.id.spinner_232_2_rate);
        spinner_232_2_out = (Spinner) view.findViewById(R.id.spinner_232_2_out);
        spinner_232_3_rate = (Spinner) view.findViewById(R.id.spinner_232_3_rate);
        spinner_232_3_out = (Spinner) view.findViewById(R.id.spinner_232_3_out);
        spinner_485_1_rate = (Spinner) view.findViewById(R.id.spinner_485_1_rate);
        spinner_485_1_out = (Spinner) view.findViewById(R.id.spinner_485_1_out);

        serial_btn_defult = (Button) view.findViewById(R.id.serial_btn_defult);
        serial_btn_save = (Button) view.findViewById(R.id.serial_btn_save);

    }

    private void SetListener() {
        serial_btn_save.setOnClickListener(this);
        serial_btn_defult.setOnClickListener(this);
        ArrayAdapter<String> adapterSpinner_rate = new ArrayAdapter<String>(getContext(), R.layout.spinner_item,
                this.getActivity().getResources().getStringArray(R.array.serial_rate_spinner));
        spinner_232_1_rate.setAdapter(adapterSpinner_rate);
        spinner_232_1_rate.setSelection(mSPTools.getSharedInt(SPKey.SerialKey.RATE_232_1));

        spinner_232_2_rate.setAdapter(adapterSpinner_rate);
        spinner_232_2_rate.setSelection(mSPTools.getSharedInt(SPKey.SerialKey.RATE_232_2));

        spinner_232_3_rate.setAdapter(adapterSpinner_rate);
        spinner_232_3_rate.setSelection(mSPTools.getSharedInt(SPKey.SerialKey.RATE_232_3));

        spinner_485_1_rate.setAdapter(adapterSpinner_rate);
        spinner_485_1_rate.setSelection(mSPTools.getSharedInt(SPKey.SerialKey.RATE_485_1));


        ArrayAdapter<String> adapterSpinner_out = new ArrayAdapter<String>(getContext(), R.layout.spinner_item,
                this.getActivity().getResources().getStringArray(R.array.serial_out_spinner));
        spinner_232_1_out.setAdapter(adapterSpinner_out);
        spinner_232_1_out.setSelection(mSPTools.getSharedInt(SPKey.SerialKey.serial_out_232_1));

        spinner_232_2_out.setAdapter(adapterSpinner_out);
        spinner_232_2_out.setSelection(mSPTools.getSharedInt(SPKey.SerialKey.serial_out_232_2));

        spinner_232_3_out.setAdapter(adapterSpinner_out);
        spinner_232_3_out.setSelection(mSPTools.getSharedInt(SPKey.SerialKey.serial_out_232_3));

        spinner_485_1_out.setAdapter(adapterSpinner_out);
        spinner_485_1_out.setSelection(mSPTools.getSharedInt(SPKey.SerialKey.serial_out_485_1));

        spinner_232_1_rate.setOnItemSelectedListener(this);
        spinner_232_2_rate.setOnItemSelectedListener(this);
        spinner_232_3_rate.setOnItemSelectedListener(this);
        spinner_485_1_rate.setOnItemSelectedListener(this);
        spinner_232_1_out.setOnItemSelectedListener(this);
        spinner_232_2_out.setOnItemSelectedListener(this);
        spinner_232_3_out.setOnItemSelectedListener(this);
        spinner_485_1_out.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.serial_btn_save:
                break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //获取选中值
        Spinner spinner = (Spinner) parent;
        String data = (String) spinner.getItemAtPosition(position);
        switch (spinner.getId()){
            case R.id.spinner_232_1_rate:
                mSPTools.putSharedInt(SPKey.SerialKey.RATE_232_1,position);
                mClientSendManage.setBaudRate((byte) 0x01,(byte) (++position));
                break;
            case R.id.spinner_232_2_rate:
                mSPTools.putSharedInt(SPKey.SerialKey.RATE_232_2,position);
                mClientSendManage.setBaudRate((byte) 0x02,(byte) (++position));
                break;
            case R.id.spinner_232_3_rate:
                mSPTools.putSharedInt(SPKey.SerialKey.RATE_232_3,position);
                mClientSendManage.setBaudRate((byte) 0x03,(byte) (++position));
                break;
            case R.id.spinner_485_1_rate:
                mSPTools.putSharedInt(SPKey.SerialKey.RATE_485_1,position);
                mClientSendManage.setBaudRate((byte) 0x04,(byte) (++position));
                break;
            case R.id.spinner_232_1_out:
                mSPTools.putSharedInt(SPKey.SerialKey.serial_out_232_1,position);
                break;
            case R.id.spinner_232_2_out:
                mSPTools.putSharedInt(SPKey.SerialKey.serial_out_232_2,position);
                break;
            case R.id.spinner_232_3_out:
                mSPTools.putSharedInt(SPKey.SerialKey.serial_out_232_3,position);
                break;
            case R.id.spinner_485_1_out:
                mSPTools.putSharedInt(SPKey.SerialKey.serial_out_485_1,position);
                break;
        }
       // Toast.makeText(this.getActivity(),":"+data,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
