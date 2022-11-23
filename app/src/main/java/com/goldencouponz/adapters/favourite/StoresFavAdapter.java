package com.goldencouponz.adapters.favourite;


import android.content.Context;
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
import com.goldencouponz.viewModles.favourite.FavouriteViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoresFavAdapter extends RecyclerView.Adapter<StoresFavAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<Store> store = new ArrayList<>();
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    FavouriteViewModel.Listener listener;

    public StoresFavAdapter(Context context, FavouriteViewModel.Listener listener) {
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
        holder.bindStore(store.get(position));
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
        private StoreListItemBinding storeListItemBinding;
        FavouriteViewModel.Listener listener;

        HomePageViewHolder(@NonNull StoreListItemBinding storeListItemBinding, FavouriteViewModel.Listener listener) {
            super(storeListItemBinding.getRoot());
            this.storeListItemBinding = storeListItemBinding;
            this.listener = listener;
        }

        private void bindStore(Store store) {
            storeListItemBinding.favId.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_fav));
            storeListItemBinding.favId.setImageTintList(null);
            storeListItemBinding.favId.setOnClickListener(v -> {
                removeFav();
                    });

            storeListItemBinding.storeNameId.setText(Utility.fixNullString(String.valueOf(store.getTitle())));
            storeListItemBinding.couponCountId.setText(Utility.fixNullString(String.valueOf(store.getStoreCouponsCount())));
            Picasso.get().load(store.getFile()).into(storeListItemBinding.storeImgId);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("storeId", store.getId());
                    bundle.putString("type","no");
                    Navigation.findNavController(v).navigate(R.id.storeDetailsFragment, bundle);
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
                               listener.click(1,getAdapterPosition());
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

