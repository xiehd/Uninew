package com.uninew.net.JT905.bean;

import android.util.Log;

import com.uninew.net.JT905.common.ProtocolTool;

import java.io.Serializable;

/**
 * 报警标志
 * 使用思路：报警触发这每次只能设置该类中的一个字段，报警时上报时通过调用类中的setWarnFlag方式进行设置报警标志。
 *
 */
public class Location_AlarmFlag implements Serializable{
	
	private static final long serialVersionUID = 2304917061916632147L;
	private static final String TAG="Location_AlarmFlag";
	private static final boolean D=true;
	
	private boolean isEmergency;//紧急报警 bit0
	private boolean preWarn;//预警bit1
	private boolean isGNSSFault ;//GNSS故障bit2
	private boolean isGNSSAerial ;//GNSS天线未接或被剪断bit3
	private boolean isGNSSShortCircuit;//GNSS天线短路bit4
	private boolean mainUndervoltage;//主电源欠压报警bit5
	private boolean mainPowerDown;//主电源掉电bit6
	private boolean mainLCDError;//主显示屏故障bit7
	private boolean TTSError;//TTS模块故障bit8
	private boolean cameraError;//摄像头故障bit9
	private boolean meterError;//计价器故障bit10
	private boolean evaluatorError;//服务评价器故障bit11
	private boolean ADError;//广告牌故障bit12
	private boolean LEDScreenError;//LED显示屏故障bit13
	private boolean securityModulError;//安全模块故障bit14
	private boolean  LEDLightError;//LED顶灯故障bit15
	private boolean overSpeeding ;//超速报警bit16
	private boolean continuousDriving ;//连续驾驶bit17
	private boolean accumulatedOvertimeDriving;//累计驾驶超时bit18
	private boolean overTimeParking;//超时停车bit19
	private boolean inOutAreaOrLine;//进出区域/路线bit20
	private boolean runtimeNotEnoughOrTooLong;//路段行驶时间不足/过长bit21
	private boolean forbiddenRouteRun;//禁行路段行驶bit22
	private boolean speedSensorError;//速度传感器故障bit23
	private boolean illegalAccOn;//车辆非法点火bit24
	private boolean illegalMoveCar;//车辆非法移位bit25
	private boolean ISUStrorageError;//ISU存储异常bit26
	private boolean recordingError;//录音异常bit27
	private boolean meterClockOverError;//计价器实时时钟超过规定的误差范围bit28
	//设置成公共字段，是方便在通讯端获取当前变化的报警。
	public int warnBitNumber=-1;//报警bit位 0-28
	public int warnBitValue;//报警bit设置值0或1

    private static final int Bit_Warning=0x01;
    private static final int Bit_UnWarning=0x00;

	/**
	 * 设置报警标志，注意单次只能设置一个报警开始或者解除，所以触发也是单次只能触发一次
	 * @param warnFlag 当前的报警标志，会根据当前报警状态进行设置某一个bit位的状态
	 * @return 设置完成后的报警标志
	 */
	public int setWarnFlag(int warnFlag){
		Log.v(TAG,"setWarnFlag,warnFlag="+warnFlag);
		if (warnBitNumber==-1){
			return warnFlag;
		}
		Log.v(TAG,"setWarnFlag,warnFlag="+warnFlag+",warnBitNumber="+warnBitNumber+",warnBitValue="+warnBitValue);
		return ProtocolTool.setBit(warnFlag,warnBitNumber,warnBitValue);
	}

	public Location_AlarmFlag() {
		super();
	}

	public boolean isEmergency() {
		return isEmergency;
	}

