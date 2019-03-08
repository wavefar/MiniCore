package org.wavefar.lib.utils;

import org.wavefar.lib.BuildConfig;

import android.util.Log;

/**
 * 日志工具类
 * 
 * @author summer 时间： 2013-8-20 上午10:50:14
 */
public final class LogUtil {

	public static final boolean DEBUG = true;

	public static void v(String tag, String msg) {
		if (DEBUG) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (DEBUG) {
			Log.i(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (DEBUG) {
			Log.e(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (DEBUG){
			Log.w(tag, msg);
		}
	}

	public static void v(String tag, String msg, Throwable throwable) {
		if (DEBUG) {
			Log.v(tag, msg, throwable);
		}
	}

	public static void d(String tag, String msg, Throwable throwable) {
		if (DEBUG) {
			Log.d(tag, msg, throwable);
		}
	}

	public static void i(String tag, String msg, Throwable throwable) {
		if (DEBUG) {
			Log.i(tag, msg, throwable);
		}
	}

	public static void e(String tag, String msg, Throwable throwable) {
		if (DEBUG) {
			Log.e(tag, msg, throwable);
		}
	}

	public static void w(String tag, String msg, Throwable throwable) {
		if (DEBUG) {
			Log.w(tag, msg, throwable);
		}
	}
}
