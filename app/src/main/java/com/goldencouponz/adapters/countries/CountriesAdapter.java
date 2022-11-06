package com.goldencouponz.adapters.countries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.goldencouponz.databinding.CountryItemBinding;
import com.goldencouponz.models.appsetting.Country;
import com.goldencouponz.utility.Utility;
import com.goldencouponz.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<Country> countries = new ArrayList<>();
    Context context;
    private int checkedPosition = -1;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View viewItem, int position, int countryId, String name, String currency);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CountriesAdapter(Context context) {
        this.context = context;
    }

    public void addCategory(List<Country> countries) {
        this.countries.addAll(countries);
        notifyDataSetChanged();
    }

    public List<Country> getCategories() {
        return countries;
    }


    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new HomePageViewHolder(CountryItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageViewHolder holder, int position) {
        holder.bindRestaurant(countries.get(position));
    }

    @Override
    public int getItemCount() {
        return countries == null ? 0 : countries.size();
    }

    public void setCountries(List<Country> countries) {
        this.countries.addAll(countries);
        notifyDataSetChanged();
    }

    class HomePageViewHolder extends RecyclerView.ViewHolder {
        private CountryItemBinding countryItemBinding;

        HomePageViewHolder(@NonNull CountryItemBinding countryItemBinding) {
            super(countryItemBinding.getRoot());
            this.countryItemBinding = countryItemBinding;
        }

        private void bindRestaurant(Country countries) {
            countryItemBinding.countryNameId.setText(Utility.fixNullString(String.valueOf(countries.getTitle())));
            Utils.fetchSvg(context, countries.getFlag(), countryItemBinding.countryImgId);
            if (checkedPosition == -1) {
                countryItemBinding.checkId.setVisibility(View.GONE);
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    countryItemBinding.checkId.setVisibility(View.VISIBLE);
                } else {
                    countryItemBinding.checkId.setVisibility(View.GONE);
                }
            }
            itemView.setOnClickListener(view -> {
                countryItemBinding.checkId.setVisibility(View.VISIBLE);
                if (checkedPosition != getAdapterPosition()) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = getAdapterPosition();
                    String countryName = countries.getTitle();
                    int countryId = countries.getId();
                    String currency = countries.getCurrency();
                    listener.onItemClick(countryItemBinding.getRoot(), checkedPosition, countryId, countryName,currency);

                }
            });
        }


    }
}

