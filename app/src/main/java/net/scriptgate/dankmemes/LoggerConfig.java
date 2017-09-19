package net.scriptgate.dankmemes;

import android.util.Log;

import java8.util.function.BiConsumer;

public class LoggerConfig {
	public static final boolean ON = true;

	public static final BiConsumer<String, String> DEBUG = new BiConsumer<String, String>() {
		@Override
		public void accept(String TAG, String message) {
			if (LoggerConfig.ON) {
				Log.d(TAG, message);
			}
		}
	};
}
