package tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/11 0011.
 */

public final class TimeTool {

    private TimeTool() {

    }

    private static final String date_format = "yyyy-MM-dd HH:mm:ss";
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>();

    public static DateFormat getDateFormat() {
        DateFormat df = threadLocal.get();
        if (df == null) {
            df = new SimpleDateFormat(date_format);
            threadLocal.set(df);
        }
        return df;
    }

    public static String formatDate(Date date) throws ParseException {
        return getDateFormat().format(date);
    }

    public static Date parse(String strDate) throws ParseException {
        return getDateFormat().parse(strDate);
    }

    public static long parseToLong(String strDate) throws ParseException {
        return getDateFormat().parse(strDate).getTime();
    }

    public static long parseToLong(String strDate,String format) throws ParseException {
        return new SimpleDateFormat(format).parse(strDate).getTime();
    }

}
