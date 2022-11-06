package com.goldencouponz.adapters.stores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.ProductCategoriesItemBinding;
import com.goldencouponz.models.home.Category;
import com.goldencouponz.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class ProductsCategoriesAdapter extends RecyclerView.Adapter<ProductsCategoriesAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<Category> categories = new ArrayList<>();
    Context context;
    private int checkedPosition = -1;
    private ProductsCategoriesAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View viewItem, int position, int categoryId);
    }

    public void setOnItemClickListener(ProductsCategoriesAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public ProductsCategoriesAdapter(Context context) {
        this.context = context;
    }



    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new HomePageViewHolder(ProductCategoriesItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageViewHolder holder, int position) {
        holder.bindRestaurant(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    class HomePageViewHolder extends RecyclerView.ViewHolder {
        private ProductCategoriesItemBinding categoriesItemBinding;

        HomePageViewHolder(@NonNull ProductCategoriesItemBinding categoriesItemBinding) {
            super(categoriesItemBinding.getRoot());
            this.categoriesItemBinding = categoriesItemBinding;
        }

        private void bindRestaurant(Category category) {
            Log.i("cat",category.getTitle()+"?");
            categoriesItemBinding.textId.setText(Utility.fixNullString(category.getCategory().getTitle()));
            if (checkedPosition == -1) {
                categoriesItemBinding.linearId.setBackground(context.getResources().getDrawable(R.drawable.bk_category_uncheck));
//                categoriesItemBinding.linearId.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.category_bk)));
            }
            else {
                if (checkedPosition == getAdapterPosition()) {
                    categoriesItemBinding.linearId.setBackground(context.getResources().getDrawable(R.drawable.bk_category));
                    categoriesItemBinding.linearId.setBackgroundTintList(null);
                } else {
                    categoriesItemBinding.linearId.setBackground(context.getResources().getDrawable(R.drawable.bk_category_uncheck));
//                    categoriesItemBinding.linearId.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.category_bk)));
                }
            }
            itemView.setOnClickListener(view -> {
                categoriesItemBinding.linearId.setBackground(context.getResources().getDrawable(R.drawable.bk_category));
                categoriesItemBinding.linearId.setBackgroundTintList(null);
                if (checkedPosition != getAdapterPosition()) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = getAdapterPosition();
                    int categoryId = category.getCategory().getId();
                    listener.onItemClick(categoriesItemBinding.getRoot(), checkedPosition, categoryId);

                }
            });
        }


    }

}
