package com.base.app.domain.restconnection.callback;

/**
 * Created by faizf on 7/12/2017.
 */

public interface CallbackConnection {
    void onSuccess(Object o);
    void onSuccessNull();
    void onSuccessChange();
    void onError(int code, String message);

    void onTryAgain();
}
