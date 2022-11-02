package com.goldencouponz.adapters.stores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.CouponItemBinding;
import com.goldencouponz.models.store.StoreCoupons;
import com.goldencouponz.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class CopounzAdapter extends RecyclerView.Adapter<CopounzAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<StoreCoupons> store = new ArrayList<>();
    Context context;

    public CopounzAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new HomePageViewHolder(CouponItemBinding.inflate(inflater, parent, false));
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

        HomePageViewHolder(@NonNull CouponItemBinding storeGrideItemBinding) {
            super(storeGrideItemBinding.getRoot());
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
                storeGrideItemBinding.timeId.setText(Utility.fixNullString((store.getCopies()) + " " + context.getResources().getString(R.string.sar)));
            } else {
                storeGrideItemBinding.numberOfCopiesId.setVisibility(View.GONE);
                storeGrideItemBinding.copyCouponId.setVisibility(View.GONE);
                storeGrideItemBinding.getOfferId.setVisibility(View.VISIBLE);
            }
        }

    }
}

