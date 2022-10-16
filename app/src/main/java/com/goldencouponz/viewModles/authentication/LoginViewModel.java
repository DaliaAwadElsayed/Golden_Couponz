package com.goldencouponz.viewModles.authentication;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.LoginFragmentBinding;

public class LoginViewModel extends ViewModel {
    LoginFragmentBinding loginFragmentBinding;
    Context context;

    public void init(LoginFragmentBinding loginFragmentBinding, Context context) {
        this.context = context;
        this.loginFragmentBinding = loginFragmentBinding;
        loginFragmentBinding.signUpId.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.registerationFragment));
        loginFragmentBinding.forgetPassId.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.forgetPasswordFragment));

    }
}