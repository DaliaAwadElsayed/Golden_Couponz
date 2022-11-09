package com.goldencouponz.viewModles.aboutgolden;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.WhatsAppFragmentBinding;
import com.goldencouponz.adapters.home.WhatsAppAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.models.wrapper.WhatsApp;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WhatsAppViewModel extends ViewModel {
    WhatsAppFragmentBinding whatsAppFragmentBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    WhatsAppAdapter whatsAppAdapter;

    public void init(WhatsAppFragmentBinding whatsAppFragmentBinding, Context context) {
        this.context = context;
        this.whatsAppFragmentBinding = whatsAppFragmentBinding;
        whatsApp();
    }

    private void whatsApp() {
        whatsAppFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getWhatsApp(GoldenNoLoginSharedPreference.getUserLanguage(context)).enqueue(new Callback<WhatsApp>() {
            @Override
            public void onResponse(Call<WhatsApp> call, Response<WhatsApp> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        if (!response.body().getMessages().isEmpty()) {
                            whatsAppFragmentBinding.progress.setVisibility(View.GONE);
                            whatsAppAdapter = new WhatsAppAdapter(context);
                            whatsAppAdapter.setStores(response.body().getMessages());
                            whatsAppFragmentBinding.couponzRecyclerId.setAdapter(whatsAppAdapter);

                        } else {
                            Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                            whatsAppFragmentBinding.progress.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<WhatsApp> call, Throwable t) {
                Log.i("onFailure", t.toString());
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
                whatsAppFragmentBinding.progress.setVisibility(View.GONE);
            }
        });
    }

}