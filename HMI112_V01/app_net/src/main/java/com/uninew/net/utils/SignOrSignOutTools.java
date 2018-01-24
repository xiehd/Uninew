package com.uninew.net.utils;

import com.uninew.car.db.signs.SignOrSignOut;
import com.uninew.net.JT905.bean.T_SignInReport;
import com.uninew.net.JT905.bean.T_SignOutReport;

import java.io.Serializable;

/**
 *
 * 签到签退数据库对象转换
 * Created by Administrator on 2017/11/9.
 */

public class SignOrSignOutTools implements Serializable{

    public static SignOrSignOut getSignDB(T_SignInReport signInMsg){
        SignOrSignOut sign = new SignOrSignOut();
        sign.setSignState(0x00);//签到
        sign.setPlateNumber(signInMsg.getBusinessLicense());
        sign.setBusinessLicense(signInMsg.getDriverCertificate());
        sign.setDriverCertificate(signInMsg.getCarNumber());
        sign.setBootTime(signInMsg.getBootTime());
        return  sign;
    }

    public static SignOrSignOut getSignOutDB(T_SignOutReport signOutMsg){
        SignOrSignOut signout = new SignOrSignOut();
        signout.setSignState(0x01);//签退
        signout.setPlateNumber(signOutMsg.getBusinessLicense());
        signout.setBusinessLicense(signOutMsg.getDriverCertificate());
        signout.setDriverCertificate(signOutMsg.getCarNumber());
        signout.setBootTime(signOutMsg.getBootTime());
        signout.setShutdownTime(signOutMsg.getShutDownTime());
        signout.setMeterKValue(signOutMsg.getMeterKValue());
        signout.setOnDutyMileage(signOutMsg.getMileage());
        signout.setRunMileage(signOutMsg.getOperationMileage());
        signout.setCarTimes(signOutMsg.getTrips());
        signout.setTimingTime(signOutMsg.getTimingTime());
        signout.setAmount(signOutMsg.getTotalIncome());
        signout.setCashCardAmount(signOutMsg.getCardIncome());
        signout.setCardTimes(signOutMsg.getCardTimes());
        signout.setNightMileage(signOutMsg.getBetweenMileage());
        signout.setAllMileage(signOutMsg.getTotalMileage());
        signout.setRevenueAllMileage(signOutMsg.getTotalOperationMileage());
        signout.setPrice(signOutMsg.getUnitPrice());
        signout.setSignOutType(signOutMsg.getSignOutWay());
        signout.setExtendedAttributes(signOutMsg.getExtend());
        return  signout;
    }
}
