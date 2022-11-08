package com.goldencouponz.fragments.aboutgolden;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.databinding.FragmentUpdateBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.aboutgolden.UpdateViewModel;

public class UpdateFragment extends Fragment {

    private UpdateViewModel mViewModel;
    FragmentUpdateBinding fragmentUpdateBinding;

    public static UpdateFragment newInstance() {
        return new UpdateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentUpdateBinding = FragmentUpdateBinding.inflate(inflater, container, false);
        return fragmentUpdateBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UpdateViewModel.class);
        ((MainActivity) getActivity()).showBottomMenu();
        mViewModel.init(fragmentUpdateBinding,getContext());
    }

}