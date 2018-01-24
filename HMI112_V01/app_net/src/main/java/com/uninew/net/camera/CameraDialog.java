package com.uninew.net.camera;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.uninew.net.R;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public class CameraDialog extends Dialog {
    private FrameLayout camera_view;
    private float mWidth = 0;
    private float mHeight = 0;
    private Context mContext;
    private View mView;

    public CameraDialog(@NonNull Context context) {
        super(context, R.style.MyProgressDialog);
        this.mContext = context;
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mView = inflater.inflate(R.layout.dialog_camera, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(mView);
        camera_view = (FrameLayout) mView.findViewById(R.id.fl_camera_view);
        setAttributes((float) 0.6,(float) 0.7);
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }

    public FrameLayout getCamera_view() {
        return camera_view;
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
        lp.alpha=0f;
        dialogWindow.setAttributes(lp);
    }
}
