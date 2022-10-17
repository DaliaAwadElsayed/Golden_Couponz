package com.goldencouponz.viewModles.onboarding;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.e.goldencouponz.databinding.NotificationAlertFragmentBinding;

public class NotificationAlertViewModel extends ViewModel {
    NotificationAlertFragmentBinding notificationAlertFragmentBinding;
    Context context;

    public void init(NotificationAlertFragmentBinding notificationAlertFragmentBinding, Context context) {
        this.context = context;
        this.notificationAlertFragmentBinding = notificationAlertFragmentBinding;
    }

}