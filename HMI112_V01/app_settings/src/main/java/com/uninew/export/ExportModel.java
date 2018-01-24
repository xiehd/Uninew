package com.uninew.export;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.uninew.car.db.paramSet.ParamSetKey;
import com.uninew.car.db.paramSet.ParamSetLocalDataSource;
import com.uninew.car.db.paramSet.ParamSetLocalSource;
import com.uninew.car.db.paramSet.ParamSetting;
import com.uninew.car.db.revenue.Revenue;
import com.uninew.car.db.revenue.RevenueLocalDataSource;
import com.uninew.car.db.revenue.RevenueLocalSource;
import com.uninew.car.db.settings.BaseLocalDataSource;
import com.uninew.car.db.settings.BaseLocalSource;
import com.uninew.car.db.signs.SignLocalDataSource;
import com.uninew.car.db.signs.SignLocalSource;
import com.uninew.car.db.signs.SignOrSignOut;
import com.uninew.net.JT905.bean.T_LocationReport;
import com.uninew.net.JT905.bean.T_OperationDataReport;
import com.uninew.net.JT905.bean.T_SignInReport;
import com.uninew.net.JT905.bean.T_SignOutReport;
import com.uninew.net.JT905.common.Define;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import tools.TimeTool;


/**
 * Created by Administrator on 2017/10/18 0018.
 */

public class ExportModel {

    private static final String TAG = "ExportModel";
    private Context mContext;
    public static final String SDCARD_PATH = "/mnt/media_rw/udisk/export";
    private static final String SAVA_PARAMSET_PATH = SDCARD_PATH + File.separator
            + Define.Default_DeviceId + ".CFG";
    private static final String SAVA_SIGNORSIGNOUT_PATH = SDCARD_PATH
            + File.separator + Define.Default_DeviceId + "QT.DAT";
    private static final String SAVA_REVENUE_PATH = SDCARD_PATH + File.separator
            + Define.Default_DeviceId + "YY.DAT";
    private static final String PLATFORM_MSG_TITLE = "主服务器IP,主服务器端口,备用服务器IP" +
            ",备用服务器端口,车牌号码,设备ID,超速报警值";
    private static final String PLATFORM_PARAM_TITLE = "心跳间隔时间,tcp消息应答超时时间," +
            "tcp消息重传次数,每次最长通话时间,通话复位电话,恢复出厂设置电话,";
    private static final String LOCATION_PARAM_TITLE = "位置汇报策略,位置汇报方案," +
            "未登录汇报间隔时间或者距离,ACC OFF汇报间隔时间或者距离,ACC ON汇报间隔时间或者距离," +
            "空车汇报间隔时间或者距离,重车汇报间隔时间或者距离,休眠汇报间隔时间或者距离,紧急报警汇报间隔时间或者距离";
    private ParamSetLocalSource paramSetLocalSource;
    private BaseLocalSource baseLocalSource;

    public ExportModel(Context context) {
        this.mContext = context;
        paramSetLocalSource = ParamSetLocalDataSource.getInstance(context);
        baseLocalSource = BaseLocalDataSource.getInstance(context);
    }

    private ExportCallBack mCallBack;

