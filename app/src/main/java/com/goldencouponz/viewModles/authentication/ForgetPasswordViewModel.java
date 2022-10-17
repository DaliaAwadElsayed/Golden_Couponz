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

    public void init(ForgetPasswordFragmentBinding forgetPasswordFragmentBinding, Context context) {
        this.context = context;
        this.forgetPasswordFragmentBinding = forgetPasswordFragmentBinding;
        forgetPasswordFragmentBinding.toolBarId.setClickable(true);
        forgetPasswordFragmentBinding.backId.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.loginFragment));

    }

    private void resetPassword() {

    }
}