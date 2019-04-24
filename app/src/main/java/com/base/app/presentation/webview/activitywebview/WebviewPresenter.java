package com.base.app.presentation.webview.activitywebview;

import android.content.Context;

import com.base.app.presentation.base.BasePresenter;

public class WebviewPresenter extends BasePresenter<WebviewContract.View> implements WebviewContract.Presenter {

    private Context context;

    public WebviewPresenter(Context context) {
        this.context = context;
    }
}
