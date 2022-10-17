package com.goldencouponz.fragments.favourite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.R;
import com.goldencouponz.viewModles.favourite.FavouriteStoresViewModel;

public class FavouriteStoresFragment extends Fragment {

    private FavouriteStoresViewModel mViewModel;

    public static FavouriteStoresFragment newInstance() {
        return new FavouriteStoresFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favourite_stores_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FavouriteStoresViewModel.class);
        // TODO: Use the ViewModel
    }

}