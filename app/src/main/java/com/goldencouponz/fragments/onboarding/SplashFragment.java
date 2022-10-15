package com.goldencouponz.fragments.onboarding;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.SplashFragmentBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.onboarding.SplashViewModel;

public class SplashFragment extends Fragment {

    private SplashViewModel mViewModel;
    private SplashFragmentBinding splashFragmentBinding;
    Activity activity;

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        splashFragmentBinding = SplashFragmentBinding.inflate(inflater, container, false);

        return splashFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).hideToolbar();
        ((MainActivity) getActivity()).hideBottomMenu();
        if (!((MainActivity) getActivity()).isInternetAvailable()) {
            Toast.makeText(getContext(), R.string.no_internet_message, Toast.LENGTH_SHORT).show();
        }

        mViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        mViewModel.init(splashFragmentBinding, getContext(),getActivity());

    }

}
