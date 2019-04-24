package com.base.app.domain.restconnection.command;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import com.base.app.domain.restconnection.callback.CallbackConnection;
import com.base.app.domain.restconnection.utils.Utils;
import com.base.app.domain.restconnection.utils.UtilsCodeCheck;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseConnection<T> {

    protected ProgressDialog progressDialog;
    protected Activity activity;
    private UtilsCodeCheck utilsCodeCheck;
    protected Utils util;
    private T mRestInterface;

    public abstract T createRestInterface();

    public T getRestInterface() {
        return mRestInterface;
    }

    public BaseConnection(Activity activity) {
        this.activity = activity;
        mRestInterface = createRestInterface();
        utilsCodeCheck = new UtilsCodeCheck(activity);
        util = new Utils(activity);
    }

    protected void failure(String message, AlertDialog alertDialog, CallbackConnection callbackConnection, int code) {
        util.showDialogError(activity, message, callbackConnection, 500);
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected  <K> void genericCall(Call<K> call, ProgressDialog progressDialog, CallbackConnection callbackConnection) {
        call.enqueue(new Callback<K>() {
            @Override
            public void onResponse(Call<K> call, Response<K> response) {
                utilsCodeCheck.checkCode(callbackConnection, response, progressDialog);
            }

            @Override
            public void onFailure(Call<K> call, Throwable t) {
                failure(t.getMessage(), progressDialog, callbackConnection, 500);
            }
        });
    }
}
