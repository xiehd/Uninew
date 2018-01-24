package com.uninew.net.Alarm.overspeed;

public interface IOverSpeedJudgeListener {

	/**
	 * 超速
	 * @param state 状态：0-结束，1-开始，2-持续
	 * @param time 持续时间（结束时有效）
	 */
	public void overSpeed(int state, long time);
	
	/**
	 * 预超速
	 * @param state 状态：0-结束，1-开始，2-持续
	 * @param time 持续时间（结束时有效）
	 */
	public void preOverSpeed(int state, long time);
	
}
