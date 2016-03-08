package com.yehui.utils.utils;

import android.util.Log;

import com.yehui.utils.application.YehuiApplication;

/**
 * 2016年1月5日10:46:19
 * 打印日志工具类
 */
public class LogUtil {

	/**
	 * 防止被实例化
	 */
	private LogUtil() {
        /* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static String TAG = YehuiApplication.TAG;

	public static boolean model = true;

	public static void d(String msg) {
		if (model)
			Log.d(TAG, msg);
	}

	public static void e(String msg) {
		if (model) {
			Log.e(TAG, msg);
		}
	}

	public static void v(String msg) {
		if (model) {
			Log.v(TAG, msg);
		}
	}

	public static void i(String msg) {
		if (model) {
			Log.i(TAG, msg);
		}
	}

	public static String getTraceInfo() {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		sb.append("class: ").append(stacks[1].getClassName()).append("; method: ").append(stacks[1].getMethodName()).append("; number: ")
				.append(stacks[1].getLineNumber());
		return sb.toString();
	}
}
