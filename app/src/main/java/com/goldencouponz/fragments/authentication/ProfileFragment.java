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

import com.e.goldencouponz.databinding.EditProfileDialogBinding;
import com.e.goldencouponz.databinding.LangDialogBinding;
import com.e.goldencouponz.databinding.LogOutDialogBinding;
import com.e.goldencouponz.databinding.ProfileCountriesDialogBinding;
import com.e.goldencouponz.databinding.ProfileFragmentBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.authentication.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    ProfileFragmentBinding profileFragmentBinding;
    LogOutDialogBinding logOutDialogBinding;
    EditProfileDialogBinding editProfileDialogBinding;
    LangDialogBinding langDialogBinding;
    ProfileCountriesDialogBinding profileCountriesDialogBinding;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        profileFragmentBinding = ProfileFragmentBinding.inflate(inflater, container, false);
        logOutDialogBinding = LogOutDialogBinding.inflate(inflater, container, false);
        editProfileDialogBinding = EditProfileDialogBinding.inflate(inflater, container, false);
        langDialogBinding = LangDialogBinding.inflate(inflater, container, false);
        profileCountriesDialogBinding = ProfileCountriesDialogBinding.inflate(inflater, container, false);

        return profileFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        if (Build.VERSION.RELEASE.equals("12")) {
            ((MainActivity) getActivity()).showBottomMenu();
        } else {
            ((MainActivity) getActivity()).showBottomMenu2();
        }
        mViewModel.init(profileFragmentBinding, logOutDialogBinding, editProfileDialogBinding, langDialogBinding,
                profileCountriesDialogBinding, getContext());

    }

}