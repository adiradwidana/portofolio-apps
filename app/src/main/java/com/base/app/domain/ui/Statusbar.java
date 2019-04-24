package com.base.app.domain.ui;

import android.content.Context;

/**
 * Created by cahayapangriptaalam on 03/26/18 for KakaPOS.
 */

public class Statusbar {

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
