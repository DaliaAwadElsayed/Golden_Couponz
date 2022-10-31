package com.goldencouponz.viewModles.stores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.viewpager.widget.ViewPager;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.StoreDetailsFragmentBinding;
import com.goldencouponz.adapters.home.SlidersAdapter;
import com.goldencouponz.adapters.stores.TabLayoutAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.viewModles.home.HomeViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreDetailsViewModel extends ViewModel implements ViewPager.OnPageChangeListener {
    StoreDetailsFragmentBinding storeDetailsFragmentBinding;
    Context context;
    int storeId;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    SlidersAdapter addsBannerAdapter;
    int interfaceSize;
    Timer timer;
    int currentPage = 0;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 2000; // time in milliseconds between successive task executions.

    public void init(StoreDetailsFragmentBinding storeDetailsFragmentBinding, Context context, int storeId) {
        this.context = context;
        this.storeDetailsFragmentBinding = storeDetailsFragmentBinding;
        this.storeId = storeId;
        storeDetailsFragmentBinding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    //  Collapsed
                    storeDetailsFragmentBinding.sliderId.setVisibility(View.GONE);

                } else {
                    //Expanded
                    storeDetailsFragmentBinding.sliderId.setVisibility(View.VISIBLE);

                }
            }
        });
        tabLayout();
        addsBannerAdapter = new SlidersAdapter(sliderSize, context);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage < interfaceSize) {
                    storeDetailsFragmentBinding.sliderId.setCurrentItem(currentPage++, true);
                    if (currentPage == interfaceSize) {
                        currentPage = 0;
                    }
                }
            }
        };
        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
        storeDetailsFragmentBinding.sliderId.setAdapter(addsBannerAdapter);
        storeDetailsFragmentBinding.sliderId.addOnPageChangeListener(this);
        getStoreDetails();

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onPageScrollStateChanged(int state) {
        Log.i("slider", String.valueOf(addsBannerAdapter.getCount()));
        if (state == addsBannerAdapter.getCount()) {
            storeDetailsFragmentBinding.sliderId.setCurrentItem(0, true);
        }

    }

    private void getStoreDetails() {
        String lang = GoldenNoLoginSharedPreference.getUserLanguage(context);
        String fcmToken = "";
        int countryId = GoldenNoLoginSharedPreference.getUserCountryId(context);
        int id = storeId;
        Log.i("COUNTRYID", countryId + "?");
        apiInterface.getSingleStore(lang, fcmToken, countryId, String.valueOf(id)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        Picasso.get().load(response.body().getStore().getFile()).into(storeDetailsFragmentBinding.storeImgId);
                        Picasso.get().load(response.body().getStore().getCover()).into(storeDetailsFragmentBinding.coverId);
                        storeDetailsFragmentBinding.storeNameId.setText(response.body().getStore().getTitle());
                        int size = response.body().getStore().getStoreCategories().size();
                        StringBuffer br = new StringBuffer();
                        for (int i = 0; i < size; i++) {
                            String text = response.body().getStore().getStoreCategories().get(i).getCategory().getTitle();
                            if (i < size - 1) {
                                br.append(text + " , ");
                            } else if (i == size - 1) {
                                br.append(text + ".");
                            }
                        }
                        storeDetailsFragmentBinding.storeCatId.setText(br.toString());
                        if (!response.body().getStore().getStoreSliders().isEmpty()) {
                            addsBannerAdapter.setSliders(response.body().getStore().getStoreSliders());
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == addsBannerAdapter.getCount()) {
            storeDetailsFragmentBinding.sliderId.setCurrentItem(0, true);
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    HomeViewModel.sliderSize sliderSize = new HomeViewModel.sliderSize() {
        @Override
        public void size(int size) {
            interfaceSize = size;
        }

    };

    private void tabLayout() {
        storeDetailsFragmentBinding.tabLayoutId.addTab(storeDetailsFragmentBinding.tabLayoutId.newTab().setText(context.getResources().getString(R.string.coupons_label)));
        storeDetailsFragmentBinding.tabLayoutId.addTab(storeDetailsFragmentBinding.tabLayoutId.newTab().setText(context.getResources().getString(R.string.product_offers)));
        storeDetailsFragmentBinding.tabLayoutId.setTabGravity(TabLayout.GRAVITY_FILL);
        final TabLayoutAdapter adapter = new TabLayoutAdapter(context, ((AppCompatActivity) context).getSupportFragmentManager(), storeDetailsFragmentBinding.tabLayoutId.getTabCount());
        storeDetailsFragmentBinding.viewPagerId.setAdapter(adapter);
        storeDetailsFragmentBinding.viewPagerId.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(storeDetailsFragmentBinding.tabLayoutId));
        storeDetailsFragmentBinding.viewPagerId.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(storeDetailsFragmentBinding.tabLayoutId));

        storeDetailsFragmentBinding.tabLayoutId.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//              storeDetailsFragmentBinding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}