package com.uninew.until;

/**
 * Created by Administrator on 2017/11/8.
 */

public interface SPKey {

    String BASESETTINGS_SPINNER_TIME = "Base_sp_time";

    String BASESETTINGS_SPINNER_PRINT = "Base_sp_print";

    interface SerialKey {
        String RATE_232_1 = "rate_232_1";
        String RATE_232_2 = "rate_232_2";
        String RATE_232_3 = "rate_232_3";
        String RATE_485_1 = "rate_485_1";

        String serial_out_232_1 = "serial_out_232_1";
        String serial_out_232_2 = "serial_out_232_2";
        String serial_out_232_3 = "serial_out_232_3";
        String serial_out_485_1 = "serial_out_485_1";

    }
    interface IOKey{
        String IOKey_use1 = "IOKey_spinner_use1";
        String IOKey_use2 = "IOKey_spinner_use2";
        String IOKey_use3 = "IOKey_spinner_use3";
        String IOKey_use4 = "IOKey_spinner_use4";

        String IOKey_out_use1 = "IOKey_out_spinner_use1";
        String IOKey_out_use2 = "IOKey_out_spinner_use2";
        String IOKey_out_use3 = "IOKey_out_spinner_use3";
        String IOKey_out_use4 = "IOKey_out_spinner_use4";

        String IOKey_level1 = "IOKey_spinner_level1";
        String IOKey_level2 = "IOKey_spinner_level2";
        String IOKey_level3 = "IOKey_spinner_level3";
        String IOKey_level4 = "IOKey_spinner_level4";

        String IOKey_dvr1 = "IOKey_spinner_dvr1";
        String IOKey_dvr2 = "IOKey_spinner_dvr2";
        String IOKey_dvr3 = "IOKey_spinner_dvr3";
        String IOKey_dvr4 = "IOKey_spinner_dvr4";
    }
}
