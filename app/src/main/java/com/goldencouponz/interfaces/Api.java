package com.goldencouponz.interfaces;

import com.goldencouponz.models.appsetting.AboutApp;
import com.goldencouponz.models.home.Store;
import com.goldencouponz.models.user.ChangePassword;
import com.goldencouponz.models.user.UserRegisteration;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.WhatsApp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
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
    Call<ApiResponse> addSlider(@Header("Accept-Language") String AcceptLanguage);

    //api/stores
    @GET("api/stores")
    Call<ApiResponse> getStore(@Header("Authorization") String authHeader, @Header("Accept-Language") String AcceptLanguage, @Header("fcm-token") String fcmToken, @Query("category_id") int category_id);

    //api/stores
    @GET("api/stores/{id}")
    Call<ApiResponse> getSingleStore(@Header("Authorization") String authHeader, @Header("Accept-Language") String AcceptLanguage, @Header("fcm-token") String fcmToken, @Header("country") int countryId, @Path("id") String id);

    //api/copy-coupon?
    @FormUrlEncoded
    @POST("api/copy-coupon")
    Call<ApiResponse> copyCoupon(@Field("coupon_id") int coupon_id);

    //api/fav-stores/
    @GET("api/fav-stores/{id}")
    Call<ApiResponse> userMakeFav(@Header("Authorization") String authHeader, @Header("Accept-Language") String AcceptLanguage, @Path("id") String id);

    //api/remove-fav-stores/
    @GET("api/remove-fav-stores/{id}")
    Call<ApiResponse> userRemoveFav(@Header("Authorization") String authHeader, @Header("Accept-Language") String AcceptLanguage, @Path("id") String id);

    //api/fav-store-coupons/2
    @GET("api/fav-store-coupons/{id}")
    Call<ApiResponse> userMakeFavCoupons(@Header("Authorization") String authHeader, @Header("Accept-Language") String AcceptLanguage, @Path("id") String id);

    //{{baseUrl}}/api/coupons/2
    @GET("api/coupons/{id}")
    Call<ApiResponse> userGetCoupon(@Header("Accept-Language") String AcceptLanguage, @Header("fcm-token") String fcmToken, @Path("id") String id);

    //api/products?store_id=35
    @GET("api/products/{id}")
    Call<ApiResponse> userGetSingleProduct(@Header("fcm-token") String fcmToken, @Header("country") int country, @Header("Accept-Language") String AcceptLanguage, @Path("id") String id);

    //api/remove-fav-store-coupons/2
    @GET("api/remove-fav-store-coupons/{id}")
    Call<ApiResponse> userRemoveFavCoupons(@Header("Authorization") String authHeader, @Header("Accept-Language") String AcceptLanguage, @Path("id") String id);

    //api/products?store_id=35
    @GET("api/products")
    Call<ApiResponse> getStoreProducts(@Header("fcm-token") String fcmToken, @Header("country") int country, @Header("Accept-Language") String AcceptLanguage, @Query("store_id") String store_id, @Query("category_id") String category_id, @Query("subcategory_id") String subcategory_id);

    //api/user-fav-store-coupons
    @GET("api/user-fav-store-coupons")
    Call<ApiResponse> getFavouriteCopounz(@Header("Authorization") String authHeader, @Header("Accept-Language") String AcceptLanguage);

    //api/user-fav-stores
    @GET("api/user-fav-stores")
    Call<ApiResponse> getFavouriteStores(@Header("Authorization") String authHeader, @Header("Accept-Language") String AcceptLanguage);

    //api/fav-stores
    @POST("api/fav-stores")
    Call<ApiResponse> favMultiStore(@Header("Authorization") String authHeader, @Header("Accept-Language") String AcceptLanguage, @Body Store userRegisteration);

    //{{baseUrl}}/api/whatsapp-messages
    @GET("api/whatsapp-messages")
    Call<WhatsApp> getWhatsApp(@Header("Accept-Language") String AcceptLanguage);

    //api/delete-account
    @DELETE("api/delete-account")
    Call<ApiResponse> deleteAccount(@Header("Authorization") String authHeader);

    //api/notifications
    @GET("api/notifications")
    Call<ApiResponse> userGetNotification(@Header("Authorization") String authHeader);

    //api/read-single-notifications/68b45b1d-d42c-4506-9414-89906f991f24
    @GET("api/read-single-notifications/{id}")
    Call<ApiResponse> readNotification(@Header("Authorization") String authHeader, @Path("id") String id);

    //{{baseUrl}}/api/delete-single-notifications/ab7e7483-df27-4e87-8ca8-e5518cd24c76
    @GET("api/delete-single-notifications/{id}")
    Call<ApiResponse> deleteNotification(@Header("Authorization") String authHeader, @Path("id") String id);

    //{{baseUrl}}/api/oauth
    @POST("/api/oauth")
    Call<ApiResponse> socialLogin(@Header("access_token") String access_token, @Header("driver") String driver);

}

