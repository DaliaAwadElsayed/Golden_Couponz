package com.goldencouponz.adapters.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.databinding.SeeAllItemBinding;
import com.goldencouponz.models.home.Slider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SeeAllAdapter extends RecyclerView.Adapter<SeeAllAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<Slider> store = new ArrayList<>();
    Context context;
    SeeAllAdapter.OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(View viewItem, int position, int storeId);
    }

    public void setOnItemClickListener(SeeAllAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
    public SeeAllAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new HomePageViewHolder(SeeAllItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageViewHolder holder, int position) {
        holder.bindRestaurant(store.get(position));
    }

    @Override
    public int getItemCount() {
        return store == null ? 0 : store.size();
    }

    public void setStores(List<Slider> store) {
        this.store.addAll(store);
        notifyDataSetChanged();
    }

    class HomePageViewHolder extends RecyclerView.ViewHolder {
        private SeeAllItemBinding storeGrideItemBinding;

        HomePageViewHolder(@NonNull SeeAllItemBinding storeGrideItemBinding) {
            super(storeGrideItemBinding.getRoot());
            this.storeGrideItemBinding = storeGrideItemBinding;
        }

        private void bindRestaurant(Slider store) {
            Picasso.get().load(store.getFile()).into(storeGrideItemBinding.viewPagerItemImage1);
            storeGrideItemBinding.clickId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v1) {
                    listener.onItemClick(storeGrideItemBinding.getRoot(),getAdapterPosition(),store.getStoreId());
                   }
            });
        }

    }
}

