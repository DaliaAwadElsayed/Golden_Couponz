package com.goldencouponz.fragments.authentication;

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

import com.e.goldencouponz.databinding.ForgetPasswordFragmentBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.authentication.ForgetPasswordViewModel;

public class ForgetPasswordFragment extends Fragment {

    private ForgetPasswordViewModel mViewModel;
    ForgetPasswordFragmentBinding forgetPasswordFragmentBinding;

    public static ForgetPasswordFragment newInstance() {
        return new ForgetPasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        forgetPasswordFragmentBinding = ForgetPasswordFragmentBinding.inflate(inflater, container, false);
        return forgetPasswordFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ForgetPasswordViewModel.class);
        mViewModel.init(forgetPasswordFragmentBinding, getContext(),getArguments().getString("login"));
        String part = (Build.VERSION.RELEASE);
        String[] parts = part.split("\\.");
        int release = Integer.parseInt(parts[0]); // 004
        Log.i("RELEASEEE", release + "?");
        if (release >= 11) {

            ((MainActivity) getActivity()).hideBottomMenu();
        }else {
            ((MainActivity) getActivity()).hideBottomMenu2();
        }
    }

}