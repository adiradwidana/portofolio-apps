package com.base.app.data.entity.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cahaya on 3/27/17.
 */

public class RequestLogin {
    @SerializedName("email")
    String mEmail;

    @SerializedName("pass")
    String mPassword;

    public RequestLogin(String email, String password) {
        this.mEmail = email;
        this.mPassword = password;
    }
}
