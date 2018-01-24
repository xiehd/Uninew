package com.uninew.car.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/9/13 0013.
 */

public class BaseViewHolder {
    //复用的View
    private final View mConvertView;
    //所有控件集合
    private SparseArray<View> mViews;
    //记录位置信息
    private int mPosition;
    private Context mContext;

    /**
     * BaseViewHolder 构造函数
     * @param context 上下文对象
     * @param parent 父类容器
     * @param layoutId 布局 Id
     * @param position item位置信息
     */
    public BaseViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        this.mContext = context;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        //设置 tag
        mConvertView.setTag(this);
    }

    public int getPosition() {
        return mPosition;
    }

    /**
     * 通过 viewId 获取控件
     * @param viewId 控件id
     * @param <T> View 子类
     * @return 返回 View
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    //返回 ViewHolder
    public static BaseViewHolder getViewHolder(Context context, View convertView, ViewGroup parent, int layoutId, int position) {

        //BaseViewHolder 为空，创建新的，否则返回已存在的
        if (convertView == null) {
            return new BaseViewHolder(context, parent, layoutId, position);
        } else {
            BaseViewHolder holder = (BaseViewHolder) convertView.getTag();
            //更新 item 位置信息
            holder.mPosition = position;
            return holder;
        }
    }

    //获取 convertView
    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 设置 TextView 的值
     * @param viewId
     * @param text
     * @return
     */
    public BaseViewHolder setText(int viewId, String text)
    {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 设置 TextView 的值
     * @param viewId
     * @param stringId
     * @return
     */
    public BaseViewHolder setText(int viewId, int stringId)
    {
        TextView tv = getView(viewId);
        tv.setText(mContext.getString(stringId));
        return this;
    }


    /**
     * 设置 TextView 的值
     * @param viewId
     * @param colorId
     * @return
     */
    public BaseViewHolder setTextColor(int viewId, int colorId)
    {
        TextView tv = getView(viewId);
        tv.setTextColor(mContext.getResources().getColor(colorId));
        return this;
    }
    /**
     * 设置TImageView的值
     * @param viewId
     * @param resId
     * @return
     */
    public BaseViewHolder setImageResource(int viewId, int resId)
    {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    /**
     * 设置是否可见
     * @param viewId
     * @param visible
     * @return
     */
    public BaseViewHolder setVisible(int viewId, boolean visible)
    {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 设置是否可见
     * @param viewId
     * @param visible View.VISIBLE,View.GONE,View.INVISIBLE
     * @return
     */
    public BaseViewHolder setVisibility(int viewId, int visible)
    {
        View view = getView(viewId);
        view.setVisibility(visible);
        return this;
    }

    /**
     * 设置tag
     * @param viewId
     * @param tag
     * @return
     */
    public BaseViewHolder setTag(int viewId, Object tag)
    {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public BaseViewHolder setTag(int viewId, int key, Object tag)
    {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }
    /**
     * 设置 Checkable
     * @param viewId
     * @param checked
     * @return
     */
    public BaseViewHolder setChecked(int viewId, boolean checked)
    {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * 点击事件
     */
    public BaseViewHolder setOnClickListener(int viewId,View.OnClickListener listener)
    {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 触摸事件
     */
    public BaseViewHolder setOnTouchListener(int viewId,View.OnTouchListener listener)
    {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    /**
     * 长按事件
     */
    public BaseViewHolder setOnLongClickListener(int viewId,View.OnLongClickListener listener)
    {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }
}
