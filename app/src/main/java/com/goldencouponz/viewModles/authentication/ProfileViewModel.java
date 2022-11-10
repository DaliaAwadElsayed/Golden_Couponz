package com.goldencouponz.viewModles.authentication;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.com.dtag.livia.utility.Local;
import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.EditProfileDialogBinding;
import com.e.goldencouponz.databinding.LangDialogBinding;
import com.e.goldencouponz.databinding.LogOutDialogBinding;
import com.e.goldencouponz.databinding.ProfileCountriesDialogBinding;
import com.e.goldencouponz.databinding.ProfileFragmentBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.adapters.countries.CountriesAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.appsetting.Country;
import com.goldencouponz.models.user.UserRegisteration;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.LocaleHelper;
import com.goldencouponz.utility.Utility;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

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
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("en")) {
            getCountryAndCurrencyWithId("en");
        } else {
            getCountryAndCurrencyWithId("ar");
        }
        profileFragmentBinding.countryChangeId.setText(GoldenNoLoginSharedPreference.getUserCountryName(context));
        profileFragmentBinding.countryNumId.setText("" + GoldenNoLoginSharedPreference.getUserCountryId(context));
        profileFragmentBinding.currencyNumId.setText("" + GoldenNoLoginSharedPreference.getUserCurrency(context));
        Log.i("GoldenPreference", GoldenNoLoginSharedPreference.getUserLanguage(context) + "?");
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

    private void showLogOutDialog() {
        final View view3 = logOutDialogBinding.getRoot();
        showLogOutDialog.setContentView(view3);
        showLogOutDialog.setCancelable(true);
        showLogOutDialog.show();

        logOut();
    }

    public Country findMemberByName(int id, List<Country> countries) {
        // go through list of members and compare name with given name

        for (Country member : countries) {
            if (member.getId().equals(id)) {
                Log.i("MEMBER", member.getTitle() + "//" + member.getCurrency() + "?");
                profileFragmentBinding.countryChangeId.setText(member.getTitle());
                profileFragmentBinding.currencyNumId.setText(member.getCurrency());
                GoldenNoLoginSharedPreference.saveUserCountry(context, 0, id, profileFragmentBinding.countryChangeId.getText().toString(), profileFragmentBinding.currencyNumId.getText().toString());
                return member; // return member when name found
            }
        }
        return null; // return null when no member with given name could be found
    }

    private void getCountryAndCurrencyWithId(String lang) {
        int countryId = GoldenNoLoginSharedPreference.getUserCountryId(context);
        countriesAdapter = new CountriesAdapter(context);
        apiInterface.getCountries(lang).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() && response.body() != null) {
                        profileFragmentBinding.progress.setVisibility(View.GONE);
                        if (!response.body().getCountries().isEmpty()) {
                            countriesAdapter.setCountries(response.body().getCountries());
                            findMemberByName(countryId, response.body().getCountries());

                            countriesAdapter.setOnItemClickListener(new CountriesAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View viewItem, int position, int id, String code, String currency) {

                                }
                            });

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });

    }

    private void showProfileCountryDialog() {
        final View view3 = profileCountriesDialogBinding.getRoot();
        showProfileCountryDialog.setContentView(view3);
        showProfileCountryDialog.setCancelable(true);
        showProfileCountryDialog.show();
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("en")) {
            getCountries("en");
        } else if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
            getCountries("ar");
        }
        profileCountriesDialogBinding.cancelCountryId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileCountryDialog.dismiss();
            }
        });
    }

    private void showEditProfileDialog() {
        final View view3 = editProfileDialogBinding.getRoot();
        showEditProfileDialog.setContentView(view3);
        showEditProfileDialog.setCancelable(true);
        showEditProfileDialog.show();
        editProfileDialogBinding.passwordRelativeId.setOnClickListener(view -> {
            showEditProfileDialog.dismiss();
            Navigation.findNavController(profileFragmentBinding.getRoot()).navigate(R.id.changePasswordFragment);
        });
        editProfileDialogBinding.editRelativeId.setOnClickListener(view -> {
            showEditProfileDialog.dismiss();
            Navigation.findNavController(profileFragmentBinding.getRoot()).navigate(R.id.editProfileFragment);
        });

        editProfileDialogBinding.cancelProfileId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog.dismiss();
            }
        });
        editProfileDialogBinding.deleteAccountId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog.dismiss();
                Navigation.findNavController(profileFragmentBinding.getRoot()).navigate(R.id.deleteAccountFragment);
            }
        });
    }

    private void showLangDialog() {
        final View view3 = langDialogBinding.getRoot();
        showLangDialog.setContentView(view3);
        showLangDialog.setCancelable(true);
        showLangDialog.show();
        langDialogBinding.cancelLangId.setOnClickListener(v -> showLangDialog.dismiss());
        changeLanguage();
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
        logOutDialogBinding.cancelId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogOutDialog.dismiss();
            }
        });
        logOutDialogBinding.cancelLogOutId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogOutDialog.dismiss();
            }
        });
        logOutDialogBinding.logOutSureId.setOnClickListener(new View.OnClickListener() {
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
                showLogOutDialog.dismiss();
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

        //click buttons
        profileFragmentBinding.codeId.setOnClickListener(view -> showProfileCountryDialog());
        profileFragmentBinding.editId.setOnClickListener(view -> showEditProfileDialog());
        profileFragmentBinding.langId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLangDialog();
            }
        });
        profileFragmentBinding.logOutId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogOutDialog();
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


    private void changeLanguage() {
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("en")) {
            langDialogBinding.checkEnglishId.setVisibility(View.VISIBLE);
            langDialogBinding.checkArabicId.setVisibility(View.GONE);

        } else {
            langDialogBinding.checkEnglishId.setVisibility(View.GONE);
            langDialogBinding.checkArabicId.setVisibility(View.VISIBLE);
        }
        langDialogBinding.englishRelativeId.setOnClickListener(view -> {
            Log.i("clickss", "SAVED");
            GoldenNoLoginSharedPreference.saveUserLang(context, "en");
            LocaleHelper.setLocale(context, "en");
            Local.Companion.updateResources(context);
            resetApplication();
        });
        langDialogBinding.arabicRelativeId.setOnClickListener(view -> {
            Log.i("clickss", "SAVEDDD");
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