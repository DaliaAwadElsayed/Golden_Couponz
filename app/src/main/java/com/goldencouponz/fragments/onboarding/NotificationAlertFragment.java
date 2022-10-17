package com.goldencouponz.fragments.onboarding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.e.goldencouponz.databinding.NotificationAlertFragmentBinding;
import com.goldencouponz.activities.MainActivity;
import com.goldencouponz.viewModles.onboarding.NotificationAlertViewModel;

public class NotificationAlertFragment extends Fragment {

    private NotificationAlertViewModel mViewModel;
    NotificationAlertFragmentBinding notificationAlertFragmentBinding;

    public static NotificationAlertFragment newInstance() {
        return new NotificationAlertFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        notificationAlertFragmentBinding = NotificationAlertFragmentBinding.inflate(inflater, container, false);
        return notificationAlertFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NotificationAlertViewModel.class);
        mViewModel.init(notificationAlertFragmentBinding, getContext());
        notificationAlertFragmentBinding.notNowId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewActivity();
            }
        });

    }

    private void moveToNewActivity() {
        Bundle extras = getActivity().getIntent().getExtras();
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.putExtra("language", extras.getString("language"));
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
//        getActivity().finish();
    }
}