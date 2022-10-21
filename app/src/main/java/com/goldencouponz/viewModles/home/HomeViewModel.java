package com.goldencouponz.viewModles.home;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.HomeFragmentBinding;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;

public class HomeViewModel extends ViewModel {
    HomeFragmentBinding homeFragmentBinding;
    Context context;

    public void init(HomeFragmentBinding homeFragmentBinding, Context context) {
        this.context = context;
        this.homeFragmentBinding = homeFragmentBinding;
        if (!GoldenSharedPreference.isLoggedIn(context)&&!GoldenSharedPreference.getUserShowLogin(context).equals("close")) {

            Navigation.findNavController(homeFragmentBinding.getRoot()).navigate(R.id.loginFragment);
        }
    }
}