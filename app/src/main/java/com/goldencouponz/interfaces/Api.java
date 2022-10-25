package com.goldencouponz.interfaces;

import com.goldencouponz.models.appsetting.AboutApp;
import com.goldencouponz.models.user.ChangePassword;
import com.goldencouponz.models.user.UserRegisteration;
import com.goldencouponz.models.wrapper.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    //api/update profile
    @POST("api/updateprofile")
    Call<ApiResponse> userEditProfile(@Header("Authorization") String authHeader, @Body UserRegisteration userRegisteration);

    //api/change-password
    @POST("api/change-password")
    Call<ApiResponse> changePassword(@Header("Authorization") String authHeader, @Body ChangePassword changePassword);

    //api/general-views
    @GET("api/general-views")
    Call<AboutApp> generalViews(@Header("Accept-Language") String AcceptLanguage);

    //api/categories?parent_id=0
    @GET("api/categories")
    Call<ApiResponse> getCategories(@Header("Accept-Language") String AcceptLanguage, @Header("fcm-token") String fcmToken, @Query("parent_id") int parent_id);

    //api/sliders
    @GET("api/sliders")
    Call<ApiResponse> addSlider();
}
