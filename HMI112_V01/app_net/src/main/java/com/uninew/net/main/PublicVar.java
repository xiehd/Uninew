package com.uninew.net.main;

/**
 * Created by Administrator on 2017/9/2 0002.
 */

public class PublicVar {

    public static final int ACC_OFF=0x00;
    public static final int ACC_ON=0x01;
    public static int ACC_State=ACC_ON;/**当前ACC状态*/

    public static final int Login_OFF=0x00;
    public static final int Login_ON=0x01;
    public static int Login_State=Login_OFF;/**当前登录状态*/

    public static final int Car_Empty=0x00;
    public static final int Car_NonEmpty=0x01;
    public static int Car_State=Car_Empty; /**当前空车重车状态:空车、重车*/

}
