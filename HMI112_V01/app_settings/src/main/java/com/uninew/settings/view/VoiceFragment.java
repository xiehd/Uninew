package com.uninew.settings.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;

import com.uninew.settings.R;
import com.uninew.settings.interfaces.IVoicePresenter;
import com.uninew.settings.interfaces.IVoiceView;
import com.uninew.settings.presenter.VoicePresenter;


/**
 * Created by Administrator on 2017/8/30.
 */


public class VoiceFragment extends Fragment implements IVoiceView,View.OnClickListener{
    private View view;
    private SeekBar more_voice_seekbar;
    private Switch more_system_switch;
    private IVoicePresenter mIVoicePresenter;
    private int mCerrentVoice = 0;
    private ImageView voice_image_sub,voice_image_add;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_voice, container, false);
        initView();
        mIVoicePresenter = new VoicePresenter(this);
        SetListener();
        return view;
    }

    private void initView(){
        more_system_switch = (Switch) view.findViewById(R.id.more_system_switch);
        more_voice_seekbar = (SeekBar) view.findViewById(R.id.more_voice_seekbar);
        voice_image_sub = (ImageView) view.findViewById(R.id.voice_image_sub);
        voice_image_add = (ImageView) view.findViewById(R.id.voice_image_add);
    }

    private void SetListener(){
        voice_image_sub.setOnClickListener(this);
        voice_image_add.setOnClickListener(this);


        more_system_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mIVoicePresenter.setTochVoice(isChecked);
                // mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
                //声音模式
                // AudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                //静音模式
                //AudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                //震动模式
                //AudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            }
        });
        more_voice_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mCerrentVoice = progress;
                mIVoicePresenter.setVoice(mCerrentVoice);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                mCerrentLight = seekBar.getProgress();
//                mIVoicePresenter.setVoice(mCerrentLight);
            }
        });
    }

    @Override
    public void ShowTochVoice(boolean mode) {
        more_system_switch.setChecked(mode);
    }

    @Override
    public void ShowCurrentVoice(int voice) {
        mCerrentVoice = voice;
            more_voice_seekbar.setProgress(mCerrentVoice);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.voice_image_add:
                mCerrentVoice++;
                more_voice_seekbar.setProgress(mCerrentVoice);
                break;
            case R.id.voice_image_sub:
                mCerrentVoice--;
                more_voice_seekbar.setProgress(mCerrentVoice);
                break;
        }
    }
}
