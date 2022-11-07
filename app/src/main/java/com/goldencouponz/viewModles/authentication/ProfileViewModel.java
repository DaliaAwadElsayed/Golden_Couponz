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
import com.e.goldencouponz.databinding.CountryDialogBinding;
import com.e.goldencouponz.databinding.EditProfileDialogBinding;
import com.e.goldencouponz.databinding.EditProfileFragmentBinding;
import com.e.goldencouponz.databinding.LangDialogBinding;
import com.e.goldencouponz.databinding.LogOutDialogBinding;
import com.e.goldencouponz.databinding.ProfileCountriesDialogBinding;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {
    ProfileFragmentBinding profileFragmentBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    CountriesAdapter countriesAdapter;
    private FragmentActivity activity;
    int countrySavedId;
    LogOutDialogBinding logOutDialogBinding;
    EditProfileDialogBinding editProfileDialogBinding;
    LangDialogBinding langDialogBinding;
    ProfileCountriesDialogBinding profileCountriesDialogBinding;
    BottomSheetDialog showProfileCountryDialog;
    BottomSheetDialog showEditProfileDialog;
    BottomSheetDialog showLangDialog;
    BottomSheetDialog showLogOutDialog;

    public void init(ProfileFragmentBinding profileFragmentBinding, LogOutDialogBinding logOutDialogBinding,
                     EditProfileDialogBinding editProfileDialogBinding,
                     LangDialogBinding langDialogBinding,
                     ProfileCountriesDialogBinding profileCountriesDialogBinding, Context context) {
        this.context = context;
        this.editProfileDialogBinding = editProfileDialogBinding;
        this.langDialogBinding = langDialogBinding;
        this.profileCountriesDialogBinding = profileCountriesDialogBinding;
        this.logOutDialogBinding = logOutDialogBinding;
        this.profileFragmentBinding = profileFragmentBinding;
        showProfileCountryDialog = new BottomSheetDialog(context);
        showEditProfileDialog = new BottomSheetDialog(context);
        showLogOutDialog = new BottomSheetDialog(context);
        showLangDialog = new BottomSheetDialog(context);

        this.activity = (FragmentActivity) context;
        Log.i("langChangeId", GoldenNoLoginSharedPreference.getUserLanguage(context) + "??");
        Log.i("countryChangeName", GoldenNoLoginSharedPreference.getUserCountryName(context) + "??");
        Log.i("countryChangeId", GoldenNoLoginSharedPreference.getUserCountryId(context) + "??");
        Log.i("currencyChangeId", GoldenNoLoginSharedPreference.getUserCurrency(context) + "??");

        profileFragmentBinding.countryChangeId.setText(GoldenNoLoginSharedPreference.getUserCountryName(context));
        profileFragmentBinding.countryNumId.setText("" + GoldenNoLoginSharedPreference.getUserCountryId(context));
        profileFragmentBinding.currencyNumId.setText("" + GoldenNoLoginSharedPreference.getUserCurrency(context));

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
                            profileCountriesDialogBinding.homeRecyclerView.setVisibility(View.VISIBLE);
                            countriesAdapter.setCountries(response.body().getCountries());
                            profileCountriesDialogBinding.homeRecyclerView.setAdapter(countriesAdapter);

                            countriesAdapter.setOnItemClickListener(new CountriesAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View viewItem, int position, int id, String code, String currency) {
                                    countrySavedId = id;
                                    profileCountriesDialogBinding.homeRecyclerView.setVisibility(View.GONE);
                                    profileFragmentBinding.countryChangeId.setText(code);
                                    profileFragmentBinding.currencyNumId.setText(currency);
                                    GoldenNoLoginSharedPreference.saveUserCountry(context, position, id, code, currency);
                                    showProfileCountryDialog.dismiss();
                                }
                            });

                        } else {
                            showProfileCountryDialog.dismiss();
                            profileCountriesDialogBinding.homeRecyclerView.setVisibility(View.GONE);
                            Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                            profileFragmentBinding.progress.setVisibility(View.GONE);
                        }
                    } else {
                        showProfileCountryDialog.dismiss();
                        profileCountriesDialogBinding.homeRecyclerView.setVisibility(View.GONE);
                        profileFragmentBinding.progress.setVisibility(View.GONE);
                        Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showProfileCountryDialog.dismiss();
                    profileCountriesDialogBinding.homeRecyclerView.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                    profileFragmentBinding.progress.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
                profileFragmentBinding.progress.setVisibility(View.GONE);
                showProfileCountryDialog.dismiss();
            }
        });
    }

    private void logOut() {
        ///TODO DELETE THIS
        /////////
        profileFragmentBinding.logOutSureId.setEnabled(true);
        profileFragmentBinding.logOutSureId.setClickable(true);
        profileFragmentBinding.cancelId.setEnabled(true);
        profileFragmentBinding.cancelId.setClickable(true);
        profileFragmentBinding.cancelLogOutId.setEnabled(true);
        profileFragmentBinding.cancelLogOutId.setClickable(true);
        enableClick(false);
        Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_up);
        profileFragmentBinding.logOutLinearId.startAnimation(animation);
        profileFragmentBinding.relativeId.setBackgroundColor(context.getResources().getColor(R.color.alpha));
        profileFragmentBinding.relativeId.setAlpha(0.5f);
        profileFragmentBinding.logOutLinearId.setVisibility(View.VISIBLE);
        profileFragmentBinding.cancelId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDown();
            }
        });
        profileFragmentBinding.cancelLogOutId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDown();
            }
        });
        profileFragmentBinding.logOutSureId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoldenSharedPreference.clearSharedPreference(context);
                if (profileFragmentBinding.langChangeId.getText().equals("اللغة العربية")) {
                    GoldenNoLoginSharedPreference.saveUserLang(context, "ar");
                } else if (profileFragmentBinding.langChangeId.getText().equals("English")) {
                    GoldenNoLoginSharedPreference.saveUserLang(context, "en");
                }
                countrySavedId = Integer.parseInt(profileFragmentBinding.countryNumId.getText().toString());
                Log.i("DATAIS", countrySavedId + "/" + profileFragmentBinding.countryChangeId.getText().toString() + "/" +
                        profileFragmentBinding.currencyNumId.getText().toString());
                GoldenNoLoginSharedPreference.saveUserCountry(context, 0, countrySavedId, profileFragmentBinding.countryChangeId.getText().toString(),
                        profileFragmentBinding.currencyNumId.getText().toString()
                );
                Navigation.findNavController(profileFragmentBinding.getRoot()).navigate(R.id.homeFragment);
                profileFragmentBinding.progress.setVisibility(View.VISIBLE);
                apiInterface.userLogOut("Bearer" + GoldenSharedPreference.getToken(context)).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.code() == 200) {
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
        });

    }

    void clicks() {
        profileFragmentBinding.cancelLogOutId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDown();
            }
        });
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

        profileFragmentBinding.slideDownLogId.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                logoutDown();
                return true;
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