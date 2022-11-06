package com.goldencouponz.viewModles.stores;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.StoreDetailsFragmentBinding;
import com.goldencouponz.adapters.home.SlidersAdapter;
import com.goldencouponz.adapters.stores.CopounzAdapter;
import com.goldencouponz.adapters.stores.ProductAdapter;
import com.goldencouponz.adapters.stores.ProductsCategoriesAdapter;
import com.goldencouponz.adapters.stores.ProductsSubCategoriesAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;
import com.goldencouponz.viewModles.home.HomeViewModel;
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
    int storeId, position;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    SlidersAdapter addsBannerAdapter;
    int interfaceSize;
    Timer timer;
    int currentPage = 0;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 2000; // time in milliseconds between successive task executions.
    private ClipboardManager myClipboard;
    private ClipData myClip;
    int all = 0;

    public void init(StoreDetailsFragmentBinding storeDetailsFragmentBinding, Context context, int storeId) {
        this.context = context;
        this.storeDetailsFragmentBinding = storeDetailsFragmentBinding;
        this.storeId = storeId;
        storeDetailsFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category));
        storeDetailsFragmentBinding.allId.setBackgroundTintList(null);
        getProducts("", GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context), storeId, "", "");
        if (GoldenSharedPreference.isLoggedIn(context)) {
            getStoreDetails("Bearer" + GoldenSharedPreference.getToken(context));
        } else {
            getStoreDetails("");
        }
        storeDetailsFragmentBinding.backId.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.homeFragment));
        storeDetailsFragmentBinding.cancelCopyCouponId.setOnClickListener(v -> {
            storeDetailsFragmentBinding.relativeId.setVisibility(View.GONE);
            storeDetailsFragmentBinding.copyCouponLinearId.setVisibility(View.GONE);
            Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_down);
            storeDetailsFragmentBinding.copyCouponLinearId.startAnimation(animation);

        });
        storeDetailsFragmentBinding.cancelCopyCoupon2Id.setOnClickListener(v -> {
            storeDetailsFragmentBinding.relativeId.setVisibility(View.GONE);
            storeDetailsFragmentBinding.copyCouponLinear2Id.setVisibility(View.GONE);
            Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_down);
            storeDetailsFragmentBinding.copyCouponLinear2Id.startAnimation(animation);
            });
        storeDetailsFragmentBinding.appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {

            if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                //  Collapsed
                storeDetailsFragmentBinding.sliderId.setVisibility(View.GONE);

            } else {
                //Expanded
                storeDetailsFragmentBinding.sliderId.setVisibility(View.VISIBLE);

            }
        });
        storeDetailsFragmentBinding.yesActiveId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        tabLayout();
        addsBannerAdapter = new SlidersAdapter(sliderSize, context);
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage < interfaceSize) {
                storeDetailsFragmentBinding.sliderId.setCurrentItem(currentPage++, true);
                if (currentPage == interfaceSize) {
                    currentPage = 0;
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
        if (GoldenSharedPreference.isLoggedIn(context)) {
            getStoreDetails("Bearer" + GoldenSharedPreference.getToken(context));
        } else {
            getStoreDetails("");
        }

    }

    private void social() {
        storeDetailsFragmentBinding.fbId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://www.facebook.com/goldencouponzz/");
            }
        });
        storeDetailsFragmentBinding.snapId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://www.snapchat.com/add/goldencouponz?share_id=9XqCMUv88XE&locale=ar-EG*/");
            }
        });
        storeDetailsFragmentBinding.instagramId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://www.instagram.com/golden.couponz/");
            }
        });
        storeDetailsFragmentBinding.youtubeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://m.youtube.com/channel/UCBGqENRX6vzULXYfan-1hgw");
            }
        });
    }

    private void social2() {
        storeDetailsFragmentBinding.fb2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://www.facebook.com/goldencouponzz/");
            }
        });
        storeDetailsFragmentBinding.snap2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://www.snapchat.com/add/goldencouponz?share_id=9XqCMUv88XE&locale=ar-EG*/");
            }
        });
        storeDetailsFragmentBinding.instagram2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://www.instagram.com/golden.couponz/");
            }
        });
        storeDetailsFragmentBinding.youtube2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://m.youtube.com/channel/UCBGqENRX6vzULXYfan-1hgw");
            }
        });

    }

    private void dialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.rate_dialog);
        TextView yes, no;
        yes = dialog.findViewById(R.id.yesId);
        no = dialog.findViewById(R.id.noId);
        dialog.show();
        yes.setOnClickListener(v -> supportUs());
        no.setOnClickListener(v -> dialog.dismiss());
    }

    private void supportUs() {
        //https://play.google.com/store/apps/details?id=com.codesroots.goldencoupon
        Uri uri = Uri.parse("market://details?id=" + "com.codesroots.goldencoupon");
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onPageScrollStateChanged(int state) {
        Log.i("slider", String.valueOf(addsBannerAdapter.getCount()));
        if (state == addsBannerAdapter.getCount()) {
            storeDetailsFragmentBinding.sliderId.setCurrentItem(0, true);
        }

    }

    private void downLogin() {
        storeDetailsFragmentBinding.relativeId.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_down);
        storeDetailsFragmentBinding.logLinearId.startAnimation(animation);
        storeDetailsFragmentBinding.logLinearId.setVisibility(View.GONE);
         }

    private void getStoreDetails(String token) {
        storeDetailsFragmentBinding.progress.setVisibility(View.VISIBLE);
        storeDetailsFragmentBinding.allId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all = 1;
                storeDetailsFragmentBinding.subCategoryRecyclerView.setVisibility(View.GONE);
                storeDetailsFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category));
                storeDetailsFragmentBinding.allId.setBackgroundTintList(null);
                if (GoldenSharedPreference.isLoggedIn(context)) {
                    getStoreDetails("Bearer" + GoldenSharedPreference.getToken(context));
                } else {
                    getStoreDetails("");
                }

            }

        });
        String lang = GoldenNoLoginSharedPreference.getUserLanguage(context);
        String fcmToken = "";
        int countryId = GoldenNoLoginSharedPreference.getUserCountryId(context);
        int id = storeId;
        Log.i("COUNTRYID", countryId + "?");
        apiInterface.getSingleStore(token, lang, fcmToken, countryId, String.valueOf(id)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                        if (response.body().getStore().getIsFavorite() == 0) {
                            storeDetailsFragmentBinding.favId.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_fav));
                            storeDetailsFragmentBinding.favId.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.black)));
                        } else {
                            storeDetailsFragmentBinding.favId.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_fav));
                            storeDetailsFragmentBinding.favId.setImageTintList(null);

                        }
                        storeDetailsFragmentBinding.favId.setOnClickListener(v -> {
                            if (GoldenSharedPreference.isLoggedIn(context)) {
                                if (response.body().getStore().getIsFavorite() == 0) {
                                    storeFav();
                                } else {
                                    removeFav();
                                }
                            } else {
                                loginCheck();
                            }
                        });

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
                        if (!response.body().getStore().getStore_coupons().isEmpty()) {
                            CopounzAdapter categoriesAdapter = new CopounzAdapter(context, listener);
                            categoriesAdapter.setStores(response.body().getStore().getStore_coupons());
                            storeDetailsFragmentBinding.recyclerId.setAdapter(categoriesAdapter);
                            //coupon click
                            Picasso.get().load(response.body().getStore().getFile()).into(storeDetailsFragmentBinding.copyCouponImgId);
                            Picasso.get().load(response.body().getStore().getFile()).into(storeDetailsFragmentBinding.copyCouponImg2Id);
                            storeDetailsFragmentBinding.couponValueId.setText(response.body().getStore().getStore_coupons().get(position).getCoupon());
                            storeDetailsFragmentBinding.couponValue2Id.setText(response.body().getStore().getStore_coupons().get(position).getCoupon());
                            storeDetailsFragmentBinding.copyId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    storeDetailsFragmentBinding.relativeId.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < storeDetailsFragmentBinding.nestedId.getChildCount(); i++) {
                                        View child = storeDetailsFragmentBinding.nestedId.getChildAt(i);
                                        child.setEnabled(false);
                                    }
                                    for (int i = 0; i < storeDetailsFragmentBinding.appbar.getChildCount(); i++) {
                                        View child = storeDetailsFragmentBinding.appbar.getChildAt(i);
                                        child.setEnabled(false);
                                    }
                                    storeDetailsFragmentBinding.copyCouponLinear2Id.setVisibility(View.VISIBLE);
                                    storeDetailsFragmentBinding.copyCouponLinearId.setVisibility(View.GONE);
                                    Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_up);
                                    storeDetailsFragmentBinding.copyCouponLinear2Id.startAnimation(animation);
                                    social2();
                                }
                            });
                            storeDetailsFragmentBinding.copy2Id.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    copyFun(response.body().getStore().getStore_coupons().get(position).getId(), response.body().getStore().getStore_coupons().get(position).getCouponLink());
                                }
                            });
                            storeDetailsFragmentBinding.goToYoutubeId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openUrl(response.body().getStore().getStore_coupons().get(position).getVideoFile());
                                }
                            });
                            storeDetailsFragmentBinding.shareId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    shareVia(response.body().getStore().getStore_coupons().get(position).getCoupon(),
                                            response.body().getStore().getTitle(), response.body().getStore().getStore_coupons().get(position).getCouponLink());
                                }
                            });
                        }
                        storeDetailsFragmentBinding.noActiveId.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openUrl(response.body().getStore().getStore_coupons().get(position).getVideoFile());

                            }
                        });

                        if (!response.body().getStore().getStoreCategories().isEmpty()) {
                            ProductsCategoriesAdapter categoriesAdapter = new ProductsCategoriesAdapter(context);
                            storeDetailsFragmentBinding.categoryRecyclerView.setAdapter(categoriesAdapter);
                            categoriesAdapter.setCategories(response.body().getStore().getStoreCategories());
                            categoriesAdapter.setOnItemClickListener(new ProductsCategoriesAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View viewItem, int position, int categoryId) {
                                    subCategory(categoryId, GoldenNoLoginSharedPreference.getUserLanguage(context));
                                }
                            });


                        }
                    } else {
                        storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                        Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void subCategory(int parentId, String lang) {
        storeDetailsFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category_uncheck));
        storeDetailsFragmentBinding.subCategoryRecyclerView.setVisibility(View.VISIBLE);
        storeDetailsFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getCategories(lang, "deviceToken", parentId).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        if (!response.body().getCategories().isEmpty()) {
                            storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                            ProductsSubCategoriesAdapter categoriesAdapter = new ProductsSubCategoriesAdapter(context);
                            storeDetailsFragmentBinding.subCategoryRecyclerView.setAdapter(categoriesAdapter);
                            categoriesAdapter.setCategories(response.body().getCategories());
                            categoriesAdapter.setOnItemClickListener(new ProductsSubCategoriesAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View viewItem, int position, int categoryId, int subCatId) {
                                    Log.i("CATEGORYIDDD", storeId + "?" + categoryId + "?" + subCatId);
                                    getProducts("", GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context), storeId, String.valueOf(categoryId), String.valueOf(subCatId));
                                }
                            });
                        } else {
                            Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                            storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                storeDetailsFragmentBinding.progress.setVisibility(View.GONE);

            }
        });

    }

    private void getProducts(String deviceToken, int country, String lang, int storeId, String catId, String subCatId) {
        storeDetailsFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getStoreProducts(deviceToken, country, lang, storeId, catId, subCatId).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                        if (!response.body().getProducts().getData().isEmpty()) {
                            ProductAdapter storeProduct = new ProductAdapter(context);
                            storeProduct.setStores(response.body().getProducts().getData());
                            storeDetailsFragmentBinding.idRVUsers.setAdapter(storeProduct);
                            storeDetailsFragmentBinding.idRVUsers.setVisibility(View.VISIBLE);

                        } else {
                            storeDetailsFragmentBinding.idRVUsers.setVisibility(View.GONE);
                            storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                        storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void copyFun(int coupon, String url) {
        apiInterface.copyCoupon(coupon).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                        String text;
                        text = storeDetailsFragmentBinding.couponValue2Id.getText().toString();
                        myClip = ClipData.newPlainText("text", text);
                        myClipboard.setPrimaryClip(myClip);
                        Toast.makeText(context, R.string.code_copy, Toast.LENGTH_SHORT).show();
                        openUrl(url);
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

    private void openUrl(String url) {
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    private void tabLayout() {
        storeDetailsFragmentBinding.tabLayoutId.addTab(storeDetailsFragmentBinding.tabLayoutId.newTab().setText(context.getResources().getString(R.string.coupons_label)));
        storeDetailsFragmentBinding.tabLayoutId.addTab(storeDetailsFragmentBinding.tabLayoutId.newTab().setText(context.getResources().getString(R.string.product_offers)));
        storeDetailsFragmentBinding.tabLayoutId.setTabGravity(TabLayout.GRAVITY_FILL);
        storeDetailsFragmentBinding.tabLayoutId.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    storeDetailsFragmentBinding.recyclerId.setVisibility(View.VISIBLE);
                    storeDetailsFragmentBinding.productLinearId.setVisibility(View.GONE);
                } else {
                    storeDetailsFragmentBinding.productLinearId.setVisibility(View.VISIBLE);
                    storeDetailsFragmentBinding.recyclerId.setVisibility(View.GONE);

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

    private void shareVia(String coupon, String store, String url) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = context.getResources().getString(R.string.share_msg_part1) + "(" + coupon + ")" +
                context.getResources().getString(R.string.share_msg_part2) + "(" + store +
                ")" + context.getResources().getString(R.string.click_link) + " " + url;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_via)));
    }

    private void storeFav() {
        String lang = GoldenNoLoginSharedPreference.getUserLanguage(context);
        String token = GoldenSharedPreference.getToken(context);
        apiInterface.userMakeFav("Bearer" + token, lang, String.valueOf(storeId)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        storeDetailsFragmentBinding.favId.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_fav));
                        storeDetailsFragmentBinding.favId.setImageTintList(null);
                        if (GoldenSharedPreference.isLoggedIn(context)) {
                            getStoreDetails("Bearer" + GoldenSharedPreference.getToken(context));
                        } else {
                            getStoreDetails("");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void removeFav() {
        String lang = GoldenNoLoginSharedPreference.getUserLanguage(context);
        String token = GoldenSharedPreference.getToken(context);
        apiInterface.userRemoveFav("Bearer" + token, lang, String.valueOf(storeId)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        storeDetailsFragmentBinding.favId.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_fav));
                        storeDetailsFragmentBinding.favId.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.black)));
                        if (GoldenSharedPreference.isLoggedIn(context)) {
                            getStoreDetails("Bearer" + GoldenSharedPreference.getToken(context));
                        } else {
                            getStoreDetails("");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();

            }
        });
    }


    public interface Listener {
        void click(int click, int position);

        void clickShare(String coupon, String store, String url);
    }

    Listener listener = new Listener() {
        @Override
        public void click(int click, int position) {
            click = click;
            position = position;
            if (click == 1) {
                storeDetailsFragmentBinding.copyCouponLinearId.setVisibility(View.VISIBLE);
                storeDetailsFragmentBinding.relativeId.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_up);
                storeDetailsFragmentBinding.copyCouponLinearId.startAnimation(animation);
                social();
            }
            if (click == 3) {
                if (GoldenSharedPreference.isLoggedIn(context)) {
                    getStoreDetails("Bearer" + GoldenSharedPreference.getToken(context));
                } else {
                    getStoreDetails("");
                }
            }
            if (click == 5) {
                loginCheck();
            }
        }

        //String coupon, String store, String url
        public void clickShare(String coupon, String store, String url) {
            shareVia(coupon, store, url);
        }

    };

    private void loginCheck() {
        storeDetailsFragmentBinding.logLinearId.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_up);
        storeDetailsFragmentBinding.logLinearId.startAnimation(animation);
        storeDetailsFragmentBinding.relativeId.setVisibility(View.VISIBLE);
        storeDetailsFragmentBinding.signId.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.loginFragment));
        storeDetailsFragmentBinding.slideDownLogId.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                downLogin();
                return true;
            }
        });
        storeDetailsFragmentBinding.cancelLogId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLogin();
            }
        });
    }
}
