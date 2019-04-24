package com.base.app.domain.ui;

import android.content.Context;

/**
 * Created by cahayapangriptaalam on 03/26/18 for KakaPOS.
 */

public class Navigationbar {

    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean hasNavigationBar (Context context)
    {
        int id = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && context.getResources().getBoolean(id);
    }
}
