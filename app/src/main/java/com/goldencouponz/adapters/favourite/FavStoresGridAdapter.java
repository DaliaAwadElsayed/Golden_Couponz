package com.goldencouponz.adapters.favourite;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.FavStoreGrideItemBinding;
import com.goldencouponz.models.home.Store;
import com.goldencouponz.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavStoresGridAdapter extends RecyclerView.Adapter<FavStoresGridAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<Store> store = new ArrayList<>();
    Context context;
    ArrayList<Integer> selectedStores = new ArrayList<Integer>();

    private FavStoresGridAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View viewItem, int position, ArrayList<Integer> selectedStores);
    }

    public void setOnItemClickListener(FavStoresGridAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public FavStoresGridAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new HomePageViewHolder(FavStoreGrideItemBinding.inflate(inflater, parent, false));
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
        private FavStoreGrideItemBinding storeGrideItemBinding;

        HomePageViewHolder(@NonNull FavStoreGrideItemBinding storeGrideItemBinding) {
            super(storeGrideItemBinding.getRoot());
            this.storeGrideItemBinding = storeGrideItemBinding;
        }

        private void bindRestaurant(Store store) {
            storeGrideItemBinding.storeNameId.setText(Utility.fixNullString(String.valueOf(store.getTitle())));
            Picasso.get().load(store.getFile()).into(storeGrideItemBinding.storeImgId);

            storeGrideItemBinding.checkBoxId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedStores.add(store.getId());
                        storeGrideItemBinding.circleId.setBackground(context.getResources().getDrawable(R.drawable.ic_check));
                    } else {
                        selectedStores.remove(store.getId());
                        storeGrideItemBinding.circleId.setBackground(null);
                    }
                    listener.onItemClick(storeGrideItemBinding.getRoot(), getAdapterPosition(), selectedStores);

                }
            });

        }

    }
}

