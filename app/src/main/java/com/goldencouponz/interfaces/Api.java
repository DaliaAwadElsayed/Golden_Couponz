package com.goldencouponz.interfaces;

import com.goldencouponz.models.wrapper.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface Api {
    //api/countries
    @GET("api/countries")
    Call<ApiResponse> getCountries(@Header("Accept-Language") String AcceptLanguage);

}
