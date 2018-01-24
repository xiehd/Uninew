package com.uninew.maintanence.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.uninew.maintanence.adapter.VersionDialogAdapter;
import com.uninew.settings.R;

import java.util.List;
import java.util.Map;

public class VersionDialog extends Dialog {
	private View view;
	private DisplayMetrics mDmetrics;
	private WindowManager.LayoutParams lp;
	private Window dialogWindow;
	private ListView listView;
	private Context context;
	public VersionDialogAdapter adapter;
	private List<Map<String, String>> dataList;
	private View.OnClickListener onClickListener;
	private int type;
	public VersionDialog(Context context, List<Map<String, String>> mDataList,
			View.OnClickListener onClickListener,int type) {
		super(context);
		this.context = context;
		view = LayoutInflater.from(context).inflate(R.layout.version_list, null);
		dialogWindow = getWindow();
		lp = dialogWindow.getAttributes();
		mDmetrics = context.getResources().getDisplayMetrics();
		this.type = type;
		this.dataList = mDataList;
		this.onClickListener = onClickListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initiView();

	}

	private void initiView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);
		listView = (ListView) view.findViewById(R.id.ls_versions);
		VersionDialogAdapter adapter = new VersionDialogAdapter(context, dataList, onClickListener,type);
		listView.setAdapter(adapter);
		lp.width = (int) (mDmetrics.widthPixels * 0.7);
		dialogWindow.setAttributes(lp);
	}



}
