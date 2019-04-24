package com.base.app.domain.utils;

import android.util.Log;

import com.base.app.BuildConfig;


/**
 * Created by cahaya on 11/3/16.
 */

public class Logger {
    public static void i(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message == null ? "null" : message);
        }
    }

    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message == null ? "null" : message);
        }
    }

    public static void e(String tag, String message) {
        if (BuildConfig.DEBUG) {
//            TimberLogger.e("tag %s", message);
            Log.e(tag, message == null ? "null" : message);
        }
    }
}
