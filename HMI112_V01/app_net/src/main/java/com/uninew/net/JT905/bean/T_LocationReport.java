package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/19 0019.
 */

public class T_LocationReport extends BaseBean {
    /**
     * 报警标识
     **/
    private int alarmIndication;
    /**
     * 状态
     **/
    private int state;
    /**
     * 纬度
     **/
    private double latitude;
    /**
     * 经度
     **/
    private double longitude;
    /**
     * 高程 m
     **/
    private double elevation;
    /**
     * 速度 s
     **/
    private float speed;
    /**
     * 方向 0-360
     **/
    private float direction;

    /**
     * 时间 毫秒
     **/
    private long time;


    public T_LocationReport() {

    }

    /**
     * 正常位置上报
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @param elevation 单位米
     * @param speed     秒每米
     * @param direction 0-359
     * @param time
     */
    public T_LocationReport(double longitude,
                            double latitude, double elevation, float speed, float direction,
                            long time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.speed = speed;
        this.direction = direction;
        this.time = time;
    }

    public T_LocationReport(int alarmIndication, int state, double latitude, double longitude, double elevation,
                            float speed, float direction, long time) {
        this.alarmIndication = alarmIndication;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.speed = speed;
        this.direction = direction;
        this.time = time;
    }


    @Override
    public byte[] getDataBytes() {
        byte[] bytes = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);
        try {
            out.writeInt(alarmIndication);
            out.writeInt(state);
            //TODO 808与905差异点
            out.writeInt((int) (latitude * 60 * 10000));
            out.writeInt((int) (longitude * 60 * 10000));
//            out.writeShort((int) elevation);
            out.writeShort((int) (speed * 10));
            out.writeByte((int) direction);
            out.write(ProtocolTool.getBCD12TimeBytes(time));
            if (hasAddInfo) {//如果存在附加信息
                if (addMessages != null && !addMessages.isEmpty()) {
                    for (byte[] b : addMessages) {
                        out.write(b);
                    }
                }
            }
            out.flush();
            bytes = byteStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteStream != null) {
                    byteStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    @Override
    public Object getDataPacket(byte[] datas) {
        {
            ByteArrayInputStream stream = new ByteArrayInputStream(datas);
            DataInputStream in = new DataInputStream(stream);
            try {
                alarmIndication = in.readInt();
                state = in.readInt();
                latitude = in.readInt();
                longitude = in.readInt();
                elevation = in.readUnsignedShort();
                speed = in.readUnsignedShort();
                direction = in.readUnsignedShort();
                byte[] b = new byte[6];
                in.read(b);
                time = ProtocolTool.getTimeFromBCD12(ProtocolTool.bcd2Str(b));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return this;
        }
    }

    public class AddId {
        /**
         * 里程，1/10km对应车上里程表读数
         */
        public static final int MILEAGE = 0x01;
        /**
         * 油量，1/10L,对应车上油量表读数
         */
        public static final int OIL_MASS = 0x02;
        /**
         * 行驶记录功能获取的速度，1/10km/h
         */
        public static final int DRIVE_SPEED = 0x03;
        /**
         * 需要人工确认报警事件的ID，WORD，从1 开始计数
         */
        public static final int NEED_MANUAL_CONFIRM_ALARM_ID = 0x04;


        /**
         * 超速报警附加信息，见SpeedAlarmAdd
         */
        public static final int SPEED_ALARM = 0x11;
        /**
         * 进出区域/路线报警附加信息，见InOutAreaAlarmAdd
         */
        public static final int IN_OUT_AREA_ALARM = 0x12;
        /**
         * 路段行驶时间不足/过长报警附加信息 见DriveTimeAlarm
         */
        public static final int DRIVE_TIME_ALARM = 0x13;
        /**
         * 0x25 4 扩展车辆信号状态位，定义见表20-1
         */
        public static final int EXPAND_CAR_SIGNAL_STATE = 0x25;
        /**
         * 0x2A 2 IO状态位，定义见表20-2
         */
        public static final int IO_STATE = 0x2A;
        /**
         * 0x2B 4 模拟量，bit0-15，AD0；bit16-31，AD1
         */
        public static final int ANALOG_QUANTITY = 0x2B;
        /**
         * 0x30 1 BYTE，无线通信网络信号强度
         */
        public static final int WIRELESS_NETWORK_SIGNAL_STRENGTH = 0x30;
        /**
         * 0x31 1 BYTE，GNSS定位卫星数
         */
        public static final int GNSS_POSTION_STATELLITE_NUMBER = 0x31;
        /**
         * 自定义区域
         */
        public static final int CUSTOM_AREA = 0xE0;
        /**
         * String[8]，最后经过站点ID
         */
        public static final int LAST_THROUGH_STATION = 0xB0;
        /**
         * BYTE，上下行，0：上行；1：下行
         **/
        public static final int ROUTE_TYPE = 0xB1;

    }


    /**
     * 扩展车辆信号状态位
     */
    public class ExpandCarSingalState {
        /**
         * 0 1：近光灯信号
         */
        public static final int NEAR_LIGHT_SINGAL = 0x01;
        /**
         * 1 1：远光灯信号
         */
        public static final int DISTANCE_LIGHT_SINGAL = 0x02;
        /**
         * 2 1：右转向灯信号
         */
        public static final int RIGHT_LIGHT_SINGAL = 0x04;
        /**
         * 3 1：左转向灯信号
         */
        public static final int LEFT_LIGHT_SINGAL = 0x08;
        /**
         * 4 1：制动信号
         */
        public static final int BRAKING_SINGAL = 0x10;
        /**
         * 5 1：倒档信号
         */
        public static final int BACK_GEAR_SINGAL = 0x20;
        /**
         * 6 1：雾灯信号
         */
        public static final int FOGLIGHT_SINGAL = 0x40;
        /**
         * 7 1：示廓灯
         */
        public static final int CLEARANCE_LAMP_SINGAL = 0x80;
        /**
         * 8 1：喇叭信号
         */
        public static final int HORN_SINGAL = 0x100;
        /**
         * 9 1：空调状态
         */
        public static final int AIR_STATE = 0x200;
        /**
         * 10 1：空挡信号
         */
        public static final int NEUTRAL_GEAR_SINGAL = 0x400;
        /**
         * 11 1：缓速器工作
         */
        public static final int BUFFER_WORK = 0x800;
        /**
         * 12 1：ABS工作
         */
        public static final int ABS_WORK = 0x1000;
        /**
         * 13 1：加热器工作
         */
        public static final int HEAT_WORK = 0x2000;
        /**
         * 14 1：离合器状态
         */
        public static final int CLUTCH_WORK = 0x4000;
    }

    /**
     * IO状态位
     */
    public class IOState {
        /**
         * 0 1：深度休眠状态
         */
        public static final int DEEP_SLEEP = 0x01;
        /**
         * 1 1：休眠状态
         */
        public static final int SLEEP = 0x02;
    }

    /**
     * 存在附加信息
     */
    private boolean hasAddInfo = false;
    private List<byte[]> addMessages = null;

    /**
     * 检查是否有添加附加信息
     *
     * @return 有
     */
    public boolean isHasAddInfo() {
        return hasAddInfo;
    }

    /**
     * 设置是否添加附加信息
     *
     * @param hasAddInfo true:添加
     */
    public void setHasAddInfo(boolean hasAddInfo) {
        this.hasAddInfo = hasAddInfo;
        addMessages = new ArrayList<byte[]>();
    }

    /**
     * 设置附加信息 里程
     *
     * @param mileage
     */
    public void setMileage(double mileage) {
        if (!isHasAddInfo()) {
            setHasAddInfo(true);
        }
        byte[] bytes = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);
        try {
            out.writeByte(AddId.MILEAGE);
            out.writeByte(4);
            out.writeInt((int) mileage * 10);
            bytes = byteStream.toByteArray();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteStream != null) {
                    byteStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bytes != null && bytes.length > 0) {
                addMessages.add(bytes);
            }
        }
    }

