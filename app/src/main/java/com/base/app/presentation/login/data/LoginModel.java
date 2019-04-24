package com.base.app.presentation.login.data;

import android.app.Activity;
import android.app.ProgressDialog;

import com.base.app.data.entity.request.RequestLogin;
import com.base.app.data.entity.response.Login;
import com.base.app.domain.restconnection.callback.CallbackConnection;
import com.base.app.domain.restconnection.command.BaseConnection;
import com.base.app.domain.restconnection.service.ApiClient;

import retrofit2.Call;

public class LoginModel extends BaseConnection<LoginRestInterface> {
    public LoginModel(Activity activity, ProgressDialog progressDialog) {
        super(activity);
        this.progressDialog = progressDialog;
    }

    @Override
    public LoginRestInterface createRestInterface() {
        return ApiClient.getInstance().createInterface(LoginRestInterface.class, activity);
    }

    public void login(String email, String password, CallbackConnection callbackConnection) {
        progressDialog.show();
        RequestLogin requestLogin = new RequestLogin(email, password);
        Call<Login> call = getRestInterface().login(requestLogin);
        genericCall(call, progressDialog, callbackConnection);
    }

}
