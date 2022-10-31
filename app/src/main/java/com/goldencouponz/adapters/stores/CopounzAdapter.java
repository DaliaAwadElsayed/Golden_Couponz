package com.goldencouponz.adapters.stores;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.databinding.StoreGrideItemBinding;
import com.goldencouponz.models.home.Store;
import com.goldencouponz.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CopounzAdapter extends RecyclerView.Adapter<CopounzAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<Store> store = new ArrayList<>();
    Context context;

    public CopounzAdapter(Context context) {
        this.context = context;
    }

    public void addCategory(List<Store> store) {
        this.store.addAll(store);
        notifyDataSetChanged();
    }

    public List<Store> getCategories() {
        return store;
    }


    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new HomePageViewHolder(StoreGrideItemBinding.inflate(inflater, parent, false));
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
        private StoreGrideItemBinding storeGrideItemBinding;

        HomePageViewHolder(@NonNull StoreGrideItemBinding storeGrideItemBinding) {
            super(storeGrideItemBinding.getRoot());
            this.storeGrideItemBinding = storeGrideItemBinding;
        }

        private void bindRestaurant(Store store) {
            storeGrideItemBinding.storeNameId.setText(Utility.fixNullString(String.valueOf(store.getTitle())));
            storeGrideItemBinding.couponCountId.setText(Utility.fixNullString(String.valueOf(store.getStoreCouponsCount())));
            Picasso.get().load(store.getFile()).into(storeGrideItemBinding.storeImgId);
        }

    }
}

