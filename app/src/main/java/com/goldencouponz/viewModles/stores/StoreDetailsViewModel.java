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
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.CopyCouponDialogBinding;
import com.e.goldencouponz.databinding.LoginCheckDialogBinding;
import com.e.goldencouponz.databinding.NoCouponProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.ProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.SecondCopyCouponDialogBinding;
import com.e.goldencouponz.databinding.SecondProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.StoreDetailsFragmentBinding;
import com.goldencouponz.adapters.stores.CopounzAdapter;
import com.goldencouponz.adapters.stores.ProductSlidersAdapter;
import com.goldencouponz.adapters.stores.ProductStoreAdapter;
import com.goldencouponz.adapters.stores.ProductsCategoriesAdapter;
import com.goldencouponz.adapters.stores.ProductsSubCategoriesAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.room.DatabaseClient;
import com.goldencouponz.room.SavedData;
import com.goldencouponz.utility.Utility;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;
import com.goldencouponz.viewModles.home.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
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
    ProductSlidersAdapter addsBannerAdapter;
    int interfaceSize;
    Timer timer;
    int currentPag = 0;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 2000; // time in milliseconds between successive task executions.
    private ClipboardManager myClipboard;
    private ClipData myClip;
    int all = 0;
    private int currentPagee = 1;
    ProductStoreAdapter storeProduct;
    int lastPage;
    String type;
    CopyCouponDialogBinding copyCouponDialogBinding;
    SecondCopyCouponDialogBinding secondCopyCouponDialogBinding;
    SecondProductDetailsDialogBinding secondProductDetailsDialogBinding;
    ProductDetailsDialogBinding productDetailsDialogBinding;
    NoCouponProductDetailsDialogBinding noCouponProductDetailsDialogBinding;
    LoginCheckDialogBinding loginCheckDialogBinding;
    BottomSheetDialog showCopyCouponDialog;
    BottomSheetDialog loginCheckDialog;
    BottomSheetDialog showProductDetailsDialog;
    BottomSheetDialog secondShowProductDetailsDialog;
    BottomSheetDialog showSecondCopyCouponDialog;
    BottomSheetDialog showNoCouponDialog;

    String youTube = "https://www.youtube.com/@goldencouponz";
    String snapChat = "https://www.snapchat.com/add/goldencouponz?share_id=9XqCMUv88XE&locale=ar-EG*/";
    String instagram = "https://www.instagram.com/golden.couponz/";
    String facebook = "https://www.facebook.com/goldencouponzz/";

    public void init(StoreDetailsFragmentBinding storeDetailsFragmentBinding,
                     CopyCouponDialogBinding copyCouponDialogBinding,
                     SecondCopyCouponDialogBinding secondCopyCouponDialogBinding,
                     ProductDetailsDialogBinding productDetailsDialogBinding,
                     SecondProductDetailsDialogBinding secondProductDetailsDialogBinding,
                     NoCouponProductDetailsDialogBinding noCouponProductDetailsDialogBinding,
                     LoginCheckDialogBinding loginCheckDialogBinding,
                     Context context, int storeId, String type) {
        this.context = context;
        this.storeDetailsFragmentBinding = storeDetailsFragmentBinding;
        this.copyCouponDialogBinding = copyCouponDialogBinding;
        this.secondCopyCouponDialogBinding = secondCopyCouponDialogBinding;
        this.secondProductDetailsDialogBinding = secondProductDetailsDialogBinding;
        this.productDetailsDialogBinding = productDetailsDialogBinding;
        this.loginCheckDialogBinding = loginCheckDialogBinding;
        this.noCouponProductDetailsDialogBinding = noCouponProductDetailsDialogBinding;
        this.storeId = storeId;
        this.type = type;
        showCopyCouponDialog = new BottomSheetDialog(context);
        loginCheckDialog = new BottomSheetDialog(context);
        showSecondCopyCouponDialog = new BottomSheetDialog(context);
        showNoCouponDialog = new BottomSheetDialog(context);
        secondShowProductDetailsDialog = new BottomSheetDialog(context);
        showProductDetailsDialog = new BottomSheetDialog(context);
        storeDetailsFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category));
        storeDetailsFragmentBinding.allId.setBackgroundTintList(null);
        storeProduct = new ProductStoreAdapter(context, listener);
        if (GoldenSharedPreference.isLoggedIn(context)) {
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
                            getStoreDetails("Bearer" + GoldenSharedPreference.getToken(context), token);
                            storeProduct = new ProductStoreAdapter(context, listener);
                            getProducts("", token, GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context), String.valueOf(storeId), "", "", currentPagee);
                            storeDetailsFragmentBinding.idNesteddSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                @Override
                                public void onScrollChange(NestedScrollView view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                    if (scrollY == view.getChildAt(0).getMeasuredHeight() - view.getMeasuredHeight()) {
                                        currentPagee++;
                                        Log.i("currentPageScrolling", String.valueOf(currentPagee));
                                        Log.i("LastPageScrolling", String.valueOf(lastPage));
                                        if (currentPagee <= lastPage) {
                                            Log.i("currentPageIf", String.valueOf(currentPagee));
                                            getProducts("", token, GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context), String.valueOf(storeId), "", "", currentPagee);
                                        }
                                    }
                                }
                            });

                        }
                    });
        } else {
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
                            getStoreDetails("", token);
                            getProducts("", token, GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context), String.valueOf(storeId), "", "", currentPagee);
                            storeDetailsFragmentBinding.idNesteddSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                @Override
                                public void onScrollChange(NestedScrollView view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                    if (scrollY == view.getChildAt(0).getMeasuredHeight() - view.getMeasuredHeight()) {
                                        currentPagee++;
                                        Log.i("LastPageScrolling", String.valueOf(lastPage));
                                        if (currentPagee <= lastPage) {
                                            getProducts("", token, GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context), String.valueOf(storeId), "", "", currentPagee);
                                        }
                                    }
                                }
                            });

                        }
                    });
        }
        storeDetailsFragmentBinding.backId.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.homeFragment));
        storeDetailsFragmentBinding.appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {

            if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                //  Collapsed
                storeDetailsFragmentBinding.sliderId.setVisibility(View.GONE);

            } else {
                //Expanded
                storeDetailsFragmentBinding.sliderId.setVisibility(View.VISIBLE);

            }
        });
        tabLayout();
        addsBannerAdapter = new ProductSlidersAdapter(sliderSize, context);
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPag < interfaceSize) {
                storeDetailsFragmentBinding.sliderId.setCurrentItem(currentPag++, true);
                if (currentPag == interfaceSize) {
                    currentPag = 0;
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
                            getStoreDetails("Bearer" + GoldenSharedPreference.getToken(context), token);
                        }
                    });
        } else {
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

                            getStoreDetails("", token);
                        }
                    });
        }


    }

    //saveItemsInDataBase
    private void saveTask(String name) {
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                //creating a task
                SavedData task = new SavedData();
                Log.i("SAVEDWORDS","??"+storeId+ storeDetailsFragmentBinding.storeNameId.getText().toString());
                task.setWord(name);
                task.setStoreId(storeId);
                task.setType("store");
                //adding to database
                DatabaseClient.getInstance(context).getAppDatabase()
                        .taskDao()
                        .insert(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.i("DATABASE", "SAVED");
            }
        }

        SaveTask st = new SaveTask();
        st.execute();

    }

    private void social() {
        copyCouponDialogBinding.fbId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(facebook);
            }
        });
        copyCouponDialogBinding.snapId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(snapChat);
            }
        });
        copyCouponDialogBinding.instagramId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(instagram);
            }
        });
        copyCouponDialogBinding.youtubeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(youTube);
            }
        });
    }

    private void social2() {
        secondCopyCouponDialogBinding.fb2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(facebook);
            }
        });
        secondCopyCouponDialogBinding.snap2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(snapChat);
            }
        });
        secondCopyCouponDialogBinding.instagram2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(instagram);
            }
        });
        secondCopyCouponDialogBinding.youtube2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(youTube);
            }
        });

    }

    private void social3() {
        productDetailsDialogBinding.fb2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(facebook);
            }
        });
        productDetailsDialogBinding.snap2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(snapChat);
            }
        });
        productDetailsDialogBinding.instagram2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(instagram);
            }
        });
        productDetailsDialogBinding.youtube2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(youTube);
            }
        });

    }

    private void social5() {
        noCouponProductDetailsDialogBinding.fb2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(facebook);
            }
        });
        noCouponProductDetailsDialogBinding.snap2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(snapChat);
            }
        });
        noCouponProductDetailsDialogBinding.instagram2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(instagram);
            }
        });
        noCouponProductDetailsDialogBinding.youtube2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(youTube);
            }
        });

    }

    private void social4() {
        secondProductDetailsDialogBinding.fb2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(facebook);
            }
        });
        secondProductDetailsDialogBinding.snap2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(snapChat);
            }
        });
        secondProductDetailsDialogBinding.instagram2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(instagram);
            }
        });
        secondProductDetailsDialogBinding.youtube2Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(youTube);
            }
        });

    }

    private void rateDialog() {
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


    private void getStoreDetails(String token, String fcmToken) {
        storeDetailsFragmentBinding.progress.setVisibility(View.VISIBLE);
        storeDetailsFragmentBinding.allId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all = 1;
                storeDetailsFragmentBinding.subCategoryRecyclerView.setVisibility(View.GONE);
                storeDetailsFragmentBinding.allId.setBackground(context.getResources().getDrawable(R.drawable.bk_category));
                storeDetailsFragmentBinding.allId.setBackgroundTintList(null);
                if (GoldenSharedPreference.isLoggedIn(context)) {
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
                                    storeProduct = new ProductStoreAdapter(context, listener);
                                    currentPagee = 1;
                                    getProducts("", token, GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context), String.valueOf(storeId), "", "", currentPagee);
                                    getStoreDetails("Bearer" + GoldenSharedPreference.getToken(context), token);
                                }
                            });
                } else {
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
                                    storeProduct = new ProductStoreAdapter(context, listener);
                                    currentPagee = 1;
                                    getProducts("", token, GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context), String.valueOf(storeId), "", "", currentPagee);
                                    getStoreDetails("", token);
                                }
                            });
                }

            }

        });
        String lang = GoldenNoLoginSharedPreference.getUserLanguage(context);
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
                                loginCheckDialog();
                            }
                        });

                        Picasso.get().load(response.body().getStore().getFile()).into(storeDetailsFragmentBinding.storeImgId);
                        Picasso.get().load(response.body().getStore().getCover()).into(storeDetailsFragmentBinding.coverId);
                        storeDetailsFragmentBinding.storeNameId.setText(response.body().getStore().getTitle());
                        if (type.equals("search")) {
                            Log.i("SAVEDINSEARCH","?");
                            saveTask(response.body().getStore().getTitle());
                        }
                        storeDetailsFragmentBinding.storeCatId.setText(response.body().getStore().getDetails());
                        if (!response.body().getStore().getStoreSliders().isEmpty()) {
                            addsBannerAdapter.setSliders(response.body().getStore().getStoreSliders());
                            addsBannerAdapter.setOnItemClickListener(new ProductSlidersAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View viewItem, int position) {
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
                                                    if (GoldenSharedPreference.isLoggedIn(context)) {
                                                        getSingleCoupons("Bearer" + GoldenSharedPreference.getToken(context), 0, "slider");
                                                    } else {
                                                        getSingleCoupons("", 0, "slider");
                                                    }
//                                                    getSingleSliderCoupons("Bearer" + GoldenSharedPreference.getToken(context), 0, token);
                                                }
                                            });
                                }
                            });
                        }
                        if (!response.body().getStore().getStore_coupons().isEmpty()) {
                            CopounzAdapter categoriesAdapter = new CopounzAdapter(context, listener);
                            categoriesAdapter.setStores(response.body().getStore().getStore_coupons());
                            storeDetailsFragmentBinding.recyclerId.setAdapter(categoriesAdapter);
                        }
                        if (!response.body().getStore().getStoreCategories().isEmpty()) {
                            ProductsCategoriesAdapter categoriesAdapter = new ProductsCategoriesAdapter(context);
                            storeDetailsFragmentBinding.categoryRecyclerView.setAdapter(categoriesAdapter);
                            categoriesAdapter.setCategories(response.body().getStore().getStoreCategories());
                            categoriesAdapter.setOnItemClickListener(new ProductsCategoriesAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View viewItem, int position, int categoryId) {
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
                                                    storeProduct = new ProductStoreAdapter(context, listener);
                                                    currentPagee = 1;
                                                    getProducts("", token, GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context), String.valueOf(storeId), String.valueOf(categoryId), "", currentPagee);
                                                }
                                            });
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


    private void getSingleCoupons(String token, int position, String clickType) {
        if (clickType.equals("slider")) {
            showSecondCopyCouponDialog();
        } else {
            showCopyCouponDialog();

        }
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
                        if (!response.body().getStore().getStore_coupons().isEmpty()) {
                            CopounzAdapter categoriesAdapter = new CopounzAdapter(context, listener);
                            categoriesAdapter.setStores(response.body().getStore().getStore_coupons());
                            storeDetailsFragmentBinding.recyclerId.setAdapter(categoriesAdapter);
                            //coupon click
                            Picasso.get().load(response.body().getStore().getFile()).into(copyCouponDialogBinding.copyCouponImgId);
                            Picasso.get().load(response.body().getStore().getFile()).into(secondCopyCouponDialogBinding.copyCouponImg2Id);
                            copyCouponDialogBinding.couponValueId.setText(response.body().getStore().getStore_coupons().get(position).getCoupon());
                            secondCopyCouponDialogBinding.couponValue2Id.setText(response.body().getStore().getStore_coupons().get(position).getCoupon());
                            copyCouponDialogBinding.copyId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showSecondCopyCouponDialog();
                                }
                            });
                            secondCopyCouponDialogBinding.copy2Id.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    copyFun(response.body().getStore().getStore_coupons().get(position).getId(), response.body().getStore().getStoreLink());
                                }
                            });
                            if (response.body().getStore().getStore_coupons().get(position).getVideoFile() == null) {
                                secondCopyCouponDialogBinding.goToYoutube2Id.setVisibility(View.GONE);
                                copyCouponDialogBinding.goToYoutubeId.setVisibility(View.GONE);

                            } else {
                                copyCouponDialogBinding.goToYoutubeId.setVisibility(View.VISIBLE);
                                secondCopyCouponDialogBinding.goToYoutube2Id.setVisibility(View.VISIBLE);
                            }
                            copyCouponDialogBinding.goToYoutubeId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openUrl(response.body().getStore().getStore_coupons().get(position).getVideoFile());
                                }
                            });
                            secondCopyCouponDialogBinding.goToYoutube2Id.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openUrl(response.body().getStore().getStore_coupons().get(position).getVideoFile());
                                }
                            });
                            secondCopyCouponDialogBinding.noActiveId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sendNoActive(secondCopyCouponDialogBinding.couponValue2Id.getText().toString(),
                                            response.body().getStore().getTitle(), response.body().getStore().getStore_coupons().get(position).getWhatsapp()
                                    );
                                }
                            });
                            secondCopyCouponDialogBinding.shareId.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    shareVia(response.body().getStore().getStore_coupons().get(position).getCoupon(),
                                            response.body().getStore().getTitle(), response.body().getStore().getStore_coupons().get(position).getCouponLink());
                                }
                            });

                        }

                    }
                } else {
                    storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();

                }
            }


            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailuree", t.toString());
                storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getSingleProduct(String id, String deviceToken, int country, String lang, String storeId, String catId, String subCatId) {
        storeDetailsFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getStoreProducts(id, deviceToken, country, lang, storeId, catId, subCatId, "", "", 1).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        Log.i("IDDDDD", id + "?");
                        storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                        //product dialog
                        productDetailsDialogBinding.newPriceId.setText(Utility.fixNullString(response.body().getSingleProduct().getProductCountry().getDiscountPrice()));
                        productDetailsDialogBinding.currentPriceId.setPaintFlags(productDetailsDialogBinding.currentPriceId.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        productDetailsDialogBinding.discountId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getProductCountry().getDiscount())));
                        productDetailsDialogBinding.currentPriceId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getProductCountry().getPrice())));
                        productDetailsDialogBinding.detailsId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getDetails())));
                        Picasso.get().load(response.body().getSingleProduct().getFile()).into(productDetailsDialogBinding.productId);
                        productDetailsDialogBinding.currencyNewId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                        productDetailsDialogBinding.currencyCurrentId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                        Picasso.get().load(response.body().getSingleProduct().getStore().getFile()).into(productDetailsDialogBinding.storeImgId);
                        productDetailsDialogBinding.couponValueId.setText(Utility.fixNullString(response.body().getSingleProduct().getCoupon()));
                        productDetailsDialogBinding.shareId.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shareVia(response.body().getSingleProduct().getCoupon(), response.body().getSingleProduct().getStore().getTitle()
                                        , response.body().getSingleProduct().getStore().getStoreLink());
                            }
                        });
                        secondProductDetailsDialogBinding.noActiveId.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendNoActive(secondProductDetailsDialogBinding.couponValueId.getText().toString(),
                                        response.body().getSingleProduct().getStore().getTitle(), response.body().getProducts().getWhatsapp()
                                );
                            }
                        });

                        productDetailsDialogBinding.couponValue2Id.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showSecondProductDetailsDialog();

                            }
                        });
