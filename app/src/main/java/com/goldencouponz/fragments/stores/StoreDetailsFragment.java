package com.goldencouponz.fragments.stores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.R;
import com.goldencouponz.viewModles.StoreDetailsViewModel;

public class StoreDetailsFragment extends Fragment {

    private StoreDetailsViewModel mViewModel;

    public static StoreDetailsFragment newInstance() {
        return new StoreDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.store_details_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StoreDetailsViewModel.class);
        // TODO: Use the ViewModel
    }

}