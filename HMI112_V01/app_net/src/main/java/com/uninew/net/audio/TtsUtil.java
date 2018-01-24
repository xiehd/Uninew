package com.uninew.net.audio;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

public class TtsUtil implements SynthesizerListener{


	private static final String TAG = "AudioFocusChangeListenerT";
	private static SpeechSynthesizer mTts;
	private static String voicer = "";
	/**
	 * Google 原生申请声音焦点
	 */
	private AudioManager am;
	private static SharedPreferences mSharedPreferences;
	private static Toast mToast;
	private static Context mContext;
	private boolean isPause = false;

	private int audioStreamType = AudioManager.STREAM_NOTIFICATION;

	public static boolean isCanSpeech = true;// 是否播报

	private boolean isPlaying = false;


	public TtsUtil(Context context) {
		this.mContext =context;
		mTts = SpeechSynthesizer.createSynthesizer(context,
				mTtsInitListener);
		am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		init();
		setParam();

	}



	/**
	 * Sets the audio stream type for this MediaPlayer. See {@link AudioManager}
	 * for a list of stream types. Must call this method before prepare() or
	 * prepareAsync() in order for the target stream type to become effective
	 * thereafter.
	 *
	 * @param audioStreamType the audio stream type
	 * @see AudioManager
	 */
	public void setAudioStreamType(int audioStreamType) {
		this.audioStreamType = audioStreamType;
	}

	/**
	 * 实现音频焦点监听器，如果有其他竞争者一起争夺AudioFocus，通过监听AudioFocus，APP这方做出相应的变化
	 */
	private AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {

		public void onAudioFocusChange(int focusChange) {

			Log.i(TAG, "onAudioFocusChange focusChange=" + focusChange);

			switch (focusChange) {
				case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
					// 暂时失去AudioFocus，但是可以继续播放

					break;

				case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
					// 暂时失去Audio Focus，并会很快再次获得
					//例如：打完电话后恢复播放
					Log.i(TAG, "暂时失去Audio Focus，并会很快再次获得");
					pauseSpeaking();
//                    isPlayMusic = false;
					break;
				case (AudioManager.AUDIOFOCUS_LOSS):
					// 失去了Audio Focus，并将会持续很长的时间
					//例如：打开收音机，当前会失去焦点，只有重新进入播放界面
					Log.i(TAG, "失去了Audio Focus，并将会持续很长的时间");
//					pauseSpeaking();
//                    isPlayMusic = false;
					break;

				case (AudioManager.AUDIOFOCUS_GAIN):
					// 获得了Audio Focus
					//打完电话后，再次通知获得焦点并播放
					Log.i(TAG, "获得了Audio Focus");
//                    isPlayMusic = true;
					//播放
//                    play();
//					stopSpeak();
					resumeSpeaking();
					break;
				case (AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK):
					Log.i(TAG, "获得了AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK");
//                    isPlayMusic = true;
					//播放
//                    play();
					resumeSpeaking();
					break;
				default:
					break;
			}

		}

	};

	private void init() {
		mSharedPreferences = mContext.getSharedPreferences(
				"com.iflytek.setting", Activity.MODE_PRIVATE);
		mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
	}

	/**
	 * 参数设置
	 *
	 * @return
	 */
	private void setParam() {
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
//		 mTts.setParameter(SpeechConstant.STREAM_TYPE, audioStreamType+"");

//		// 设置播放器音频流类型
//		mTts.setParameter(SpeechConstant.STREAM_TYPE,
//				mSharedPreferences.getString("stream_preference", audioStreamType+""));// 5 用于通知的音频流

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
	 * @param text
	 * @return
	 */
	public int speak(String text){
		// 播放的时候申请焦点
		int isRequestSucess = am.requestAudioFocus(mAudioFocusListener, audioStreamType,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
		Log.i(TAG, "播放的时候申请焦点=" + isRequestSucess);
		if (isRequestSucess == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
			Log.i(TAG, "申请焦点失败，无法播放");
			return -1;
		}
//		// 设置播放器音频流类型
//		mTts.setParameter(SpeechConstant.STREAM_TYPE,
//				mSharedPreferences.getString("stream_preference", audioStreamType+""));// 5 用于通知的音频流
		mTts.setParameter(SpeechConstant.STREAM_TYPE, audioStreamType+"");
		Log.i(TAG, "申请焦点成功，即将播放");
		if (isCanSpeech == true) {
			if (mTts.isSpeaking()) {
				stopSpeak();
			}
			Log.d(TAG, text);
			int i = mTts.startSpeaking(text, this);
			Log.d(TAG, "return:"+i);
			if(TextUtils.isEmpty(text)){
				int isRequest = am.abandonAudioFocus(mAudioFocusListener);
				if (isRequest == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
					Log.i(TAG, "释放声音焦点失败，无法播放");
				}else {
					Log.i(TAG, "释放声音焦点成功，即将播放");
				}
			}
			return i;
		} else {
			return -1;
		}
	}

	public void pauseSpeaking(){
		if(null != mTts && isPause == false && isPlaying){
			mTts.pauseSpeaking();
		}
	}

	public void resumeSpeaking(){
		if(isPause && null != mTts){
			mTts.resumeSpeaking();
		}
	}

	/**
	 * 停止播放
	 */
	public void stopSpeak() {
		if (null != mTts) {
			try {
				isPlaying = false;
				mTts.stopSpeaking();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//释放声音焦点
		int isRequestSucess = am.abandonAudioFocus(mAudioFocusListener);
		if (isRequestSucess == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
			Log.i(TAG, "释放声音焦点失败，无法播放");
		}else {
			Log.i(TAG, "释放声音焦点成功，即将播放");
		}
	}

	/**
	 * 合成回调监听。
	 */
//	private SynthesizerListener mTtsListener = new SynthesizerListener() {

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {

		}

		@Override
		public void onCompleted(SpeechError error) {
		    Log.d("tts","\"播放完成\"");
			stopSpeak();
		}

		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
			// TODO Auto-generated method stub
		}


		@Override
		public void onSpeakBegin() {
			// TODO Auto-generated method stub
			isPlaying = true;

		}

		@Override
		public void onSpeakPaused() {
			// TODO Auto-generated method stub
			isPause = true;
		}

		@Override
		public void onSpeakResumed() {
			// TODO Auto-generated method stub
			isPause = false;
		}

//	};
}