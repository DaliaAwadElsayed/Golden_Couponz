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

import com.e.goldencouponz.databinding.WhatsAppFragmentBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.aboutgolden.WhatsAppViewModel;

public class WhatsAppFragment extends Fragment {

    private WhatsAppViewModel mViewModel;
    WhatsAppFragmentBinding whatsAppFragmentBinding;

    public static WhatsAppFragment newInstance() {
        return new WhatsAppFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        whatsAppFragmentBinding = WhatsAppFragmentBinding.inflate(inflater, container, false);
        return whatsAppFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WhatsAppViewModel.class);
        mViewModel.init(whatsAppFragmentBinding, getContext());
        int release = Integer.parseInt(Build.VERSION.RELEASE);
        if (release >= 11) {
            ((MainActivity) getActivity()).showBottomMenu();
        } else {
            ((MainActivity) getActivity()).showBottomMenu2();
        }
    }

}