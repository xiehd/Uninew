package com.uninew.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uninew.auto.phone.R;
import com.uninew.file.dao.StationDao;

import java.util.List;

public class ShowListAdapter extends BaseAdapter {

	private List<StationDao> list;
	private Context mcontext;
	private LayoutInflater listContainer;

	public ShowListAdapter(Context context, List<StationDao> data) {
		this.mcontext = context;
		listContainer = LayoutInflater.from(context);
		list = data;
	}

	
	public List<StationDao> getList() {
		return list;
	}


	public void setList(List<StationDao> list) {
		this.list = list;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holper holper = null;
		if (convertView == null) {
			holper = new Holper();
			 //获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.list_item, null);
			//holper.setting_list_station = (TextView) convertView.findViewById(R.id.setting_list_station);
			holper.setting_list_id = (TextView) convertView.findViewById(R.id.setting_list_id);
			holper.setting_list_name = (TextView) convertView.findViewById(R.id.setting_list_name);
			convertView.setTag(holper);
		} else {
			holper = (Holper) convertView.getTag();
		}
		
		//holper.setting_list_station.setText(list.get(position).getStationName());
		holper.setting_list_id.setText("编号："+list.get(position).getStationId());
		holper.setting_list_name.setText("名称："+list.get(position).getStationName());
		
		return convertView;
	}

	class Holper {
		//public TextView setting_list_station;
		public TextView setting_list_id;
		public TextView setting_list_name;
	}

}
