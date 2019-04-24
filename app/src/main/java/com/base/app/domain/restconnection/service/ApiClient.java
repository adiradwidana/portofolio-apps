package com.base.app.domain.restconnection.service;

import android.app.Activity;

import com.base.app.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by faizf on 2/8/2017.
 */

public class ApiClient extends Activity {

    public static final String BASE_URL = BuildConfig.BASE_URL;
    private static Retrofit retrofit = null;
    private static ApiClient instance;


    public static Retrofit getClient(Activity activity) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(httpLoggingInterceptor);
        httpClient.connectTimeout(3, TimeUnit.MINUTES);
        httpClient.readTimeout(3, TimeUnit.MINUTES);
        OkHttpClient client = httpClient.build();

//        RxJava2CallAdapterFactory rxAdapter = RxJava2CallAdapterFactory.create();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .addCallAdapterFactory(rxAdapter)
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static ApiClient getInstance() {
        if (instance == null) {
            synchronized (ApiClient.class) {
                if (instance == null)
                    instance = new ApiClient();
            }
        }
        return instance;
    }

    public<T> T createInterface(Class<T> T, Activity activity) {
        return getClient(activity).create(T);
    }
}
