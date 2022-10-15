package com.goldencouponz.fragments.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.databinding.FirstSkipFragmentBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.onboarding.FirstSkipViewModel;

public class FirstSkipFragment extends Fragment {

    private FirstSkipViewModel mViewModel;
    FirstSkipFragmentBinding firstSkipFragmentBinding;

    public static FirstSkipFragment newInstance() {
        return new FirstSkipFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        firstSkipFragmentBinding = FirstSkipFragmentBinding.inflate(inflater, container, false);
        return firstSkipFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FirstSkipViewModel.class);
        ((MainActivity) getActivity()).hideToolbar();
        ((MainActivity) getActivity()).hideBottomMenu();
        mViewModel.init(firstSkipFragmentBinding, getContext());
    }

}