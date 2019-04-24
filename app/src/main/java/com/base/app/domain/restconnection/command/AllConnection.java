package com.base.app.domain.restconnection.command;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import com.base.app.domain.restconnection.callback.CallbackConnection;
import com.base.app.domain.restconnection.service.ApiClient;
import com.base.app.domain.restconnection.service.ApiEndpoint;
import com.base.app.domain.restconnection.utils.Utils;
import com.base.app.domain.restconnection.utils.UtilsCodeCheck;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllConnection {

    private Activity activity;
    private UtilsCodeCheck utilsCodeCheck;
    private Utils util;
    private ApiEndpoint rest;

    public AllConnection(Activity activity) {
        this.activity = activity;
        this.utilsCodeCheck = new UtilsCodeCheck(activity);
        this.util = new Utils(activity);
        rest = ApiClient.getClient(activity).create(ApiEndpoint.class);
    }

    private void failure(String message, AlertDialog alertDialog, CallbackConnection callbackConnection, int code) {
        util.showDialogError(activity, message, callbackConnection, 500);
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <T> void genericCall(Call<T> call, ProgressDialog progressDialog, CallbackConnection callbackConnection) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                utilsCodeCheck.checkCode(callbackConnection, response, progressDialog);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                failure(t.getMessage(), progressDialog, callbackConnection, 500);
            }
        });
    }

}
