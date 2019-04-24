package com.base.app.presentation.login;

import android.app.Activity;

import com.base.app.presentation.base.BasePresenter;
import com.base.app.presentation.login.data.LoginModel;
import com.base.app.domain.restconnection.callback.CallbackConnection;


public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private LoginModel mModel;

    LoginPresenter(Activity activity) {
        mModel = new LoginModel(activity, progress(null, activity));
    }

    @Override
    public void loadDataFromLogin(String username, String password) {
        checkViewAttached();

        mModel.login(username, password, new CallbackConnection() {
            @Override
            public void onSuccess(Object o) {
                if (getView()!=null){
                    getView().displayLoginSuccess();
                }
            }

            @Override
            public void onSuccessNull() {

            }

            @Override
            public void onSuccessChange() {

            }

            @Override
            public void onError(int code, String message) {
                if (getView()!=null) {
                    getView().displayLoginFailed(code, message);
                }
            }

            @Override
            public void onTryAgain() {

            }
        });
    }
}
