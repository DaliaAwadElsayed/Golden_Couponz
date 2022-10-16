package com.goldencouponz.fragments.authentication;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.e.goldencouponz.databinding.RegisterationFragmentBinding;

public class RegisterationViewModel extends ViewModel {
    RegisterationFragmentBinding registerationFragmentBinding;
    Context context;

    public void init(RegisterationFragmentBinding registerationFragmentBinding, Context context) {
        this.context = context;
        this.registerationFragmentBinding = registerationFragmentBinding;
    }
}