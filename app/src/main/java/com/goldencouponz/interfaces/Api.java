package com.goldencouponz.interfaces;

import com.goldencouponz.models.appsetting.AboutApp;
import com.goldencouponz.models.user.UserRegisteration;
import com.goldencouponz.models.wrapper.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Api {
    //api/countries
    @GET("api/countries")
    Call<ApiResponse> getCountries(@Header("Accept-Language") String AcceptLanguage);

    ///api/login
    @FormUrlEncoded
    @POST("api/login")
    Call<ApiResponse> userLogin(@Field("email") String email, @Field("password") String password);

    //api/logout
    @GET("api/logout")
    Call<ApiResponse> userLogOut(@Header("Authorization") String authHeader);

    //api/register
    @POST("api/register")
    Call<ApiResponse> userRegister(@Body UserRegisteration registeration);

    //{{baseUrl}}/api/profile
    @GET("api/profile")
    Call<UserRegisteration> getProfile(@Header("Authorization") String authHeader);

    //api/general-views
    @GET("api/general-views")
    Call<AboutApp> generalViews(@Header("Accept-Language") String AcceptLanguage);

}
