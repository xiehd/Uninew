package com.uninew.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by rong on 2017-04-13.
 */

public class TimeTools {

    private TimeTools() {

    }

    public static String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("%4d",year));
//        sb.append("-");
        sb.append((month+1)>10 ? month+1 : "0"+(month+1));
//        sb.append("-");
        sb.append((day)>10 ? day: "0"+(day));
//        sb.append("_");
        sb.append((hour)>10 ? hour : "0"+(hour));
//        sb.append(":");
        sb.append((minute)>10 ? minute : "0"+(minute));
//        sb.append(":");
//        sb.append((second+1)>10 ? second+1 : "0"+(second+1));
        return sb.toString();
    }
}



