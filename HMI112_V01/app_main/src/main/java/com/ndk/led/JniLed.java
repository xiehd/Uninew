package com.ndk.led;

public class JniLed {
	private static volatile JniLed instance = null;

	static {
		System.loadLibrary("uninew_gpio_led");
	}

	public static native int WatchDogCtrl(int paramInt);

	public static JniLed getInstance() {
		if (instance == null)
			instance = new JniLed();
		return instance;
	}

	public native static int LedInit();

	public native static byte[] SendRc522Data();

}