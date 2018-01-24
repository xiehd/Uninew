package com.uninew.car.constant;

/**
 * Created by Administrator on 2017/8/29.
 */

public interface DefineActivityAction {

    /**设置主Activity对应的Action*/
    public String Action_MainReSet="receiver_main_reset";
    /**发送设备状态对象*/
    public String Action_StateDate="Action_Device_StateDate";
    /**设备状态Action*/
    public String Action_Device_State="receiver_device_state";
    interface DeviceState{
        public String Device_State="device_state";//0x00 故障；0x01：正常
        public String Device_Type="device_type";//类型：0x00：摄像头
                                                  //       0x01: 计价器
                                                    //    0x02: 視頻
                                                      //   0x03: 空車屏
                                                        // 0x04: GPS天線
                                                         //0x05: 通訊模塊
                                                         //0x06: 供電
                                                         //0x07: 出租服務

    }




}
