package com.uninew.car.phone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.uninew.car.R;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/9/2 0002.
 */

public class DialerFragment extends Fragment implements DialerContract.View, View.OnClickListener {

    private DialerPresenter mDialPresenter;
    private View mViem;
    private EditText et_input;
    private Button one;
    private Button two;
    private Button three;
    private Button four;
    private Button five;
    private Button six;
    private Button seven;
    private Button eight;
    private Button nine;
    private Button zero;
    private Button del;
    private Button xin;
    private Button jin;
    private ImageButton img_del;
    private Button call;

    private static volatile DialerFragment INSTANCE;

    public static DialerFragment getInstance() {
        if (INSTANCE != null) {

        } else {
            synchronized (DialerFragment.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DialerFragment();
                }
            }
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mViem == null) {
            mViem = inflater.inflate(R.layout.fragment_dialer, container, false);
            init();
        }
        return mViem;
    }

    private void init() {
        mDialPresenter = new DialerPresenter(this,this.getActivity().getApplicationContext());
        initView();
        hideKeyboard();
        mDialPresenter.init(et_input.getText().toString().trim());
        initAction();
    }

    private void initView() {
        et_input = (EditText) mViem.findViewById(R.id.et_input);
        one = (Button) mViem.findViewById(R.id.key_one);
        two = (Button) mViem.findViewById(R.id.key_two);
        three = (Button) mViem.findViewById(R.id.key_three);
        four = (Button) mViem.findViewById(R.id.key_four);
        five = (Button) mViem.findViewById(R.id.key_five);
        six = (Button) mViem.findViewById(R.id.key_six);
        seven = (Button) mViem.findViewById(R.id.key_seven);
        eight = (Button) mViem.findViewById(R.id.key_eight);
        nine = (Button) mViem.findViewById(R.id.key_nine);
        zero = (Button) mViem.findViewById(R.id.key_zero);
        del = (Button) mViem.findViewById(R.id.key_del);
        xin = (Button) mViem.findViewById(R.id.key_xin);
        jin = (Button) mViem.findViewById(R.id.key_jin);
        img_del = (ImageButton) mViem.findViewById(R.id.img_del);
        call = (Button) mViem.findViewById(R.id.key_call);
    }

    /**
     * 点击是事件初始化的方法
     */
    private void initAction() {
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        del.setOnClickListener(this);
        xin.setOnClickListener(this);
        jin.setOnClickListener(this);
        img_del.setOnClickListener(this);
        call.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDialPresenter != null) {
            mDialPresenter.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDialPresenter != null) {
            mDialPresenter.stop();
        }
    }

    @Override
    public void setPresenter(DialerContract.Presenter presenter) {

    }

    @Override
    public void showPhone(String number) {
        et_input.setText(number);
        et_input.setSelection(number.length());
    }

    /**
     * 隐藏输入框的软键盘
     */
    private void hideKeyboard() {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            et_input.setInputType(InputType.TYPE_NULL);
        } else {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus;
                setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(et_input, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int index = et_input.getSelectionStart();
        int id = v.getId();
        switch (id) {
            case R.id.key_one:
                mDialPresenter.enterPhone(index, "1");
                break;
            case R.id.key_two:
                mDialPresenter.enterPhone(index, "2");
                break;
            case R.id.key_three:
                mDialPresenter.enterPhone(index, "3");
                break;
            case R.id.key_four:
                mDialPresenter.enterPhone(index, "4");
                break;
            case R.id.key_five:
                mDialPresenter.enterPhone(index, "5");
                break;
            case R.id.key_six:
                mDialPresenter.enterPhone(index, "6");
                break;
            case R.id.key_seven:
                mDialPresenter.enterPhone(index, "7");
                break;
            case R.id.key_eight:
                mDialPresenter.enterPhone(index, "8");
                break;
            case R.id.key_nine:
                mDialPresenter.enterPhone(index, "9");
                break;
            case R.id.key_zero:
                mDialPresenter.enterPhone(index, "0");
                break;
            case R.id.key_xin:
                mDialPresenter.enterPhone(index, "*");
                break;
            case R.id.key_jin:
                mDialPresenter.enterPhone(index, "#");
                break;
            /** 清空 **/
            case R.id.img_del:
                mDialPresenter.cleared();
                break;
            /** 删除 **/
            case R.id.key_del:
                mDialPresenter.delete(index);
                break;
            case R.id.key_call:
                mDialPresenter.call(et_input.getText().toString());
                break;
            default:
                break;
        }
    }
}