    /**
     * 设置附加信息 油量
     *
     * @param oilVolume
     */
    public void setOilVolume(double oilVolume) {
        if (!isHasAddInfo()) {
            setHasAddInfo(true);
        }
        byte[] bytes = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);
        try {
            out.writeByte(AddId.OIL_MASS);
            out.writeByte(2);
            out.writeShort((int) oilVolume * 10);
            bytes = byteStream.toByteArray();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteStream != null) {
                    byteStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bytes != null && bytes.length > 0) {
                addMessages.add(bytes);
            }
        }
    }


    /**
     * 设置附加信息 行驶记录功能获取的速度 单位：是km/h
     *
     * @param driveSpeed
     */
    public void setDriveSpeed(double driveSpeed) {
        if (!isHasAddInfo()) {
            setHasAddInfo(true);
        }
        byte[] bytes = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);
        try {
            out.writeByte(AddId.OIL_MASS);
            out.writeByte(2);
            out.writeShort((int) driveSpeed * 10);
            bytes = byteStream.toByteArray();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteStream != null) {
                    byteStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bytes != null && bytes.length > 0) {
                addMessages.add(bytes);
            }
        }
    }

    /**
     * 需要人工确认报警事件的 ID，WORD，从 1 开始计数
     *
     * @param alarmId
     */
    public void setAlarmId(double alarmId) {
        if (!isHasAddInfo()) {
            setHasAddInfo(true);
        }
        byte[] bytes = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);
        try {
            out.writeByte(AddId.NEED_MANUAL_CONFIRM_ALARM_ID);
            out.writeByte(2);
            out.writeShort((int) alarmId * 10);
            bytes = byteStream.toByteArray();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteStream != null) {
                    byteStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bytes != null && bytes.length > 0) {
                addMessages.add(bytes);
            }
        }
    }

    /**
     * 超速报警附加信息
     *
     * @param positionType 位置类型  1：圆形区域；2：矩形区域；3：多边形区域；4：路段
     * @param sectionsId   区域或路段ID
     * @param direction    方向  0：进；1：出
     */
    public void setAreaAlarm(int positionType, int sectionsId, int direction) {
        if (!isHasAddInfo()) {
            setHasAddInfo(true);
        }
        byte[] bytes = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);
        try {
            out.writeByte(AddId.IN_OUT_AREA_ALARM);
            out.writeByte(6);
            out.writeByte(positionType);
            out.writeInt(sectionsId);
            out.writeByte(direction);
            bytes = byteStream.toByteArray();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteStream != null) {
                    byteStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bytes != null && bytes.length > 0) {
                addMessages.add(bytes);
            }
        }
    }

    /**
     * 进出区域/路线报警
     *
     * @param positionType 位置类型
     *                     0：无特定位置1：圆形区域；2：矩形区域；3：多边形区域；4：路段
     * @param sectionsId   区域或路段ID
     */
    public void setSpeedAlarm(int positionType, int sectionsId) {
        if (!isHasAddInfo()) {
            setHasAddInfo(true);
        }
        byte[] bytes = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);
        try {
            out.writeByte(AddId.SPEED_ALARM);
            if (positionType == 0) {
                out.writeByte(1);
                out.writeByte(positionType);
            } else {
                out.writeByte(5);
                out.writeByte(positionType);
                out.writeInt(sectionsId);
            }
            bytes = byteStream.toByteArray();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteStream != null) {
                    byteStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bytes != null && bytes.length > 0) {
                addMessages.add(bytes);
            }
        }
    }


    /**
     * 路段行驶时间不足/过长报警
     *
     * @param sectionsId 路段ID
     * @param time       路段行驶时间  单位为秒
     * @param result     结果  0：不足 1：过长
     */
    public void setDriveTimeAlarm(int sectionsId, long time, int result) {
        if (!isHasAddInfo()) {
            setHasAddInfo(true);
        }
        byte[] bytes = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);
        try {
            out.writeByte(AddId.DRIVE_TIME_ALARM);
            out.writeByte(7);
            out.writeInt(sectionsId);
            out.writeShort((int) time);
            out.writeByte(result);
            bytes = byteStream.toByteArray();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteStream != null) {
                    byteStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bytes != null && bytes.length > 0) {
                addMessages.add(bytes);
            }
        }
    }

    /**
     * 扩展车辆信号状态位
     *
     * @param signalState
     */
    public void setSignalState(int signalState) {
        if (!isHasAddInfo()) {
            setHasAddInfo(true);
        }
        byte[] bytes = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);
        try {
            out.writeByte(AddId.EXPAND_CAR_SIGNAL_STATE);
            out.writeByte(4);
            out.writeInt(signalState);
            bytes = byteStream.toByteArray();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteStream != null) {
                    byteStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bytes != null && bytes.length > 0) {
                addMessages.add(bytes);
            }
        }
    }

    /**
     * IO状态位
     *
     * @param ioState
     */
    public void setIoState(int ioState) {
        if (!isHasAddInfo()) {
            setHasAddInfo(true);
        }
        byte[] bytes = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);
        try {
            out.writeByte(AddId.IO_STATE);
            out.writeByte(2);
            out.writeShort(ioState);
            bytes = byteStream.toByteArray();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteStream != null) {
                    byteStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bytes != null && bytes.length > 0) {
                addMessages.add(bytes);
            }
        }
    }

    /**
     * 0x2B 4 模拟量，bit0-15，AD0；bit16-31，AD1
     *
     * @param AnalogQuantity
     */
    public void setAnalogQuantity(int AnalogQuantity) {
        if (!isHasAddInfo()) {
            setHasAddInfo(true);
        }
        byte[] bytes = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);
        try {
            out.writeByte(AddId.ANALOG_QUANTITY);
            out.writeByte(4);
            out.writeInt(AnalogQuantity);
            bytes = byteStream.toByteArray();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteStream != null) {
                    byteStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bytes != null && bytes.length > 0) {
                addMessages.add(bytes);
            }
        }
    }

    /**
     * 0x30 1 BYTE，无线通信网络信号强度
     *
     * @param NetworkSignal
     */
    public void setNetworkSignal(int NetworkSignal) {
        if (!isHasAddInfo()) {
            setHasAddInfo(true);
        }
        byte[] bytes = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);
        try {
            out.writeByte(AddId.WIRELESS_NETWORK_SIGNAL_STRENGTH);
            out.writeByte(1);
            out.writeByte(NetworkSignal);
            bytes = byteStream.toByteArray();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteStream != null) {
                    byteStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bytes != null && bytes.length > 0) {
                addMessages.add(bytes);
            }
        }
    }

    /**
     * 0x31 1 BYTE，GNSS定位卫星数
     *
     * @param StatelliteNumber
     */
    public void setStatelliteNumber(int StatelliteNumber) {
        if (!isHasAddInfo()) {
            setHasAddInfo(true);
        }
        byte[] bytes = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);
        try {
            out.writeByte(AddId.GNSS_POSTION_STATELLITE_NUMBER);
            out.writeByte(1);
            out.writeByte(StatelliteNumber);
            bytes = byteStream.toByteArray();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteStream != null) {
                    byteStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bytes != null && bytes.length > 0) {
                addMessages.add(bytes);
            }
        }
    }


    @Override
    public int getTcpId() {
        return this.tcpId;
    }

    @Override
    public void setTcpId(int tcpId) {
        this.tcpId = tcpId;
    }

    @Override
    public int getTransportId() {
        return this.transportId;
    }

    @Override
    public void setTransportId(int transportId) {
        this.transportId = transportId;
    }

    @Override
    public String getSmsPhoneNumber() {
        return this.smsPhonenumber;
    }

    @Override
    public void setSmsPhoneNumber(String smsPhonenumber) {
        this.smsPhonenumber = smsPhonenumber;
    }

    @Override
    public int getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public int getMsgId() {
        return BaseMsgID.LOCATION_INFORMATION_REPORT;
    }

    @Override
    public String toString() {
        return "T_LocationReport{" +
                "alarmIndication=" + alarmIndication +
                ", state=" + state +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", elevation=" + elevation +
                ", speed=" + speed +
                ", direction=" + direction +
                ", time=" + time +
                ", hasAddInfo=" + hasAddInfo +
                ", addMessages=" + addMessages +
                '}';
    }

    public int getAlarmIndication() {
        return alarmIndication;
    }

    public void setAlarmIndication(int alarmIndication) {
        this.alarmIndication = alarmIndication;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<byte[]> getAddMessages() {
        return addMessages;
    }

    public void setAddMessages(List<byte[]> addMessages) {
        this.addMessages = addMessages;
    }
}
