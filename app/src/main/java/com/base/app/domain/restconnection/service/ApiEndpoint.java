package com.base.app.domain.restconnection.service;

import com.base.app.data.entity.request.RequestLogin;
import com.base.app.data.entity.response.Login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by faizf on 2/8/2017.
 */

public interface ApiEndpoint {

    @POST("login")
    Call<Login> doLogin(@Body RequestLogin requestLogin);
}
