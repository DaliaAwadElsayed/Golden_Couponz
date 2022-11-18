package com.goldencouponz.viewModles.authentication;

import android.content.Context;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.EditProfileFragmentBinding;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.user.UserRegisteration;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.Utility;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileViewModel extends ViewModel {
    EditProfileFragmentBinding editProfileFragmentBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    UserRegisteration userRegisteration;

    public void init(EditProfileFragmentBinding editProfileFragmentBinding, Context context) {
        this.context = context;
        this.editProfileFragmentBinding = editProfileFragmentBinding;
        userRegisteration = new UserRegisteration();
        userProfile();
        editProfileFragmentBinding.backId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.profileFragment);
            }
        });
        editProfileFragmentBinding.saveId.setOnClickListener(view -> {
            if (inputValid()) {
                userEditProfile();
            }
        });
    }

    private void userProfile() {
        editProfileFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getProfile("Bearer" + GoldenSharedPreference.getToken(context)).enqueue(new Callback<UserRegisteration>() {
            @Override
            public void onResponse(Call<UserRegisteration> call, Response<UserRegisteration> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        editProfileFragmentBinding.progress.setVisibility(View.GONE);
                        editProfileFragmentBinding.phoneId.setText(Utility.fixNullString(response.body().getUser().getPhone()));
                        editProfileFragmentBinding.confirmEmailId.setText(Utility.fixNullString(response.body().getUser().getEmail()));
                        editProfileFragmentBinding.emailId.setText(Utility.fixNullString(response.body().getUser().getEmail()));
                        editProfileFragmentBinding.nameId.setText(Utility.fixNullString(response.body().getUser().getName()));
                        GoldenNoLoginSharedPreference.saveUserEmail(context, response.body().getUser().getEmail());

                    } else {
                        editProfileFragmentBinding.progress.setVisibility(View.GONE);
                        editProfileFragmentBinding.phoneId.setText(Utility.fixNullString(GoldenSharedPreference.getPhone(context)));
                        editProfileFragmentBinding.confirmEmailId.setText(Utility.fixNullString(GoldenSharedPreference.getKeyEmail(context)));
                        editProfileFragmentBinding.emailId.setText(Utility.fixNullString(GoldenSharedPreference.getKeyEmail(context)));
                        editProfileFragmentBinding.nameId.setText(Utility.fixNullString(GoldenSharedPreference.getName(context)));
                    }

                } else {
                    editProfileFragmentBinding.progress.setVisibility(View.GONE);
                    editProfileFragmentBinding.phoneId.setText(Utility.fixNullString(GoldenSharedPreference.getPhone(context)));
                    editProfileFragmentBinding.emailId.setText(Utility.fixNullString(GoldenSharedPreference.getKeyEmail(context)));
                    editProfileFragmentBinding.confirmEmailId.setText(Utility.fixNullString(GoldenSharedPreference.getKeyEmail(context)));
                    editProfileFragmentBinding.nameId.setText(Utility.fixNullString(GoldenSharedPreference.getName(context)));
                }

            }

            @Override
            public void onFailure(Call<UserRegisteration> call, Throwable t) {
                editProfileFragmentBinding.progress.setVisibility(View.GONE);
                editProfileFragmentBinding.phoneId.setText(Utility.fixNullString(GoldenSharedPreference.getPhone(context)));
                editProfileFragmentBinding.emailId.setText(Utility.fixNullString(GoldenSharedPreference.getKeyEmail(context)));
                editProfileFragmentBinding.nameId.setText(Utility.fixNullString(GoldenSharedPreference.getName(context)));
            }
        });

    }

    private void userEditProfile() {
        editProfileFragmentBinding.progress.setVisibility(View.VISIBLE);
        userRegisteration.setName(Utility.fixNullString(editProfileFragmentBinding.nameId.getText().toString()));
        if (editProfileFragmentBinding.phoneId.getText().toString().isEmpty()) {
            userRegisteration.setPhone("null");
        } else {
            userRegisteration.setPhone(Utility.fixNullString(editProfileFragmentBinding.phoneId.getText().toString()));
        }
        userRegisteration.setEmail(Utility.fixNullString(editProfileFragmentBinding.emailId.getText().toString()));
        userRegisteration.setEmail_confirmation(Utility.fixNullString(editProfileFragmentBinding.confirmEmailId.getText().toString()));
        apiInterface.userEditProfile("Bearer" + GoldenSharedPreference.getToken(context), userRegisteration).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    editProfileFragmentBinding.progress.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.successfulyupdate, Toast.LENGTH_SHORT).show();
                    userProfile();
                } else {
                    editProfileFragmentBinding.progress.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                editProfileFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private boolean inputValid() {
        return nameValid() && emailValid() && emailsIsValid();
    }

    private boolean nameValid() {
        String name = editProfileFragmentBinding.nameId.getText().toString();
        if (!name.isEmpty()) {
            return true;
        } else {
            editProfileFragmentBinding.nameId.setError(context.getResources().getString(R.string.enterurname));
            editProfileFragmentBinding.nameId.requestFocus();
            return false;
        }
    }

    private boolean emailsIsValid() {
        String firstEmail = editProfileFragmentBinding.emailId.getText().toString();
        String confirmEmail = editProfileFragmentBinding.confirmEmailId.getText().toString();
        if (!firstEmail.equals(confirmEmail)) {
            editProfileFragmentBinding.confirmEmailId.setError(context.getResources().getString(R.string.emailsdoesntmatch));
            editProfileFragmentBinding.confirmEmailId.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean phoneValid() {
        String name = editProfileFragmentBinding.phoneId.getText().toString();
        if (!name.isEmpty()) {
            return true;
        } else {
            editProfileFragmentBinding.phoneId.setError(context.getResources().getString(R.string.enterphone));
            editProfileFragmentBinding.phoneId.requestFocus();
            return false;
        }
    }


    private boolean emailValid() {
        String email = editProfileFragmentBinding.emailId.getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            editProfileFragmentBinding.emailId.setError(context.getResources().getString(R.string.entervalidemail));
            editProfileFragmentBinding.emailId.requestFocus();

            return false;
        }
    }
}