package com.uninew.net.Alarm.overspeed;

import android.util.Log;


public class OverSpeedJudge implements IOverSpeedJudge{
	
	private boolean D=true;
	private static final String TAG="OverSpeedJudge";
	private IOverSpeedJudgeListener mOverSpeedJudgeListener;
	
	public OverSpeedJudge(IOverSpeedJudgeListener mOverSpeedJudgeListener) {
		super();
		this.mOverSpeedJudgeListener = mOverSpeedJudgeListener;
	}

	@Override
	public void sendSpeed(float speed) {
		// TODO Auto-generated method stub
		speedHandle(speed);
	}

	@Override
	public void setSpeedLimit(int speedLimit) {
		// TODO Auto-generated method stub
		this.speedLimit=speedLimit;
	}

	@Override
	public void setPreInterverSpeed(int preInterverSpeed) {
		// TODO Auto-generated method stub
		this.preInterverTime=preInterverSpeed;
	}

	@Override
	public void setOverSPeedPersistTime(int overSPeedPersistTime) {
		// TODO Auto-generated method stub
		this.OverSPeedPersistTime=overSPeedPersistTime;
	}

	@Override
	public void setPreOverSPeedPersistTime(int preOverSPeedPersistTime) {
		// TODO Auto-generated method stub
		this.PreOverSPeedPersistTime=preOverSPeedPersistTime;
	}

	@Override
	public void setWarnIntervalTime(int WarnIntervalTime) {
		// TODO Auto-generated method stub
		this.WarnIntervalTime=WarnIntervalTime;
	}

	@Override
	public void setPreWarnIntervalTime(int preWarnIntervalTime) {
		// TODO Auto-generated method stub
		this.PreWarnIntervalTime=preWarnIntervalTime;
	}
	
	// ////////////////////////超速处理方法/////////////////////////////////////////////////
	int preCount;
	int overCount;
	int prePersistCount;
	int persistCount;
	// 预超速开始时间
	long startPreOverSpeedTime;
	// 超速开始时间
	long startOverSpeedTime;
	// 预超速判断持续时间
	public int PreOverSPeedPersistTime = 3;
	// 超速报警判断持续时间
	public int OverSPeedPersistTime = 5;
	// 预超速报警间隔（持续预超速的话间隔多久语音提示）
	public int PreWarnIntervalTime = 10;
	// 超速报警间隔（持续超速的话间隔多久语音提示）
	public int WarnIntervalTime = 10;

	// 超速预警值（KM/H）
	public int preInterverTime = 80;
	//限速值，默认90
	private int speedLimit=90;
	

