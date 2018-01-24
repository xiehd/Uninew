package com.uninew.settings.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;

import com.uninew.settings.R;
import com.uninew.settings.interfaces.ILightPresenter;
import com.uninew.settings.interfaces.ILightView;
import com.uninew.settings.presenter.LightPresenter;


/**
 * Created by Administrator on 2017/8/30.
 */


public class LightFragment extends Fragment implements ILightView, View.OnClickListener {
    private View view;
    private ILightPresenter mILightPresenter;

    private SeekBar more_light_seekbar;
    private Switch more_light_switch;
    private int mCurrentLight = 0;
    private ImageView light_image_add, light_image_sub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_light, container, false);
        initView();
        mILightPresenter = new LightPresenter(this.getActivity(), this);
        this.getActivity().getWindow();
        SetListener();
        return view;
    }

    private void initView() {
        more_light_seekbar = (SeekBar) view.findViewById(R.id.more_light_seekbar);
        more_light_switch = (Switch) view.findViewById(R.id.more_light_switch);

        light_image_add = (ImageView) view.findViewById(R.id.light_image_add);
        light_image_sub = (ImageView) view.findViewById(R.id.light_image_sub);

    }

    private void SetListener() {

        light_image_add.setOnClickListener(this);
        light_image_sub.setOnClickListener(this);
        more_light_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mILightPresenter.setAutoBrightness(1);
                } else {
                    mILightPresenter.setAutoBrightness(0);
                }
            }
        });
        more_light_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCurrentLight = progress;
                mILightPresenter.setLight(mCurrentLight);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void ShowTochLight(boolean mode) {
        more_light_switch.setChecked(mode);
    }

    @Override
    public void ShowCurrentLight(int light) {
        mCurrentLight = light;
        more_light_seekbar.setProgress(mCurrentLight);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.light_image_add:
                mCurrentLight++;
                more_light_seekbar.setProgress(mCurrentLight);
                break;
            case R.id.light_image_sub:
                mCurrentLight--;
                more_light_seekbar.setProgress(mCurrentLight);
                break;
        }
    }
}
