package com.goldencouponz.viewModles.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.HomeFragmentBinding;
import com.e.goldencouponz.databinding.LoginCheckDialogBinding;
import com.e.goldencouponz.databinding.SeeAllDialogBinding;
import com.goldencouponz.adapters.home.CategoriesAdapter;
import com.goldencouponz.adapters.home.SeeAllAdapter;
import com.goldencouponz.adapters.home.SlidersAdapter;
import com.goldencouponz.adapters.home.StoresGridAdapter;
import com.goldencouponz.adapters.home.StoresListAdapter;
import com.goldencouponz.adapters.notification.NotificationAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.messaging.FirebaseMessaging;
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
    StoresGridAdapter storesGrideAdapter;
    StoresListAdapter storesListAdapter;
    int all = 0;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    int interfaceSize;
    Timer timer;
    int currentPage = 0;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    Activity activity;
    BottomSheetDialog loginCheckDialog;
    BottomSheetDialog seeAllDialog;
    LoginCheckDialogBinding loginCheckDialogBinding;
    SeeAllDialogBinding seeAllDialogBinding;
    Animation shake;
    NotificationAdapter notificationAdapter;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onPageScrollStateChanged(int state) {
        Log.i("slider", String.valueOf(addsBannerAdapter.getCount()));
        if (state == addsBannerAdapter.getCount()) {
            homeFragmentBinding.viewPager.setCurrentItem(0, true);
        }

    }

    private void notification() {
        apiInterface.userGetNotification("Bearer" + GoldenSharedPreference.getToken(context)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        if (!response.body().getNotifications().isEmpty()) {
                            notificationAdapter = new NotificationAdapter(context);
                            notificationAdapter.setStores(response.body().getNotifications());
                            int size = response.body().getNotifications().size();
                            int count = 0;
                            if (size > 0) {
                                for (int i = 0; i < size; i++) {
                                    if (response.body().getNotifications().get(i).getReadAt() == null) {
                                        count++;
                                        homeFragmentBinding.notificationBadge.setText("" + count);
                                        homeFragmentBinding.notificationBadge.setVisibility(View.VISIBLE);
                                        shake = AnimationUtils.loadAnimation(context, R.anim.shake);
                                        homeFragmentBinding.notificationId.startAnimation(shake);
                                    }
                                }

                            }

                        } else {
                            homeFragmentBinding.notificationBadge.setVisibility(View.GONE);
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

    private void addsSlider() {
        apiInterface.addSlider(GoldenNoLoginSharedPreference.getUserLanguage(context)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        if (!response.body().getSliders().isEmpty()) {
                            addsBannerAdapter.setSliders(response.body().getSliders());
                            SeeAllAdapter seeAllAdapter = new SeeAllAdapter(context);
                            seeAllDialogBinding.bannerRecyclerId.setAdapter(seeAllAdapter);
                            seeAllAdapter.setStores(response.body().getSliders());
                            seeAllAdapter.setOnItemClickListener(new SeeAllAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View viewItem, int position, int storeId) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("storeId", storeId);
                                    Navigation.findNavController(homeFragmentBinding.getRoot()).navigate(R.id.storeDetailsFragment, bundle);
                                    seeAllDialog.dismiss();
                                }
                            });
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

    public interface sliderSize {
        void size(int size);
    }

    public void init(HomeFragmentBinding homeFragmentBinding, SeeAllDialogBinding seeAllDialogBinding, LoginCheckDialogBinding loginCheckDialogBinding, Context context) {
        this.context = context;
        this.seeAllDialogBinding = seeAllDialogBinding;
        this.loginCheckDialogBinding = loginCheckDialogBinding;
        this.homeFragmentBinding = homeFragmentBinding;
        loginCheckDialog = new BottomSheetDialog(context);
        seeAllDialog = new BottomSheetDialog(context);
        homeFragmentBinding.searchId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.searchFragment);
            }
        });
        if (GoldenSharedPreference.isLoggedIn(context)) {
            notification();
            homeFragmentBinding.notificationLayoutId.setVisibility(View.VISIBLE);
        } else {
            homeFragmentBinding.notificationLayoutId.setVisibility(View.GONE);
        }

        homeFragmentBinding.seeAllId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeAllDialog();
            }
        });

        changeLayout();
        if (!GoldenSharedPreference.isLoggedIn(context) && !GoldenSharedPreference.getUserShowLogin(context).equals("close")) {
            Navigation.findNavController(homeFragmentBinding.getRoot()).navigate(R.id.loginFragment);
        }
        addsBannerAdapter = new SlidersAdapter(sliderSize, context);
        storesGridChoice();
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
        homeFragmentBinding.notificationId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.notificationFragment);
            }
        });
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

    private void changeLayout() {
        homeFragmentBinding.listId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storesListChoice();
            }
        });
        homeFragmentBinding.gridId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storesGridChoice();
            }
        });
    }

    private void storesGridChoice() {
        homeFragmentBinding.gridId.setVisibility(View.GONE);
        homeFragmentBinding.listId.setVisibility(View.VISIBLE);
        homeFragmentBinding.listId.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_list));
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("en")) {
            categories("en");
            if (GoldenSharedPreference.isLoggedIn(context)) {
                stores("Bearer" + GoldenSharedPreference.getToken(context), "en", 0);
            } else {
                stores("", "en", 0);

            }
            homeFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category));
            homeFragmentBinding.allId.setBackgroundTintList(null);

        } else if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
            categories("ar");
            if (GoldenSharedPreference.isLoggedIn(context)) {
                stores("Bearer" + GoldenSharedPreference.getToken(context), "ar", 0);
            } else {
                stores("", "ar", 0);

            }
            homeFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category));
            homeFragmentBinding.allId.setBackgroundTintList(null);
        }
        homeFragmentBinding.allId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all = 1;
                homeFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category));
                homeFragmentBinding.allId.setBackgroundTintList(null);
                if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("en")) {
                    if (GoldenSharedPreference.isLoggedIn(context)) {
                        stores("Bearer" + GoldenSharedPreference.getToken(context), "en", 0);
                    } else {
                        stores("", "en", 0);
                    }
                    categories("en");
                } else if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
                    if (GoldenSharedPreference.isLoggedIn(context)) {
                        stores("Bearer" + GoldenSharedPreference.getToken(context), "ar", 0);
                    } else {
                        stores("", "ar", 0);

                    }
                    categories("ar");

                }

            }
        });

    }

    private void storesListChoice() {
        homeFragmentBinding.listId.setVisibility(View.GONE);
        homeFragmentBinding.gridId.setVisibility(View.VISIBLE);
        homeFragmentBinding.listId.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_grid));
        Log.i("Language", GoldenNoLoginSharedPreference.getUserLanguage(context) + "?");
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("en")) {
            categories("en");
            if (GoldenSharedPreference.isLoggedIn(context)) {
                storesList("Bearer" + GoldenSharedPreference.getToken(context), "en", 0);
            } else {
                storesList("", "en", 0);

            }
            homeFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category));
            homeFragmentBinding.allId.setBackgroundTintList(null);

        } else if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
            categories("ar");
            if (GoldenSharedPreference.isLoggedIn(context)) {
                storesList("Bearer" + GoldenSharedPreference.getToken(context), "ar", 0);
            } else {
                storesList("", "ar", 0);

            }
            homeFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category));
            homeFragmentBinding.allId.setBackgroundTintList(null);
        }
        homeFragmentBinding.allId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category));
                homeFragmentBinding.allId.setBackgroundTintList(null);
                if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("en")) {
                    if (GoldenSharedPreference.isLoggedIn(context)) {
                        storesList("Bearer" + GoldenSharedPreference.getToken(context), "en", 0);
                    } else {
                        storesList("", "en", 0);

                    }
                    categories("en");
                } else if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
                    if (GoldenSharedPreference.isLoggedIn(context)) {
                        storesList("Bearer" + GoldenSharedPreference.getToken(context), "ar", 0);
                    } else {
                        storesList("", "ar", 0);

                    }
                    categories("ar");
                }

            }
        });

    }

    private void categories(String lang) {
        homeFragmentBinding.progress.setVisibility(View.VISIBLE);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.w("TAG", token + "?");
                        apiInterface.getCategories(lang, token, 0).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 200 && response.body() != null) {
                                        if (!response.body().getCategories().isEmpty()) {
                                            internet();
                                            categoriesAdapter = new CategoriesAdapter(context);
                                            homeFragmentBinding.categoryRecyclerView.setAdapter(categoriesAdapter);
                                            categoriesAdapter.setCategories(response.body().getCategories());
                                            categoriesAdapter.setOnItemClickListener(new CategoriesAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(View viewItem, int position, int categoryId) {
                                                    homeFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category_uncheck));
//                                    homeFragmentBinding.allId.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.category_bk)));
                                                    if (homeFragmentBinding.listId.getVisibility() == View.VISIBLE) {
                                                        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("en")) {
                                                            if (GoldenSharedPreference.isLoggedIn(context)) {
                                                                stores("Bearer" + GoldenSharedPreference.getToken(context), "en", categoryId);
                                                            } else {
                                                                stores("", "en", categoryId);

                                                            }
                                                        } else if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
                                                            if (GoldenSharedPreference.isLoggedIn(context)) {
                                                                stores("Bearer" + GoldenSharedPreference.getToken(context), "ar", categoryId);
                                                            } else {
                                                                stores("", "ar", categoryId);

                                                            }
                                                        }
                                                    } else if (homeFragmentBinding.gridId.getVisibility() == View.VISIBLE) {
                                                        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("en")) {
                                                            if (GoldenSharedPreference.isLoggedIn(context)) {
                                                                storesList("Bearer" + GoldenSharedPreference.getToken(context), "en", 0);
                                                            } else {
                                                                storesList("", "en", categoryId);

                                                            }
                                                        } else if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
                                                            if (GoldenSharedPreference.isLoggedIn(context)) {
                                                                storesList("Bearer" + GoldenSharedPreference.getToken(context), "ar", 0);
                                                            } else {
                                                                storesList("", "ar", categoryId);

                                                            }
                                                        }
                                                    }
                                                }
                                            });


                                        } else {
                                            Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                                            homeFragmentBinding.progress.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.i("onFailure", t.toString());
                                noInternet();
                            }
                        });

                    }
                });

    }

    private void storesList(String token, String lang, int categoryId) {
        homeFragmentBinding.homeListRecyclerView.setVisibility(View.VISIBLE);
        homeFragmentBinding.homeGrideRecyclerView.setVisibility(View.GONE);
        homeFragmentBinding.progress.setVisibility(View.VISIBLE);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String device = task.getResult();
                        Log.w("TAGGGG", device + "?");
                        apiInterface.getStore(token, lang, device, categoryId).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 200 && response.body() != null) {
                                        if (!response.body().getStores().isEmpty()) {
                                            internet();
                                            storesListAdapter = new StoresListAdapter(context, listener);
                                            storesListAdapter.setStores(response.body().getStores());
                                            homeFragmentBinding.homeListRecyclerView.setAdapter(storesListAdapter);
                                        } else {
                                            Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                                            homeFragmentBinding.progress.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.i("onFailure", t.toString());
                                noInternet();
                            }
                        });
                    }
                });
    }

    private void stores(String token, String lang, int categoryId) {
        homeFragmentBinding.homeListRecyclerView.setVisibility(View.GONE);
        homeFragmentBinding.homeGrideRecyclerView.setVisibility(View.VISIBLE);
        homeFragmentBinding.progress.setVisibility(View.VISIBLE);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String device = task.getResult();
                        Log.w("TAGGRIDE", device + "?");

                        apiInterface.getStore(token, lang, device, categoryId).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 200 && response.body() != null) {
                                        if (!response.body().getStores().isEmpty()) {
                                            internet();
                                            storesGrideAdapter = new StoresGridAdapter(context);
                                            storesGrideAdapter.setStores(response.body().getStores());
                                            homeFragmentBinding.homeGrideRecyclerView.setAdapter(storesGrideAdapter);

                                        } else {
                                            Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                                            homeFragmentBinding.progress.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.i("onFailure", t.toString());
                                noInternet();
                            }
                        });
                    }
                });
    }

    private void noInternet() {
        homeFragmentBinding.noInternetConId.setVisibility(View.VISIBLE);
        homeFragmentBinding.progress.setVisibility(View.GONE);
        Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
        homeFragmentBinding.nestedScrollId.setVisibility(View.INVISIBLE);
    }

    private void internet() {
        homeFragmentBinding.noInternetConId.setVisibility(View.GONE);
        homeFragmentBinding.progress.setVisibility(View.GONE);
        homeFragmentBinding.nestedScrollId.setVisibility(View.VISIBLE);

    }


    HomeViewModel.Listener listener = new HomeViewModel.Listener() {
        @Override
        public void click(int click) {
            click = click;
            if (click == 1) {
                if (GoldenSharedPreference.isLoggedIn(context)) {
                    storesList("Bearer" + GoldenSharedPreference.getToken(context), GoldenNoLoginSharedPreference.getUserLanguage(context), 0);
                } else {
                    storesList("", GoldenNoLoginSharedPreference.getUserLanguage(context), 0);

                }
            }
            if (click == 2) {
                loginCheckDialog();
            }
        }

    };

    public interface Listener {
        void click(int click);
    }

    private void seeAllDialog() {
        final View view2 = seeAllDialogBinding.getRoot();
        seeAllDialog.setContentView(view2);
        seeAllDialog.setCancelable(true);
        seeAllDialog.show();
        seeAllDialogBinding.cancelLogId.setOnClickListener(v -> {
            seeAllDialog.dismiss();
        });


    }

    private void loginCheckDialog() {
        final View view2 = loginCheckDialogBinding.getRoot();
        loginCheckDialog.setContentView(view2);
        loginCheckDialog.setCancelable(true);
        loginCheckDialog.show();
        loginCheckDialogBinding.signId.setOnClickListener(v -> {
            loginCheckDialog.dismiss();
            Navigation.findNavController(homeFragmentBinding.getRoot()).navigate(R.id.loginFragment);
        });

        loginCheckDialogBinding.cancelLogId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginCheckDialog.dismiss();
            }
        });
        loginCheckDialogBinding.cancelLogId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginCheckDialog.dismiss();
            }
        });
    }

}