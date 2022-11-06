package com.goldencouponz.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.ActivitySplashBinding;
import com.goldencouponz.adapters.countries.CountriesAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding splashFragmentBinding;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    CountriesAdapter countriesAdapter;
    String language,country;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashFragmentBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        if (GoldenNoLoginSharedPreference.getUserLanguage(this).equals("ar") || GoldenNoLoginSharedPreference.getUserLanguage(this).equals("en")) {
            language = GoldenNoLoginSharedPreference.getUserLanguage(this);
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            i.putExtra("language", language);
            i.putExtra("country", country);
            startActivity(i);
        }
        if (!isInternetAvailable()) {
            Toast.makeText(this, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
        }
        splashScreen();
        splashFragmentBinding.continueId.setOnClickListener(view -> {
            Intent i = new Intent(SplashActivity.this, SkipActivity.class);
            i.putExtra("language", language);
            startActivity(i);
            finish();
        });
        splashFragmentBinding.countriesLinearId.setVisibility(View.GONE);
        splashFragmentBinding.arButtonId.setOnClickListener(view -> {
            continueProp();
            splashFragmentBinding.prefCountryId.setText(R.string.fav_country);
            splashFragmentBinding.continueId.setText(R.string.ar_continue_label);
            splashFragmentBinding.homeRecyclerView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            splashFragmentBinding.countriesLinearId.setVisibility(View.GONE);
            splashFragmentBinding.enButtonId.setBackground(getResources().getDrawable(R.drawable.bk_border_button));
            splashFragmentBinding.arButtonId.setBackground(getResources().getDrawable(R.drawable.bk_button));
            getCountries("ar");
            language = "ar";
            //TODO UPDATE USER COUNTRY AND ID
            GoldenNoLoginSharedPreference.saveUserLang(SplashActivity.this,"ar");

        });
        splashFragmentBinding.enButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueProp();
                splashFragmentBinding.prefCountryId.setText(R.string.preferred_country);
                splashFragmentBinding.continueId.setText(R.string.en_continue_label);
                splashFragmentBinding.homeRecyclerView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                splashFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                splashFragmentBinding.arButtonId.setBackground(getResources().getDrawable(R.drawable.bk_border_button));
                splashFragmentBinding.enButtonId.setBackground(getResources().getDrawable(R.drawable.bk_button));
                getCountries("en");
                language = "en";
                GoldenNoLoginSharedPreference.saveUserLang(SplashActivity.this,"en");
            }
        });

    }

    private void continueProp() {
        splashFragmentBinding.continueId.setAlpha(0.5f);
        splashFragmentBinding.continueId.setEnabled(false);
        splashFragmentBinding.continueId.setBackground(getResources().getDrawable(R.drawable.bk_border_button));
        splashFragmentBinding.continueId.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));

    }

    private void splashScreen() {
        final Handler handler = new Handler();
        new Thread(() -> {
            try {
                Thread.sleep(2500);

            } catch (Exception e) {
            }
            handler.post(() -> {
                if (!GoldenSharedPreference.isLoggedIn(this)) {
                    animation();
                }

            });

        }).start();
    }

    private void animation() {
        splashFragmentBinding.langLinearId.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.slide_up);
        splashFragmentBinding.langLinearId.startAnimation(animation);
        splashFragmentBinding.logoId.startAnimation(animation);
    }

    private void getCountries(String lang) {
        countriesAdapter = new CountriesAdapter(this);
        splashFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getCountries(lang).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() && response.body() != null) {
                        splashFragmentBinding.progress.setVisibility(View.GONE);
                        if (!response.body().getCountries().isEmpty()) {
                            splashFragmentBinding.countriesLinearId.setVisibility(View.VISIBLE);
                            splashFragmentBinding.noInternetConId.setVisibility(View.GONE);
                            splashFragmentBinding.homeRecyclerView.setVisibility(View.VISIBLE);
                            splashFragmentBinding.prefCountryId.setVisibility(View.VISIBLE);
                            splashFragmentBinding.countriesLinearId.setVisibility(View.VISIBLE);
                            countriesAdapter.setCountries(response.body().getCountries());
                            splashFragmentBinding.homeRecyclerView.setAdapter(countriesAdapter);
                            countriesAdapter.setOnItemClickListener(new CountriesAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View viewItem, int position,int id,String name,String currency) {
                                    if (position != -1) {
                                        GoldenNoLoginSharedPreference.saveUserCountry(SplashActivity.this,position,id,name,currency);
                                        splashFragmentBinding.continueId.setAlpha(1f);
                                        splashFragmentBinding.continueId.setEnabled(true);
                                        splashFragmentBinding.continueId.setBackground(getResources().getDrawable(R.drawable.bk_button));
                                        splashFragmentBinding.continueId.setBackgroundTintList(null);
                                    } else {
                                        splashFragmentBinding.continueId.setAlpha(0.5f);
                                        splashFragmentBinding.continueId.setEnabled(false);
                                        splashFragmentBinding.continueId.setBackground(getResources().getDrawable(R.drawable.bk_border_button));
                                    }
                                }
                            });

                        } else {
                            splashFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                            splashFragmentBinding.progress.setVisibility(View.GONE);
                            splashFragmentBinding.noInternetConId.setVisibility(View.GONE);
                        }
                    } else {
                        splashFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                        Toast.makeText(SplashActivity.this, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                        splashFragmentBinding.progress.setVisibility(View.GONE);
                        splashFragmentBinding.noInternetConId.setVisibility(View.GONE);
                    }
                } else {
                    splashFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                    splashFragmentBinding.noInternetConId.setVisibility(View.GONE);
                    Toast.makeText(SplashActivity.this, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                    splashFragmentBinding.progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (lang.equals("en")) {
                    splashFragmentBinding.textInternetId.setText(R.string.en_no_internet_message);
                } else {
                    splashFragmentBinding.textInternetId.setText(R.string.ar_no_internet_message);
                }
                splashFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                splashFragmentBinding.noInternetConId.setVisibility(View.VISIBLE);
                splashFragmentBinding.countriesLinearId.setVisibility(View.VISIBLE);
                splashFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                splashFragmentBinding.progress.setVisibility(View.GONE);
                splashFragmentBinding.prefCountryId.setVisibility(View.GONE);

            }
        });
    }

    public boolean isInternetAvailable() {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {

            Log.e("isInternetAvailable:", e.toString());
            return false;
        }
    }


}