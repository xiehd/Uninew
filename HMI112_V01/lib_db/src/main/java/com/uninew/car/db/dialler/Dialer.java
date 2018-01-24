package com.uninew.car.db.dialler;

/**
 * Created by Administrator on 2017/9/7 0007.
 */

public class Dialer {
    private int id;
    private String contact;//联系人
    private String phone;// 电话号码
    private String callDate;//通话时间
    private int callTime;//通话时长，单位：秒
    private int diallerState;//通话类型，1 来电 .INCOMING_TYPE；2 已拨 .OUTGOING_；3 未接 .MISSED_

    public Dialer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public int getCallTime() {
        return callTime;
    }

    public void setCallTime(int callTime) {
        this.callTime = callTime;
    }

    public int getDiallerState() {
        return diallerState;
    }

    public void setDiallerState(int diallerState) {
        this.diallerState = diallerState;
    }

    @Override
    public String toString() {
        return "Dialer{" +
                "id=" + id +
                ", contact='" + contact + '\'' +
                ", phone='" + phone + '\'' +
                ", callDate='" + callDate + '\'' +
                ", callTime=" + callTime +
                ", diallerState=" + diallerState +
                '}';
    }
}