    public void setExportCallBack(ExportCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface ExportCallBack {
        /**
         * 导出成功
         */
        void onSuccess();

        /**
         * 失败
         *
         * @param erroe 0：数据库没有对应的数据，1：数据写入失败
         */
        void onFailure(int erroe);
    }

//    public void exportParamSet(final ExportCallBack callBack) {
//        ParamSetLocalDataSource.getInstance(mContext).getAllDBDatas(new ParamSetLocalSource.LoadParamSettesCallBack() {
//            @Override
//            public void onDBBaseDataLoaded(List<ParamSetting> buffers) {
//                P_ParamSet mParamSet = new P_ParamSet();
//                List<P_ParamSet.Param> params = new ArrayList<P_ParamSet.Param>();
//                for (ParamSetting paramSetting : buffers) {
//                    P_ParamSet.Param param = new P_ParamSet.Param();
//                    param.paramId = paramSetting.getKey();
//                    param.paramLength = paramSetting.getLength();
//                    Log.d(TAG,"id:"+  param.paramId );
//                    if (mParamSet.isBCDType(paramSetting.getKey()) || mParamSet.isStringType(paramSetting.getKey())) {
//                        param.paramValue = paramSetting.getValue();
//                    } else {
//                        param.paramValue = ProtocolTool.stringToInt(paramSetting.getValue());
//                    }
//                    params.add(param);
//                }
//                mParamSet.setParams(params);
//                Log.d(TAG,"savePath:"+SAVA_PARAMSET_PATH);
//                if (SDCardUtils.isSDCardEnable()) {
//                    exportFile(mParamSet.getDataBytes(), SAVA_PARAMSET_PATH, callBack);
//                }
//            }
//
//            @Override
//            public void onDataNotAailable() {
//                callBack.onFailure(0);
//            }
//        });
//    }

    private String mainIp = "";
    private String mainPort = "";
    private String spareIp = "";
    private String sparePort = "";
    private String maxSpeed = "";
    private String busNumbler = "";
    private String deviceId = "";
    private String heartBeat = "";
    private String timeOut = "";
    private String reTime = "";
    private String callTime = "";
    private String resetPhone = "";
    private String restoreSettingsPhone = "";
    private String locationStratege = "";
    private String locationPlan = "";
    private String unLogin = "";
    private String accOff = "";
    private String accOn = "";
    private String empty = "";
    private String noEmoty = "";
    private String sleep = "";
    private String emergency = "";

    public void exportParamSet() {
        StringBuilder builder = new StringBuilder();
        builder.append(PLATFORM_MSG_TITLE + ImportModel.WRAP);
        paramSetLocalSource.getParamSettesByKey(new ParamSetLocalSource.LoadParamSettesCallBack() {
            @Override
            public void onDBBaseDataLoaded(List<ParamSetting> buffers) {
                for (ParamSetting p : buffers) {
                    int key = p.getKey();
                    switch (key) {
                        case ParamSetKey.MainIpOrDomain:
                            mainIp = p.getValue();
                            break;
                        case ParamSetKey.MainTcpPort:
                            mainPort = p.getValue();
                            break;
                        case ParamSetKey.SpareIpOrDomain:
                            spareIp = p.getValue();
                            break;
                        case ParamSetKey.SpareTcpPort:
                            sparePort = p.getValue();
                            break;
                        case ParamSetKey.MaximumSpeed:
                            maxSpeed = p.getValue();
                            break;

                    }
                }
            }

            @Override
            public void onDataNotAailable() {

            }
        }, new int[]{
                ParamSetKey.MainIpOrDomain,
                ParamSetKey.MainTcpPort,
                ParamSetKey.SpareIpOrDomain,
                ParamSetKey.SpareTcpPort,
                ParamSetKey.MaximumSpeed,
        });
        baseLocalSource.getPlateNumber(new BaseLocalSource.GetBaseSettingCallBack() {
            @Override
            public void onDBBaseDataLoaded(String s) {
                busNumbler = s;
            }

            @Override
            public void onDataNotAailable() {

            }
        });
        baseLocalSource.getTerminalNumber(new BaseLocalSource.GetBaseSettingCallBack() {
            @Override
            public void onDBBaseDataLoaded(String s) {
                deviceId = s;
            }

            @Override
            public void onDataNotAailable() {

            }
        });
        builder.append(mainIp);
        builder.append(ImportModel.COMMA);
        builder.append(mainPort);
        builder.append(ImportModel.COMMA);
        builder.append(spareIp);
        builder.append(ImportModel.COMMA);
        builder.append(sparePort);
        builder.append(ImportModel.COMMA);
        builder.append(busNumbler);
        builder.append(ImportModel.COMMA);
        builder.append(deviceId);
        builder.append(ImportModel.COMMA);
        builder.append(maxSpeed);
        builder.append(ImportModel.WRAP);
        paramSetLocalSource.getParamSettesByKey(new ParamSetLocalSource.LoadParamSettesCallBack() {
            @Override
            public void onDBBaseDataLoaded(List<ParamSetting> buffers) {
                for (ParamSetting p : buffers) {
                    int key = p.getKey();
                    switch (key) {
                        case ParamSetKey.HeartBeat:
                            heartBeat = p.getValue();
                            break;
                        case ParamSetKey.TcpResponseTimeOut:
                            timeOut = p.getValue();
                            break;
                        case ParamSetKey.TcpResendTime:
                            reTime = p.getValue();
                            break;
                        case ParamSetKey.MaximumCallTimePerOne:
                            callTime = p.getValue();
                            break;
                        case ParamSetKey.ResetPhoneNumber:
                            resetPhone = p.getValue();
                            break;
                        case ParamSetKey.RestoreSettingsPhoneNumber:
                            restoreSettingsPhone = p.getValue();
                            break;
                    }
                }
            }

            @Override
            public void onDataNotAailable() {

            }
        }, new int[]{
                ParamSetKey.HeartBeat,
                ParamSetKey.TcpResponseTimeOut,
                ParamSetKey.TcpResendTime,
                ParamSetKey.MaximumCallTimePerOne,
                ParamSetKey.ResetPhoneNumber,
                ParamSetKey.RestoreSettingsPhoneNumber
        });
        builder.append(PLATFORM_PARAM_TITLE);
        builder.append(ImportModel.WRAP);
        builder.append(heartBeat);
        builder.append(ImportModel.COMMA);
        builder.append(timeOut);
        builder.append(ImportModel.COMMA);
        builder.append(reTime);
        builder.append(ImportModel.COMMA);
        builder.append(callTime);
        builder.append(ImportModel.COMMA);
        builder.append(resetPhone);
        builder.append(ImportModel.COMMA);
        builder.append(restoreSettingsPhone);
        builder.append(ImportModel.WRAP);
        paramSetLocalSource.getParamSettesByKey(new ParamSetLocalSource.LoadParamSettesCallBack() {
            @Override
            public void onDBBaseDataLoaded(List<ParamSetting> buffers) {
                for (ParamSetting p : buffers) {
                    int key = p.getKey();
                    switch (key) {
                        case ParamSetKey.LocationReportStratege:
                            locationStratege = p.getValue();
                            break;
                        case ParamSetKey.LocationReportPlan:
                            locationPlan = p.getValue();
                            break;
                        case ParamSetKey.UnLoginReportIntervalTime:
                            unLogin = p.getValue();
                            break;
                        case ParamSetKey.AccOffReportIntervalTime:
                            accOff = p.getValue();
                            break;
                        case ParamSetKey.AccOnfReportIntervalTime:
                            accOn = p.getValue();
                            break;
                        case ParamSetKey.EmptyReportIntervalTime:
                            empty = p.getValue();
                            break;
                        case ParamSetKey.NonEmptyReportIntervalTime:
                            noEmoty = p.getValue();
                            break;
                        case ParamSetKey.SleepReportIntervalTime:
                            sleep = p.getValue();
                            break;
                        case ParamSetKey.EmergencyReportIntervalTime:
                            emergency = p.getValue();
                            break;
                    }
                }
            }

            @Override
            public void onDataNotAailable() {

            }
        }, new int[]{
                ParamSetKey.LocationReportStratege,
                ParamSetKey.LocationReportPlan,
                ParamSetKey.UnLoginReportIntervalTime,
                ParamSetKey.AccOffReportIntervalTime,
                ParamSetKey.AccOnfReportIntervalTime,
                ParamSetKey.EmptyReportIntervalTime,
                ParamSetKey.NonEmptyReportIntervalTime,
                ParamSetKey.SleepReportIntervalTime,
                ParamSetKey.EmergencyReportIntervalTime
        });
        builder.append(LOCATION_PARAM_TITLE);
        builder.append(ImportModel.WRAP);
        builder.append(locationStratege);
        builder.append(ImportModel.COMMA);
        builder.append(locationPlan);
        builder.append(ImportModel.COMMA);
        builder.append(unLogin);
        builder.append(ImportModel.COMMA);
        builder.append(accOff);
        builder.append(ImportModel.COMMA);
        builder.append(accOn);
        builder.append(ImportModel.COMMA);
        builder.append(empty);
        builder.append(ImportModel.COMMA);
        builder.append(noEmoty);
        builder.append(ImportModel.COMMA);
        builder.append(sleep);
        builder.append(ImportModel.COMMA);
        builder.append(emergency);
        builder.append(ImportModel.WRAP);
        try {
            exportFile(builder.toString().getBytes(ImportModel.CHARSET_GBK), SDCARD_PATH
                    + File.separator + ImportModel.PARAM_FILE_NAME);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void exportSignOrSignOut() {
        SignLocalDataSource.getInstance(mContext).getAllDBDatas(new SignLocalSource.LoadSignsCallBack() {
            @Override
            public void onDBBaseDataLoaded(List<SignOrSignOut> buffers) {
                List<byte[]> datas = new ArrayList<>();
                for (SignOrSignOut signOrSignOut : buffers) {
                    int state = signOrSignOut.getSignState();
                    if (state == 0) {
                        T_SignInReport signInReport = new T_SignInReport();
                        signInReport.setBootTime(signOrSignOut.getBootTime());
                        signInReport.setBusinessLicense(signOrSignOut.getBusinessLicense());
                        signInReport.setCarNumber(signOrSignOut.getPlateNumber());
                        signInReport.setDriverCertificate(signOrSignOut.getDriverCertificate());
                        try {
                            T_LocationReport locationReport = new T_LocationReport(signOrSignOut.getAlarm()
                                    , signOrSignOut.getState(), signOrSignOut.getLatitude()
                                    , signOrSignOut.getLongitude(), signOrSignOut.getElevation()
                                    , (float) signOrSignOut.getSpeed(), (float) signOrSignOut.getDirection()
                                    , TimeTool.parseToLong(signOrSignOut.getTime()));
                            signInReport.setLocationReport(locationReport);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        signInReport.setExtend(signOrSignOut.getExtendedAttributes());
                        datas.add(signInReport.getDataBytes());
                    } else {
                        T_SignOutReport signOutReport = new T_SignOutReport();
                        signOutReport.setBootTime(signOrSignOut.getBootTime());
                        signOutReport.setBetweenMileage((float) signOrSignOut.getNightMileage());
                        signOutReport.setCardIncome(signOrSignOut.getCardTimes());
                        signOutReport.setMeterKValue(signOrSignOut.getMeterKValue());
                        signOutReport.setCardIncome((float) signOrSignOut.getCashCardAmount());
                        signOutReport.setTrips(signOrSignOut.getCarTimes());
                        signOutReport.setCardTimes(signOrSignOut.getCardTimes());
                        signOutReport.setMileage((float) signOrSignOut.getRunMileage());
                        signOutReport.setOperationMileage((float) signOrSignOut.getOnDutyMileage());
                        signOutReport.setTimingTime(signOrSignOut.getTimingTime());
                        signOutReport.setTotalIncome((float) signOrSignOut.getAmount());
                        signOutReport.setTotalMileage((float) signOrSignOut.getAllMileage());
                        signOutReport.setTotalOperationMileage((float) signOrSignOut.getRevenueAllMileage());
                        signOutReport.setShutDownTime(signOrSignOut.getShutdownTime());
                        signOutReport.setTotalOperation(signOrSignOut.getRevenueTimes());
                        signOutReport.setUnitPrice((float) signOrSignOut.getPrice());
                        signOutReport.setBusinessLicense(signOrSignOut.getBusinessLicense());
                        signOutReport.setCarNumber(signOrSignOut.getPlateNumber());
                        signOutReport.setDriverCertificate(signOrSignOut.getDriverCertificate());
                        signOutReport.setExtend(signOrSignOut.getExtendedAttributes());
                        signOutReport.setSignOutWay(signOrSignOut.getSignOutType());
                        try {
                            T_LocationReport locationReport = new T_LocationReport(signOrSignOut.getAlarm()
                                    , signOrSignOut.getState(), signOrSignOut.getLatitude()
                                    , signOrSignOut.getLongitude(), signOrSignOut.getElevation()
                                    , (float) signOrSignOut.getSpeed(), (float) signOrSignOut.getDirection()
                                    , TimeTool.parseToLong(signOrSignOut.getTime()));
                            signOutReport.setLocationReport(locationReport);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        datas.add(signOutReport.getDataBytes());
                    }
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(stream);
                try {
                    out.writeShort(datas.size());
                    for (byte[] data : datas) {
                        out.write(data);
                    }
                    out.flush();
                    exportFile(stream.toByteArray(), SAVA_SIGNORSIGNOUT_PATH);
                } catch (IOException var13) {
                    var13.printStackTrace();
                } finally {
                    try {
                        if (stream != null) {
                            stream.close();
                        }

                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException var12) {
                        var12.printStackTrace();
                    }

                }
            }

            @Override
            public void onDataNotAailable() {
                if(mCallBack != null)
                mCallBack.onFailure(0);
            }
        });
    }


    public void exportRevenue() {
        RevenueLocalDataSource.getInstance(mContext).getAllDBDatas(new RevenueLocalSource.LoadRevenueCallback() {
            @Override
            public void onDBBaseDataLoaded(List<Revenue> buffers) {
                List<byte[]> datas = new ArrayList<byte[]>();
                for (Revenue revenue : buffers) {
                    T_LocationReport locationReport = new T_LocationReport();
                    locationReport.getDataPacket(revenue.getUpCarLocation());
                    T_OperationDataReport operationDataReport = new T_OperationDataReport(
                            (T_LocationReport) locationReport.getDataPacket(revenue.getUpCarLocation()),
                            (T_LocationReport) locationReport.getDataPacket(revenue.getDownLocation()),
                            revenue.getRevenueId(), revenue.getEvaluationId(), revenue.getEvaluation(),
                            revenue.getEvaluationExtended(), revenue.getOrderId(), revenue.getRevenueDatas()
                    );
                    datas.add(operationDataReport.getDataBytes());
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(stream);
                try {
                    out.writeShort(datas.size());
                    for (byte[] data : datas) {
                        out.write(data);
                    }
                    out.flush();
                    exportFile(stream.toByteArray(), SAVA_REVENUE_PATH);
                } catch (IOException var13) {
                    var13.printStackTrace();
                } finally {
                    try {
                        if (stream != null) {
                            stream.close();
                        }

                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException var12) {
                        var12.printStackTrace();
                    }

                }
            }

            @Override
            public void onDataNotAailable() {
                if(mCallBack != null)
                mCallBack.onFailure(0);
            }
        });
    }


    private void exportFile(final byte[] saveBuffer, final String savePath) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "savePath:" + savePath);
                final File file = FileUtils.getFileByPath(savePath);
                if (file != null) {
                    if (!FileUtils.isFileExists(file)) {
                        FileUtils.createFileByDeleteOldFile(file);
                    }
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    boolean s = FileIOUtils.writeFileFromBytesByStream(file, saveBuffer, true);
                    Message msg = Message.obtain();
                    msg.obj = s;
                    msg.what = WHAT_EXPORT_RESULT;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();

    }

    private static final int WHAT_EXPORT_RESULT = 0x01;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case WHAT_EXPORT_RESULT:
                    boolean s = (boolean) msg.obj;
                    if (s) {
                        mCallBack.onSuccess();
                    } else {
                        mCallBack.onFailure(1);
                    }
                    break;
            }
        }
    };

}
