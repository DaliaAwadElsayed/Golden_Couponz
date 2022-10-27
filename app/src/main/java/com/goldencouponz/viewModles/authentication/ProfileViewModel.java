package com.goldencouponz.viewModles.authentication;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.com.dtag.livia.utility.Local;
import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.ProfileFragmentBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.adapters.countries.CountriesAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.user.UserRegisteration;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.LocaleHelper;
import com.goldencouponz.utility.Utility;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {
    ProfileFragmentBinding profileFragmentBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    CountriesAdapter countriesAdapter;
    private FragmentActivity activity;

    public void init(ProfileFragmentBinding profileFragmentBinding, Context context) {
        this.context = context;
        this.profileFragmentBinding = profileFragmentBinding;
        this.activity = (FragmentActivity) context;
        Log.i("langChangeId", GoldenNoLoginSharedPreference.getUserLanguage(context) + "??");
        Log.i("countryChangeId", GoldenNoLoginSharedPreference.getUserCountryName(context) + "??");
        profileFragmentBinding.countryChangeId.setText(GoldenNoLoginSharedPreference.getUserCountryName(context));
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
            profileFragmentBinding.langChangeId.setText(R.string.arabic_label);
        } else if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("en")) {
            profileFragmentBinding.langChangeId.setText(R.string.english_label);
        }
        clicks();
        if (GoldenSharedPreference.isLoggedIn(context)) {
            profileFragmentBinding.noLoginLinearId.setVisibility(View.GONE);
            profileFragmentBinding.loginLinearId.setVisibility(View.VISIBLE);
            profileFragmentBinding.logOutId.setVisibility(View.VISIBLE);
            userProfile();
        } else {
            profileFragmentBinding.logOutId.setVisibility(View.GONE);
            profileFragmentBinding.noLoginLinearId.setVisibility(View.VISIBLE);
            profileFragmentBinding.loginLinearId.setVisibility(View.GONE);

        }

    }

    private void getCountries(String lang) {
        enableClick(false);
        countriesAdapter = new CountriesAdapter(context);
        profileFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getCountries(lang).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() && response.body() != null) {
                        profileFragmentBinding.progress.setVisibility(View.GONE);
                        if (!response.body().getCountries().isEmpty()) {
                            profileFragmentBinding.countriesLinearId.setVisibility(View.VISIBLE);
                            Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_up);
                            profileFragmentBinding.countriesLinearId.startAnimation(animation);
                            profileFragmentBinding.homeRecyclerView.setVisibility(View.VISIBLE);
                            countriesAdapter.setCountries(response.body().getCountries());
                            profileFragmentBinding.homeRecyclerView.setAdapter(countriesAdapter);
                            profileFragmentBinding.relativeId.setBackgroundColor(context.getResources().getColor(R.color.alpha));
                            profileFragmentBinding.relativeId.setAlpha(0.5f);
                            enableClick(false);
                            countriesAdapter.setOnItemClickListener(new CountriesAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View viewItem, int position, String code) {
                                    profileFragmentBinding.relativeId.setBackgroundColor(0);
                                    profileFragmentBinding.relativeId.setAlpha(1f);
                                    enableClick(true);
                                    profileFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                                    profileFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                                    Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_down);
                                    profileFragmentBinding.countriesLinearId.startAnimation(animation);
                                    profileFragmentBinding.countryChangeId.setText(code);
                                    GoldenNoLoginSharedPreference.saveUserCountry(context, position, code);

                                }
                            });

                        } else {
                            enableClick(true);
                            profileFragmentBinding.relativeId.setBackgroundColor(0);
                            profileFragmentBinding.relativeId.setAlpha(1f);
                            profileFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                            Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                            profileFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                            profileFragmentBinding.progress.setVisibility(View.GONE);
                        }
                    } else {
                        enableClick(true);
                        profileFragmentBinding.relativeId.setBackgroundColor(0);
                        profileFragmentBinding.relativeId.setAlpha(1f);
                        profileFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                        profileFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                        profileFragmentBinding.progress.setVisibility(View.GONE);
                        Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    enableClick(true);
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
                enableClick(true);

            }
        });
    }

    private void logOut() {
        ///TODO DELETE THIS
        /////////
        GoldenSharedPreference.clearSharedPreference(context);
        Navigation.findNavController(profileFragmentBinding.getRoot()).navigate(R.id.homeFragment);
        if (profileFragmentBinding.langChangeId.getText().equals("اللغة العربية")) {
            GoldenNoLoginSharedPreference.saveUserLang(context, "ar");
        } else if (profileFragmentBinding.langChangeId.getText().equals("English")) {
            GoldenNoLoginSharedPreference.saveUserLang(context, "en");
        }
        GoldenNoLoginSharedPreference.saveUserCountry(context, 0, profileFragmentBinding.countryChangeId.getText().toString());
        ////////
        profileFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.userLogOut("Bearer" + GoldenSharedPreference.getToken(context)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    GoldenSharedPreference.clearSharedPreference(context);
                    if (profileFragmentBinding.langChangeId.getText().equals("اللغة العربية")) {
                        GoldenNoLoginSharedPreference.saveUserLang(context, "ar");
                    } else if (profileFragmentBinding.langChangeId.getText().equals("English")) {
                        GoldenNoLoginSharedPreference.saveUserLang(context, "en");
                    }
                    GoldenNoLoginSharedPreference.saveUserCountry(context, 0, profileFragmentBinding.countryChangeId.getText().toString());
                    Navigation.findNavController(profileFragmentBinding.getRoot()).navigate(R.id.homeFragment);
                    profileFragmentBinding.progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                profileFragmentBinding.progress.setVisibility(View.GONE);
                Log.i("ONFAILURE", t.toString());
//                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    void clicks() {
        profileFragmentBinding.supportUsId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportUs();
            }
        });
        profileFragmentBinding.signId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.loginFragment);
            }
        });
        //Language
        profileFragmentBinding.cancelLangId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                langDown();
            }
        });
        profileFragmentBinding.slideDownLangId.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                langDown();
                return true;
            }
        });
        //Country
        profileFragmentBinding.cancelCountryId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countryDown();

            }
        });
        profileFragmentBinding.slideDownCountryId.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                countryDown();
                return true;
            }
        });
        //Profile
        profileFragmentBinding.cancelProfileId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileDown();
            }
        });
        profileFragmentBinding.slideDownProfileId.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                profileDown();
                return true;
            }
        });
        //click buttons
        profileFragmentBinding.codeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("en")) {
                    getCountries("en");
                } else {
                    getCountries("ar");
                }

            }
        });
        profileFragmentBinding.langId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage();
            }
        });
        profileFragmentBinding.editId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
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

    private void countryDown() {
        enableClick(true);
        profileFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
        profileFragmentBinding.countriesLinearId.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_down);
        profileFragmentBinding.countriesLinearId.startAnimation(animation);
        profileFragmentBinding.relativeId.setBackgroundColor(0);
        profileFragmentBinding.relativeId.setAlpha(1f);

    }

    private void langDown() {
        enableClick(true);
        Animation animation1 = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_down);
        profileFragmentBinding.languageLinearId.startAnimation(animation1);
        profileFragmentBinding.languageLinearId.setVisibility(View.GONE);
        profileFragmentBinding.languageLinearId.setVisibility(View.INVISIBLE);
        Log.i("VISIBILTY", profileFragmentBinding.languageLinearId.getVisibility() + "?");
        profileFragmentBinding.relativeId.setBackgroundColor(0);
        profileFragmentBinding.relativeId.setAlpha(1f);

    }

    private void profileDown() {
        enableClick(true);
        Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_down);
        profileFragmentBinding.profileLinearId.startAnimation(animation);
        profileFragmentBinding.relativeId.setBackgroundColor(0);
        profileFragmentBinding.relativeId.setAlpha(1f);
        profileFragmentBinding.profileLinearId.setVisibility(View.GONE);

    }
    private void supportUs() {
        //https://play.google.com/store/apps/details?id=com.codesroots.goldencoupon
        Uri uri = Uri.parse("market://details?id=" + "com.codesroots.goldencoupon");
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }
    private void enableClick(Boolean aBoolean) {
        profileFragmentBinding.codeId.setClickable(aBoolean);
        profileFragmentBinding.editId.setClickable(aBoolean);
        profileFragmentBinding.langId.setClickable(aBoolean);
        profileFragmentBinding.termsId.setClickable(aBoolean);
        profileFragmentBinding.privacyId.setClickable(aBoolean);
        profileFragmentBinding.whoAreId.setClickable(aBoolean);
        profileFragmentBinding.logOutId.setClickable(aBoolean);
        profileFragmentBinding.supportUsId.setClickable(aBoolean);
    }

    private void editProfile() {
        enableClick(false);
        Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_up);
        profileFragmentBinding.profileLinearId.startAnimation(animation);
        profileFragmentBinding.relativeId.setBackgroundColor(context.getResources().getColor(R.color.alpha));
        profileFragmentBinding.relativeId.setAlpha(0.5f);
        profileFragmentBinding.profileLinearId.setVisibility(View.VISIBLE);
        profileFragmentBinding.passwordRelativeId.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.changePasswordFragment);
        });
        profileFragmentBinding.editRelativeId.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.editProfileFragment);
        });

    }

    private void changeLanguage() {
        enableClick(false);
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("en")) {
            profileFragmentBinding.checkEnglishId.setVisibility(View.VISIBLE);
            profileFragmentBinding.checkArabicId.setVisibility(View.GONE);

        } else {
            profileFragmentBinding.checkEnglishId.setVisibility(View.GONE);
            profileFragmentBinding.checkArabicId.setVisibility(View.VISIBLE);
        }
        Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_up);
        profileFragmentBinding.languageLinearId.startAnimation(animation);
        profileFragmentBinding.relativeId.setBackgroundColor(context.getResources().getColor(R.color.alpha));
        profileFragmentBinding.relativeId.setAlpha(0.5f);
        profileFragmentBinding.languageLinearId.setVisibility(View.VISIBLE);
        profileFragmentBinding.englishRelativeId.setOnClickListener(view -> {
            GoldenNoLoginSharedPreference.saveUserLang(context, "en");
            LocaleHelper.setLocale(context, "en");
            Local.Companion.updateResources(context);
            resetApplication();

        });

        profileFragmentBinding.arabicRelativeId.setOnClickListener(view -> {
            GoldenNoLoginSharedPreference.saveUserLang(context, "ar");
            LocaleHelper.setLocale(context, "ar");
            Local.Companion.updateResources(context);
            resetApplication();
        });
    }

    private void resetApplication() {
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
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