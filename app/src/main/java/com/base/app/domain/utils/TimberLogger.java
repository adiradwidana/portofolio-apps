package com.base.app.domain.utils;


import com.base.app.BuildConfig;


/**
 * Created by cahaya on 11/3/16.
 */

public class TimberLogger {
    public static void i(String tag, String message) {
        if (BuildConfig.DEBUG) {
            timber.log.Timber.i(tag, message == null ? "null" : message);
        }
    }

    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG) {
            timber.log.Timber.d(tag, message == null ? "null" : message);
        }
    }

    public static void es(String message) {
        if (BuildConfig.DEBUG) {
            timber.log.Timber.e("TMI %s", message);
//            Log.e(tag, message == null ? "null" : message);
        }
    }
}
