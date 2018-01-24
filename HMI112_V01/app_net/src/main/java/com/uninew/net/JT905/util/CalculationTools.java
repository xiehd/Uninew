package com.uninew.net.JT905.util;

/**
 * Created by Administrator on 2017/9/2 0002.
 */

public class CalculationTools {


    /**
     * 获取两个经纬度之间的距离
     *
     * @param LonA 经度A
     * @param LatA 纬度A
     * @param LonB 经度B
     * @param LatB 纬度B
     * @return 距离（米）
     */
    public static double getDistance(double LonA, double LatA, double LonB,
                                     double LatB) {
        // 东西经，南北纬处理，只在国内可以不处理(假设都是北半球，南半球只有澳洲具有应用意义)
        double MLonA = LonA;
        double MLonB = LonB;
        // 地球半径（千米）
        double R = 6371.004;
        double C = Math.sin(rad(LatA)) * Math.sin(rad(LatB))
                + Math.cos(rad(LatA)) * Math.cos(rad(LatB))
                * Math.cos(rad(MLonA - MLonB));
        return (R * Math.acos(C)) * 1000;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
