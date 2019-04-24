package com.base.app.domain.utils.graphic;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.base.app.domain.utils.Logger;


/**
 * Created by cahaya on 8/22/16.
 */
public class DimensionUtil {

    public static float dpToPx(Resources r, float dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static float spToPx(Resources r, float sp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, sp, r.getDisplayMetrics());
    }

    public static int getDisplayWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static int getDisplayHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static int getStatusBarHeight(Activity activity){
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        Logger.d("Dimension", "statusBarHeight: " + result + "px");
        return result;
    }

    public static int getNavigationBarHeight(Context context) {
        int navBarSize = 0;
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                DisplayMetrics realDisplayMetrics = new DisplayMetrics();
                display.getMetrics(displayMetrics);
                display.getRealMetrics(realDisplayMetrics);
                if(displayMetrics.heightPixels != realDisplayMetrics.heightPixels) {
                    return getNavigationBarSize(context);
                }
            } else {
                Resources resources = context.getResources();
                int resourceID = resources.getIdentifier("config_showNavigationBar", "bool", "android");
                if (resourceID > 0 && resources.getBoolean(resourceID))
                    return getNavigationBarSize(context);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Logger.d("Dimension", navBarSize + "px");
        return navBarSize;
    }

    private static int getNavigationBarSize(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
