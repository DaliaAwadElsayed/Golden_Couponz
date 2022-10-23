package com.goldencouponz.viewModles.authentication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.ChangePasswordFragmentBinding;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.user.ChangePassword;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordViewModel extends ViewModel {
    ChangePasswordFragmentBinding changePasswordFragmentBinding;
    Context context;
    ChangePassword changePassword;
    private Api apiInterface = RetrofitClient.getInstance().getApi();

    public void init(ChangePasswordFragmentBinding changePasswordFragmentBinding, Context context) {
        this.changePasswordFragmentBinding = changePasswordFragmentBinding;
        this.context = context;
        changePasswordFragmentBinding.backId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.profileFragment);
            }
        });
        changePasswordFragmentBinding.forgetPasswordId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("login", "yes");
                Navigation.findNavController(view).navigate(R.id.forgetPasswordFragment, bundle);
            }
        });
        changePassword = new ChangePassword();
        changePasswordFragmentBinding.saveId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        if (passwordInputValid()) {
            changePasswordFragmentBinding.progress.setVisibility(View.VISIBLE);
            changePassword.setOldPassword(changePasswordFragmentBinding.currentPasswordId.getText().toString());
            changePassword.setNewPassword(changePasswordFragmentBinding.newPasswordId.getText().toString());
            changePassword.setNewPasswordConfirmation(changePasswordFragmentBinding.confirmNewPasswordId.getText().toString());
            apiInterface.changePassword("Bearer" + GoldenSharedPreference.getToken(context), changePassword).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                    if (response.code() == 200) {
                        login(GoldenSharedPreference.getKeyEmail(context), changePasswordFragmentBinding.newPasswordId.getText().toString());
                        changePasswordFragmentBinding.progress.setVisibility(View.GONE);
                    } else if (response.code() == 400) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String userMessage = null;
                        try {
                            userMessage = jsonObject.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.i("MESSAGE", ""+userMessage);
                        changePasswordFragmentBinding.progress.setVisibility(View.GONE);
                        if (userMessage.equals("New Password cannot be same as your current password")) {
                            Toast.makeText(context, R.string.oldPasswordAsNew, Toast.LENGTH_SHORT).show();
                        } else if (userMessage.equals("Your current password does not matches with the password")) {
                            Toast.makeText(context, R.string.oldpasswordisincorret, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        changePasswordFragmentBinding.progress.setVisibility(View.GONE);
                        Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                    changePasswordFragmentBinding.progress.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
                }

            });
        }
    }

    private void login(String email, String password) {
        apiInterface.userLogin(email, password).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    GoldenSharedPreference.saveUser(context, response.body(), response.body().getId());
                    Toast.makeText(context, R.string.changepassworddone, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });

    }

    private boolean passwordInputValid() {
        return oldPasswordEditText() && passwordStrongValid() && passwordsIsValid();
    }

    private boolean oldPasswordEditText() {
        if (changePasswordFragmentBinding.currentPasswordId.getText().toString().matches("")) {
            changePasswordFragmentBinding.currentPasswordId.setError(context.getResources().getString(R.string.enterpassword));
            changePasswordFragmentBinding.currentPasswordId.requestFocus();
            return false;
        }

        return true;
    }

    private boolean passwordStrongValid() {
        String name = changePasswordFragmentBinding.newPasswordId.getText().toString();
        if (name.isEmpty()) {
            changePasswordFragmentBinding.newPasswordId.setError(context.getResources().getString(R.string.enterpassword));
            changePasswordFragmentBinding.newPasswordId.requestFocus();
            return false;
        } else if (!name.isEmpty() && name.length() < 8) {
            changePasswordFragmentBinding.newPasswordId.setError(context.getResources().getString(R.string.lessthan8letters));
            changePasswordFragmentBinding.newPasswordId.requestFocus();
            return false;

        } else {
            return true;
        }
    }

    private boolean passwordsIsValid() {
        String firstPass = changePasswordFragmentBinding.newPasswordId.getText().toString();
        String confirmPass = changePasswordFragmentBinding.confirmNewPasswordId.getText().toString();
        if (!firstPass.equals(confirmPass)) {
            Toast.makeText(context, context.getResources().getString(R.string.passwordsdoesntmatch), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


}