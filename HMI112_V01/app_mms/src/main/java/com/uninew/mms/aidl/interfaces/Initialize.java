package com.uninew.mms.aidl.interfaces;


public class Initialize {
    private static final String TAG = "Initialize";
    private static final boolean D = true;
    private static IMcuReceiveListener mcuReceiveListener;
    private static IMcuSend mcuManage;
    public static Initialize initialize;


    public Initialize() {
    }

    public Initialize(IMcuReceiveListener mcuReceiveListener) {
        this.mcuReceiveListener = mcuReceiveListener;
    }

    /**
     * 获得发送对象
     *
     * @return
     */
    public static IMcuSend getMcuManage() {
        if (mcuManage == null)
            mcuManage = new McuManage(mcuReceiveListener);
        return mcuManage;
    }

    public static IMcuReceiveListener getMcuReceiveListener() {
        if (mcuReceiveListener == null){
//            if (D)
//                Log.e(TAG, "----getMcuReceiveListener--null---");
            throw new Error("getMcuReceiveListener is NULL!!");
        }
        return mcuReceiveListener;
    }

    public static void setMcuReceiveListener(IMcuReceiveListener mcuReceiveListener) {
        Initialize.mcuReceiveListener = mcuReceiveListener;
    }


}