	public void setEmergency(boolean emergency) {
		isEmergency = emergency;
		warnBitNumber=0;
		if (emergency){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isPreWarn() {
		return preWarn;
	}

	public void setPreWarn(boolean preWarn) {
		this.preWarn = preWarn;
		warnBitNumber=1;
		if (preWarn){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isGNSSFault() {
		return isGNSSFault;
	}

	public void setGNSSFault(boolean GNSSFault) {
		isGNSSFault = GNSSFault;
		warnBitNumber=2;
		if (GNSSFault){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isGNSSAerial() {
		return isGNSSAerial;
	}

	public void setGNSSAerial(boolean GNSSAerial) {
		isGNSSAerial = GNSSAerial;
		warnBitNumber=3;
		if (GNSSAerial){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isGNSSShortCircuit() {
		return isGNSSShortCircuit;
	}

	public void setGNSSShortCircuit(boolean GNSSShortCircuit) {
		isGNSSShortCircuit = GNSSShortCircuit;
		warnBitNumber=4;
		if (GNSSShortCircuit){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isMainUndervoltage() {
		return mainUndervoltage;
	}

	public void setMainUndervoltage(boolean mainUndervoltage) {
		this.mainUndervoltage = mainUndervoltage;
		warnBitNumber=5;
		if (mainUndervoltage){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isMainPowerDown() {
		return mainPowerDown;
	}

	public void setMainPowerDown(boolean mainPowerDown) {
		this.mainPowerDown = mainPowerDown;
		warnBitNumber=6;
		if (mainPowerDown){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isMainLCDError() {
		return mainLCDError;
	}

	public void setMainLCDError(boolean mainLCDError) {
		this.mainLCDError = mainLCDError;
		warnBitNumber=7;
		if (mainLCDError){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isTTSError() {
		return TTSError;
	}

	public void setTTSError(boolean TTSError) {
		this.TTSError = TTSError;
		warnBitNumber=8;
		if (TTSError){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isCameraError() {
		return cameraError;
	}

	public void setCameraError(boolean cameraError) {
		this.cameraError = cameraError;
		warnBitNumber=9;
		if (cameraError){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isMeterError() {
		return meterError;
	}

	public void setMeterError(boolean meterError) {
		this.meterError = meterError;
		warnBitNumber=10;
		if (meterError){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isEvaluatorError() {
		return evaluatorError;
	}

	public void setEvaluatorError(boolean evaluatorError) {
		this.evaluatorError = evaluatorError;
		warnBitNumber=11;
		if (evaluatorError){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isADError() {
		return ADError;
	}

	public void setADError(boolean ADError) {
		this.ADError = ADError;
		warnBitNumber=12;
		if (ADError){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isLEDScreenError() {
		return LEDScreenError;
	}

	public void setLEDScreenError(boolean LEDScreenError) {
		this.LEDScreenError = LEDScreenError;
		warnBitNumber=13;
		if (LEDScreenError){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isSecurityModulError() {
		return securityModulError;
	}

	public void setSecurityModulError(boolean securityModulError) {
		this.securityModulError = securityModulError;
		warnBitNumber=14;
		if (securityModulError){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isLEDLightError() {
		return LEDLightError;
	}

	public void setLEDLightError(boolean LEDLightError) {
		this.LEDLightError = LEDLightError;
		warnBitNumber=15;
		if (LEDLightError){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isOverSpeeding() {
		return overSpeeding;
	}

	public void setOverSpeeding(boolean overSpeeding) {
		this.overSpeeding = overSpeeding;
		warnBitNumber=16;
		if (overSpeeding){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isContinuousDriving() {
		return continuousDriving;
	}

	public void setContinuousDriving(boolean continuousDriving) {
		this.continuousDriving = continuousDriving;
		warnBitNumber=17;
		if (continuousDriving){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isAccumulatedOvertimeDriving() {
		return accumulatedOvertimeDriving;
	}

	public void setAccumulatedOvertimeDriving(boolean accumulatedOvertimeDriving) {
		this.accumulatedOvertimeDriving = accumulatedOvertimeDriving;
		warnBitNumber=18;
		if (accumulatedOvertimeDriving){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isOverTimeParking() {
		return overTimeParking;
	}

	public void setOverTimeParking(boolean overTimeParking) {
		this.overTimeParking = overTimeParking;
		warnBitNumber=19;
		if (overTimeParking){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isInOutAreaOrLine() {
		return inOutAreaOrLine;
	}

	public void setInOutAreaOrLine(boolean inOutAreaOrLine) {
		this.inOutAreaOrLine = inOutAreaOrLine;
		warnBitNumber=20;
		if (inOutAreaOrLine){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isRuntimeNotEnoughOrTooLong() {
		return runtimeNotEnoughOrTooLong;
	}

	public void setRuntimeNotEnoughOrTooLong(boolean runtimeNotEnoughOrTooLong) {
		this.runtimeNotEnoughOrTooLong = runtimeNotEnoughOrTooLong;
		warnBitNumber=21;
		if (runtimeNotEnoughOrTooLong){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isForbiddenRouteRun() {
		return forbiddenRouteRun;
	}

	public void setForbiddenRouteRun(boolean forbiddenRouteRun) {
		this.forbiddenRouteRun = forbiddenRouteRun;
		warnBitNumber=22;
		if (forbiddenRouteRun){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isSpeedSensorError() {
		return speedSensorError;
	}

	public void setSpeedSensorError(boolean speedSensorError) {
		this.speedSensorError = speedSensorError;
		warnBitNumber=23;
		if (speedSensorError){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isIllegalAccOn() {
		return illegalAccOn;
	}

	public void setIllegalAccOn(boolean illegalAccOn) {
		this.illegalAccOn = illegalAccOn;
		warnBitNumber=24;
		if (illegalAccOn){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isIllegalMoveCar() {
		return illegalMoveCar;
	}

	public void setIllegalMoveCar(boolean illegalMoveCar) {
		this.illegalMoveCar = illegalMoveCar;
		warnBitNumber=25;
		if (illegalMoveCar){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean ISUStrorageError() {
		return ISUStrorageError;
	}

	public void setISUStrorageError(boolean ISUStrorageError) {
		this.ISUStrorageError = ISUStrorageError;
		warnBitNumber=26;
		if (ISUStrorageError){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isRecordingError() {
		return recordingError;
	}

	public void setRecordingError(boolean recordingError) {
		this.recordingError = recordingError;
		warnBitNumber=27;
		if (recordingError){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	public boolean isMeterClockOverError() {
		return meterClockOverError;
	}

	public void setMeterClockOverError(boolean meterClockOverError) {
		this.meterClockOverError = meterClockOverError;
		warnBitNumber=28;
		if (meterClockOverError){
			warnBitValue=Bit_Warning;
		}else{
			warnBitValue=Bit_UnWarning;
		}
	}

	@Override
	public String toString() {
		return "Location_AlarmFlag{" +
				"isEmergency=" + isEmergency +
				", preWarn=" + preWarn +
				", isGNSSFault=" + isGNSSFault +
				", isGNSSAerial=" + isGNSSAerial +
				", isGNSSShortCircuit=" + isGNSSShortCircuit +
				", mainUndervoltage=" + mainUndervoltage +
				", mainPowerDown=" + mainPowerDown +
				", mainLCDError=" + mainLCDError +
				", TTSError=" + TTSError +
				", cameraError=" + cameraError +
				", meterError=" + meterError +
				", evaluatorError=" + evaluatorError +
				", ADError=" + ADError +
				", LEDScreenError=" + LEDScreenError +
				", securityModulError=" + securityModulError +
				", LEDLightError=" + LEDLightError +
				", overSpeeding=" + overSpeeding +
				", continuousDriving=" + continuousDriving +
				", accumulatedOvertimeDriving=" + accumulatedOvertimeDriving +
				", overTimeParking=" + overTimeParking +
				", inOutAreaOrLine=" + inOutAreaOrLine +
				", runtimeNotEnoughOrTooLong=" + runtimeNotEnoughOrTooLong +
				", forbiddenRouteRun=" + forbiddenRouteRun +
				", speedSensorError=" + speedSensorError +
				", illegalAccOn=" + illegalAccOn +
				", illegalMoveCar=" + illegalMoveCar +
				", ISUStrorageError=" + ISUStrorageError +
				", recordingError=" + recordingError +
				", meterClockOverError=" + meterClockOverError +
				", warnBitNumber=" + warnBitNumber +
				", warnBitValue=" + warnBitValue +
				'}';
	}
}
