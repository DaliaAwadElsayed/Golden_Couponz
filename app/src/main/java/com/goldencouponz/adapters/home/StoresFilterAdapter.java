package com.goldencouponz.adapters.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.databinding.CheckStoreFilterItemBinding;
import com.goldencouponz.models.home.Store;
import com.goldencouponz.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class StoresFilterAdapter extends RecyclerView.Adapter<StoresFilterAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<Store> store = new ArrayList<>();
    Context context;
    StoresFilterAdapter.OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(View viewItem, int position,int id, String store);
    }

    public void setOnItemClickListener(StoresFilterAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
    public StoresFilterAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new HomePageViewHolder(CheckStoreFilterItemBinding.inflate(inflater, parent, false));
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
        private CheckStoreFilterItemBinding storeGrideItemBinding;

        HomePageViewHolder(@NonNull CheckStoreFilterItemBinding storeGrideItemBinding) {
            super(storeGrideItemBinding.getRoot());
            this.storeGrideItemBinding = storeGrideItemBinding;
        }

        private void bindRestaurant(Store store) {
            storeGrideItemBinding.textId.setText(Utility.fixNullString(String.valueOf(store.getTitle())));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(storeGrideItemBinding.getRoot(), getAdapterPosition(),store.getId(), store.getTitle());
                }
            });

        }

    }
}

