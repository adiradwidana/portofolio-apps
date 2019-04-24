package com.base.app.domain.utils;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Created by cahayaPangriptaAlam on 15/12/18.
 */
public class PermissionUtils {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static boolean verifyCameraPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            int locationPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
            if (locationPermission == 0) {
                return true;
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                return false;
            }
        } else {
            return true;
        }
    }
}
