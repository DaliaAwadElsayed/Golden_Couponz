package com.goldencouponz.adapters.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.StoreListItemBinding;
import com.goldencouponz.models.home.Store;
import com.goldencouponz.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StoresListAdapter extends RecyclerView.Adapter<StoresListAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<Store> store = new ArrayList<>();
    Context context;

    public StoresListAdapter(Context context) {
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
        return new HomePageViewHolder(StoreListItemBinding.inflate(inflater, parent, false));
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

        HomePageViewHolder(@NonNull StoreListItemBinding storeGrideItemBinding) {
            super(storeGrideItemBinding.getRoot());
            this.storeGrideItemBinding = storeGrideItemBinding;
        }

        private void bindRestaurant(Store store) {
            if (store.getIsFavorite()==1){
                storeGrideItemBinding.favId.setVisibility(View.GONE);
                storeGrideItemBinding.addFavId.setVisibility(View.VISIBLE);
            }else {
                storeGrideItemBinding.favId.setVisibility(View.VISIBLE);
                storeGrideItemBinding.addFavId.setVisibility(View.GONE);
            }
            storeGrideItemBinding.storeNameId.setText(Utility.fixNullString(String.valueOf(store.getTitle())));
            storeGrideItemBinding.couponCountId.setText(Utility.fixNullString(String.valueOf(store.getStoreCouponsCount())));
            Picasso.get().load(store.getFile()).placeholder(R.drawable.ic_loading).placeholder(R.drawable.ic_loading).into(storeGrideItemBinding.storeImgId);
        }

    }
}

