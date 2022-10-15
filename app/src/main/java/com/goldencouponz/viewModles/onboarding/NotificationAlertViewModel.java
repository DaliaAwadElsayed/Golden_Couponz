package com.goldencouponz.viewModles.onboarding;

import android.content.Context;
import android.view.View;

import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModel;

import com.e.goldencouponz.databinding.NotificationAlertFragmentBinding;

public class NotificationAlertViewModel extends ViewModel {
    NotificationAlertFragmentBinding notificationAlertFragmentBinding;
    Context context;

    public void init(NotificationAlertFragmentBinding notificationAlertFragmentBinding, Context context) {
        this.context = context;
        this.notificationAlertFragmentBinding = notificationAlertFragmentBinding;
        notificationAlertFragmentBinding.allowAlertId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {

                } else {

                }

            }
        });

    }
}