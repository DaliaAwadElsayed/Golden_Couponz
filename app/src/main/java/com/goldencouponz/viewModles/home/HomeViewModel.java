package com.goldencouponz.viewModles.home;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.HomeFragmentBinding;
import com.goldencouponz.utility.GoldenSharedPreference;

public class HomeViewModel extends ViewModel {
    HomeFragmentBinding homeFragmentBinding;
    Context context;

    public void init(HomeFragmentBinding homeFragmentBinding, Context context) {
        this.context = context;
        this.homeFragmentBinding = homeFragmentBinding;
        if (GoldenSharedPreference.getLOGIN(context) == 1&&!GoldenSharedPreference.getUserShowLogin(context).equals("close")) {
            Navigation.findNavController(homeFragmentBinding.getRoot()).navigate(R.id.loginFragment);
        }
    }
}