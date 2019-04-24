package com.base.app.domain.restconnection.utils;

import android.app.Activity;
import android.app.AlertDialog;

import com.base.app.domain.restconnection.callback.CallbackConnection;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by faizf on 7/11/2017.
 */
public class UtilsCodeCheck {

    private Activity activity;
    private Utils utils;
    private CallbackConnection callbackConnection;

    public UtilsCodeCheck(Activity activity) {
        this.activity = activity;
        this.utils = new Utils(activity);
    }

    public <E> void checkCode(CallbackConnection callbackConnection, Response<E> o, AlertDialog alertDialog) {
        this.callbackConnection = callbackConnection;
        switch (o.code()) {
            case 200:
                callbackConnection.onSuccess(o.body());
                break;
            case 202:
                callbackConnection.onSuccessChange();
                break;
            case 203:
                showError(o);
                break;
            case 204:
                callbackConnection.onSuccessNull();
                break;
            case 400:
                callbackConnection.onError(o.code(), o.message());
                break;
            case 403:
                utils.showExpired(activity);
                break;
            case 409:
                // utils.showUnauthorized(activity);
                break;
            case 500:
                callbackConnection.onError(o.code(), o.message());
                break;
            default:
                showError(o);
                break;
        }
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
    }

    public <E> void showError(Response<E> o) {
        String message = "";
        try {
            message = o.errorBody().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        utils.showDialogError(activity, message, callbackConnection, o.code());
    }
}
