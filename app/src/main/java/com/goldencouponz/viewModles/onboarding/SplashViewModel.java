package com.goldencouponz.viewModles.onboarding;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.SplashFragmentBinding;
import com.goldencouponz.adapters.countries.CountriesAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.GoldenSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashViewModel extends ViewModel {
    SplashFragmentBinding splashFragmentBinding;
    Context context, mContext;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    CountriesAdapter countriesAdapter;
    Activity activity;


    public void init(SplashFragmentBinding splashFragmentBinding, Context context, Activity activity) {
        this.context = context;
        this.mContext=context;
        this.splashFragmentBinding = splashFragmentBinding;
        this.activity = activity;
        splashScreen();
//        splashFragmentBinding.continueId.setOnClickListener(view -> {
//            Local.Companion.updateResources(context);
//            LocaleHelper.setLocale(context, GoldenSharedPreference.getSelectedLanguageValue(context));
//            Navigation.findNavController(view).navigate(R.id.firstSkipFragment);
//              });
        splashFragmentBinding.countriesLinearId.setVisibility(View.GONE);
        splashFragmentBinding.arButtonId.setOnClickListener(view -> {
            continueProp();
//            LocaleHelper.setLocale(context, "ar");
//            GoldenSharedPreference.changeLanguage(context, 1);
               splashFragmentBinding.prefCountryId.setText(R.string.fav_country);
            splashFragmentBinding.continueId.setText(R.string.ar_continue_label);
            splashFragmentBinding.homeRecyclerView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            splashFragmentBinding.countriesLinearId.setVisibility(View.GONE);
            splashFragmentBinding.enButtonId.setBackground(context.getResources().getDrawable(R.drawable.bk_border_button));
            splashFragmentBinding.arButtonId.setBackground(context.getResources().getDrawable(R.drawable.bk_button));
            getCountries("ar");

        });
        splashFragmentBinding.enButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueProp();
//                LocaleHelper.setLocale(context, "en");
//                GoldenSharedPreference.changeLanguage(context, 0);
//                mContext = LocaleHelper.setLocale(mContext, "en");
//                resources = mContext.getResources();
                splashFragmentBinding.prefCountryId.setText(R.string.preferred_country);
                splashFragmentBinding.continueId.setText(R.string.en_continue_label);
                splashFragmentBinding.homeRecyclerView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                splashFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                splashFragmentBinding.arButtonId.setBackground(context.getResources().getDrawable(R.drawable.bk_border_button));
                splashFragmentBinding.enButtonId.setBackground(context.getResources().getDrawable(R.drawable.bk_button));
                getCountries("en");
            }
        });
    }

    private void continueProp() {
        splashFragmentBinding.continueId.setAlpha(0.5f);
        splashFragmentBinding.continueId.setEnabled(false);
        splashFragmentBinding.continueId.setBackground(context.getResources().getDrawable(R.drawable.bk_border_button));
        splashFragmentBinding.continueId.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grey)));

    }

    private void splashScreen() {
        final Handler handler = new Handler();
        new Thread(() -> {
            try {
                Thread.sleep(2500);

            } catch (Exception e) {
            }
            handler.post(() -> {
                if (!GoldenSharedPreference.isLoggedIn(context)) {
                    animation();
                }

            });

        }).start();
    }

    private void animation() {
        splashFragmentBinding.langLinearId.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_up);
        splashFragmentBinding.langLinearId.startAnimation(animation);
        splashFragmentBinding.logoId.startAnimation(animation);
    }

    private void getCountries(String lang) {
        countriesAdapter = new CountriesAdapter(context);
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
                                public void onItemClick(View viewItem, int position) {
                                    if (position != -1) {
                                        splashFragmentBinding.continueId.setAlpha(1f);
                                        splashFragmentBinding.continueId.setEnabled(true);
                                        splashFragmentBinding.continueId.setBackground(context.getResources().getDrawable(R.drawable.bk_button));
                                        splashFragmentBinding.continueId.setBackgroundTintList(null);
                                    } else {
                                        splashFragmentBinding.continueId.setAlpha(0.5f);
                                        splashFragmentBinding.continueId.setEnabled(false);
                                        splashFragmentBinding.continueId.setBackground(context.getResources().getDrawable(R.drawable.bk_border_button));
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
                        Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                        splashFragmentBinding.progress.setVisibility(View.GONE);
                        splashFragmentBinding.noInternetConId.setVisibility(View.GONE);
                    }
                } else {
                    splashFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                    splashFragmentBinding.noInternetConId.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
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

}
