package com.uninew.mangaement.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.uninew.mangaement.interfaces.IBaseSettingView;
import com.uninew.mangaement.presenter.BasePresenter;
import com.uninew.settings.R;


/**
 * Created by Administrator on 2017/8/30.
 */


public class BaseSettingFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener,
        IBaseSettingView {
    private View view;
    private static final String TAG = "BaseSettingFragment";
    private EditText mangae_setting_base_carNumber, mangae_setting_base_terminalNumber, mangae_setting_base_canmpertName,
            mangae_setting_base_dvrNumber, edi_latch_time, edi_delay_time;
    private Button mangae_setting_base_zhuce, mangae_btn_defult, mangae_btn_save;
    private Spinner spinner_base_time, spinner__base_printer;
    private String[] spinnerArray1, spinnerArray2;
    private BasePresenter mBasePresenter;

    private String plateNumber = "0000";
    private String terminalNumber = "0000";
    private String companyNumber = "0000";
    private String dvrSerialNumber = "0000";
    private String outTimeExite = "100";
    private String printSensitivity = "0";
    private String prerecordTime = "30";
    private String delayTime = "30";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mangae_base, container, false);
        Log.i("xhd","eeeeeeeeeeee");
        spinnerArray1 = this.getActivity().getResources().getStringArray(R.array.base_time_spinner);
        spinnerArray2 = this.getActivity().getResources().getStringArray(R.array.base_printer_spinner);
        Log.i("xhd","ffffffffffffff");
        initView();
        SetListener();
        Log.i("xhd","gggggggggggggg");
        mBasePresenter = new BasePresenter(this, this.getActivity());
        Log.i("xhd","kkkkkkkkkkkkk");
        return view;
    }

    private void initView() {
        mangae_setting_base_carNumber = (EditText) view.findViewById(R.id.mangae_setting_base_carNumber);
        mangae_setting_base_terminalNumber = (EditText) view.findViewById(R.id.mangae_setting_base_terminalNumber);
        mangae_setting_base_canmpertName = (EditText) view.findViewById(R.id.mangae_setting_base_canmpertName);
        mangae_setting_base_dvrNumber = (EditText) view.findViewById(R.id.mangae_setting_base_dvrNumber);
        edi_latch_time = (EditText) view.findViewById(R.id.edi_latch_time);
        edi_delay_time = (EditText) view.findViewById(R.id.edi_delay_time);

        mangae_setting_base_zhuce = (Button) view.findViewById(R.id.mangae_setting_base_zhuce);
        mangae_btn_defult = (Button) view.findViewById(R.id.mangae_btn_defult);
        mangae_btn_save = (Button) view.findViewById(R.id.mangae_btn_save);

        spinner_base_time = (Spinner) view.findViewById(R.id.spinner_base_time);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, spinnerArray1);
        spinner_base_time.setAdapter(adapter);

        spinner__base_printer = (Spinner) view.findViewById(R.id.spinner__base_printer);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, spinnerArray2);
        spinner__base_printer.setAdapter(adapter2);

    }

    private void SetListener() {
        spinner_base_time.setOnItemSelectedListener(this);
        spinner__base_printer.setOnItemSelectedListener(this);

        mangae_setting_base_zhuce.setOnClickListener(this);
        mangae_btn_defult.setOnClickListener(this);
        mangae_btn_save.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_base_time:
                // Log.i("xhd", "11111" + spinnerArray1[position]);
                outTimeExite = spinnerArray1[position];
                break;
            case R.id.spinner__base_printer:
                //Log.i("xhd", "2222" + spinnerArray2[position]);
                printSensitivity = spinnerArray2[position];
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mangae_btn_save://保存
                plateNumber = mangae_setting_base_carNumber.getText().toString();
                terminalNumber = mangae_setting_base_terminalNumber.getText().toString();
                companyNumber = mangae_setting_base_canmpertName.getText().toString();
                dvrSerialNumber = mangae_setting_base_dvrNumber.getText().toString();
                prerecordTime = edi_latch_time.getText().toString();
                delayTime = edi_delay_time.getText().toString();
                mBasePresenter.SaveInitData(plateNumber, terminalNumber, companyNumber, dvrSerialNumber,
                        outTimeExite, printSensitivity, prerecordTime, dvrSerialNumber);
                break;
            case R.id.mangae_btn_defult://默认
                mBasePresenter.Setdefault();
                break;
            case R.id.mangae_setting_base_zhuce://
                break;
        }

    }

    @Override
    public void ShowCarNumber(String carNumber) {
        mangae_setting_base_carNumber.setText(carNumber);
    }

    @Override
    public void ShowTerminal(String terminal) {
        mangae_setting_base_terminalNumber.setText(terminal);
    }

    @Override
    public void ShowCompanyName(String companyName) {
        mangae_setting_base_canmpertName.setText(companyName);
    }

    @Override
    public void ShowDvrsenNumber(String dvrNumber) {
        mangae_setting_base_dvrNumber.setText(dvrNumber);
    }

    @Override
    public void ShowOutTime(String outTime) {
        int index = 0;
        for (int i = 0; i < spinnerArray1.length; i++) {
            if (outTime.equals(spinnerArray1[i])) {
                index = i;
            }
        }
        spinner_base_time.setSelection(index, true);
    }

    @Override
    public void ShowPinter(String pinter) {
        int index = 0;
        for (int i = 0; i < spinnerArray2.length; i++) {
            if (pinter.equals(spinnerArray2[i])) {
                index = i;
            }
        }
        spinner__base_printer.setSelection(index, true);
    }

    @Override
    public void ShowPretime(String Pretime) {
        edi_latch_time.setText(Pretime);

    }

    @Override
    public void ShowDelaytime(String delaytime) {
        edi_delay_time.setText(delaytime);
    }
}
