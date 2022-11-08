package com.goldencouponz.viewModles.favourite;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.FavouriteStoresFragmentBinding;
import com.goldencouponz.adapters.favourite.FavStoresGridAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.home.Store;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteStoresViewModel extends ViewModel {
    FavouriteStoresFragmentBinding favouriteStoresFragmentBinding;
    Context context;
    FavStoresGridAdapter storesGrideAdapter;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    Store store;

    public void init(FavouriteStoresFragmentBinding favouriteStoresFragmentBinding, Context context) {
        this.context = context;
        this.favouriteStoresFragmentBinding = favouriteStoresFragmentBinding;
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("en")) {
            stores("Bearer" + GoldenSharedPreference.getToken(context), "en", 0);
        } else if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
            stores("Bearer" + GoldenSharedPreference.getToken(context), "ar", 0);
        }
        favouriteStoresFragmentBinding.nextId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CLICKSS", "YES" + store);
                favInterestedStores();
            }
        });
    }

    private void stores(String token, String lang, int categoryId) {
        favouriteStoresFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getStore(token, lang, "deviceToken", categoryId).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        if (!response.body().getStores().isEmpty()) {
                            favouriteStoresFragmentBinding.progress.setVisibility(View.GONE);
                            storesGrideAdapter = new FavStoresGridAdapter(context);
                            storesGrideAdapter.setStores(response.body().getStores());
                            favouriteStoresFragmentBinding.homeGrideRecyclerView.setAdapter(storesGrideAdapter);
                            storesGrideAdapter.setOnItemClickListener(new FavStoresGridAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View viewItem, int position, ArrayList<Integer> selectedStores) {
                                    Log.i("selectedStores", selectedStores.toString() + "?");
                                    store = new Store(selectedStores);

                                }
                            });
                        } else {
                            Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                            favouriteStoresFragmentBinding.progress.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
                favouriteStoresFragmentBinding.progress.setVisibility(View.GONE);
            }
        });
    }

    private void favInterestedStores() {
        apiInterface.favMultiStore("Bearer" + GoldenSharedPreference.getToken(context), GoldenNoLoginSharedPreference.getUserLanguage(context), store).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        Navigation.findNavController(favouriteStoresFragmentBinding.getRoot()).navigate(R.id.homeFragment);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

}