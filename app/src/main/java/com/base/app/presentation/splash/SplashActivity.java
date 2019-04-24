package com.base.app.presentation.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.base.app.R;
import com.base.app.presentation.login.LoginActivity;
import com.base.app.presentation.home.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    private final Handler mHideHandler = new Handler();

    @BindView(R.id.activity_splash_iv_logo) ImageView mIvLogo;
    @BindView(R.id.activity_splash_fl_splash_container) FrameLayout mFrameContainer;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mFrameContainer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

            mIvLogo.setVisibility(View.VISIBLE);

            delaySplashScreen();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        hide();

    }

    /**
     * delay splash screen for a second.
     */
    private void delaySplashScreen() {
        new Handler().postDelayed(() -> {
//            if (PongodevSession.getInstance().getUserToken().isEmpty())
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//            else
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));

            finish();
        }, 2000);
    }


    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Schedule a runnable to remove the status and navigation bar after a delay
        //mHideHandler.removeCallbacks(mShowPart2Runnable);
        // pada saat status bar hide, dia smooth
        mHideHandler.postDelayed(mHidePart2Runnable, 300);
    }

}