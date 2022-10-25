package com.goldencouponz.adapters.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.CategoriesItemBinding;
import com.goldencouponz.models.home.Category;
import com.goldencouponz.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<Category> categories = new ArrayList<>();
    Context context;

    public CategoriesAdapter(Context context) {
        this.context = context;
    }

    public void addCategory(List<Category> categories) {
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    public List<Category> getCategories() {
        return categories;
    }


    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new HomePageViewHolder(CategoriesItemBinding.inflate(inflater, parent, false));
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
        private CategoriesItemBinding categoriesItemBinding;

        HomePageViewHolder(@NonNull CategoriesItemBinding categoriesItemBinding) {
            super(categoriesItemBinding.getRoot());
            this.categoriesItemBinding = categoriesItemBinding;
        }

        private void bindRestaurant(Category category) {
            if (category.getIcon() != null) {
                Picasso.get().load(category.getIcon()).placeholder(R.drawable.ic_loading).error(R.drawable.ic_loading).into(categoriesItemBinding.imgId);
            }
            categoriesItemBinding.textId.setText(Utility.fixNullString(category.getTitle()));
        }
    }
}
