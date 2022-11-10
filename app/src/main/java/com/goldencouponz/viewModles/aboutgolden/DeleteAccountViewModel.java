package com.goldencouponz.viewModles.aboutgolden;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.DeleteAccountFragmentBinding;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteAccountViewModel extends ViewModel {
    DeleteAccountFragmentBinding deleteAccountFragmentBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();

    public void init(DeleteAccountFragmentBinding deleteAccountFragmentBinding, Context context) {
        this.context = context;
        this.deleteAccountFragmentBinding = deleteAccountFragmentBinding;
        deleteAccountFragmentBinding.deleteId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAcc();
            }
        });
        deleteAccountFragmentBinding.backId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.profileFragment);
            }
        });
    }

    private void deleteAcc() {
        deleteAccountFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.deleteAccount("Bearer" + GoldenSharedPreference.getToken(context)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() && response.body() != null) {
                        GoldenSharedPreference.clearSharedPreference(context);
                        Navigation.findNavController(deleteAccountFragmentBinding.getRoot()).navigate(R.id.loginFragment);
                        Toast.makeText(context, R.string.account_deleted, Toast.LENGTH_SHORT).show();
                        deleteAccountFragmentBinding.progress.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                        deleteAccountFragmentBinding.progress.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                    deleteAccountFragmentBinding.progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                deleteAccountFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}