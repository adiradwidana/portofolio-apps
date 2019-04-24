package com.base.app.presentation.base;


import android.app.Activity;
import android.app.ProgressDialog;

import com.base.app.BuildConfig;
import com.base.app.R;

public class BasePresenter<T extends BaseViewImpl> implements BasePresenterImpl<T> {

    protected ProgressDialog progressDialog;
    private T view;

    @Override
    public void attachView(T mvpView) {
        view = mvpView;
    }

    @Override
    public void detachView() {
        view = null;
    }

    public T getView() {
        return view;
    }

    public void checkViewAttached() {
        if (!isViewAttached() && BuildConfig.DEBUG) {
            throw new MvpViewNotAttachedException();
        }
    }

    private boolean isViewAttached() {
        return view != null;
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" + " requesting data to the Presenter");
        }
    }

    public ProgressDialog progress(String message, Activity activity){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        if (message == null) {
            progressDialog.setMessage(activity.getResources().getString(R.string.content_progress));
        }else {
            progressDialog.setMessage(message);
        }
        return progressDialog;
    }
}
