package com.goldencouponz.adapters.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.PagerAdapter;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.BannerLayoutBinding;
import com.goldencouponz.models.home.Slider;
import com.goldencouponz.viewModles.home.HomeViewModel;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SlidersAdapter extends PagerAdapter {
    private List<Slider> sliders;
    private static final String TAG = "adapter";
    int size;
    private HomeViewModel.sliderSize sliderSize;
    Context context;

    public SlidersAdapter(HomeViewModel.sliderSize sliderSize, Context context) {
        this.sliderSize = sliderSize;
        this.context = context;
    }

    public List<Slider> getSliders() {
        return sliders;
    }

    public void setSliders(List<Slider> sliders) {
        this.sliders = sliders;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return sliders == null ? 0 : sliders.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        BannerLayoutBinding layoutBinding = BannerLayoutBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        if (sliders.get(position).getFile() != null) {
                 Picasso.get().load(sliders.get(position).getFile()).into(layoutBinding.viewPagerItemImage1);

        }

        size = sliders.size();
        sliderSize.size(size);
        container.addView(layoutBinding.getRoot());
        layoutBinding.viewPagerItemImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("storeId", sliders.get(position).getStoreId());
                bundle.putString("type","no");
                Navigation.findNavController(v).navigate(R.id.storeDetailsFragment, bundle);
            }
        });

        return layoutBinding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((MaterialCardView) object);
    }

}



