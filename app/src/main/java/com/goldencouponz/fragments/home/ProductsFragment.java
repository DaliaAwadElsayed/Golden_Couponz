package com.goldencouponz.fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.databinding.NoCouponProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.ProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.ProductsFragmentBinding;
import com.e.goldencouponz.databinding.SecondProductDetailsDialogBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.home.ProductsViewModel;

public class ProductsFragment extends Fragment {

    private ProductsViewModel mViewModel;
    ProductsFragmentBinding productsFragmentBinding;
    ProductDetailsDialogBinding productDetailsDialogBinding;
    SecondProductDetailsDialogBinding secondProductDetailsDialogBinding;
    NoCouponProductDetailsDialogBinding noCouponProductDetailsDialogBinding;

    public static ProductsFragment newInstance() {
        return new ProductsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        productsFragmentBinding = ProductsFragmentBinding.inflate(inflater, container, false);
        productDetailsDialogBinding = ProductDetailsDialogBinding.inflate(inflater, container, false);
        secondProductDetailsDialogBinding = SecondProductDetailsDialogBinding.inflate(inflater, container, false);
        noCouponProductDetailsDialogBinding = NoCouponProductDetailsDialogBinding.inflate(inflater, container, false);
        return productsFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProductsViewModel.class);
        ((MainActivity) getActivity()).showBottomMenu();
        mViewModel.init(productsFragmentBinding, productDetailsDialogBinding, secondProductDetailsDialogBinding, noCouponProductDetailsDialogBinding, getContext());
    }

}