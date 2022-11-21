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

import com.e.goldencouponz.databinding.LoginCheckDialogBinding;
import com.e.goldencouponz.databinding.NoCouponProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.ProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.SearchFragmentBinding;
import com.e.goldencouponz.databinding.SecondProductDetailsDialogBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.aboutgolden.SearchViewModel;

public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;
    SearchFragmentBinding searchFragmentBinding;
    LoginCheckDialogBinding loginCheckDialogBinding;
    ProductDetailsDialogBinding productDetailsDialogBinding;
    SecondProductDetailsDialogBinding secondProductDetailsDialogBinding;
    NoCouponProductDetailsDialogBinding noCouponProductDetailsDialogBinding;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        searchFragmentBinding = SearchFragmentBinding.inflate(inflater, container, false);
        loginCheckDialogBinding = LoginCheckDialogBinding.inflate(inflater, container, false);
        productDetailsDialogBinding = ProductDetailsDialogBinding.inflate(inflater, container, false);
        secondProductDetailsDialogBinding = SecondProductDetailsDialogBinding.inflate(inflater, container, false);
        noCouponProductDetailsDialogBinding = NoCouponProductDetailsDialogBinding.inflate(inflater, container, false);
        return searchFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        if (Build.VERSION.RELEASE.equals("12")) {
            ((MainActivity) getActivity()).hideBottomMenu();
        }else {
            ((MainActivity) getActivity()).hideBottomMenu2();
        }
        mViewModel.init(searchFragmentBinding,loginCheckDialogBinding,  productDetailsDialogBinding,
                 secondProductDetailsDialogBinding,
                 noCouponProductDetailsDialogBinding,
                getContext()
                );
    }

}