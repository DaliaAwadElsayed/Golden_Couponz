package com.goldencouponz.adapters.stores;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.CouponItemBinding;
import com.goldencouponz.models.store.StoreCoupons;
import com.goldencouponz.utility.Utility;
import com.goldencouponz.viewModles.stores.StoreDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

public class CopounzAdapter extends RecyclerView.Adapter<CopounzAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<StoreCoupons> store = new ArrayList<>();
    Context context;
    StoreDetailsViewModel.Listener listener;

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
            storeGrideItemBinding.getOfferId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(store.getCouponLink()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            });
        }

    }
}

