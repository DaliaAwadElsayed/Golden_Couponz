package com.goldencouponz.fragments.stores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.databinding.CopyCouponDialogBinding;
import com.e.goldencouponz.databinding.LoginCheckDialogBinding;
import com.e.goldencouponz.databinding.ProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.SecondCopyCouponDialogBinding;
import com.e.goldencouponz.databinding.SecondProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.StoreDetailsFragmentBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.stores.StoreDetailsViewModel;

public class StoreDetailsFragment extends Fragment {

    private StoreDetailsViewModel mViewModel;
    StoreDetailsFragmentBinding storeDetailsFragmentBinding;
    CopyCouponDialogBinding copyCouponDialogBinding;
    SecondCopyCouponDialogBinding secondCopyCouponDialogBinding;
    ProductDetailsDialogBinding productDetailsDialogBinding;
    LoginCheckDialogBinding loginCheckDialogBinding;
    SecondProductDetailsDialogBinding secondProductDetailsDialogBinding;

    public static StoreDetailsFragment newInstance() {
        return new StoreDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        storeDetailsFragmentBinding = StoreDetailsFragmentBinding.inflate(inflater, container, false);

        copyCouponDialogBinding = CopyCouponDialogBinding.inflate(inflater, container, false);
        secondCopyCouponDialogBinding = SecondCopyCouponDialogBinding.inflate(inflater, container, false);
        productDetailsDialogBinding = ProductDetailsDialogBinding.inflate(inflater, container, false);
        secondProductDetailsDialogBinding = SecondProductDetailsDialogBinding.inflate(inflater, container, false);
        loginCheckDialogBinding = LoginCheckDialogBinding.inflate(inflater, container, false);
        return storeDetailsFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StoreDetailsViewModel.class);
        ((MainActivity) getActivity()).hideBottomMenu();
        mViewModel.init(storeDetailsFragmentBinding, copyCouponDialogBinding, secondCopyCouponDialogBinding, productDetailsDialogBinding
                ,secondProductDetailsDialogBinding , loginCheckDialogBinding, getContext(), getArguments().getInt("storeId"));
    }

}