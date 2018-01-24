package com.uninew.maintanence.dialog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

import com.uninew.settings.R;

/**
 * 对话框管理类
 * @author lusy
 * 创建日期:2017-1-14
 */
public class DialogManager {
	private volatile static DialogManager instance = null;
	private ProgressDialog progressDialog;

	private DialogManager() {

	};

	public static DialogManager getInstance() {
		if (null == instance) {
			synchronized (DialogManager.class) {
				if (null == instance) {
					instance = new DialogManager();
				}
			}
		}
		return instance;
	}
	/**
	 * 显示确定按钮对话框
	 * @param context 上下文
	 * @param title 对话框标题
	 * @param msg 显示的消息
	 * @param onclickListener 按钮点击事件
	 */
	public void showOneButtonDialog(Context context, String title, String msg,
			OnClickListener onclickListener) {
		if(null!=context){
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle(title);
			builder.setMessage(msg);
			builder.setPositiveButton(context.getResources().getString(R.string.btnOk), onclickListener);
			builder.create();
			builder.show();
		}
		
	}
	/**
	 * 显示确定,取消按钮对话框
	 * @param context 上下文
	 * @param title 对话框标题
	 * @param msg 提示信息
	 * @param okLinstener 确定按钮点击事件
	 * @param noClickListener 取消按钮点击事件
	 */
	public void showTwoButtonDialog(Context context, String title, String msg,
			OnClickListener okLinstener, OnClickListener noClickListener) {
		if(null!=context){
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle(title);
			builder.setMessage(msg);
			builder.setPositiveButton(context.getResources().getString(R.string.btnOk), okLinstener);
			builder.setNegativeButton(context.getResources().getString(R.string.btnNo), noClickListener);
			builder.create();
			builder.show();
		}
		
	}
/**
 * 显示进度条对话框
 * @param context
 * @param msg 提示消息
 */
	public void showProgressDialog(Context context, String msg) {
		if(null!=context){
			if(null==progressDialog){
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage(msg);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setCancelable(false);
			}
			if (!progressDialog.isShowing()) {
				progressDialog.show();
			}
		}
		
	}
/**
 * 关闭进度条对话框
 */
	public void closeProgressDialog() {
		if (progressDialog != null ) {
			if(progressDialog.isShowing()){
				progressDialog.dismiss();
				progressDialog = null;
			}
		}
	}

}
