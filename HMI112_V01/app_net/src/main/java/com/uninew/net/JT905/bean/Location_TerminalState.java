package com.uninew.net.JT905.bean;

import android.util.Log;

import com.uninew.net.JT905.common.ProtocolTool;

import java.io.Serializable;

/**
 * 终端状态
 *
 * @author rong
 */
public class Location_TerminalState implements Serializable {

    private static final long serialVersionUID = -4261766262770545628L;
    private static final String TAG="Location_TerminalState";
    private static final boolean D=true;

    private boolean isLocation ;//定位状态bit0
    private boolean isSourthLatitude;//南北纬bit1
    private boolean isWestLogitude;//东西经bit2
    private boolean isRunning;//运营状态bit3
    private boolean isReserve;//是否已经预约bit4
    private boolean isEmptyToHeavy;//空车转重车bit5
    private boolean isHeavyToEmpty;//重车转空车bit6
    private boolean isAccOn;//acc状态bit8
    private boolean isHeavy;//是否重车bit9
    private boolean isOilCut;//油路是否断开bit10
    private boolean isCircuitCut;//电路是否断开bit11
    private boolean isDoorLocked;//车门加解锁bit12
    private boolean isCarLocket;//车辆锁定bit13
    private boolean isToOpreationTimes;//是否达到运营次数bit14
    //设置成公共字段，是方便在通讯端获取当前变化的状态。
    public int stateBitNumber=-1;//报警bit位 0-28
    public int stateBitValue;//报警bit设置值0或1

    private static final int state_ON = 0x01;
    private static final int state_OFF = 0x00;

    public Location_TerminalState() {
        super();
    }


    /**
     * 设置状态标志，注意单次只能设置一个状态开始或者结束，所以触发也是单次只能触发一次
     * @param state 当前的标志，会根据当前状态进行设置某一个bit位的状态
     * @return 设置完成后的标志
     */
    public int setState(int state){
        if (stateBitNumber==-1){
            return state;
        }
        if(D)Log.v(TAG,"setState,state="+state+",stateBitNumber="+stateBitNumber+",stateBitValue="+stateBitValue);
        return ProtocolTool.setBit(state,stateBitNumber,stateBitValue);
    }


    public boolean isLocation() {
        return isLocation;
    }
    public void setLocation(boolean location) {
        isLocation = location;
        stateBitNumber=0;
        if (location){
            stateBitValue=state_ON;
        }else{
            stateBitValue=state_OFF;
        }
    }

    public boolean isSourthLatitude() {
        return isSourthLatitude;
    }

    public void setSourthLatitude(boolean sourthLatitude) {
        isSourthLatitude = sourthLatitude;
        stateBitNumber=1;
        if (sourthLatitude){
            stateBitValue=state_ON;
        }else{
            stateBitValue=state_OFF;
        }
    }

    public boolean isWestLogitude() {
        return isWestLogitude;
    }

    public void setWestLogitude(boolean westLogitude) {
        isWestLogitude = westLogitude;
        stateBitNumber=2;
        if (westLogitude){
            stateBitValue=state_ON;
        }else{
            stateBitValue=state_OFF;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
        stateBitNumber=3;
        if (running){
            stateBitValue=state_ON;
        }else{
            stateBitValue=state_OFF;
        }
    }

    public boolean isReserve() {
        return isReserve;
    }

    public void setReserve(boolean reserve) {
        isReserve = reserve;
        stateBitNumber=4;
        if (reserve){
            stateBitValue=state_ON;
        }else{
            stateBitValue=state_OFF;
        }
    }

    public boolean isEmptyToHeavy() {
        return isEmptyToHeavy;
    }

    public void setEmptyToHeavy(boolean emptyToHeavy) {
        isEmptyToHeavy = emptyToHeavy;
        stateBitNumber=5;
        if (emptyToHeavy){
            stateBitValue=state_ON;
        }else{
            stateBitValue=state_OFF;
        }
    }

    public boolean isHeavyToEmpty() {
        return isHeavyToEmpty;
    }

    public void setHeavyToEmpty(boolean heavyToEmpty) {
        isHeavyToEmpty = heavyToEmpty;
        stateBitNumber=6;
        if (heavyToEmpty){
            stateBitValue=state_ON;
        }else{
            stateBitValue=state_OFF;
        }
    }

    public boolean isAccOn() {
        return isAccOn;
    }

    public void setAccOn(boolean accOn) {
        isAccOn = accOn;
        stateBitNumber=8;
        if (accOn){
            stateBitValue=state_ON;
        }else{
            stateBitValue=state_OFF;
        }
    }

    public boolean isHeavy() {
        return isHeavy;
    }

    public void setHeavy(boolean heavy) {
        isHeavy = heavy;
        stateBitNumber=9;
        if (heavy){
            stateBitValue=state_ON;
        }else{
            stateBitValue=state_OFF;
        }
    }

    public boolean isOilCut() {
        return isOilCut;
    }

    public void setOilCut(boolean oilCut) {
        isOilCut = oilCut;
        stateBitNumber=10;
        if (oilCut){
            stateBitValue=state_ON;
        }else{
            stateBitValue=state_OFF;
        }
    }

    public boolean isCircuitCut() {
        return isCircuitCut;
    }

    public void setCircuitCut(boolean circuitCut) {
        isCircuitCut = circuitCut;
        stateBitNumber=11;
        if (circuitCut){
            stateBitValue=state_ON;
        }else{
            stateBitValue=state_OFF;
        }
    }

    public boolean isDoorLocked() {
        return isDoorLocked;
    }

    public void setDoorLocked(boolean doorLocked) {
        isDoorLocked = doorLocked;
        stateBitNumber=12;
        if (doorLocked){
            stateBitValue=state_ON;
        }else{
            stateBitValue=state_OFF;
        }
    }

    public boolean isCarLocket() {
        return isCarLocket;
    }

    public void setCarLocket(boolean carLocket) {
        isCarLocket = carLocket;
        stateBitNumber=13;
        if (carLocket){
            stateBitValue=state_ON;
        }else{
            stateBitValue=state_OFF;
        }
    }

    public boolean isToOpreationTimes() {
        return isToOpreationTimes;
    }

    public void setToOpreationTimes(boolean toOpreationTimes) {
        isToOpreationTimes = toOpreationTimes;
        stateBitNumber=14;
        if (toOpreationTimes){
            stateBitValue=state_ON;
        }else{
            stateBitValue=state_OFF;
        }
    }

    @Override
    public String toString() {
        return "Location_TerminalState{" +
                "isLocation=" + isLocation +
                ", isSourthLatitude=" + isSourthLatitude +
                ", isWestLogitude=" + isWestLogitude +
                ", isRunning=" + isRunning +
                ", isReserve=" + isReserve +
                ", isEmptyToHeavy=" + isEmptyToHeavy +
                ", isHeavyToEmpty=" + isHeavyToEmpty +
                ", isAccOn=" + isAccOn +
                ", isHeavy=" + isHeavy +
                ", isOilCut=" + isOilCut +
                ", isCircuitCut=" + isCircuitCut +
                ", isDoorLocked=" + isDoorLocked +
                ", isCarLocket=" + isCarLocket +
                ", isToOpreationTimes=" + isToOpreationTimes +
                ", stateBitNumber=" + stateBitNumber +
                ", stateBitValue=" + stateBitValue +
                '}';
    }
}
