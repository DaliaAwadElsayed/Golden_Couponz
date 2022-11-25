package com.goldencouponz.fragments.home;

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

import com.e.goldencouponz.databinding.ArrangeDialogBinding;
import com.e.goldencouponz.databinding.BrandListDialogBinding;
import com.e.goldencouponz.databinding.FiltterDialogBinding;
import com.e.goldencouponz.databinding.NoCouponProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.ProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.ProductsFragmentBinding;
import com.e.goldencouponz.databinding.SecondProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.StoreListDialogBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.home.ProductsViewModel;

public class ProductsFragment extends Fragment {

    private ProductsViewModel mViewModel;
    ProductsFragmentBinding productsFragmentBinding;
    ProductDetailsDialogBinding productDetailsDialogBinding;
    SecondProductDetailsDialogBinding secondProductDetailsDialogBinding;
    NoCouponProductDetailsDialogBinding noCouponProductDetailsDialogBinding;
    ArrangeDialogBinding arrangeDialogBinding;
    BrandListDialogBinding brandListDialogBinding;
    FiltterDialogBinding filtterDialogBinding;
    StoreListDialogBinding storeListDialogBinding;
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
        arrangeDialogBinding = ArrangeDialogBinding.inflate(inflater, container, false);
        brandListDialogBinding = BrandListDialogBinding.inflate(inflater, container, false);
        filtterDialogBinding = FiltterDialogBinding.inflate(inflater, container, false);
        storeListDialogBinding = StoreListDialogBinding.inflate(inflater, container, false);
        return productsFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProductsViewModel.class);
        String part = (Build.VERSION.RELEASE);
        String[] parts = part.split("\\.");
        int release = Integer.parseInt(parts[0]); // 004
        Log.i("RELEASEEE", release + "?");
        if (release >= 11) {

            ((MainActivity) getActivity()).showBottomMenu();
        }else {
            ((MainActivity) getActivity()).showBottomMenu2();
        }           mViewModel.init(productsFragmentBinding, productDetailsDialogBinding,
                secondProductDetailsDialogBinding, noCouponProductDetailsDialogBinding,
                arrangeDialogBinding,brandListDialogBinding,filtterDialogBinding,storeListDialogBinding, getContext());
    }

}