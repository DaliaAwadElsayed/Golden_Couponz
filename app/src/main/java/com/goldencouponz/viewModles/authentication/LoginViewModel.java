package com.goldencouponz.viewModles.authentication;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.LoginFragmentBinding;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    LoginFragmentBinding loginFragmentBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();

    public void init(LoginFragmentBinding loginFragmentBinding, Context context) {
        this.context = context;
        this.loginFragmentBinding = loginFragmentBinding;
        loginFragmentBinding.toolBarId.setClickable(true);
        loginFragmentBinding.signUpId.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.registerationFragment));
        loginFragmentBinding.forgetPassId.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("login", "no");
            Navigation.findNavController(view).navigate(R.id.forgetPasswordFragment, bundle);
        });
        loginFragmentBinding.signInId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputValid()) {
                    login(loginFragmentBinding.emailId.getText().toString(), loginFragmentBinding.passwordId.getText().toString());
                }
            }
        });
        loginFragmentBinding.closeId.setOnClickListener(view -> {
            GoldenSharedPreference.saveUserCloseLogIn(context, "close");
            Navigation.findNavController(view).navigate(R.id.homeFragment);
        });

    }

    private void login(String email, String password) {
        loginFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.userLogin(email, password).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    loginFragmentBinding.progress.setVisibility(View.GONE);
                    GoldenSharedPreference.saveUser(context, response.body(), response.body().getId());
                    Navigation.findNavController(loginFragmentBinding.getRoot()).navigate(R.id.homeFragment);
                } else {
                    loginFragmentBinding.progress.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.thereisanerrorwiththedata, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                loginFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_LONG).show();

            }
        });

    }

    private boolean inputValid() {
        return emailValid() && passwordIsValid();
    }

    private boolean passwordIsValid() {
        String firstPass = loginFragmentBinding.passwordId.getText().toString();
        if (firstPass.length() < 8) {
            loginFragmentBinding.passwordId.setError(context.getResources().getString(R.string.lessthan8letters));
            return false;
        } else {
            return true;
        }
    }

    private boolean emailValid() {
        String email = loginFragmentBinding.emailId.getText().toString();

        if (!loginFragmentBinding.emailId.getText().toString().isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            loginFragmentBinding.emailId.setError(context.getResources().getString(R.string.entervalidemail));
            return false;
        }
    }
}