package com.base.app.domain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


/**
 * Created by cahayaPangriptaAlam on 03/26/18 for KakaPOS.
 */

public class BaseApp extends MultiDexApplication {

    /**
     * Exit current activity
     *
     * @param currentActivity
     */
    public static void exit(Activity currentActivity) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        currentActivity.startActivity(intent);
        currentActivity.finish();
        System.exit(0);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers());*/
//        PongodevSession.getInstance().initialize(this);

    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(base);
    }


}
