package com.uninew.net.JT905.comm.common;

public interface ActionDefine {

    /**
     * dvr状态通知
     */
    String Dvr_State = "Com.DVR.State";

    String Check_Dvr_State = "Com.DVR.check_State";
    //	/** dvr状态通知键值*/
    //	String DvrState_Key = "DvrState";

    /**
     * dvr显示模式设置
     */
    String DvrDisplayMode = "Com.DVR.DisplayMode";

    /**
     * 通讯状态
     */
    String ServerLinkState = "Com.ServerLink.State";

    /**
     * GPS模拟运行位置
     */
    String CurrentLocation = "Com.location.CurrentLocation";


    /**
     * 定位状态 1已定位,0未定位
     */
    String LocationState = "Com.LocationService.RespondState";

    public interface GPS {
        /**
         * 经度
         */
        String Longitude = "Longitude";
        /**
         * 纬度
         */
        String Latitude = "Latitude";
        /**
         * 方向
         */
        String Direction = "Direction";
        /**
         * 速度
         */
        String Speed = "Speed";
    }

    /**
     * DVR显示模式设置
     *
     * @author Administrator
     */
    public interface DVRDisplayMode {
        /**
         * 显示模式
         */
        String KEY_DISPLAY_MODE = "split";
        /**
         * 显示序号
         */
        String KEY_DISPLAY_SERIAL = "pass";
    }


    interface DVRState {
        String KEY_MAXNUM = "maxNum";
        String KEY_ACCSTATE = "accState";
        String KEY_GPSSTATE = "gpsState";
        String KEY_NETSTATE = "netState";
        String KEY_DISKSTATE = "diskState";
        String KEY_CAMERASTATE = "cameraState";
        String KEY_VIDEOSTATE = "videoState";
    }

    interface MMSKey {
        String key_type = "type";
        String key_value = "value";
        String key_result = "result";
    }

    interface TaxiKey {
        String key_deviceState = "deviceState";
        String key_taxiState = "taxiState";
        String key_deviceNumber = "deviceNumber";
        String key_carNUmber = "carNumber";
        String key_oprateNumber = "oprateNumber";
    }


}
