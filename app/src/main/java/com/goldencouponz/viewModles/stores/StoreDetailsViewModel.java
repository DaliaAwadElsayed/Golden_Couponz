package com.goldencouponz.viewModles.stores;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;
import androidx.viewpager.widget.ViewPager;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.StoreDetailsFragmentBinding;
import com.goldencouponz.adapters.home.SlidersAdapter;
import com.goldencouponz.adapters.stores.CopounzAdapter;
import com.goldencouponz.adapters.stores.ProductAdapter;
import com.goldencouponz.adapters.stores.ProductsCategoriesAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
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

    public void init(StoreDetailsFragmentBinding storeDetailsFragmentBinding, Context context, int storeId) {
        this.context = context;
        this.storeDetailsFragmentBinding = storeDetailsFragmentBinding;
        this.storeId = storeId;
        storeDetailsFragmentBinding.cancelCopyCouponId.setOnClickListener(v -> {
            storeDetailsFragmentBinding.appbar.setBackgroundColor(context.getResources().getColor(R.color.white));
            storeDetailsFragmentBinding.appbar.setAlpha(1f);
            storeDetailsFragmentBinding.nestedId.setBackgroundColor(context.getResources().getColor(R.color.white));
            storeDetailsFragmentBinding.nestedId.setAlpha(1f);
            for (int i = 0; i < storeDetailsFragmentBinding.nestedId.getChildCount(); i++) {
                View child = storeDetailsFragmentBinding.nestedId.getChildAt(i);
                child.setEnabled(true);
            }
            for (int i = 0; i <  storeDetailsFragmentBinding.appbar.getChildCount(); i++) {
                View child = storeDetailsFragmentBinding.appbar.getChildAt(i);
                child.setEnabled(true);
            }
            storeDetailsFragmentBinding.copyCouponLinearId.setVisibility(View.GONE);
            Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_down);
            storeDetailsFragmentBinding.copyCouponLinearId.startAnimation(animation);

        });
        storeDetailsFragmentBinding.cancelCopyCoupon2Id.setOnClickListener(v -> {
            storeDetailsFragmentBinding.appbar.setBackgroundColor(context.getResources().getColor(R.color.white));
            storeDetailsFragmentBinding.appbar.setAlpha(1f);
            storeDetailsFragmentBinding.nestedId.setBackgroundColor(context.getResources().getColor(R.color.white));
            storeDetailsFragmentBinding.nestedId.setAlpha(1f);
            for (int i = 0; i < storeDetailsFragmentBinding.nestedId.getChildCount(); i++) {
                View child = storeDetailsFragmentBinding.nestedId.getChildAt(i);
                child.setEnabled(true);
            }
            for (int i = 0; i <  storeDetailsFragmentBinding.appbar.getChildCount(); i++) {
                View child = storeDetailsFragmentBinding.appbar.getChildAt(i);
                child.setEnabled(true);
            }
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
        getStoreDetails();

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
                                    storeDetailsFragmentBinding.appbar.setBackgroundColor(context.getResources().getColor(R.color.alpha));
                                    storeDetailsFragmentBinding.appbar.setAlpha(0.5f);
                                    storeDetailsFragmentBinding.nestedId.setBackgroundColor(context.getResources().getColor(R.color.alpha));
                                    storeDetailsFragmentBinding.nestedId.setAlpha(0.5f);
                                    for (int i = 0; i < storeDetailsFragmentBinding.nestedId.getChildCount(); i++) {
                                        View child = storeDetailsFragmentBinding.nestedId.getChildAt(i);
                                        child.setEnabled(false);
                                    }
                                    for (int i = 0; i <  storeDetailsFragmentBinding.appbar.getChildCount(); i++) {
                                        View child = storeDetailsFragmentBinding.appbar.getChildAt(i);
                                        child.setEnabled(false);
                                    }
                                    storeDetailsFragmentBinding.copyCouponLinear2Id.setVisibility(View.VISIBLE);
                                    Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_up);
                                    storeDetailsFragmentBinding.copyCouponLinear2Id.startAnimation(animation);
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
                        if (!response.body().getStore().getStore_products().isEmpty()) {
                            ProductAdapter storeProduct = new ProductAdapter(context);
                            storeProduct.setStores(response.body().getStore().getStore_products());
                            storeDetailsFragmentBinding.idRVUsers.setAdapter(storeProduct);
                        }
                        if (!response.body().getStore().getStoreCategories().isEmpty()) {
                            ProductsCategoriesAdapter storeProduct = new ProductsCategoriesAdapter(context);
                            storeProduct.setCategories(response.body().getStore().getStoreCategories());
                            storeDetailsFragmentBinding.categoryRecyclerView.setAdapter(storeProduct);
                            storeProduct.setOnItemClickListener(new ProductsCategoriesAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View viewItem, int position, int categoryId) {
                                    storeDetailsFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category_uncheck));
                                    ProductsCategoriesAdapter storeProduct = new ProductsCategoriesAdapter(context);
                                    storeProduct.setCategories(response.body().getStore().getStoreCategories());
                                    storeDetailsFragmentBinding.categoryRecyclerView.setAdapter(storeProduct);

                                }
                            });
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

    public interface Listener {
        void click(int click, int position);
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

    private void disableClicksDialog1() {
        storeDetailsFragmentBinding.shareId.setClickable(false);
        storeDetailsFragmentBinding.tabLayoutId.setClickable(false);
        storeDetailsFragmentBinding.couponzLinearId.setClickable(false);
        storeDetailsFragmentBinding.shareId.setClickable(false);
        storeDetailsFragmentBinding.copy2Id.setClickable(false);
        storeDetailsFragmentBinding.copyCouponLinear2Id.setClickable(false);
        storeDetailsFragmentBinding.noActiveId.setClickable(false);
        storeDetailsFragmentBinding.yesActiveId.setClickable(false);
        storeDetailsFragmentBinding.cancelCopyCouponId.setClickable(true);
        storeDetailsFragmentBinding.copyId.setClickable(true);
        storeDetailsFragmentBinding.goToYoutube2Id.setEnabled(false);
        storeDetailsFragmentBinding.goToYoutubeId.setEnabled(true);
    }

    private void disableClicksDialog2() {
        storeDetailsFragmentBinding.shareId.setClickable(false);
        storeDetailsFragmentBinding.tabLayoutId.setClickable(false);
        storeDetailsFragmentBinding.couponzLinearId.setClickable(false);
        storeDetailsFragmentBinding.shareId.setClickable(false);
        storeDetailsFragmentBinding.copy2Id.setClickable(true);
        storeDetailsFragmentBinding.copyCouponLinear2Id.setClickable(true);
        storeDetailsFragmentBinding.noActiveId.setClickable(true);
        storeDetailsFragmentBinding.yesActiveId.setClickable(true);
        storeDetailsFragmentBinding.copyId.setClickable(false);
        storeDetailsFragmentBinding.cancelCopyCouponId.setClickable(false);
        storeDetailsFragmentBinding.goToYoutube2Id.setEnabled(true);
        storeDetailsFragmentBinding.goToYoutubeId.setEnabled(false);

    }

    Listener listener = new Listener() {
        @Override
        public void click(int click, int position) {
            click = click;
            position = position;
            if (click == 1) {
                storeDetailsFragmentBinding.copyCouponLinearId.setVisibility(View.VISIBLE);
                storeDetailsFragmentBinding.appbar.setBackgroundColor(context.getResources().getColor(R.color.alpha));
                storeDetailsFragmentBinding.appbar.setAlpha(0.5f);
                storeDetailsFragmentBinding.nestedId.setBackgroundColor(context.getResources().getColor(R.color.alpha));
                storeDetailsFragmentBinding.nestedId.setAlpha(0.5f);
                for (int i = 0; i < storeDetailsFragmentBinding.nestedId.getChildCount(); i++) {
                    View child = storeDetailsFragmentBinding.nestedId.getChildAt(i);
                    child.setEnabled(false);
                }
                for (int i = 0; i <  storeDetailsFragmentBinding.appbar.getChildCount(); i++) {
                    View child = storeDetailsFragmentBinding.appbar.getChildAt(i);
                    child.setEnabled(false);
                }
                Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_up);
                storeDetailsFragmentBinding.copyCouponLinearId.startAnimation(animation);
            }
        }

    };
}