package com.uninew.maintanence.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uninew.maintanence.interfaces.IDataPresenter;
import com.uninew.maintanence.interfaces.IDataView;
import com.uninew.maintanence.presenter.DataPresenter;
import com.uninew.settings.R;


/**
 * Created by Administrator on 2017/8/30.
 */


public class DataFragment extends Fragment implements View.OnClickListener, IDataView {
    private View view;
    private Button btn_driver_msg;
    private Button btn_data_import;
    private Button btn_data_export;
    private Button btn_data_recovery;
    private Button btn_data_cleardb;
    private Button btn_data_log_export;
    private Button btn_revenue_export;
    private Button btn_signOrSignOut_export;
    private Button btn_alarm_export;

    private IDataPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_device_data, container, false);
        initView();
        initListener();
        return view;
    }

    private void init() {
        if (getActivity() != null)
            mPresenter = new DataPresenter(this, this.getActivity().getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.start();
        }
    }


    private void initView() {
        btn_driver_msg = (Button) view.findViewById(R.id.btn_driver_msg);
        btn_data_import = (Button) view.findViewById(R.id.btn_data_import);
        btn_data_export = (Button) view.findViewById(R.id.btn_data_export);
        btn_data_recovery = (Button) view.findViewById(R.id.btn_data_recovery);
        btn_data_cleardb = (Button) view.findViewById(R.id.btn_data_cleardb);
        btn_data_log_export = (Button) view.findViewById(R.id.btn_data_log_export);
        btn_revenue_export = (Button) view.findViewById(R.id.btn_revenue_export);
        btn_signOrSignOut_export = (Button) view.findViewById(R.id.btn_signOrSignOut_export);
        btn_alarm_export = (Button) view.findViewById(R.id.btn_alarm_export);
    }

    private void initListener() {
        btn_alarm_export.setOnClickListener(this);
        btn_data_cleardb.setOnClickListener(this);
        btn_data_export.setOnClickListener(this);
        btn_data_import.setOnClickListener(this);
        btn_data_log_export.setOnClickListener(this);
        btn_data_recovery.setOnClickListener(this);
        btn_driver_msg.setOnClickListener(this);
        btn_revenue_export.setOnClickListener(this);
        btn_signOrSignOut_export.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_alarm_export://报警导出
                if (mPresenter != null) {
                    mPresenter.DataExport(5);
                }
                break;
            case R.id.btn_data_cleardb:
                break;
            case R.id.btn_data_export:
                if (mPresenter != null) {
                    mPresenter.DataExport(1);
                }
                break;
            case R.id.btn_data_import:
                if (mPresenter != null) {
                    mPresenter.DataImport(1);
                }
                break;
            case R.id.btn_data_log_export:
                if (mPresenter != null) {
                    mPresenter.DataExport(2);
                }
                break;
            case R.id.btn_driver_msg:
                if (mPresenter != null) {
                    mPresenter.DataImport(0);
                }
                break;
            case R.id.btn_revenue_export://签到签退导出
                if (mPresenter != null) {
                    mPresenter.DataExport(4);
                }
                break;
            case R.id.btn_signOrSignOut_export://运营数据导出
                if (mPresenter != null) {
                    mPresenter.DataExport(3);
                }
                break;
            case R.id.btn_data_recovery:
                break;
        }
    }

    @Override
    public void onSuccess(int type) {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null)
            mPresenter.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.stop();
    }

    @Override
    public void onFailure(int type, int error) {

    }
}
