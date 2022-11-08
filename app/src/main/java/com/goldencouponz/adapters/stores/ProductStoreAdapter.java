package com.goldencouponz.adapters.stores;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.databinding.ProductItemBinding;
import com.goldencouponz.models.store.StoreProduct;
import com.goldencouponz.utility.Utility;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.viewModles.stores.StoreDetailsViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductStoreAdapter extends RecyclerView.Adapter<ProductStoreAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<StoreProduct> store = new ArrayList<>();
    Context context;
    StoreDetailsViewModel.Listener listener;

    public ProductStoreAdapter(Context context, StoreDetailsViewModel.Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new HomePageViewHolder(ProductItemBinding.inflate(inflater, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageViewHolder holder, int position) {
        holder.bindRestaurant(store.get(position));
    }

    @Override
    public int getItemCount() {
        return store == null ? 0 : store.size();
    }

    public void setStores(List<StoreProduct> store) {
        this.store.addAll(store);
        notifyDataSetChanged();
    }

    class HomePageViewHolder extends RecyclerView.ViewHolder {
        private ProductItemBinding storeGrideItemBinding;
        StoreDetailsViewModel.Listener listener;

        HomePageViewHolder(@NonNull ProductItemBinding storeGrideItemBinding, StoreDetailsViewModel.Listener listener) {
            super(storeGrideItemBinding.getRoot());
            this.storeGrideItemBinding = storeGrideItemBinding;
            this.listener = listener;
        }

        private void bindRestaurant(StoreProduct store) {
            if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
                storeGrideItemBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                storeGrideItemBinding.oldPriceId.setText(Utility.fixNullString(String.valueOf(store.getProductCountry().getDiscountPrice()) + " " + GoldenNoLoginSharedPreference.getUserCurrency(context)));

            } else {
                storeGrideItemBinding.oldPriceId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)) + " " + store.getProductCountry().getDiscountPrice());
                storeGrideItemBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            storeGrideItemBinding.oldPriceId.setPaintFlags(storeGrideItemBinding.oldPriceId.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            storeGrideItemBinding.discountId.setText(Utility.fixNullString(String.valueOf(store.getProductCountry().getDiscount())));
            storeGrideItemBinding.newPriceId.setText(Utility.fixNullString(String.valueOf(store.getProductCountry().getPrice())));
            storeGrideItemBinding.titleId.setText(Utility.fixNullString(String.valueOf(store.getTitle())));
            Picasso.get().load(store.getFile()).into(storeGrideItemBinding.productId);
            storeGrideItemBinding.currencyId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
            Picasso.get().load(store.getStore().getFile()).into(storeGrideItemBinding.storeImgId);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("COUUUU",store.getCoupon()+"?");
                    listener.click(4, getAdapterPosition(),store.getCoupon());
                }
            });
        }

    }
}

