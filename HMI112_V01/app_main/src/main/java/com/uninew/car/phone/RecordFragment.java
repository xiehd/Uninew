package com.uninew.car.phone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.uninew.car.R;
import com.uninew.car.adapter.RecordAdapter;
import com.uninew.car.db.dialler.Dialer;

import java.util.List;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class RecordFragment extends Fragment implements RecordContract.View,RecordAdapter.RecordsItemCallBack{

    private View mView;
    private ListView lv_record_show;
    private RecordContract.Presenter mPresenter;
    private RecordAdapter mAdapter;

    private static volatile RecordFragment INSTANCE;

    public static RecordFragment getInstance() {
        if (INSTANCE != null) {

        } else {
            synchronized (RecordFragment.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecordFragment();
                }
            }
        }
        return INSTANCE;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_record, container, false);
            init();
        }
        return mView;
    }

    private void init() {
        if (getActivity() != null) {
            mPresenter = new RecordPresenter(this, this.getActivity().getApplicationContext());
            initView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.stop();
        }
    }

    private void initView() {
        lv_record_show = (ListView) mView.findViewById(R.id.lv_record_show);
    }

    @Override
    public void setPresenter(RecordContract.Presenter presenter) {

    }

    @Override
    public void showDailers(List<Dialer> dialers) {
        if (getActivity() != null) {
            mAdapter = new RecordAdapter(dialers, this.getActivity().getApplicationContext());
            mAdapter.setRecordsItemCallBack(this);
            lv_record_show.setAdapter(mAdapter);
        }
    }

    @Override
    public void onClick(View v, Dialer dialer) {
        mPresenter.call(dialer.getPhone());
    }
}