//nocoupon//
                        noCouponProductDetailsDialogBinding.newPriceId.setText(Utility.fixNullString(response.body().getSingleProduct().getProductCountry().getDiscountPrice()));
                        noCouponProductDetailsDialogBinding.currentPriceId.setPaintFlags(productDetailsDialogBinding.currentPriceId.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        noCouponProductDetailsDialogBinding.discountId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getProductCountry().getDiscount())));
                        noCouponProductDetailsDialogBinding.currentPriceId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getProductCountry().getPrice())));
                        noCouponProductDetailsDialogBinding.detailsId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getDetails())));
                        Picasso.get().load(response.body().getSingleProduct().getFile()).into(noCouponProductDetailsDialogBinding.productId);
                        noCouponProductDetailsDialogBinding.currencyNewId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                        noCouponProductDetailsDialogBinding.currencyCurrentId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                        Picasso.get().load(response.body().getSingleProduct().getStore().getFile()).into(noCouponProductDetailsDialogBinding.storeImgId);
                        noCouponProductDetailsDialogBinding.shareId.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shareOfferVia(response.body().getSingleProduct().getStore().getTitle()
                                        , response.body().getSingleProduct().getStore().getStoreLink());
                            }
                        });
                        noCouponProductDetailsDialogBinding.couponValue2Id.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openUrl(response.body().getSingleProduct().getProductLink());
                                noCouponProductDetailsDialogBinding.recomendId.setVisibility(View.VISIBLE);
                            }
                        });
                        ////
                        secondProductDetailsDialogBinding.newPriceId.setText(Utility.fixNullString(response.body().getSingleProduct().getProductCountry().getDiscountPrice()));
                        secondProductDetailsDialogBinding.currentPriceId.setPaintFlags(productDetailsDialogBinding.currentPriceId.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        secondProductDetailsDialogBinding.discountId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getProductCountry().getDiscount())));
                        secondProductDetailsDialogBinding.currentPriceId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getProductCountry().getPrice())));
                        secondProductDetailsDialogBinding.detailsId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getDetails())));
                        Picasso.get().load(response.body().getSingleProduct().getFile()).into(secondProductDetailsDialogBinding.productId);
                        secondProductDetailsDialogBinding.currencyNewId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                        secondProductDetailsDialogBinding.currencyCurrentId.setText(Utility.fixNullString(GoldenNoLoginSharedPreference.getUserCurrency(context)));
                        Picasso.get().load(response.body().getSingleProduct().getStore().getFile()).into(secondProductDetailsDialogBinding.storeImgId);
                        secondProductDetailsDialogBinding.couponValueId.setText(Utility.fixNullString(response.body().getSingleProduct().getCoupon()));
                        secondProductDetailsDialogBinding.shareId.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shareVia(response.body().getSingleProduct().getCoupon(), response.body().getSingleProduct().getStore().getTitle()
                                        , response.body().getSingleProduct().getStore().getStoreLink());
                            }
                        });
                        productDetailsDialogBinding.couponValue2Id.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showSecondProductDetailsDialog();
                            }
                        });
                        secondProductDetailsDialogBinding.copy2Id.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(context, Html.fromHtml("<font><b>" + context.getResources().getString(R.string.code_copy) + "</b></font>"), Toast.LENGTH_SHORT).show();
                                myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                                String text;
                                text = secondProductDetailsDialogBinding.couponValueId.getText().toString();
                                myClip = ClipData.newPlainText("text", text);
                                myClipboard.setPrimaryClip(myClip);
                                openUrl(response.body().getSingleProduct().getProductLink());
                            }
                        });
                        if (response.body().getSingleProduct().getVideoLink() == null) {
                            secondProductDetailsDialogBinding.goToYoutube2Id.setVisibility(View.GONE);
                            productDetailsDialogBinding.goToYoutube2Id.setVisibility(View.GONE);

                        } else {
                            productDetailsDialogBinding.goToYoutube2Id.setVisibility(View.VISIBLE);
                            secondProductDetailsDialogBinding.goToYoutube2Id.setVisibility(View.VISIBLE);
                        }
                        secondProductDetailsDialogBinding.goToYoutube2Id.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openUrl(response.body().getSingleProduct().getVideoLink());
                            }
                        });

                        ////////////////
                    } else {
                        storeDetailsFragmentBinding.idRVUsers.setVisibility(View.GONE);
                        storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                    storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
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
                                                    storeProduct = new ProductStoreAdapter(context, listener);
                                                    currentPagee = 1;
                                                    getProducts("", token, GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context), String.valueOf(storeId), String.valueOf(categoryId), String.valueOf(subCatId), currentPagee);
                                                }
                                            });
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

    private void getProducts(String id, String deviceToken, int country, String lang, String storeId, String catId, String subCatId, int page) {
        storeDetailsFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getStoreProducts(id, deviceToken, country, lang, storeId, catId, subCatId, "", "", page).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 && response.body() != null) {
                        storeDetailsFragmentBinding.progress.setVisibility(View.GONE);
                        if (!response.body().getProducts().getData().isEmpty()) {
                            storeDetailsFragmentBinding.idRVUsers.setAdapter(storeProduct);
                            lastPage = response.body().getProducts().getLastPage();
                            storeProduct.setStores(response.body().getProducts().getData());
                            storeDetailsFragmentBinding.idRVUsers.setVisibility(View.VISIBLE);
                            ////////////////
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
                        text = secondCopyCouponDialogBinding.couponValue2Id.getText().toString();
                        myClip = ClipData.newPlainText("text", text);
                        myClipboard.setPrimaryClip(myClip);
                        Toast.makeText(context, Html.fromHtml("<font><b>" + context.getResources().getString(R.string.code_copy) + "</b></font>"), Toast.LENGTH_SHORT).show();
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
        Log.i("URL", "?" + url);
        if (url != null) {
            if (url.contains("http://") || url.contains("https://")) {
                Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Wrong Url", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Missing Url", Toast.LENGTH_SHORT).show();
        }
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

    private void sendNoActive(String code, String store, String phone) {
        try {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setPackage("com.whatsapp");
            String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + context.getResources().getString(R.string.this_coupon) + "\"" + code + "\"" +
                    context.getResources().getString(R.string.not_vaild) + "\"" + store + "\"";
            sendIntent.setData(Uri.parse(url));
            context.startActivity(sendIntent);

        } catch (Exception e) {
            Log.i("EXCEPTIONn", e.toString());
            Toast.makeText(context, "WhatsApp Not Install", Toast.LENGTH_SHORT).show();
        }


    }


    private void shareOfferVia(String store, String url) {
        Intent sharingIntent = new Intent(Intent.ACTION_SENDTO);
        sharingIntent.setType("text/plain");
        String shareBody = store
                + " " + url;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_via)));
    }

    private void shareVia(String coupon, String store, String url) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = context.getResources().getString(R.string.share_msg_part1) + "\"" + coupon + "\"" +
                context.getResources().getString(R.string.share_msg_part2) + "\"" + store +
                "\"" + context.getResources().getString(R.string.click_link) + " " + url;
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

                                            getStoreDetails("Bearer" + GoldenSharedPreference.getToken(context), token);
                                        }
                                    });
                        } else {
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

                                            getStoreDetails("", token);
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

                                            getStoreDetails("Bearer" + GoldenSharedPreference.getToken(context), token);
                                        }
                                    });
                        } else {
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

                                            getStoreDetails("", token);
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


    public interface Listener {
        void click(int click, int position, int id, String coupon);

        void clickShare(String coupon, String store, String url);
    }

    Listener listener = new Listener() {
        @Override
        public void click(int click, int position, int idd, String coupon) {
            click = click;
            position = position;
            if (click == 1) {
                //open copyCouponLinear
                if (GoldenSharedPreference.isLoggedIn(context)) {
                    getSingleCoupons("Bearer" + GoldenSharedPreference.getToken(context), position, "coupon");
                } else {
                    getSingleCoupons("", position, "coupon");
                }
                social();
            }
            if (click == 3) {
                //Fav
                if (GoldenSharedPreference.isLoggedIn(context)) {
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

                                    getStoreDetails("Bearer" + GoldenSharedPreference.getToken(context), token);
                                }
                            });
                } else {
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

                                    getStoreDetails("", token);
                                }
                            });
                }
            }
            if (click == 4) {
                int finalPosition = position;
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                    return;
                                }
                                // Get new FCM registration token
                                String deviceToken = task.getResult();
                                getSingleProduct(String.valueOf(idd), deviceToken, GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context), String.valueOf(storeId), "", "");
                                if (coupon == null || coupon.equals("null") || coupon.isEmpty()) {
                                    showNoCouponProductDetailsDialog();
                                } else {
                                    showProductDetailsDialog();
                                }
                            }
                        });
            }
            if (click == 5) {
                loginCheckDialog();
            }
        }

        //String coupon, String store, String url
        public void clickShare(String coupon, String store, String url) {
            if (coupon.equals("no")) {
                shareOfferVia(store, url);
            } else {
                shareVia(coupon, store, url);
            }
        }

    };

    private void showProductDetailsDialog() {
        final View view4 = productDetailsDialogBinding.getRoot();
        showProductDetailsDialog.setContentView(view4);
        showProductDetailsDialog.setCancelable(true);
        showProductDetailsDialog.show();
        productDetailsDialogBinding.cancelLogId.setOnClickListener(v1 -> showProductDetailsDialog.cancel());
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
            productDetailsDialogBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        } else {
            productDetailsDialogBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        social3();

    }

    private void showSecondProductDetailsDialog() {
        final View view5 = secondProductDetailsDialogBinding.getRoot();
        secondShowProductDetailsDialog.setContentView(view5);
        secondShowProductDetailsDialog.setCancelable(true);
        secondShowProductDetailsDialog.show();
        secondProductDetailsDialogBinding.cancelLogId.setOnClickListener(v8 -> secondShowProductDetailsDialog.cancel());
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
            secondProductDetailsDialogBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        } else {
            secondProductDetailsDialogBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        social4();
        secondProductDetailsDialogBinding.yesActiveId.setOnClickListener(v7 -> rateDialog());

    }

    private void showNoCouponProductDetailsDialog() {
        final View view5 = noCouponProductDetailsDialogBinding.getRoot();
        showNoCouponDialog.setContentView(view5);
        showNoCouponDialog.setCancelable(true);
        showNoCouponDialog.show();
        noCouponProductDetailsDialogBinding.cancelLogId.setOnClickListener(v8 -> showNoCouponDialog.cancel());
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
            noCouponProductDetailsDialogBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        } else {
            noCouponProductDetailsDialogBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        social5();
        noCouponProductDetailsDialogBinding.rateNwId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportUs();
            }
        });

    }

    private void showSecondCopyCouponDialog() {
        final View view3 = secondCopyCouponDialogBinding.getRoot();
        showSecondCopyCouponDialog.setContentView(view3);
        showSecondCopyCouponDialog.setCancelable(true);
        showSecondCopyCouponDialog.show();
        secondCopyCouponDialogBinding.cancelCopyCoupon2Id.setOnClickListener(v1 -> showSecondCopyCouponDialog.cancel());
        secondCopyCouponDialogBinding.yesActiveId.setOnClickListener(v2 -> rateDialog());
    }

    private void showCopyCouponDialog() {
        final View view1 = copyCouponDialogBinding.getRoot();
        showCopyCouponDialog.setContentView(view1);
        showCopyCouponDialog.setCancelable(true);
        showCopyCouponDialog.show();
        copyCouponDialogBinding.cancelCopyCouponId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                showCopyCouponDialog.dismiss();

            }
        });
        social2();
    }

    private void loginCheckDialog() {
        final View view2 = loginCheckDialogBinding.getRoot();
        loginCheckDialog.setContentView(view2);
        loginCheckDialog.setCancelable(true);
        loginCheckDialog.show();
        loginCheckDialogBinding.cancelLogId.setOnClickListener(v4 -> loginCheckDialog.cancel());
        loginCheckDialogBinding.signId.setOnClickListener(v3 -> {
            loginCheckDialog.dismiss();
            Navigation.findNavController(storeDetailsFragmentBinding.getRoot()).navigate(R.id.loginFragment);
        });
    }
}
