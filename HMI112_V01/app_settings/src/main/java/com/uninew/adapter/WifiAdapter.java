package com.uninew.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uninew.settings.R;

import java.util.List;


/**
 * Created by Administrator on 2017/9/7.
 */

public class WifiAdapter extends BaseAdapter {

    private List<ScanResult> mWifiList;
    private LayoutInflater layoutInflater;
    private boolean isWifi = false;//是否已连接WiFi

    public WifiAdapter(List<ScanResult> mWifiList, Context context) {
        this.mWifiList = mWifiList;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setmWifiList(List<ScanResult> mWifiList) {
        this.mWifiList = mWifiList;
        this.notifyDataSetChanged();
    }

    /**
     * 显示当前连接的WiFi
     * @param bssid
     */
    public void notifyList(String bssid) {
        if (mWifiList != null) {
            ScanResult result = null;
            for (int i = 0;i < mWifiList.size();i++){
                if (bssid.equals(mWifiList.get(i).BSSID)){
                    result = mWifiList.get(i);
                    mWifiList.remove(i);
                    mWifiList.add(result);
                    isWifi = true;
                    this.notifyDataSetChanged();
                    break;
                }

            }

        }
    }

    @Override
    public int getCount() {
        if (mWifiList != null) {
            return mWifiList.size();
        }
        return 0;
    }

    @Override
    public ScanResult getItem(int position) {
        if (mWifiList != null) {
            if (position < mWifiList.size())
                return mWifiList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolper mViewHolper = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.wifi_item, null);
            mViewHolper = new ViewHolper();

            mViewHolper.list_wifi_layout = (RelativeLayout) convertView.findViewById(R.id.list_wifi_layout);
            mViewHolper.list_wifi_name = (TextView) convertView.findViewById(R.id.list_wifi_name);
            mViewHolper.list_wifi_signle = (ImageView) convertView.findViewById(R.id.list_wifi_signle);

            mViewHolper.more_wifiok_image = (ImageView) convertView.findViewById(R.id.more_wifiok_image);
            mViewHolper.list_wifi_text = (TextView) convertView.findViewById(R.id.list_wifi_text);

            convertView.setTag(mViewHolper);
        } else {
            mViewHolper = (ViewHolper) convertView.getTag();
        }


        mViewHolper.list_wifi_name.setText(mWifiList.get(position).SSID);
        if (position == 0){
            if (isWifi){
            mViewHolper.more_wifiok_image.setVisibility(View.VISIBLE);
            mViewHolper.list_wifi_text.setVisibility(View.VISIBLE);
            }else{
                mViewHolper.more_wifiok_image.setVisibility(View.GONE);
                mViewHolper.list_wifi_text.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    class ViewHolper {
         RelativeLayout list_wifi_layout;
         ImageView list_wifi_signle,more_wifiok_image;
         TextView list_wifi_name,list_wifi_text;


    }
}
