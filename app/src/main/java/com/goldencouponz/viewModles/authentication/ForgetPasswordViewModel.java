package com.goldencouponz.viewModles.authentication;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.ForgetPasswordFragmentBinding;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.RetrofitClient;

public class ForgetPasswordViewModel extends ViewModel {
    ForgetPasswordFragmentBinding forgetPasswordFragmentBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    String login;

    public void init(ForgetPasswordFragmentBinding forgetPasswordFragmentBinding, Context context, String login) {
        this.context = context;
        this.forgetPasswordFragmentBinding = forgetPasswordFragmentBinding;
        this.login = login;
        forgetPasswordFragmentBinding.toolBarId.setClickable(true);
        forgetPasswordFragmentBinding.backId.setOnClickListener(view -> {
            if (login.equals("yes")) {
                Navigation.findNavController(view).navigate(R.id.profileFragment);
            } else {
                Navigation.findNavController(view).navigate(R.id.loginFragment);

            }
        });

    }

    private void resetPassword() {

    }
}