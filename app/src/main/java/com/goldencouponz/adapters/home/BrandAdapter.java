package com.goldencouponz.adapters.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.databinding.CheckBrandFilterItemBinding;
import com.goldencouponz.models.store.Brand;
import com.goldencouponz.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<Brand> store = new ArrayList<>();
    Context context;
    ArrayList<Integer> selectedBrandIdItem = new ArrayList<Integer>();
    ArrayList<String> selectedBrandNamesItem = new ArrayList<String>();

    private BrandAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View viewItem, int position, ArrayList<Integer> selectedBrandIdItem,ArrayList<String> selectedBrandNamesItem);
    }

    public void setOnItemClickListener(BrandAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public BrandAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new HomePageViewHolder(CheckBrandFilterItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageViewHolder holder, int position) {
        holder.bindRestaurant(store.get(position));
    }

    @Override
    public int getItemCount() {
        return store == null ? 0 : store.size();
    }

    public void setStores(List<Brand> store) {
        this.store.addAll(store);
        notifyDataSetChanged();
    }

    class HomePageViewHolder extends RecyclerView.ViewHolder {
        private CheckBrandFilterItemBinding storeGrideItemBinding;

        HomePageViewHolder(@NonNull CheckBrandFilterItemBinding storeGrideItemBinding) {
            super(storeGrideItemBinding.getRoot());
            this.storeGrideItemBinding = storeGrideItemBinding;
        }

        private void bindRestaurant(Brand store) {
            storeGrideItemBinding.textId.setText(Utility.fixNullString(String.valueOf(store.getTitle())));
            storeGrideItemBinding.checkBoxId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedBrandIdItem.add(store.getId());
                        selectedBrandNamesItem.add(store.getTitle());
                        storeGrideItemBinding.checkId.setVisibility(View.VISIBLE);
                    } else {
                        storeGrideItemBinding.checkId.setVisibility(View.INVISIBLE);
                        selectedBrandNamesItem.remove(store.getTitle());
                        selectedBrandIdItem.remove(store.getId());
                    }
                    listener.onItemClick(storeGrideItemBinding.getRoot(), getAdapterPosition(), selectedBrandIdItem,selectedBrandNamesItem);

                }
            });

        }

    }
}

