package com.goldencouponz.viewModles.authentication;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.e.goldencouponz.databinding.ForgetPasswordFragmentBinding;

public class ForgetPasswordViewModel extends ViewModel {
    ForgetPasswordFragmentBinding forgetPasswordFragmentBinding;
    Context context;

    public void init(ForgetPasswordFragmentBinding forgetPasswordFragmentBinding, Context context) {
        this.context = context;
        this.forgetPasswordFragmentBinding = forgetPasswordFragmentBinding;
    }
}