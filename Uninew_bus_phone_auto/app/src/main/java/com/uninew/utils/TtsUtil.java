package com.uninew.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

public class TtsUtil {
	private static SpeechSynthesizer mTts;
	private static String voicer = "";
	private static TtsUtil single = null;

	private static SharedPreferences mSharedPreferences;
	private static Toast mToast;
	private static Context mContext;

	public static boolean isCanSpeech = true;// 是否播报

	public synchronized static TtsUtil getInstance(Context context) {
		mContext = context;
		if (single == null) {
			single = new TtsUtil();
			// 初始化合成对象
			mTts = SpeechSynthesizer.createSynthesizer(context,
					mTtsInitListener);
			init();
			setParam();
		}
		return single;
	}

	private static void init() {
		mSharedPreferences = mContext.getSharedPreferences(
				"com.iflytek.setting", Activity.MODE_PRIVATE);
		mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
	}

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	private static void setParam() {
		// 设置合成
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);

		// 设置发音人，设置发音人 voicer为空默认通过语音+界面指定发音人。
		mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);

		// 设置语速
		mTts.setParameter(SpeechConstant.SPEED, "50");

		// 设置音调
		mTts.setParameter(SpeechConstant.PITCH, "50");

		// 设置音量
		mTts.setParameter(SpeechConstant.VOLUME, "50");

		// 设置播放器音频流类型
		// mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");

		// 设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE,
				mSharedPreferences.getString("stream_preference", "1"));// 0则为听筒

	}

	private static void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}

	/**
	 * 初期化监听。
	 */
	private static InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d("wf", "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("初始化失败,错误码：" + code);
			}
		}
	};

	/**
	 * 开始播放
	 * 
	 * @param text
	 * @param mTtsListener
	 * @return
	 */
	public int startSpeak(String text) {
		if (isCanSpeech == true) {

			if (mTts.isSpeaking()) {
				stopSpeak();
			}
			Log.d("wf", text);
			return mTts.startSpeaking(text, mTtsListener);
		} else {
			return -1;
		}
	}

	/**
	 * 停止播放
	 */
	public void stopSpeak() {
		if (null != mTts) {
			try {
				mTts.stopSpeaking();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {

		}

		@Override
		public void onCompleted(SpeechError error) {
			// if (error == null) {
			// showTip("播放完成");
			// } else if (error != null) {
			// showTip(error.getPlainDescription(true));
			// }
		}

		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSpeakBegin() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSpeakPaused() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSpeakResumed() {
			// TODO Auto-generated method stub

		}

	};
}