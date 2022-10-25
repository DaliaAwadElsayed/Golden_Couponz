package com.goldencouponz.viewModles.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.HomeFragmentBinding;
import com.goldencouponz.adapters.home.CategoriesAdapter;
import com.goldencouponz.adapters.home.SlidersAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;
import com.google.android.material.appbar.AppBarLayout;
import com.rd.animation.type.AnimationType;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel implements ViewPager.OnPageChangeListener {
    HomeFragmentBinding homeFragmentBinding;
    Context context;
    CategoriesAdapter categoriesAdapter;
    SlidersAdapter addsBannerAdapter;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    int interfaceSize;
    Timer timer;
    int currentPage = 0;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    Activity activity;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onPageScrollStateChanged(int state) {
        Log.i("slider", String.valueOf(addsBannerAdapter.getCount()));
        if (state == addsBannerAdapter.getCount()) {
            homeFragmentBinding.viewPager.setCurrentItem(0, true);
        }

    }

    private void addsSlider() {
        apiInterface.addSlider().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        if (!response.body().getSliders().isEmpty()) {
                            addsBannerAdapter.setSliders(response.body().getSliders());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == addsBannerAdapter.getCount()) {
            homeFragmentBinding.viewPager.setCurrentItem(0, true);
        }
    }

    @Override
    public void onPageSelected(int position) {
        homeFragmentBinding.pageIndicatorView.setSelection(position);
    }

    sliderSize sliderSize = new sliderSize() {
        @Override
        public void size(int size) {
            interfaceSize = size;
        }

    };

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private State mCurrentState = State.IDLE;


    public interface sliderSize {
        void size(int size);
    }

    public void init(HomeFragmentBinding homeFragmentBinding, Context context) {
        this.context = context;
        this.homeFragmentBinding = homeFragmentBinding;
        if (!GoldenSharedPreference.isLoggedIn(context) && !GoldenSharedPreference.getUserShowLogin(context).equals("close")) {
            Navigation.findNavController(homeFragmentBinding.getRoot()).navigate(R.id.loginFragment);
        }
        categoriesAdapter = new CategoriesAdapter(context);
        addsBannerAdapter = new SlidersAdapter(sliderSize, context);
        homeFragmentBinding.categoryRecyclerView.setAdapter(categoriesAdapter);
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("en")) {
            categories("en");
        } else if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
            categories("ar");
        }
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage < interfaceSize) {
                    homeFragmentBinding.viewPager.setCurrentItem(currentPage++, true);
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
        homeFragmentBinding.categoryRecyclerView.setAdapter(categoriesAdapter);
        homeFragmentBinding.viewPager.setAdapter(addsBannerAdapter);
        homeFragmentBinding.viewPager.addOnPageChangeListener(this);
        homeFragmentBinding.pageIndicatorView.setAnimationType(AnimationType.SWAP);
        addsSlider();
        homeFragmentBinding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    //  Collapsed
                    homeFragmentBinding.viewPagerLinearId.setVisibility(View.GONE);

                } else {
                    //Expanded
                    homeFragmentBinding.viewPagerLinearId.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    private void categories(String lang) {
        homeFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getCategories(lang, "", 0).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        if (!response.body().getCategories().isEmpty()) {
                            homeFragmentBinding.progress.setVisibility(View.GONE);
                            categoriesAdapter.setCategories(response.body().getCategories());
                        } else {
                            homeFragmentBinding.progress.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                homeFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}