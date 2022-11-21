package com.goldencouponz.fragments.authentication;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.databinding.ChangePasswordFragmentBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.authentication.ChangePasswordViewModel;

public class ChangePasswordFragment extends Fragment {

    private ChangePasswordViewModel mViewModel;
    ChangePasswordFragmentBinding changePasswordFragmentBinding;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        changePasswordFragmentBinding = ChangePasswordFragmentBinding.inflate(inflater, container, false);
        return changePasswordFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChangePasswordViewModel.class);
        mViewModel.init(changePasswordFragmentBinding, getContext());
        if (Build.VERSION.RELEASE.equals("12")) {
            ((MainActivity) getActivity()).showBottomMenu();
        }else {
            ((MainActivity) getActivity()).showBottomMenu2();
        }
    }

}