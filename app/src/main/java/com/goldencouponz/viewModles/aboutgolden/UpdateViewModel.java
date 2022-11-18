package com.goldencouponz.viewModles.aboutgolden;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.e.goldencouponz.databinding.FragmentUpdateBinding;


public class UpdateViewModel extends ViewModel {
    FragmentUpdateBinding fragmentUpdateBinding;
    Context context;

    public void init(FragmentUpdateBinding fragmentUpdateBinding, Context context) {
        this.context = context;
        this.fragmentUpdateBinding = fragmentUpdateBinding;
        fragmentUpdateBinding.updateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }
    private void update() {
        //https://play.google.com/store/apps/details?id=com.codesroots.goldencoupon
        Uri uri = Uri.parse("market://details?id=" + "com.codesroots.goldencoupon");
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }
}