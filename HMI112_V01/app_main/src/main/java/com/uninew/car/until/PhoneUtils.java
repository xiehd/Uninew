package com.uninew.car.until;

import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/9/26 0026.
 */

public class PhoneUtils {
    public static ITelephony getITelephony(TelephonyManager telephony) {
        try {
            Method getITelephonyMethod = telephony.getClass().getDeclaredMethod("getITelephony");
            getITelephonyMethod.setAccessible(true);//私有化函数也能使用
            return (ITelephony) getITelephonyMethod.invoke(telephony);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 方法isPhoneNumberValid(String phoneNumber)用来判断电话号码的格式是否正确
     * 用下面的字符串规定电话格式如下：
     * ^\\(? 表示可使用(作为开头
     * (\\d{3}) 表示紧接着3个数字
     * \\)? 表示可使用)继续
     * [- ]? 表示在上述格式后可用具有选择性的“-”继续
     * (\\d{4}) 表示紧接着4个数字
     * [- ]? 表示可用具有选择性的“-”继续
     * (\\d{4})$ 表示以4个数字结束
     * 可以和下面的数字格式比较：
     * (123)456-78900  123-4567-8900  12345678900 (123)-456-78900
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        String expression01 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
        String expression02 = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
        Pattern p01 = Pattern.compile(expression01);//通过Pattern对象将电话格式传入
        Matcher m01 = p01.matcher(phoneNumber);//检查电话号码的格式是否正确
        Pattern p02 = Pattern.compile(expression02);
        Matcher m02 = p02.matcher(phoneNumber);
        if (m01.matches() || m02.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
