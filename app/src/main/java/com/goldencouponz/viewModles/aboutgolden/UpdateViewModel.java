package com.goldencouponz.viewModles.aboutgolden;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.e.goldencouponz.databinding.FragmentUpdateBinding;


public class UpdateViewModel extends ViewModel {
    FragmentUpdateBinding fragmentUpdateBinding;
    Context context;

    public void init(FragmentUpdateBinding fragmentUpdateBinding, Context context) {
        this.context = context;
        this.fragmentUpdateBinding = fragmentUpdateBinding;
    }
}