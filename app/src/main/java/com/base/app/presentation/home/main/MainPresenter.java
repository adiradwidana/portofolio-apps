package com.base.app.presentation.home.main;


import android.content.Context;

import com.base.app.presentation.base.BasePresenter;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    private Context context;

    MainPresenter(Context context) {
        this.context = context;
    }
}