package com.goldencouponz.models.wrapper;


import static com.facebook.FacebookSdk.getCacheDir;

import com.goldencouponz.interfaces.Api;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String BASE_URL = " http://app.couponake.com/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;
    int cacheSize = 30 * 1024 * 1024; // Size in mb
    Cache cache = new Cache(getCacheDir(), cacheSize);

    private RetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }


    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }
}



