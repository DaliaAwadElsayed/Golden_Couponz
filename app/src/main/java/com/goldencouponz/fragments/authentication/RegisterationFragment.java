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

import com.e.goldencouponz.databinding.CountryDialogBinding;
import com.e.goldencouponz.databinding.RegisterationFragmentBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.authentication.RegisterationViewModel;

public class RegisterationFragment extends Fragment {

    private RegisterationViewModel mViewModel;
RegisterationFragmentBinding registerationFragmentBinding;
CountryDialogBinding countryDialogBinding;
    public static RegisterationFragment newInstance() {
        return new RegisterationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        registerationFragmentBinding=RegisterationFragmentBinding.inflate(inflater,container,false);
        countryDialogBinding=CountryDialogBinding.inflate(inflater,container,false);
        return registerationFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegisterationViewModel.class);
        mViewModel.init(registerationFragmentBinding,countryDialogBinding, getContext());
        int release = Integer.parseInt(Build.VERSION.RELEASE);
        if (release >= 11) {
            ((MainActivity) getActivity()).hideBottomMenu();
        }else {
            ((MainActivity) getActivity()).hideBottomMenu2();
        }       }

}