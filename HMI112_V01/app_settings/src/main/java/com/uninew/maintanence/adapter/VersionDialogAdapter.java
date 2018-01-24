package com.uninew.maintanence.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.uninew.maintanence.model.DefineUpdate;
import com.uninew.settings.R;

import java.util.List;
import java.util.Map;

/**
 * 升级列表适配器
 * @author lusy
 * 创建日期:2017-1-14
 */
public class VersionDialogAdapter extends BaseAdapter {
	/**
	 * 数据源
	 */
	private List<Map<String, String>> datas;
	private Context context;
	/**
	 * 升级按钮点击事件
	 */
	private OnClickListener clickListener;
	private int type;
	public static final int TYPE_APK=0;
	public static final int TYPE_MCU=1;
	public static final int TYPE_OS=2;
	/**
	 * 
	 * @param context 上下文
	 * @param datas 数据集合
	 * @param clickListener 升级按钮点击事件
	 * @param type 对话框类型,APK,MCU,OS
	 */
	public VersionDialogAdapter(Context context, List<Map<String, String>> datas,
			OnClickListener clickListener, int type) {
		this.context = context;
		this.datas = datas;
		this.type = type;
		this.clickListener = clickListener;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.version_item_one, null);
			holder = new ViewHolder();
			holder.appName = (TextView) convertView.findViewById(R.id.version_name);
			holder.oldVersion = (TextView) convertView.findViewById(R.id.version_pkg_currrent);
			holder.newVersion = (TextView) convertView.findViewById(R.id.version_pkg_new);
			holder.btnUpdate = (Button) convertView.findViewById(R.id.version_handler);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = datas.get(position);
		holder.appName.setText(map.get(DefineUpdate.Key_ApkName));
		String oldVersionName = map.get(DefineUpdate.Key_OldVersion);
		String newVersionName = map.get(DefineUpdate.Key_NewVersion);
		holder.oldVersion.setText(oldVersionName);
		holder.newVersion.setText(newVersionName);
		// apk升级才有versionCode字段
		if (type==TYPE_APK) {
			final int oldCode = Integer.parseInt(map.get(DefineUpdate.key_oldVersionCode));
			final int newCode = Integer.parseInt(map.get(DefineUpdate.key_newVersionCode));
				if (newCode < oldCode) {
					// apk已经安装过了,设置为灰色升级按钮,且按钮不能用
					holder.btnUpdate.setText(context.getResources().getString(R.string.apkUpdate));
					holder.btnUpdate.setBackgroundColor(context.getResources().getColor(
							R.color.grey));
					holder.btnUpdate.setTextColor(context.getResources().getColor(R.color.black));
					// 设置按钮不可点击
					holder.btnUpdate.setEnabled(false);
				} else {
					// 新版本大于旧版本,升级
					holder.btnUpdate.setText(context.getResources().getString(R.string.apkUpdate));
					holder.btnUpdate.setBackgroundColor(context.getResources().getColor(R.color.blue));
					holder.btnUpdate.setEnabled(true);
				}
			if(oldCode==0){
				// apk没有安装过,设置按钮为安装
				holder.btnUpdate.setText(context.getResources().getString(R.string.apkInstall));
			}
			holder.btnUpdate.setTag(R.id.key_dialog_type, TYPE_APK);
		} else{
			// MCU和系统升级
			int old = Integer.parseInt(oldVersionName.toLowerCase().replace("v", "").replace(".", ""));
			int newVersion = Integer.parseInt(newVersionName.toLowerCase().replace("v", "").replace(".", ""));
			Log.e("对话框适配器", "MCU,OS新版本=" + newVersion + ",旧版本=" + old);
			old = 0;
			if (newVersion <= old) {
				Log.e("对话框适配器", "MCU,OS新旧版本一样或者SD卡升级包版本太低,不能升级");
				// 新旧版本号一样
				holder.btnUpdate.setBackgroundColor(context.getResources().getColor(R.color.grey));
				holder.btnUpdate.setTextColor(context.getResources().getColor(R.color.black));
				// 设置按钮不可点击
				holder.btnUpdate.setEnabled(false);
			}else{
				Log.e("对话框适配器", "MCU,OS可以升级");
				holder.btnUpdate.setText(context.getResources().getString(R.string.apkUpdate));
				holder.btnUpdate.setBackgroundColor(context.getResources().getColor(R.color.blue));
				holder.btnUpdate.setEnabled(true);
			}
			//MCU升级
			if(type==TYPE_MCU){
				holder.btnUpdate.setTag(R.id.key_dialog_type, TYPE_MCU);
			//系统升级
			}else if(type==TYPE_OS){
				holder.btnUpdate.setTag(R.id.key_dialog_type, TYPE_OS);
			}
		} 
		
		holder.btnUpdate.setTag(R.id.key_filePath,map.get(DefineUpdate.Key_FileAbsolutePath));
		holder.btnUpdate.setOnClickListener(clickListener);
		return convertView;
	}

	private static class ViewHolder {
		public TextView appName;
		public TextView oldVersion;
		public TextView newVersion;
		public Button btnUpdate;
	}

}
