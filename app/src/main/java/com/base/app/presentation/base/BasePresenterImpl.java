package com.base.app.presentation.base;

/**
 * Created by cahayapangriptaalam on 03/26/18 for KakaPOS.
 */

public interface BasePresenterImpl<V extends BaseViewImpl> {

    void attachView(V mvpView);

    void detachView();
}
