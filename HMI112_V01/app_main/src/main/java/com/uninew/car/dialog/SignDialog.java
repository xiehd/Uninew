package com.uninew.car.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.uninew.car.R;

import java.lang.reflect.Method;

public class SignDialog extends Dialog implements View.OnClickListener {

    private LayoutInflater layoutInflater;
    private View view;
    private Button sign_in;
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
    private EditText input;// 工号输入窗口
    private ImageButton img_del;

    private ClickListenerInterface clickListenerInterface;
    private String number;// 工号

    public SignDialog(Context context) {
        super(context, android.R.style.Theme);
        setOwnerActivity((Activity) context);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initAction();
    }

    /**
     * 签到或者签退按钮的点击事件
     *
     * @author rong
     */
    public interface ClickListenerInterface {
        /**
         * 签到事件处理的方法
         */
        void sign(String jobNumber);

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
        sign_in.setOnClickListener(this);
        img_del.setOnClickListener(this);
    }

    /**
     * 界面初始化的方法
     */
    private void initView() {
        view = layoutInflater.inflate(R.layout.sign_in_dalog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        one = (Button) view.findViewById(R.id.key_one);
        two = (Button) view.findViewById(R.id.key_two);
        three = (Button) view.findViewById(R.id.key_three);
        four = (Button) view.findViewById(R.id.key_four);
        five = (Button) view.findViewById(R.id.key_five);
        six = (Button) view.findViewById(R.id.key_six);
        seven = (Button) view.findViewById(R.id.key_seven);
        eight = (Button) view.findViewById(R.id.key_eight);
        nine = (Button) view.findViewById(R.id.key_nine);
        zero = (Button) view.findViewById(R.id.key_zero);
        del = (Button) view.findViewById(R.id.key_del);
        xin = (Button) view.findViewById(R.id.key_xin);
        jin = (Button) view.findViewById(R.id.key_jin);
        input = (EditText) view.findViewById(R.id.et_input);
        sign_in = (Button) view.findViewById(R.id.key_in);
        img_del = (ImageButton) findViewById(R.id.img_del);

        // 隐藏editText的软键盘的处理
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            input.setInputType(InputType.TYPE_NULL);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus;
                setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(input, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置工号框的显示参数
     *
     * @param number 工号
     */
    public void setJobNumber(String number) {
        if (!TextUtils.isEmpty(number)) {
            input.setText(number);
        }
    }


    /**
     * 得到工号
     *
     * @return
     */
    public String getJobNumber() {

        if (!TextUtils.isEmpty(number)) {
            return number;
        }
        return null;
    }


    /**
     * 添加点击事件的方法
     *
     * @param clickListenerInterface
     */
    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.key_one:
                showTextString("1");
                break;
            case R.id.key_two:
                showTextString("2");
                break;
            case R.id.key_three:
                showTextString("3");
                break;
            case R.id.key_four:
                showTextString("4");
                break;
            case R.id.key_five:
                showTextString("5");
                break;
            case R.id.key_six:
                showTextString("6");
                break;
            case R.id.key_seven:
                showTextString("7");
                break;
            case R.id.key_eight:
                showTextString("8");
                break;
            case R.id.key_nine:
                showTextString("9");
                break;
            case R.id.key_zero:
                showTextString("0");
                break;
            case R.id.key_xin:
                showTextString("*");
                break;
            case R.id.key_jin:
                showTextString("#");
                break;
            /** 清空 **/
            case R.id.img_del:
                if (input.hasFocus())
                    input.setText("");
                break;
            /** 删除 **/
            case R.id.key_del:
                if (input.hasFocus())
                    setSCText(input);
                break;
            /** 登录 **/
            case R.id.key_in:
                if (clickListenerInterface == null) {
                    break;
                }
                number = input.getText().toString().trim();
                clickListenerInterface.sign(number);
                break;
            default:
                break;
        }
    }

    /**
     * 删除退格的方法
     *
     * @param et
     */
    private void setSCText(EditText et) {
        if (!TextUtils.isEmpty(et.getText())) {
            int length = et.getText().length();
            et.getText().delete((length - 1), length);
        }
    }

    /**
     * 添加数字的方法
     *
     * @param s
     */
    private void showTextString(String s) {
        if (input.hasFocus())
            input.append(s);
    }


    /**
     * 让返回键失效的方法
     */
    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
    }

}
