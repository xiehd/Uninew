package com.uninew.car.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uninew.car.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class PromptDialog extends Dialog implements View.OnClickListener {

    private OnDialogClickListener mListener;
    private OnDialogTimerListener mTimerListener;
    private Context mContext;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_times;
    private Button bt_left;
    private Button bt_right;
    private LinearLayout ll_dialog_button;
    private LinearLayout ll_dialog_timer;
    private LinearLayout ll_dialog_content;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private ButtonTimerTask mButtonTimerTask;
    private TextView tv_timer_title;
    private static final long DEFAULT_TIME = 1000;
    private View mView;
    private float mWidth = 0;
    private float mHeight = 0;
    private long maxTime = 10;

    private static final int WHAT_CANCEL = 0x01;
    private static final int WHAT_TIMER_BUTTON = 0x02;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case WHAT_CANCEL:
                    PromptDialog.this.cancel();
                    closeTimer();
                    break;
                case WHAT_TIMER_BUTTON:
                    int time = msg.arg1;
                    if (mTimerListener != null) {
                        mTimerListener.onTimer(PromptDialog.this, time);
                    }
                    if (maxTime - time < 0) {
                        mTimerListener.onFinish(PromptDialog.this);
                        closeTimer();
                    } else {
                        tv_times.setText((maxTime - time) + "");
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_dialog_left:
                if (mListener != null) {
                    mListener.onLeft(this);
                }
                break;
            case R.id.bt_dialog_right:
                if (mListener != null) {
                    mListener.onRight(this);
                }
                break;
            case R.id.ll_dialog_timer_button:
                if (mTimerListener != null) {
                    mTimerListener.onClick(this);
                }
                break;
        }
    }

    public interface OnDialogClickListener {
        void onLeft(PromptDialog dialog);

        void onRight(PromptDialog dialog);
    }

    public interface OnDialogTimerListener {
        void onTimer(PromptDialog dialog, int time);

        void onFinish(PromptDialog dialog);

        void onClick(PromptDialog dialog);
    }

    /**
     * @param context
     * @param width
     * @param height
     */
    public PromptDialog(@NonNull Context context, float width, float height) {
        super(context, R.style.dialog_custom);
        this.mContext = context;
        this.mWidth = width;
        this.mHeight = height;
        isCancel = false;
    }

    /**
     * @param context
     */
    public PromptDialog(@NonNull Context context) {
        super(context, R.style.dialog_custom);
        this.mContext = context;
        isCancel = false;
    }

    public float getWidth() {
        return mWidth;
    }

    public float getHeight() {
        return mHeight;
    }

    public void setAttributes(float width, float height) {
        this.mHeight = height;
        this.mWidth = width;
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        if (mWidth <= 0) {
            lp.width = (int) (d.widthPixels * 0.6);
        } else {
            lp.width = (int) (d.widthPixels * mWidth);
        }
        // 高度设置为屏幕的0.6
        if (mHeight <= 0) {
            lp.height = (int) (d.heightPixels * 0.7);
        } else {
            lp.height = (int) (d.heightPixels * mHeight);
        }
        dialogWindow.setAttributes(lp);
    }

    public void setOnDialogClickListener(OnDialogClickListener listener) {
        bt_right.setVisibility(View.VISIBLE);
        bt_left.setVisibility(View.VISIBLE);
        ll_dialog_timer.setVisibility(View.GONE);
        this.mListener = listener;
        bt_right.setOnClickListener(this);
        bt_left.setOnClickListener(this);
    }

    public void setOnDialogClickListener(@Nullable CharSequence left, @Nullable CharSequence right, OnDialogClickListener listener) {
        setLeft(left);
        setRight(right);
        bt_right.setVisibility(View.VISIBLE);
        bt_left.setVisibility(View.VISIBLE);
        ll_dialog_timer.setVisibility(View.GONE);
        this.mListener = listener;
        bt_right.setOnClickListener(this);
        bt_left.setOnClickListener(this);
    }

    public void setOnDialogClickListener(@StringRes int left, @Nullable CharSequence right, OnDialogClickListener listener) {
        bt_right.setVisibility(View.VISIBLE);
        bt_left.setVisibility(View.VISIBLE);
        setLeft(left);
        setRight(right);
        ll_dialog_timer.setVisibility(View.GONE);
        this.mListener = listener;
        bt_right.setOnClickListener(this);
        bt_left.setOnClickListener(this);
    }

    public void setOnDialogClickListener(@StringRes int left, @StringRes int right, OnDialogClickListener listener) {
        setLeft(left);
        setRight(right);
        bt_right.setVisibility(View.VISIBLE);
        bt_left.setVisibility(View.VISIBLE);
        ll_dialog_timer.setVisibility(View.GONE);
        this.mListener = listener;
        bt_right.setOnClickListener(this);
        bt_left.setOnClickListener(this);
    }

    public void setOnDialogClickListener(@Nullable CharSequence left, @StringRes int right, OnDialogClickListener listener) {
        bt_right.setVisibility(View.VISIBLE);
        bt_left.setVisibility(View.VISIBLE);
        setLeft(left);
        setRight(right);
        ll_dialog_timer.setVisibility(View.GONE);
        this.mListener = listener;
        bt_right.setOnClickListener(this);
        bt_left.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void isSystemAlert() {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }

    private void init() {
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mView = inflater.inflate(R.layout.dialog_prompt, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(mView);
        tv_title = (TextView) mView.findViewById(R.id.tv_dialog_titles);
        tv_content = (TextView) mView.findViewById(R.id.tv_dialog_content);
        bt_left = (Button) mView.findViewById(R.id.bt_dialog_left);
        bt_right = (Button) mView.findViewById(R.id.bt_dialog_right);
        ll_dialog_button = (LinearLayout) mView.findViewById(R.id.ll_dialog_button);
        tv_times = (TextView) mView.findViewById(R.id.tv_dialog_times);
        ll_dialog_content = (LinearLayout) mView.findViewById(R.id.ll_dialog_content);
        ll_dialog_timer = (LinearLayout) mView.findViewById(R.id.ll_dialog_timer_button);
        tv_timer_title = (TextView) mView.findViewById(R.id.tv_dialog_timer_title);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        if (mWidth <= 0) {
            lp.width = (int) (d.widthPixels * 0.6);
        } else {
            lp.width = (int) (d.widthPixels * mWidth);
        }
        // 高度设置为屏幕的0.6
        if (mHeight <= 0) {
            lp.height = (int) (d.heightPixels * 0.7);
        } else {
            lp.height = (int) (d.heightPixels * mHeight);
        }
        dialogWindow.setAttributes(lp);
        bt_right.setVisibility(View.GONE);
        bt_left.setVisibility(View.GONE);
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
//        super.setTitle(title);
        if (TextUtils.isEmpty(title)) {
            tv_title.setVisibility(View.GONE);
            return;
        }
        if (tv_title != null) {
            tv_title.setText(title);
        }
    }

    @Override
    public void setTitle(@StringRes int titleId) {
//        super.setTitle(titleId);
        String content = null;
        try {
            content = mContext.getString(titleId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(content)) {
            tv_title.setVisibility(View.GONE);
            return;
        }
        if (tv_title != null) {
            tv_title.setText(content);
        }
    }

    public void setTitleColor(@Nullable ColorStateList colors) {
//        super.setTitle(title);
        if (tv_title != null) {
            tv_title.setTextColor(colors);
        }
    }

    public void setTitleColor(@ColorRes int colorsId) {
//        super.setTitle(titleId);
        try {
            if (tv_title != null) {
                tv_title.setTextColor(getContext().getResources().getColor(colorsId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContent(@Nullable CharSequence content) {
        if (TextUtils.isEmpty(content)) {
            tv_content.setVisibility(View.GONE);
            return;
        }
        if (tv_content != null) {
            tv_content.setText(content);
        }
    }

    @Override
    public void addContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
//        super.addContentView(view, params);
        tv_content.setVisibility(View.GONE);
        ll_dialog_content.addView(view, params);
        ll_dialog_content.setGravity(Gravity.LEFT);
    }

    /**
     * @param view
     * @param params
     * @param gravity See {@link android.view.Gravity}
     */
    public void addContentView(@NonNull View view, @Nullable LinearLayout.LayoutParams params, int gravity) {
        tv_content.setVisibility(View.GONE);
        ll_dialog_content.addView(view, params);
        ll_dialog_content.setGravity(gravity);
    }

    public void setContent(@StringRes int contentId) {
        String content = null;
        try {
            content = mContext.getString(contentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(content)) {
            tv_content.setVisibility(View.GONE);
            return;
        }
        if (tv_content != null) {
            tv_content.setText(content);
        }
    }

    public void setLeft(@Nullable CharSequence left) {
        if (TextUtils.isEmpty(left)) {
            bt_left.setVisibility(View.GONE);
            return;
        }
//        if (bt_left != null) {
        bt_left.setText(left);
        bt_left.setGravity(Gravity.CENTER);
//        }
    }

    public void setLeft(@StringRes int leftId) {
        String content = null;
        try {
            content = mContext.getString(leftId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(content)) {
            bt_left.setVisibility(View.GONE);
            return;
        }
//        if (bt_left != null) {
        bt_left.setText(content);
        bt_left.setGravity(Gravity.CENTER);
//        }
    }

    public void setRight(@Nullable CharSequence right) {
        if (TextUtils.isEmpty(right)) {
            bt_right.setVisibility(View.GONE);
            return;
        }
//        if (bt_right != null) {
        bt_right.setText(right);
        bt_right.setGravity(Gravity.CENTER);
//        }
    }

    public void setRight(@StringRes int rightId) {
        String content = null;
        try {
            content = mContext.getString(rightId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(content)) {
            bt_right.setVisibility(View.GONE);
            return;
        }
//        if (bt_right != null) {
        bt_right.setText(content);
        bt_right.setGravity(Gravity.CENTER);
//        }
    }

    public void showToast(@Nullable CharSequence content, long time) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        changToast(0, 0);
        if (tv_content != null) {
            tv_content.setText(content);
        }
        openTimer(time);
    }

    public void showToast(@StringRes int contentId, long time) {
        changToast(0, 0);
        String content = null;
        try {
            content = mContext.getString(contentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (tv_content != null) {
            tv_content.setText(content);
        }
        openTimer(time);
    }


    public void showToast(@Nullable CharSequence content, long time, float width, float height) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        changToast(width, height);
        if (tv_content != null) {
            tv_content.setText(content);
        }
        openTimer(time);
    }

    public void showToast(@StringRes int contentId, long time, float width, float height) {
        changToast(width, height);
        String content = null;
        try {
            content = mContext.getString(contentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tv_content != null) {
            if (!TextUtils.isEmpty(content)) {
                tv_content.setText(content);
            }
        }
        openTimer(time);
    }

    public void showTimerButton(@Nullable CharSequence title, long maxTime, OnDialogTimerListener timerListener) {
//        ll_dialog_button.setVisibility(View.GONE);
        bt_left.setVisibility(View.GONE);
        bt_right.setVisibility(View.GONE);
        ll_dialog_timer.setVisibility(View.VISIBLE);
        ll_dialog_timer.setOnClickListener(this);
        tv_times.setText(maxTime + "");
        if (!TextUtils.isEmpty(title)) {
            tv_timer_title.setText(title);
        }
        this.mTimerListener = timerListener;
        this.maxTime = maxTime;
        openTimer(DEFAULT_TIME);
    }


    public void showTimerButton(@StringRes int titleId, long maxTime, OnDialogTimerListener timerListener) {
//        ll_dialog_button.setVisibility(View.GONE);
        bt_left.setVisibility(View.GONE);
        bt_right.setVisibility(View.GONE);
        ll_dialog_timer.setVisibility(View.VISIBLE);
        ll_dialog_timer.setOnClickListener(this);
        tv_times.setText(maxTime + "");
        String title = null;
        try {
            title = mContext.getString(titleId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(title)) {
            tv_timer_title.setText(title);
        }
        this.mTimerListener = timerListener;
        this.maxTime = maxTime;
        openTimer(DEFAULT_TIME);
    }


    public void showTimerButton(long maxTime, OnDialogTimerListener timerListener) {
//        ll_dialog_button.setVisibility(View.GONE);
        bt_left.setVisibility(View.GONE);
        bt_right.setVisibility(View.GONE);
        ll_dialog_timer.setVisibility(View.VISIBLE);
        ll_dialog_timer.setOnClickListener(this);
        tv_times.setText(maxTime + "");
        this.mTimerListener = timerListener;
        this.maxTime = maxTime;
        openTimer(DEFAULT_TIME);
    }

    public void isContentCenter() {
        tv_content.setGravity(Gravity.CENTER);
    }

    public void isContentLeft() {
        tv_content.setGravity(Gravity.LEFT);
    }

    public void isContentRight() {
        tv_content.setGravity(Gravity.RIGHT);
    }

    public void isContentCenterHorizontal() {
        tv_content.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public void isContentCenterVertical() {
        tv_content.setGravity(Gravity.CENTER_VERTICAL);
    }


    private void changToast(float width, float height) {
        this.mWidth = width;
        this.mHeight = height;
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        if (mWidth <= 0) {
            lp.width = (int) (d.widthPixels * 0.4);
        } else {
            lp.width = (int) (d.widthPixels * mWidth);
        }
        // 高度设置为屏幕的0.6
        if (mHeight <= 0) {
            lp.height = (int) (d.heightPixels * 0.2);
        } else {
            lp.height = (int) (d.heightPixels * mHeight);
        }
        dialogWindow.setAttributes(lp);
        tv_title.setVisibility(View.GONE);
        ll_dialog_button.setVisibility(View.GONE);
        mView.setBackgroundResource(R.mipmap.toast_bg);

    }


    private void openTimer(long time) {
        if (mTimer != null) {
            closeTimer();
        }
        if (mTimer == null) {
            mTimer = new Timer();
            if (mTimerListener == null) {
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(WHAT_CANCEL);
                    }
                };
                if (time > 1000) {
                    mTimer.schedule(mTimerTask, time);
                } else {
                    mTimer.schedule(mTimerTask, DEFAULT_TIME);
                }
            } else {
                mButtonTimerTask = new ButtonTimerTask();
                mTimer.schedule(mButtonTimerTask, DEFAULT_TIME, DEFAULT_TIME);
            }
        }
    }

    private void closeTimer() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mButtonTimerTask != null) {
            mButtonTimerTask.cancel();
            mButtonTimerTask = null;
            mTimerListener = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private class ButtonTimerTask extends TimerTask {
        private int time = 0;

        public ButtonTimerTask() {
            time = 0;
        }

        @Override
        public void run() {
            time++;
            Message msg = Message.obtain();
            msg.what = WHAT_TIMER_BUTTON;
            msg.arg1 = time;
            mHandler.sendMessage(msg);
        }
    }

    private boolean isCancel;

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void cancel() {
        super.cancel();
        isCancel = true;
        closeTimer();
    }

    public boolean isCancel() {
        return isCancel;
    }
}
