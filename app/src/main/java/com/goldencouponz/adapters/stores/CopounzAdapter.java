package com.goldencouponz.adapters.stores;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.CouponItemBinding;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.store.StoreCoupons;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.Utility;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;
import com.goldencouponz.viewModles.stores.StoreDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CopounzAdapter extends RecyclerView.Adapter<CopounzAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<StoreCoupons> store = new ArrayList<>();
    Context context;
    StoreDetailsViewModel.Listener listener;
    private Api apiInterface = RetrofitClient.getInstance().getApi();

    public CopounzAdapter(Context context, StoreDetailsViewModel.Listener listener) {
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new HomePageViewHolder(CouponItemBinding.inflate(inflater, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageViewHolder holder, int position) {
        holder.bindRestaurant(store.get(position));
    }

    @Override
    public int getItemCount() {
        return store == null ? 0 : store.size();
    }

    public void setStores(List<StoreCoupons> store) {
        this.store.addAll(store);
        notifyDataSetChanged();
    }

    class HomePageViewHolder extends RecyclerView.ViewHolder {
        private CouponItemBinding storeGrideItemBinding;
        StoreDetailsViewModel.Listener listener;

        HomePageViewHolder(@NonNull CouponItemBinding storeGrideItemBinding, StoreDetailsViewModel.Listener listener) {
            super(storeGrideItemBinding.getRoot());
            this.listener = listener;
            this.storeGrideItemBinding = storeGrideItemBinding;
        }

        private void bindRestaurant(StoreCoupons store) {
            storeGrideItemBinding.discountId.setText(Utility.fixNullString(String.valueOf(store.getTitle())));
            storeGrideItemBinding.discountDetailsId.setText(Utility.fixNullString(String.valueOf(store.getDetails())));
            storeGrideItemBinding.hoursId.setText(Utility.fixNullString(String.valueOf(store.getLastUsed())));
            if (store.getCouponType().equals("coupon")) {
                storeGrideItemBinding.copyCouponId.setVisibility(View.VISIBLE);
                storeGrideItemBinding.getOfferId.setVisibility(View.GONE);
                storeGrideItemBinding.numberOfCopiesId.setVisibility(View.VISIBLE);
                storeGrideItemBinding.timeId.setText(Utility.fixNullString((store.getCopies()) + " " + context.getResources().getString(R.string.time)));
            } else {
                storeGrideItemBinding.numberOfCopiesId.setVisibility(View.GONE);
                storeGrideItemBinding.copyCouponId.setVisibility(View.GONE);
                storeGrideItemBinding.getOfferId.setVisibility(View.VISIBLE);
            }
            storeGrideItemBinding.copyCouponId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.click(1, getAdapterPosition());
                }
            });
            if (GoldenSharedPreference.isLoggedIn(context)) {
                if (store.getIsFavorite() == 0) {
                    storeGrideItemBinding.favId.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_fav));
                    storeGrideItemBinding.favId.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.black)));
                } else {
                    storeGrideItemBinding.favId.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_fav));
                    storeGrideItemBinding.favId.setImageTintList(null);
                }
            }
            storeGrideItemBinding.shareId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //shareVia(String coupon, String store, String url
                    //todo change title to store name
                    listener.clickShare(store.getCoupon(),store.getTitle(),store.getCouponLink());
                }
            });
            storeGrideItemBinding.favId.setOnClickListener(v -> {
                if (GoldenSharedPreference.isLoggedIn(context)) {
                    if (store.getIsFavorite() == 0) {
                        couponFav();
                    } else {
                        removeFav();
                    }
                }
                else {
                    listener.click(5,getAdapterPosition());
                }
            });
            storeGrideItemBinding.getOfferId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(store.getCouponLink()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            });
        }

        private void couponFav() {
            String lang = GoldenNoLoginSharedPreference.getUserLanguage(context);
            String token = GoldenSharedPreference.getToken(context);
            apiInterface.userMakeFavCoupons("Bearer" + token, lang, String.valueOf(store.get(getAdapterPosition()).getId())).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200 && response.body() != null) {
                            storeGrideItemBinding.favId.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_fav));
                            storeGrideItemBinding.favId.setImageTintList(null);
                            listener.click(3, getAdapterPosition());
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
            apiInterface.userRemoveFavCoupons("Bearer" + token, lang, String.valueOf(store.get(getAdapterPosition()).getId())).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200 && response.body() != null) {
                            storeGrideItemBinding.favId.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_fav));
                            storeGrideItemBinding.favId.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.black)));
                            listener.click(3, getAdapterPosition());
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

