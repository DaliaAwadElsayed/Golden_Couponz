package com.goldencouponz.viewModles.favourite;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.FragmentFavouriteBinding;
import com.goldencouponz.adapters.favourite.CopounzFavAdapter;
import com.goldencouponz.adapters.favourite.StoresFavAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteViewModel extends ViewModel {
    FragmentFavouriteBinding fragmentFavouriteBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    CopounzFavAdapter copounzFavAdapter;
    StoresFavAdapter storesFavAdapter;


    public void init(FragmentFavouriteBinding fragmentFavouriteBinding, Context context) {
        this.context = context;
        this.fragmentFavouriteBinding = fragmentFavouriteBinding;
        tabLayout();
        if (GoldenSharedPreference.isLoggedIn(context)) {
            getFavouriteCopounz("Bearer" + GoldenSharedPreference.getToken(context),
                    GoldenNoLoginSharedPreference.getUserLanguage(context));
        } else {
            pleaseLogin();
        }

    }

    private void tabLayout() {
        fragmentFavouriteBinding.tabLayoutId.addTab(fragmentFavouriteBinding.tabLayoutId.newTab().setText(context.getResources().getString(R.string.coupons_label)));
        fragmentFavouriteBinding.tabLayoutId.addTab(fragmentFavouriteBinding.tabLayoutId.newTab().setText(context.getResources().getString(R.string.stores_label)));
        fragmentFavouriteBinding.tabLayoutId.setTabGravity(TabLayout.GRAVITY_FILL);
        fragmentFavouriteBinding.tabLayoutId.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    if (GoldenSharedPreference.isLoggedIn(context)) {
                        getFavouriteCopounz("Bearer" + GoldenSharedPreference.getToken(context),
                                GoldenNoLoginSharedPreference.getUserLanguage(context));
                    } else {
                        pleaseLogin();
                    }
                } else if (tab.getPosition() == 1) {
                    fragmentFavouriteBinding.storesLinearId.setVisibility(View.VISIBLE);
                    fragmentFavouriteBinding.couponzLinearId.setVisibility(View.GONE);
                    if (GoldenSharedPreference.isLoggedIn(context)) {
                        getFavouriteStores("Bearer" + GoldenSharedPreference.getToken(context),
                                GoldenNoLoginSharedPreference.getUserLanguage(context));
                    } else {
                        pleaseLogin();
                    }

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    Listener listener = new Listener() {
        @Override
        public void click(int click, int position) {
            Log.i("CLICKS", click + "?");
            if (click == 3) {
                getFavouriteCopounz("Bearer" + GoldenSharedPreference.getToken(context),
                        GoldenNoLoginSharedPreference.getUserLanguage(context));
            } else if (click == 1) {
                getFavouriteStores("Bearer" + GoldenSharedPreference.getToken(context),
                        GoldenNoLoginSharedPreference.getUserLanguage(context));

            }
        }

        @Override
        public void clickShare(String coupon, String store, String url) {

        }

        @Override
        public void click(int i) {

        }
    };

    public interface Listener {
        void click(int click, int position);

        void clickShare(String coupon, String store, String url);

        void click(int i);
    }

    private void getFavouriteCopounz(String token, String lang) {
        fragmentFavouriteBinding.couponzLinearId.setVisibility(View.VISIBLE);
        fragmentFavouriteBinding.storesLinearId.setVisibility(View.GONE);
        fragmentFavouriteBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getFavouriteCopounz(token, lang).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        if (!response.body().getCoupons().isEmpty()) {
                            fragmentFavouriteBinding.progress.setVisibility(View.GONE);
                            copounzFavAdapter = new CopounzFavAdapter(context, listener);
                            copounzFavAdapter.setCoupons(response.body().getCoupons());
                            fragmentFavouriteBinding.couponzRecyclerId.setAdapter(copounzFavAdapter);
                        } else {
                            noFavCopounzYet();
                            fragmentFavouriteBinding.progress.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                fragmentFavouriteBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFavouriteStores(String token, String lang) {
        fragmentFavouriteBinding.storesLinearId.setVisibility(View.VISIBLE);
        fragmentFavouriteBinding.couponzLinearId.setVisibility(View.GONE);
        fragmentFavouriteBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getFavouriteStores(token, lang).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        if (!response.body().getStores().isEmpty()) {
                            fragmentFavouriteBinding.progress.setVisibility(View.GONE);
                            storesFavAdapter = new StoresFavAdapter(context, listener);
                            storesFavAdapter.setStores(response.body().getStores());
                            fragmentFavouriteBinding.storesRecyclerId.setAdapter(storesFavAdapter);
                        } else {
                            noFavCopounzYet();
                            fragmentFavouriteBinding.progress.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                fragmentFavouriteBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void noFavCopounzYet() {
        fragmentFavouriteBinding.noLoginLinearId.setVisibility(View.GONE);
        fragmentFavouriteBinding.storesLinearId.setVisibility(View.GONE);
        fragmentFavouriteBinding.couponzLinearId.setVisibility(View.GONE);
        fragmentFavouriteBinding.noCopounzLinearId.setVisibility(View.VISIBLE);
    }

    private void pleaseLogin() {
        fragmentFavouriteBinding.noLoginLinearId.setVisibility(View.VISIBLE);
        fragmentFavouriteBinding.storesLinearId.setVisibility(View.GONE);
        fragmentFavouriteBinding.couponzLinearId.setVisibility(View.GONE);
        fragmentFavouriteBinding.noCopounzLinearId.setVisibility(View.GONE);
        fragmentFavouriteBinding.signInId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.loginFragment);
            }
        });
    }


}