	private void speedHandle(float speed) {
		switch (State_Speed.getSpeedState()) {
		case SPEED_JUDGE:
			if (speed > speedLimit) {// .nextStation.getLimitSpeed()
			// 进入报警
				State_Speed.setSpeedState(State_Speed.OVERSPEED_JUDGE);
				speedHandle(speed);
			} else if (overCount >= OverSPeedPersistTime) {
				// 进入报警取消
				State_Speed.setSpeedState(State_Speed.OVERSPEED_OVER);
				speedHandle(speed);
			}
			if (speed > preInterverTime
					&& speed < speedLimit) {
				// 进入预警
				State_Speed.setSpeedState(State_Speed.PRE_OVERSPEED_JUDGE);
				speedHandle(speed);
			} else if (speed < speedLimit && preCount >= PreOverSPeedPersistTime) {
				// 预警取消
				State_Speed.setSpeedState(State_Speed.PRE_OVERSPEED_OVER);
				speedHandle(speed);
			}
			break;
		// ////////////////////////以下预警判断/////////////////////////////////////////////////
		case PRE_OVERSPEED_JUDGE:
			preCount++;
			if(D)Log.d("State_Speed", "preCount=" + preCount);
			if (preCount == PreOverSPeedPersistTime) {
				State_Speed.setSpeedState(State_Speed.PRE_OVERSPEED);
				speedHandle(speed);
			} else if (preCount > PreOverSPeedPersistTime) {
				State_Speed.setSpeedState(State_Speed.PERSIST_PRE_OVERSPEED);
				speedHandle(speed);
			} else {
				State_Speed.setSpeedState(State_Speed.SPEED_JUDGE);
			}
			break;
		case PRE_OVERSPEED:
			if(D)Log.d("State_Speed", "-------------超速预报警-----------");
			startPreOverSpeedTime=System.currentTimeMillis();
			mOverSpeedJudgeListener.preOverSpeed(DefineOverSpeed.State_Start, 0);
			State_Speed.setSpeedState(State_Speed.SPEED_JUDGE);
			break;
		case PERSIST_PRE_OVERSPEED:
			prePersistCount++;
			if(D)Log.v("State_Speed", "persistCount=" + persistCount);
			if (prePersistCount == PreWarnIntervalTime) {
				prePersistCount = 0;
				mOverSpeedJudgeListener.preOverSpeed(DefineOverSpeed.State_Persist, 0);
			}
			State_Speed.setSpeedState(State_Speed.SPEED_JUDGE);
			break;
		case PRE_OVERSPEED_OVER:
			preCount = 0;
			prePersistCount = 0;
			if(D)Log.e("State_Speed", "-------------超速预报警---------结束--");
			long time=System.currentTimeMillis()-startPreOverSpeedTime;
			mOverSpeedJudgeListener.preOverSpeed(DefineOverSpeed.State_End, (int)(time/1000));
			State_Speed.setSpeedState(State_Speed.SPEED_JUDGE);
			break;
		// ////////////////////////以下报警判断/////////////////////////////////////////////////
		case OVERSPEED_JUDGE:
			overCount++;
			if(D)Log.v("State_Speed", "overCount=" + overCount);
			if (overCount == OverSPeedPersistTime) {
				State_Speed.setSpeedState(State_Speed.OVERSPEED);
				speedHandle(speed);
			} else if (overCount > OverSPeedPersistTime) {
				State_Speed.setSpeedState(State_Speed.PERSIST_OVERSPEED);
				speedHandle(speed);
			} else {
				State_Speed.setSpeedState(State_Speed.SPEED_JUDGE);
			}
			break;
		case OVERSPEED:
			if(D)Log.e("State_Speed", "----超速报警-----");
			mOverSpeedJudgeListener.overSpeed(DefineOverSpeed.State_Start, 0);
			startOverSpeedTime = System.currentTimeMillis();
			State_Speed.setSpeedState(State_Speed.SPEED_JUDGE);
			break;
		case PERSIST_OVERSPEED:
			persistCount++;
			if(D)Log.v("State_Speed", "persistCount=" + persistCount);
			if (persistCount == WarnIntervalTime) {
				persistCount = 0;
				mOverSpeedJudgeListener.overSpeed(DefineOverSpeed.State_Persist, 0);
			}
			State_Speed.setSpeedState(State_Speed.SPEED_JUDGE);
			break;
		case OVERSPEED_OVER:
			overCount = 0;
			persistCount = 0;
			if(D)Log.e("State_Speed",
					"----超速报警--结束---持续时间："
							+ (System.currentTimeMillis() - startOverSpeedTime));
			long time2=System.currentTimeMillis() - startOverSpeedTime;
			mOverSpeedJudgeListener.overSpeed(DefineOverSpeed.State_End, time2/1000);
			State_Speed.setSpeedState(State_Speed.SPEED_JUDGE);
			break;
		case NORMAL:

			break;
		default:
			break;
		}
	}

	private enum State_Speed {
		/** 速度判定 */
		SPEED_JUDGE,
		/** 超速预警判定 */
		PRE_OVERSPEED_JUDGE,
		/** 超速判定 */
		OVERSPEED_JUDGE,
		/** 超速预警 */
		PRE_OVERSPEED,
		/** 持续超速预警 */
		PERSIST_PRE_OVERSPEED,
		/** 超速预警取消 */
		PRE_OVERSPEED_OVER,
		/** 超速报警 */
		OVERSPEED,
		/** 持续超速 */
		PERSIST_OVERSPEED,
		/** 超速报警取消 */
		OVERSPEED_OVER,
		/** 正常状态 */
		NORMAL;

		private static State_Speed state = SPEED_JUDGE;

		public static State_Speed getSpeedState() {
			return state;
		}

		public static void setSpeedState(State_Speed state) {
			// BusLog.v("State_Speed", String.format(
			// "###### State Changed: %s ==> %s ######",
			// State_Speed.state, state));
			State_Speed.state = state;
		}
	}

	public boolean isD() {
		return D;
	}

	public void setD(boolean d) {
		D = d;
	}

	public long getStartPreOverSpeedTime() {
		return startPreOverSpeedTime;
	}

	public void setStartPreOverSpeedTime(long startPreOverSpeedTime) {
		this.startPreOverSpeedTime = startPreOverSpeedTime;
	}

	public long getStartOverSpeedTime() {
		return startOverSpeedTime;
	}

	public void setStartOverSpeedTime(long startOverSpeedTime) {
		this.startOverSpeedTime = startOverSpeedTime;
	}
}
