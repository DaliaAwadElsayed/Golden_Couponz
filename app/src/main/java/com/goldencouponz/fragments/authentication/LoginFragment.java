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

import com.e.goldencouponz.databinding.LoginFragmentBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.authentication.LoginViewModel;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    LoginFragmentBinding loginFragmentBinding;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        loginFragmentBinding = LoginFragmentBinding.inflate(inflater, container, false);
        return loginFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        String part = (Build.VERSION.RELEASE);
        String[] parts = part.split("\\.");
        int release = Integer.parseInt(parts[0]); // 004
        Log.i("RELEASEEE", release + "?");
        if (release >= 11) {

            ((MainActivity) getActivity()).hideBottomMenu();
        }else {
            ((MainActivity) getActivity()).hideBottomMenu2();
        }           mViewModel.init(loginFragmentBinding, getContext(), getActivity());
        loginFragmentBinding.googleId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).signIn();

            }
        });
        loginFragmentBinding.faceBookId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).faceBookLogin();

            }
        });
    }
}