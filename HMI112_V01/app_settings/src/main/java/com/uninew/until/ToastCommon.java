package com.uninew.until;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uninew.settings.R;


/**
 * Created by Administrator on 2017/9/14.
 */

public class ToastCommon {


    public ToastCommon(){
    }


    /**
     * 显示自定义背景、字体颜色Toast
     * @param context
     * @param bg 背景图
     * @param tvString 内容
     * @param textcolor   字体颜色
     * @param textSize  字体大小（单位：px）
     */

    public static void ToastShow(Context context, Drawable bg, String tvString,int textcolor,int textSize){
        View layout = LayoutInflater.from(context).inflate(R.layout.toast_xml,null);
        TextView text = (TextView) layout.findViewById(R.id.text);
        LinearLayout linearLayout = (LinearLayout)layout.findViewById(R.id.toast_bg);
        linearLayout.setBackground(bg);
        text.setText(tvString);
        text.setTextColor(textcolor);
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    /**
     * 显示默认居中自定义Toast
     * @param context
     */

    public static void ToastShow(Context context,String tvString){
        View layout = LayoutInflater.from(context).inflate(R.layout.toast_xml,null);
        TextView text = (TextView) layout.findViewById(R.id.text);
        LinearLayout linearLayout = (LinearLayout)layout.findViewById(R.id.toast_bg);
        text.setText(tvString);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
