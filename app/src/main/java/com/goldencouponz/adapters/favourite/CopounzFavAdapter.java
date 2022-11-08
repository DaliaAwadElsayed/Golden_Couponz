package com.goldencouponz.adapters.favourite;

import android.content.Context;
import android.content.Intent;
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
import com.goldencouponz.viewModles.favourite.FavouriteViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CopounzFavAdapter extends RecyclerView.Adapter<CopounzFavAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<StoreCoupons> coupons = new ArrayList<>();
    Context context;
    FavouriteViewModel.Listener listener;
    private Api apiInterface = RetrofitClient.getInstance().getApi();

    public CopounzFavAdapter(Context context, FavouriteViewModel.Listener listener) {
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
        holder.bindCopoun(coupons.get(position));
    }

    @Override
    public int getItemCount() {
        return coupons == null ? 0 : coupons.size();
    }

    public void setCoupons(List<StoreCoupons> coupons) {
        this.coupons.addAll(coupons);
        notifyDataSetChanged();
    }

    class HomePageViewHolder extends RecyclerView.ViewHolder {
        private CouponItemBinding couponItemBinding;
        FavouriteViewModel.Listener listener;

        HomePageViewHolder(@NonNull CouponItemBinding couponItemBinding, FavouriteViewModel.Listener listener) {
            super(couponItemBinding.getRoot());
            this.listener = listener;
            this.couponItemBinding = couponItemBinding;
        }

        private void bindCopoun(StoreCoupons coupons) {
            couponItemBinding.favId.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_fav));
            couponItemBinding.favId.setImageTintList(null);
            couponItemBinding.discountId.setText(Utility.fixNullString(String.valueOf(coupons.getTitle())));
            couponItemBinding.discountDetailsId.setText(Utility.fixNullString(String.valueOf(coupons.getDetails())));
            couponItemBinding.hoursId.setText(Utility.fixNullString(String.valueOf(coupons.getLastUsed())));
            if (coupons.getCouponType().equals("coupon")) {
                couponItemBinding.copyCouponId.setVisibility(View.VISIBLE);
                couponItemBinding.getOfferId.setVisibility(View.GONE);
                couponItemBinding.numberOfCopiesId.setVisibility(View.VISIBLE);
                if (coupons.getCopies() != null) {
                    couponItemBinding.timeId.setText(Utility.fixNullString((coupons.getCopies()) + " " + context.getResources().getString(R.string.time)));
                } else {
                    couponItemBinding.timeId.setText("0" + " " + context.getResources().getString(R.string.time));

                }
            } else {
                couponItemBinding.numberOfCopiesId.setVisibility(View.GONE);
                couponItemBinding.copyCouponId.setVisibility(View.GONE);
                couponItemBinding.getOfferId.setVisibility(View.VISIBLE);
            }
            couponItemBinding.copyCouponId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.click(5, getAdapterPosition());
                }
            });

            couponItemBinding.shareId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //shareVia(String coupon, String store, String url
                    //todo change title to store name
                    listener.clickShare(coupons.getCoupon(), coupons.getTitle(), coupons.getCouponLink());
                }
            });
            couponItemBinding.favId.setOnClickListener(v -> {
                if (GoldenSharedPreference.isLoggedIn(context)) {
                    removeFav();
                }
            });
            couponItemBinding.getOfferId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(coupons.getCouponLink()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            });
        }


        private void removeFav() {
            String lang = GoldenNoLoginSharedPreference.getUserLanguage(context);
            String token = GoldenSharedPreference.getToken(context);
            apiInterface.userRemoveFavCoupons("Bearer" + token, lang, String.valueOf(coupons.get(getAdapterPosition()).getId())).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200 && response.body() != null) {
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

