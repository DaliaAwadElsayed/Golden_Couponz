package com.goldencouponz.fragments.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.databinding.HomeFragmentBinding;
import com.e.goldencouponz.databinding.LoginCheckDialogBinding;
import com.e.goldencouponz.databinding.SeeAllDialogBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.home.HomeViewModel;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    HomeFragmentBinding homeFragmentBinding;
    LoginCheckDialogBinding loginCheckDialogBinding;
    SeeAllDialogBinding seeAllDialogBinding;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        homeFragmentBinding = HomeFragmentBinding.inflate(inflater, container, false);
        loginCheckDialogBinding = LoginCheckDialogBinding.inflate(inflater, container, false);
        seeAllDialogBinding = SeeAllDialogBinding.inflate(inflater, container, false);
        return homeFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        String part = (Build.VERSION.RELEASE);
        String[] parts = part.split("\\.");
        int release = Integer.parseInt(parts[0]); // 004
        Log.i("RELEASEEE", release + "?");
        if (release >= 11) {

            ((MainActivity) getActivity()).showBottomMenu();
        }else {
            ((MainActivity) getActivity()).showBottomMenu2();
        }
        mViewModel.init(homeFragmentBinding, seeAllDialogBinding, loginCheckDialogBinding, getContext());
    }

}