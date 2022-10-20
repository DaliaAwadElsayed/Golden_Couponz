package com.goldencouponz.fragments.authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.databinding.RegisterationFragmentBinding;
import com.goldencouponz.viewModles.authentication.RegisterationViewModel;

public class RegisterationFragment extends Fragment {

    private RegisterationViewModel mViewModel;
RegisterationFragmentBinding registerationFragmentBinding;
    public static RegisterationFragment newInstance() {
        return new RegisterationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        registerationFragmentBinding=RegisterationFragmentBinding.inflate(inflater,container,false);
        return registerationFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegisterationViewModel.class);
        mViewModel.init(registerationFragmentBinding, getContext());    }

}