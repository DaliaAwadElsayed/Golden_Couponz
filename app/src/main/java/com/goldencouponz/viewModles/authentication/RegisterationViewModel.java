package com.goldencouponz.viewModles.authentication;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.RegisterationFragmentBinding;
import com.goldencouponz.adapters.countries.CountriesCodeAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.user.UserRegisteration;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterationViewModel extends ViewModel {
    RegisterationFragmentBinding registerationFragmentBinding;
    Context context;
    UserRegisteration userRegistration;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    CountriesCodeAdapter countriesAdapter;

    public void init(RegisterationFragmentBinding registerationFragmentBinding, Context context) {
        this.context = context;
        this.registerationFragmentBinding = registerationFragmentBinding;
        registerationFragmentBinding.toolBarId.setClickable(true);
        registerationFragmentBinding.backId.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.loginFragment));
        registerationFragmentBinding.signUpId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputValid()) {
                    userRegistration();
                }
            }
        });
        registerationFragmentBinding.relativeRegistId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerationFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                registerationFragmentBinding.relativeRegistId.setVisibility(View.GONE);
                registerationFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_down);
                registerationFragmentBinding.countriesLinearId.startAnimation(animation);
                registerationFragmentBinding.relativeRegistId.setBackgroundColor(context.getResources().getColor(R.color.antique_white));

            }
        });
        registerationFragmentBinding.slideDownId.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View view, MotionEvent motionEvent) {
                registerationFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                registerationFragmentBinding.relativeRegistId.setVisibility(View.GONE);
                registerationFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_down);
                registerationFragmentBinding.countriesLinearId.startAnimation(animation);
                registerationFragmentBinding.relativeRegistId.setBackgroundColor(context.getResources().getColor(R.color.antique_white));

                return true;
            }
        });
        registerationFragmentBinding.slideDownId.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                registerationFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                registerationFragmentBinding.relativeRegistId.setVisibility(View.GONE);
                registerationFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_down);
                registerationFragmentBinding.countriesLinearId.startAnimation(animation);
                registerationFragmentBinding.relativeRegistId.setBackgroundColor(context.getResources().getColor(R.color.antique_white));
                return true;
            }
        });
        registerationFragmentBinding.codeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCountries("en");

            }
        });

    }


    private void getCountries(String lang) {
        countriesAdapter = new CountriesCodeAdapter(context);
        registerationFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getCountries(lang).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() && response.body() != null) {
                        registerationFragmentBinding.progress.setVisibility(View.GONE);
                        if (!response.body().getCountries().isEmpty()) {
                            registerationFragmentBinding.countriesLinearId.setVisibility(View.VISIBLE);
                            Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_up);
                            registerationFragmentBinding.countriesLinearId.startAnimation(animation);
                            registerationFragmentBinding.homeRecyclerView.setVisibility(View.VISIBLE);
                            registerationFragmentBinding.prefCountryId.setVisibility(View.VISIBLE);
                            countriesAdapter.setCountries(response.body().getCountries());
                            registerationFragmentBinding.homeRecyclerView.setAdapter(countriesAdapter);
                            registerationFragmentBinding.relativeRegistId.setVisibility(View.VISIBLE);
                            countriesAdapter.setOnItemClickListener(new CountriesCodeAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View viewItem, String code) {
                                    registerationFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                                    registerationFragmentBinding.relativeRegistId.setVisibility(View.GONE);
                                    registerationFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                                    Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_down);
                                    registerationFragmentBinding.countriesLinearId.startAnimation(animation);
                                    registerationFragmentBinding.codeId.setText("+" + code);
                                }
                            });

                        } else {
                            registerationFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                            registerationFragmentBinding.relativeRegistId.setVisibility(View.GONE);
                            Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                            registerationFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                            registerationFragmentBinding.progress.setVisibility(View.GONE);
                        }
                    } else {
                        registerationFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                        registerationFragmentBinding.relativeRegistId.setVisibility(View.GONE);
                        registerationFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                        registerationFragmentBinding.progress.setVisibility(View.GONE);
                        Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    registerationFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                    registerationFragmentBinding.relativeRegistId.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                    registerationFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                    registerationFragmentBinding.progress.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
                registerationFragmentBinding.countriesLinearId.setVisibility(View.GONE);
                registerationFragmentBinding.progress.setVisibility(View.GONE);

            }
        });
    }

    private void userRegistration() {
        registerationFragmentBinding.progress.setVisibility(View.VISIBLE);
        userRegistration = new UserRegisteration();
        userRegistration.setName(registerationFragmentBinding.nameId.getText().toString());
        userRegistration.setEmail(registerationFragmentBinding.emailId.getText().toString());
        userRegistration.setPhone(registerationFragmentBinding.codeId.getText() + registerationFragmentBinding.phoneId.getText().toString());
        userRegistration.setPassword(registerationFragmentBinding.passwordId.getText().toString());
        userRegistration.setPassword_confirmation(registerationFragmentBinding.confirmPasswordId.getText().toString());
        apiInterface.userRegister(userRegistration).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    registerationFragmentBinding.progress.setVisibility(View.GONE);
                    login(registerationFragmentBinding.emailId.getText().toString(), registerationFragmentBinding.passwordId.getText().toString());
                } else if (response.code() == 400) {
                    registerationFragmentBinding.progress.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.theemailhasalreadybeentaken, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", "onFailure" + t.toString());
                registerationFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void login(String email, String password) {
        registerationFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.userLogin(email, password).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    registerationFragmentBinding.progress.setVisibility(View.GONE);
                    GoldenSharedPreference.saveUser(context, response.body(), response.body().getId());
                    Navigation.findNavController(registerationFragmentBinding.getRoot()).navigate(R.id.favouriteStoresFragment);
                } else {
                    registerationFragmentBinding.progress.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.thereisanerrorwiththedata, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                registerationFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_LONG).show();
            }
        });

    }


    private boolean inputValid() {
        return nameValid() && phoneValid() && codeValid() && emailValid() && emailsIsValid() && passwordStrongValid() && confirmPasswordStrongValid() && passwordsIsValid();
    }

    private boolean nameValid() {
        String name = registerationFragmentBinding.nameId.getText().toString();
        if (!name.isEmpty()) {
            return true;
        } else {
            registerationFragmentBinding.nameId.setError(context.getResources().getString(R.string.enterurname));
            registerationFragmentBinding.nameId.requestFocus();
            return false;
        }
    }


    private boolean passwordStrongValid() {
        String name = registerationFragmentBinding.passwordId.getText().toString();
        if (name.isEmpty()) {
            registerationFragmentBinding.passwordId.setError(context.getResources().getString(R.string.enterpassword));
            registerationFragmentBinding.passwordId.requestFocus();
            return false;
        } else if (!name.isEmpty() && name.length() < 6) {
            registerationFragmentBinding.passwordId.setError(context.getResources().getString(R.string.lessthan8letters));
            registerationFragmentBinding.passwordId.requestFocus();
            return false;

        } else {
            return true;
        }
    }

    private boolean confirmPasswordStrongValid() {
        String name = registerationFragmentBinding.confirmPasswordId.getText().toString();
        if (name.isEmpty()) {
            registerationFragmentBinding.confirmPasswordId.setError(context.getResources().getString(R.string.passwordsdoesntmatch));
            registerationFragmentBinding.confirmPasswordId.requestFocus();
            return false;

        } else {
            return true;
        }
    }

    private boolean passwordsIsValid() {
        String firstPass = registerationFragmentBinding.passwordId.getText().toString();
        String confirmPass = registerationFragmentBinding.confirmPasswordId.getText().toString();
        if (!firstPass.equals(confirmPass)) {
            registerationFragmentBinding.confirmPasswordId.setError(context.getResources().getString(R.string.passwordsdoesntmatch));
            registerationFragmentBinding.confirmPasswordId.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean emailsIsValid() {
        String firstEmail = registerationFragmentBinding.emailId.getText().toString();
        String confirmEmail = registerationFragmentBinding.confirmEmailId.getText().toString();
        if (!firstEmail.equals(confirmEmail)) {
            registerationFragmentBinding.confirmEmailId.setError(context.getResources().getString(R.string.emailsdoesntmatch));
            registerationFragmentBinding.confirmEmailId.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean phoneValid() {
        String name = registerationFragmentBinding.phoneId.getText().toString();
        if (!name.isEmpty()) {
            return true;
        } else {
            registerationFragmentBinding.phoneId.setError(context.getResources().getString(R.string.enterphone));
            registerationFragmentBinding.phoneId.requestFocus();
            return false;
        }
    }

    private boolean codeValid() {
        String name = registerationFragmentBinding.codeId.getText().toString();
        if (!name.isEmpty()) {
            return true;
        } else {
            Toast.makeText(context, R.string.preferred_country, Toast.LENGTH_SHORT).show();
            registerationFragmentBinding.codeId.requestFocus();
            return false;
        }
    }

    private boolean emailValid() {
        String email = registerationFragmentBinding.emailId.getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            registerationFragmentBinding.emailId.setError(context.getResources().getString(R.string.entervalidemail));
            registerationFragmentBinding.emailId.requestFocus();

            return false;
        }
    }


}