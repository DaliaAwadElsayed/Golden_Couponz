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

import com.e.goldencouponz.databinding.EditProfileFragmentBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.authentication.EditProfileViewModel;

public class EditProfileFragment extends Fragment {

    private EditProfileViewModel mViewModel;
    EditProfileFragmentBinding editProfileFragmentBinding;

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        editProfileFragmentBinding = EditProfileFragmentBinding.inflate(inflater, container, false);
        return editProfileFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);
        mViewModel.init(editProfileFragmentBinding, getContext());
        String part = (Build.VERSION.RELEASE);
        String[] parts = part.split("\\.");
        int release = Integer.parseInt(parts[0]); // 004
        Log.i("RELEASEEE", release + "?");
        if (release >= 11) {

            ((MainActivity) getActivity()).hideBottomMenu();
        } else {
            ((MainActivity) getActivity()).hideBottomMenu2();
        }
    }

}