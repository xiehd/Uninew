package com.uninew.maintanence.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.uninew.maintanence.interfaces.INetInfoModel;
import com.uninew.settings.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetManager {

    private Context mContext;
    private INetInfoModel.INetSignalListener mNetListener;
    private TelephonyManager Tel;
    private INetInfoModel.INetStateCallBack netStateCallBack;
    private int gsmSignalStrength = 0;
    private MyPhoneStateListener MyListener;

    public NetManager(Context mContext, INetInfoModel.INetSignalListener mNetListener) {
        super();
        this.mContext = mContext;
        this.mNetListener = mNetListener;
        MyListener = new MyPhoneStateListener();
        Tel = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        new NetPing().execute(0);
    }

    public int getSimState() {
        int state = Tel.getSimState();
        int simState = 0;
        switch (state) {
            case 0:
                simState = 0;
                break;
            case 1:
                simState = 1;
                break;
            case 2:
                simState = 2;
                break;
            case 3:
                simState = 2;
                break;
            case 4:
                simState = 2;
                break;
            case 5:
                simState = 3;
                break;
        }
        return simState;
    }

    public int getSimOperator() {
        int sim_service = 0;
        if (Tel.getSimOperator().equals("46000")) {
            sim_service = 1;
        } else if (Tel.getSimOperator().equals("46001")) {
            sim_service = 2;
        } else if (Tel.getSimOperator().equals("46002")) {
            sim_service = 1;
        } else if (Tel.getSimOperator().equals("46003")) {
            sim_service = 3;
        } else if (Tel.getSimOperator().equals("46011")) {
            sim_service = 3;
        } else if (TextUtils.isEmpty(Tel.getSimOperator())) {
            sim_service = 0;
        } else {
            sim_service = 0;
        }
        return sim_service;
    }

    public boolean isNetWorkRoaming() {
        return Tel.isNetworkRoaming();
    }

    public void setPhoneListener() {
        Tel.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        return;
    }

    public void stopPhoneListener() {
        Tel.listen(MyListener, PhoneStateListener.LISTEN_NONE);
        return;
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return intToIp(inetAddress.hashCode());
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("IP error", ex.toString());
        }
        return null;
    }

    public void getNetState(INetInfoModel.INetStateCallBack netStateCallBack) {
        this.netStateCallBack = netStateCallBack;

    }

    private class NetPing extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            int s = 0;
            s = Ping("www.baidu.com");
            publishProgress(s);
            return s;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (netStateCallBack != null)
                netStateCallBack.netStateCallBack(values[0]);
        }
    }

    private int Ping(String str) {
        int resault = 0;
        Process p;
        try {
            // ping -c 3 -w 100 中 ，-c 是指ping的次数 3是指ping 3次 ，-w 100
            // 以秒为单位指定超时间隔，是指超时时间为100秒
            p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + str);
            int status = p.waitFor();
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            if (status == 0) {
                resault = 1;
            } else {
                resault = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return resault;
    }

    public String getNetType() {
        String strNetworkType = mContext.getResources().getString(R.string.net_txt_unknowstate);
        NetworkInfo networkInfo = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                // TD-SCDMA networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by
                        // 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 : replace by
                        // 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by
                        // 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by
                        // 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace by
                        // 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA")
                                || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }

            }
        }
        return strNetworkType;
    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    private class MyPhoneStateListener extends PhoneStateListener

    {
        /*
		 * Get the Signal strength from the provider, each tiome there is an
		 * update 从得到的信号强度,每个tiome供应商有更新
		 */

        @Override

        public void onSignalStrengthsChanged(SignalStrength signalStrength)

        {
            gsmSignalStrength = -113 + (2 * signalStrength.getGsmSignalStrength());
            if (mNetListener != null) {
                mNetListener.getSignal(gsmSignalStrength);
            }
            super.onSignalStrengthsChanged(signalStrength);
        }

    }

    ;/* End of private Class */

}
