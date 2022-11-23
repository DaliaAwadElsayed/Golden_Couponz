package com.goldencouponz.fragments.favourite;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.databinding.FavouriteStoresFragmentBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.favourite.FavouriteStoresViewModel;

public class FavouriteStoresFragment extends Fragment {

    private FavouriteStoresViewModel mViewModel;
    FavouriteStoresFragmentBinding favouriteStoresFragmentBinding;

    public static FavouriteStoresFragment newInstance() {
        return new FavouriteStoresFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        favouriteStoresFragmentBinding = FavouriteStoresFragmentBinding.inflate(inflater, container, false);
        return favouriteStoresFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FavouriteStoresViewModel.class);
        mViewModel.init(favouriteStoresFragmentBinding,getContext());
        int release = Integer.parseInt(Build.VERSION.RELEASE);
        if (release >= 11) { ((MainActivity) getActivity()).hideBottomMenu();
        }else {
            ((MainActivity) getActivity()).hideBottomMenu2();
        }
    }

}