package com.goldencouponz.fragments.favourite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.databinding.FragmentFavouriteBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.favourite.FavouriteViewModel;

public class FavouriteFragment extends Fragment {

    private FavouriteViewModel mViewModel;
    FragmentFavouriteBinding fragmentFavouriteBinding;

    public static FavouriteFragment newInstance() {
        return new FavouriteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentFavouriteBinding = FragmentFavouriteBinding.inflate(inflater, container, false);
        return fragmentFavouriteBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);
        ((MainActivity) getActivity()).showBottomMenu();
        mViewModel.init(fragmentFavouriteBinding,getContext());
    }

}