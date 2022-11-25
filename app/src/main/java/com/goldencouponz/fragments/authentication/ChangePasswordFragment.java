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
        String part = (Build.VERSION.RELEASE);
        String[] parts = part.split("\\.");
        int release = Integer.parseInt(parts[0]); // 004
        Log.i("RELEASEEE", release + "?");
        if (release >= 11) {

            ((MainActivity) getActivity()).showBottomMenu();
        } else {
            ((MainActivity) getActivity()).showBottomMenu2();
        }
    }

}