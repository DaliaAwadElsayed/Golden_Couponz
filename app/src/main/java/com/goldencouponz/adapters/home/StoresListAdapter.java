package com.goldencouponz.adapters.home;


import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.StoreListItemBinding;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.home.Store;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.Utility;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;
import com.goldencouponz.viewModles.home.HomeViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoresListAdapter extends RecyclerView.Adapter<StoresListAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<Store> store = new ArrayList<>();
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    HomeViewModel.Listener listener;

    public StoresListAdapter(Context context, HomeViewModel.Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new HomePageViewHolder(StoreListItemBinding.inflate(inflater, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageViewHolder holder, int position) {
        holder.bindRestaurant(store.get(position));
    }

    @Override
    public int getItemCount() {
        return store == null ? 0 : store.size();
    }

    public void setStores(List<Store> store) {
        this.store.addAll(store);
        notifyDataSetChanged();
    }

    class HomePageViewHolder extends RecyclerView.ViewHolder {
        private StoreListItemBinding storeGrideItemBinding;
        HomeViewModel.Listener listener;

        HomePageViewHolder(@NonNull StoreListItemBinding storeGrideItemBinding, HomeViewModel.Listener listener) {
            super(storeGrideItemBinding.getRoot());
            this.storeGrideItemBinding = storeGrideItemBinding;
            this.listener = listener;
        }

        private void bindRestaurant(Store store) {
            if (GoldenSharedPreference.isLoggedIn(context)) {
                if (store.getIsFavorite() == 0) {
                    storeGrideItemBinding.favId.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_fav));
                    storeGrideItemBinding.favId.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.black)));
                } else {
                    storeGrideItemBinding.favId.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_fav));
                    storeGrideItemBinding.favId.setImageTintList(null);
                }
            }
            storeGrideItemBinding.favId.setOnClickListener(v -> {
                if (GoldenSharedPreference.isLoggedIn(context)) {

                    if (store.getIsFavorite() == 0) {
                        storeFav();
                    } else {
                        removeFav();
                    }
                } else {
                    listener.click(2);
                }
            });

            storeGrideItemBinding.storeNameId.setText(Utility.fixNullString(String.valueOf(store.getTitle())));
            storeGrideItemBinding.couponCountId.setText(Utility.fixNullString(String.valueOf(store.getStoreCouponsCount())));
            Picasso.get().load(store.getFile()).into(storeGrideItemBinding.storeImgId);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("storeId", store.getId());
                    Navigation.findNavController(v).navigate(R.id.storeDetailsFragment, bundle);
                }
            });

        }

        private void storeFav() {
            String lang = GoldenNoLoginSharedPreference.getUserLanguage(context);
            String token = GoldenSharedPreference.getToken(context);
            apiInterface.userMakeFav("Bearer" + token, lang, String.valueOf(store.get(getAdapterPosition()).getId())).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200 && response.body() != null) {
                            storeGrideItemBinding.favId.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_fav));
                            storeGrideItemBinding.favId.setImageTintList(null);
                            listener.click(1);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Log.i("onFailure", t.toString());
                    Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();

                }
            });
        }

        private void removeFav() {
            String lang = GoldenNoLoginSharedPreference.getUserLanguage(context);
            String token = GoldenSharedPreference.getToken(context);
            apiInterface.userRemoveFav("Bearer" + token, lang, String.valueOf(store.get(getAdapterPosition()).getId())).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200 && response.body() != null) {
                            storeGrideItemBinding.favId.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_fav));
                            storeGrideItemBinding.favId.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.black)));
                            listener.click(1);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Log.i("onFailure", t.toString());
                    Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();

                }
            });
        }

    }
}

