package com.base.app.presentation.home.main;

import com.base.app.presentation.base.BasePresenterImpl;
import com.base.app.presentation.base.BaseViewImpl;

public interface MainContract {

    interface View extends BaseViewImpl {

    }

    interface Presenter extends BasePresenterImpl<View> {

    }
}