package com.goldencouponz.fragments.authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.RegisterationFragmentBinding;
import com.goldencouponz.activities.MainActivity;

public class RegisterationFragment extends Fragment {

    private RegisterationViewModel mViewModel;
RegisterationFragmentBinding registerationFragmentBinding;
    public static RegisterationFragment newInstance() {
        return new RegisterationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.registeration_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegisterationViewModel.class);
        ((MainActivity) getActivity()).showToolbar();
        ((MainActivity) getActivity()).hideBottomMenu();
        mViewModel.init(registerationFragmentBinding, getContext());    }

}