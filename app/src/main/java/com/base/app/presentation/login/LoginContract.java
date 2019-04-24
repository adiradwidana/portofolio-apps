package com.base.app.presentation.login;


import com.base.app.presentation.base.BasePresenterImpl;
import com.base.app.presentation.base.BaseViewImpl;

/**
 * Created by cahayapangriptaalam on 03/26/18 for KakaPOS.
 */

public class LoginContract {

    interface View extends BaseViewImpl {
        void displayLoginFailed(int stateCode, String message);
        void displayLoginSuccess();
    }

    interface Presenter extends BasePresenterImpl<View> {
        void loadDataFromLogin(String username, String password);
    }

}
