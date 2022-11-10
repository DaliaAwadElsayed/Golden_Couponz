package com.goldencouponz.fragments.aboutgolden;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.databinding.CopyCouponDialogBinding;
import com.e.goldencouponz.databinding.NoCouponProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.NotificationFragmentBinding;
import com.e.goldencouponz.databinding.ProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.SecondCopyCouponDialogBinding;
import com.e.goldencouponz.databinding.SecondProductDetailsDialogBinding;
import com.goldencouponz.viewModles.aboutgolden.NotificationViewModel;

public class NotificationFragment extends Fragment {

    private NotificationViewModel mViewModel;
    NotificationFragmentBinding notificationFragmentBinding;
    CopyCouponDialogBinding copyCouponDialogBinding;
    SecondCopyCouponDialogBinding secondCopyCouponDialogBinding;
    ProductDetailsDialogBinding productDetailsDialogBinding;
    SecondProductDetailsDialogBinding secondProductDetailsDialogBinding;
    NoCouponProductDetailsDialogBinding noCouponProductDetailsDialogBinding;
    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        notificationFragmentBinding = NotificationFragmentBinding.inflate(inflater, container, false);
        copyCouponDialogBinding = CopyCouponDialogBinding.inflate(inflater, container, false);
        secondCopyCouponDialogBinding = SecondCopyCouponDialogBinding.inflate(inflater, container, false);
        productDetailsDialogBinding = ProductDetailsDialogBinding.inflate(inflater, container, false);
        secondProductDetailsDialogBinding = SecondProductDetailsDialogBinding.inflate(inflater, container, false);
        noCouponProductDetailsDialogBinding = NoCouponProductDetailsDialogBinding.inflate(inflater, container, false);
        return notificationFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        mViewModel.init(notificationFragmentBinding, copyCouponDialogBinding, secondCopyCouponDialogBinding, productDetailsDialogBinding
                , secondProductDetailsDialogBinding,
                noCouponProductDetailsDialogBinding, getContext());
    }

}