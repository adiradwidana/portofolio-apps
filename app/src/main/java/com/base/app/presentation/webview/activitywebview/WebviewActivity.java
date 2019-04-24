package com.base.app.presentation.webview.activitywebview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.base.app.R;
import com.base.app.presentation.base.BaseActivity;
import com.base.app.presentation.webview.WebviewFragment;

import java.util.Objects;

import butterknife.BindView;

public class WebviewActivity extends BaseActivity<WebviewPresenter> implements WebviewContract.View {

    @BindView(R.id.toolbar) Toolbar toolbar;
    private Bundle bundle;

    @Override
    public WebviewPresenter createPresenter() {
        return new WebviewPresenter(this);
    }

    @Override
    public int createLayout() {
        return R.layout.activity_webview;
    }

    @Override
    public void startingUpActivity(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        // show icon in toolbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            setFragment(WebviewFragment.newInstance(bundle.getString("url")));
            if (bundle.getString("title") != null) {
                getSupportActionBar().setTitle(bundle.getString("title"));
            }
        }
    }

    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
}
