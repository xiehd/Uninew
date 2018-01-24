package com.uninew.net.Alarm.illegalalram;


import com.uninew.net.Alarm.devicefailure.IDevicefaiureListener;

import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * 区域判断工具
 * Created by Administrator on 2017/10/21.
 */


public class RegoinJudge implements IRegoinJudge{
    private static final double math_pi = 3.1415926535897932384626433832795;


    private int index = 0;
    private int Intsum = 0;
    private int i = 0;
    private long Y0, Y1, X0, X1, X, nX, nY;

    double Lon1, Lon2, Lat1, Lat2, LonDist, LatDist;
    double A, C, D, E, U;
    private IDevicefaiureListener mIDevicefaiureListener;
    public RegoinJudge(IDevicefaiureListener mIDevicefaiureListener) {
        this.mIDevicefaiureListener = mIDevicefaiureListener;
    }

    private boolean ifInRegoin(double lon, double lat, RegionInfo mRegoinInfo) {

        boolean result = false;
        Intsum = 0;
        nX = (long) (10000000.0 * lon);
        nY = (long) (10000000.0 * lat);
        index = mRegoinInfo.getPointNumber();
        if (mRegoinInfo.getRegoinType() == 2) { //为圆形时
            if (Getdistance(lon, lat, mRegoinInfo.getLonlist().get(0), mRegoinInfo.getLatlist().get(0)) * 1000
                    < mRegoinInfo.getRadius()) {
                result = true;
            }
        } else if (mRegoinInfo.getRegoinType() == 1) {//为多边形时
            for (i = 0;i < index;i++){
                if (i == index - 1) {
                    X0 = (long) (10000000 * mRegoinInfo.getLonlist().get(i));
                    Y0 = (long) (10000000 * mRegoinInfo.getLatlist().get(i));
                    X1 = (long) (10000000 * mRegoinInfo.getLonlist().get(0));
                    Y1 = (long) (10000000 * mRegoinInfo.getLatlist().get(0));
                } else {
                    X0 = (long) (10000000 * mRegoinInfo.getLonlist().get(i));
                    Y0 = (long) (10000000 * mRegoinInfo.getLatlist().get(i));
                    X1 = (long) (10000000 * mRegoinInfo.getLonlist().get(i + 1));
                    Y1 = (long) (10000000 * mRegoinInfo.getLatlist().get(i + 1));
                }
                if (((nY >= Y0) && (nY < Y1)) || ((nY >= Y1) && (nY < Y0))) {

                    if (abs(Y0 - Y1) > 0) {
                        X = X0 - (X0 - X1) * (Y0 - nY) / (Y0 - Y1);//直线斜率 k = (y0-y1)/(x0-x1)
                        if (X < nX) {//存在交点在该点的左边(-无穷方向),若为(+无穷方向则为x>pt.x判定有交点.)
                            Intsum++;
                        }
                    }
                }
            }
            if (((float)Intsum %2) != 0){//总和为奇数
                result = true;
            }

        }else if(mRegoinInfo.getRegoinType() == 0){//为矩形时
            if ((lon >= mRegoinInfo.getLonlist().get(0))&&(lon <= mRegoinInfo.getLonlist().get(1))&&(lat >= mRegoinInfo.getLatlist().get(2)
            && lat <= mRegoinInfo.getLatlist().get(0)) )
                result = true;
        }


        return result;
    }

    private double Getdistance(double d_x1, double d_y1, double d_x2, double d_y2) {

        double mDistance = 0;
        Lon1 = d_x1 * math_pi / 180.0;
        Lon2 = d_x2 * math_pi / 180.0;
        Lat1 = d_y1 * math_pi / 180.0;
        Lat2 = d_y2 * math_pi / 180.0;

        //计算经差及纬差
        LonDist = Lon1 - Lon2;
        LatDist = Lat1 - Lat2;

        //以FEET(英尺)为单位，计算两点之间的距离
        A = sin(LatDist / 2.0) * sin(LatDist / 2.0) + cos(Lat1) * cos(Lat2) * sin(LonDist / 2.0) * sin(LonDist / 2.0);
        if (sqrt(A) > 1) {
            E = 1.0;
        } else {
            E = sqrt(A);
        }
        C = 2 * atan(E / sqrt(1 - E * E));
        D = (3963 - 13 * sin((Lat1 + Lat2) / 2)) * C;
        mDistance = D * 5280;//距离，单位为FEET

        //将单位FEET转换为公里
        mDistance = mDistance * (3048.0 / 10000000.0);

        U = 10.0 * 10.0 * 10.0;
        mDistance = (mDistance * U + (5.0 / 10.0)) / U;

        return mDistance;
    }


    @Override
    public void sendRegoinInfo(int id,double lon, double lat, RegionInfo mRegoinInfo) {
        if (ifInRegoin(lon,lat,mRegoinInfo)) {//区域内
            mIDevicefaiureListener.devicefailure(id,0x00);
        }else{//区域外
            mIDevicefaiureListener.devicefailure(id,0x01);
        }
    }
}
