package tools;

/**
 * Created by Administrator on 2017/9/4 0004.
 */

public final class StringsTool {
    private StringsTool() {

    }

    /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 将字符串转化为整数
     */
    public static int srtToInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * 将字符串转化为浮点
     */
    public static double srtToDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    /**
     * 将字符串转化为浮点
     */
    public static float srtToFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * 将字符串转化为浮点
     */
    public static boolean srtToBoolean(String value) {
        try {
            return Boolean.parseBoolean(value);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
