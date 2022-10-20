package com.goldencouponz.viewModles.authentication;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.ProfileFragmentBinding;
import com.goldencouponz.adapters.countries.CountriesAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.user.UserRegisteration;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.GoldenSharedPreference;
import com.goldencouponz.utility.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {
    ProfileFragmentBinding profileFragmentBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    CountriesAdapter countriesAdapter;

    public void init(ProfileFragmentBinding profileFragmentBinding, Context context) {
        this.context = context;
        this.profileFragmentBinding = profileFragmentBinding;
        profileFragmentBinding.countryChangeId.setText(GoldenSharedPreference.getUserCountryName(context));
        if (GoldenSharedPreference.getUserLanguage(context).equals("ar")) {
            profileFragmentBinding.langChangeId.setText(R.string.arabic_label);
        } else {
            profileFragmentBinding.langChangeId.setText(R.string.english_label);
        }
        clicks();
        if (GoldenSharedPreference.isLoggedIn(context)) {
            profileFragmentBinding.noLoginLinearId.setVisibility(View.GONE);
            profileFragmentBinding.loginLinearId.setVisibility(View.VISIBLE);
            userProfile();
        } else {
            profileFragmentBinding.noLoginLinearId.setVisibility(View.VISIBLE);
            profileFragmentBinding.loginLinearId.setVisibility(View.GONE);
        }

    }

    private void getCountries(String lang) {
        countriesAdapter = new CountriesAdapter(context);
        profileFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getCountries(lang).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() && response.body() != null) {
                        profileFragmentBinding.progress.setVisibility(View.GONE);
                        if (!response.body().getCountries().isEmpty()) {
                            profileFragmentBinding.relativeId.setBackgroundColor(context.getResources().getColor(R.color.alpha));
                            profileFragmentBinding.relativeId.setAlpha(0.5f);
                            profileFragmentBinding.countriesLinearId.setVisibility(View.VISIBLE);
                            Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_up);
                            profileFragmentBinding.countriesLinearId.startAnimation(animation);
                            profileFragmentBinding.homeRecyclerView.setVisibility(View.VISIBLE);
                            profileFragmentBinding.prefCountryId.setVisibility(View.VISIBLE);
                            countriesAdapter.setCountries(response.body().getCountries());
                            profileFragmentBinding.homeRecyclerView.setAdapter(countriesAdapter);
                            countriesAdapter.setOnItemClickListener(new CountriesAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View viewItem, int position, String code) {
                                    profileFragmentBinding.relativeId.setBackgroundColor(0);
                                    profileFragmentBinding.relativeId.setAlpha(1f);
                                    profileFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                                    profileFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                                    Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_down);
                                    profileFragmentBinding.countriesLinearId.startAnimation(animation);
                                    profileFragmentBinding.countryChangeId.setText(code);
                                    GoldenSharedPreference.saveUserCountry(context, position, code);

                                }
                            });

                        } else {
                            profileFragmentBinding.relativeId.setBackgroundColor(0);
                            profileFragmentBinding.relativeId.setAlpha(1f);
                            profileFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                            Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                            profileFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                            profileFragmentBinding.progress.setVisibility(View.GONE);
                        }
                    } else {
                        profileFragmentBinding.relativeId.setBackgroundColor(0);
                        profileFragmentBinding.relativeId.setAlpha(1f);
                        profileFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                        profileFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                        profileFragmentBinding.progress.setVisibility(View.GONE);
                        Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    profileFragmentBinding.relativeId.setBackgroundColor(0);
                    profileFragmentBinding.relativeId.setAlpha(1f);
                    profileFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                    profileFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                    profileFragmentBinding.progress.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
                profileFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                profileFragmentBinding.progress.setVisibility(View.GONE);
                profileFragmentBinding.relativeId.setBackgroundColor(0);
                profileFragmentBinding.relativeId.setAlpha(1f);

            }
        });
    }

    private void logOut() {
        profileFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.userLogOut("Bearer" + GoldenSharedPreference.getToken(context)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    GoldenSharedPreference.clearSharedPreference(context);
                    Navigation.findNavController(profileFragmentBinding.getRoot()).navigate(R.id.homeFragment);
                    profileFragmentBinding.progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                profileFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    void clicks() {
        profileFragmentBinding.slideDownId.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View view, MotionEvent motionEvent) {
                profileFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                profileFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_down);
                profileFragmentBinding.countriesLinearId.startAnimation(animation);
                profileFragmentBinding.relativeId.setBackgroundColor(0);
                profileFragmentBinding.relativeId.setAlpha(1f);
                return true;
            }
        });



        profileFragmentBinding.slideDownId.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                profileFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                profileFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_down);
                profileFragmentBinding.countriesLinearId.startAnimation(animation);
                profileFragmentBinding.relativeId.setBackgroundColor(0);
                profileFragmentBinding.relativeId.setAlpha(1f);
                return true;
            }
        });
        profileFragmentBinding.codeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GoldenSharedPreference.getUserLanguage(context).equals("en")) {
                    getCountries("en");
                } else {
                    getCountries("ar");
                }

            }
        });

        profileFragmentBinding.logOutId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
        profileFragmentBinding.whoAreId.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("type", "about");
            Navigation.findNavController(view).navigate(R.id.aboutFragment, bundle);
        });
        profileFragmentBinding.privacyId.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("type", "privacy");
            Navigation.findNavController(view).navigate(R.id.aboutFragment, bundle);
        });
        profileFragmentBinding.termsId.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("type", "terms");
            Navigation.findNavController(view).navigate(R.id.aboutFragment, bundle);
        });
    }

    private void userProfile() {
        profileFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getProfile("Bearer" + GoldenSharedPreference.getToken(context)).enqueue(new Callback<UserRegisteration>() {
            @Override
            public void onResponse(Call<UserRegisteration> call, Response<UserRegisteration> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        profileFragmentBinding.progress.setVisibility(View.GONE);
                        profileFragmentBinding.phoneId.setText(Utility.fixNullString(response.body().getUser().getPhone()));
                        profileFragmentBinding.emailId.setText(Utility.fixNullString(response.body().getUser().getEmail()));
                        profileFragmentBinding.nameId.setText(Utility.fixNullString(response.body().getUser().getName()));
                    } else {
                        profileFragmentBinding.progress.setVisibility(View.GONE);
                        profileFragmentBinding.phoneId.setText(Utility.fixNullString(GoldenSharedPreference.getPhone(context)));
                        profileFragmentBinding.emailId.setText(Utility.fixNullString(GoldenSharedPreference.getKeyEmail(context)));
                        profileFragmentBinding.nameId.setText(Utility.fixNullString(GoldenSharedPreference.getName(context)));
                    }

                } else {
                    profileFragmentBinding.progress.setVisibility(View.GONE);
                    profileFragmentBinding.phoneId.setText(Utility.fixNullString(GoldenSharedPreference.getPhone(context)));
                    profileFragmentBinding.emailId.setText(Utility.fixNullString(GoldenSharedPreference.getKeyEmail(context)));
                    profileFragmentBinding.nameId.setText(Utility.fixNullString(GoldenSharedPreference.getName(context)));
                }

            }

            @Override
            public void onFailure(Call<UserRegisteration> call, Throwable t) {
                profileFragmentBinding.progress.setVisibility(View.GONE);
                profileFragmentBinding.phoneId.setText(Utility.fixNullString(GoldenSharedPreference.getPhone(context)));
                profileFragmentBinding.emailId.setText(Utility.fixNullString(GoldenSharedPreference.getKeyEmail(context)));
                profileFragmentBinding.nameId.setText(Utility.fixNullString(GoldenSharedPreference.getName(context)));
            }
        });

    }

}