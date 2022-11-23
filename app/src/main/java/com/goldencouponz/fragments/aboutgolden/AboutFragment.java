package com.goldencouponz.fragments.aboutgolden;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.databinding.AboutFragmentBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.aboutgolden.AboutViewModel;

public class AboutFragment extends Fragment {

    private AboutViewModel mViewModel;
    AboutFragmentBinding aboutFragmentBinding;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        aboutFragmentBinding = AboutFragmentBinding.inflate(inflater, container, false);
        return aboutFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AboutViewModel.class);
        int release = Integer.parseInt(Build.VERSION.RELEASE);
        if (release >= 11) {
            ((MainActivity) getActivity()).hideBottomMenu();
        }else {
            ((MainActivity) getActivity()).hideBottomMenu2();

        }
        mViewModel.init(getContext(), aboutFragmentBinding,getArguments().getString("type"));
    }

}