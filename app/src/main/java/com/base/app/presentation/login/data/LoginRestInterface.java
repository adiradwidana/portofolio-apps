package com.base.app.presentation.login.data;


import com.base.app.data.entity.request.RequestLogin;
import com.base.app.data.entity.response.Login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface LoginRestInterface {
    @POST("login")
    Call<Login> login(@Body RequestLogin requestLogin);
}
