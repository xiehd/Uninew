package com.uninew.net.Alarm.overspeed;

public interface IOverSpeedJudge {
	
	/**
	 * 实时传速度值（每秒发送一次）
	 * @param speed
	 */
	public void sendSpeed(float speed);
	
	/**
	 * 设置限速值
	 * @param speedLimit
	 */
	public void setSpeedLimit(int speedLimit);
	
	/**
	 * 设置预超速值
	 * @param preInterverSpeed
	 */
	public void setPreInterverSpeed(int preInterverSpeed);
	
	/**
	 * 设置超速判定时间（秒）
	 * @param overSPeedPersistTime
	 */
	public void setOverSPeedPersistTime(int overSPeedPersistTime);
	
	/**
	 * 设置预超速判定时间（秒）
	 * @param preOverSPeedPersistTime
	 */
	public void setPreOverSPeedPersistTime(int preOverSPeedPersistTime);
	
	/**
	 * 设置持续超速上报时间间隔（秒）
	 * @param WarnIntervalTime
	 */
	public void setWarnIntervalTime(int WarnIntervalTime);
	
	/**
	 * 设置持续预超速上报时间间隔（秒）
	 * @param preWarnIntervalTime
	 */
	public void setPreWarnIntervalTime(int preWarnIntervalTime);
	


}
