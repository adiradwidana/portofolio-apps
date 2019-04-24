package com.base.app.presentation.home.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.base.app.R;
import com.base.app.presentation.base.BaseActivity;
import com.base.app.presentation.dummy.DummyFragment;
import com.base.app.presentation.webview.WebviewFragment;
import com.base.app.domain.utils.Logger;

import butterknife.BindView;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG_DUMMY = "TAG_DUMMY";
    private static final String TAG_WEBVIEW = "TAG_WEBVIEW";

    @BindView(R.id.navigation) BottomNavigationView mNavigation;
    @BindView(R.id.fragment_container) FrameLayout mFragmentContainer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    private boolean doubleBackToExitPressedOnce = false;
    private DummyFragment mDummyFragment;
    private String mActiveFragmentTag;
    private WebviewFragment mWebviewFragment;

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int createLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void startingUpActivity(Bundle savedInstanceState) {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        getPresenter().attachView(this);
//        BottomNavigationViewHelper.disableShiftMode(mNavigation);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mNavigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

        mNavigation.setOnNavigationItemSelectedListener(this);
        mDummyFragment = DummyFragment.newInstance();

        mWebviewFragment = WebviewFragment.newInstance(getString(R.string.url));
        mWebviewFragment.setMenuVisibility(true);

        getSupportFragmentManager().popBackStackImmediate();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mDummyFragment, TAG_DUMMY)
                .add(R.id.fragment_container, mWebviewFragment, TAG_DUMMY)
                .commit();

        getSupportFragmentManager().beginTransaction()
                .show(mDummyFragment)
                .hide(mWebviewFragment)
                .commit();
        mActiveFragmentTag = TAG_DUMMY;
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.tab_home:
                switchFragment(getString(R.string.activity_title_home), mDummyFragment, TAG_DUMMY);
                break;
            case R.id.tab_booking:
                switchFragment(getString(R.string.tab_webview), mWebviewFragment, TAG_WEBVIEW);
                break;
        }
        return true;
    }

    private void switchFragment(String title, Fragment fragment, String fragmentTag) {
        getSupportFragmentManager().beginTransaction()
                .hide(getSupportFragmentManager().findFragmentByTag(mActiveFragmentTag))
                .show(fragment)
                .commitAllowingStateLoss();
        mActiveFragmentTag = fragmentTag;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            ActivityCompat.finishAffinity(this);
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showToast(getString(R.string.message_back_pressed), this);
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);

    }
}