package com.goldencouponz.viewModles.aboutgolden;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.LoginCheckDialogBinding;
import com.e.goldencouponz.databinding.NoCouponProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.ProductDetailsDialogBinding;
import com.e.goldencouponz.databinding.SearchFragmentBinding;
import com.e.goldencouponz.databinding.SecondProductDetailsDialogBinding;
import com.goldencouponz.adapters.home.SearchStoresListAdapter;
import com.goldencouponz.adapters.stores.SearchProductAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.room.DatabaseClient;
import com.goldencouponz.room.Golden;
import com.goldencouponz.room.SavedData;
import com.goldencouponz.room.TasksAdapter;
import com.goldencouponz.utility.Utility;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {
    SearchFragmentBinding searchFragmentBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    SearchStoresListAdapter storesListAdapter;
    LoginCheckDialogBinding loginCheckDialogBinding;
    BottomSheetDialog loginCheckDialog;
    SearchProductAdapter storeProduct;
    BottomSheetDialog showProductDetailsDialog;
    BottomSheetDialog secondShowProductDetailsDialog;
    BottomSheetDialog showNoCouponDialog;
    ProductDetailsDialogBinding productDetailsDialogBinding;
    SecondProductDetailsDialogBinding secondProductDetailsDialogBinding;
    NoCouponProductDetailsDialogBinding noCouponProductDetailsDialogBinding;
    String youTube = "https://www.youtube.com/@goldencouponz";
    String snapChat = "https://www.snapchat.com/add/goldencouponz?share_id=9XqCMUv88XE&locale=ar-EG*/";
    String instagram = "https://www.insshowNoCouponProductDetailsDialogtagram.com/golden.couponz/";
    String facebook = "https://www.facebook.com/goldencouponzz/";
    private ClipboardManager myClipboard;
    private ClipData myClip;

    public void init(SearchFragmentBinding searchFragmentBinding,
                     LoginCheckDialogBinding loginCheckDialogBinding,
                     ProductDetailsDialogBinding productDetailsDialogBinding,
                     SecondProductDetailsDialogBinding secondProductDetailsDialogBinding,
                     NoCouponProductDetailsDialogBinding noCouponProductDetailsDialogBinding,
                     Context context) {
        this.searchFragmentBinding = searchFragmentBinding;
        this.context = context;
        this.productDetailsDialogBinding = productDetailsDialogBinding;
        this.secondProductDetailsDialogBinding = secondProductDetailsDialogBinding;
        this.noCouponProductDetailsDialogBinding = noCouponProductDetailsDialogBinding;
        this.loginCheckDialogBinding = loginCheckDialogBinding;
        storesListAdapter = new SearchStoresListAdapter(context, listener);
        loginCheckDialog = new BottomSheetDialog(context);
        storeProduct = new SearchProductAdapter(context, listener);
        secondShowProductDetailsDialog = new BottomSheetDialog(context);
        showProductDetailsDialog = new BottomSheetDialog(context);
        showNoCouponDialog = new BottomSheetDialog(context);
        getOldSearches();
        searchFragmentBinding.deleteAllProductId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseClient golden = DatabaseClient.getInstance(context);
                deleteAllProduct(golden.getAppDatabase().taskDao());
            }
        });
        searchFragmentBinding.deleteAllStoreId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseClient golden = DatabaseClient.getInstance(context);
                deleteAllStore(golden.getAppDatabase().taskDao());
            }
        });
        searchFragmentBinding.backId.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.homeFragment));
        tabLayout();
        firstTime();
        //search
    }

    private void firstTime() {
        searchFragmentBinding.storesLinearId.setVisibility(View.VISIBLE);
        searchFragmentBinding.productLinearId.setVisibility(View.GONE);
        searchFragmentBinding.searchTextId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(searchFragmentBinding.searchTextId.getText())) {
                    //history search
                    Log.i("HEREEEE", "?");
                    searchFragmentBinding.storesRecyclerId.setVisibility(View.GONE);
                } else {
                    Log.i("LANGUAGEEE", GoldenNoLoginSharedPreference.getUserLanguage(context) + "//" + searchFragmentBinding.searchTextId.getText().toString());
                    String locale = GoldenNoLoginSharedPreference.getUserLanguage(context);
                    storesListAdapter = new SearchStoresListAdapter(context, listener);
                    searchFragmentBinding.storesRecyclerId.setVisibility(View.VISIBLE);
                    getSearchStore(locale, searchFragmentBinding.searchTextId.getText().toString(), locale);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void tabLayout() {
        searchFragmentBinding.tabLayoutId.addTab(searchFragmentBinding.tabLayoutId.newTab().setText(context.getResources().getString(R.string.stores_label)));
        searchFragmentBinding.tabLayoutId.addTab(searchFragmentBinding.tabLayoutId.newTab().setText(context.getResources().getString(R.string.products_label)));
        searchFragmentBinding.tabLayoutId.setTabGravity(TabLayout.GRAVITY_FILL);
        searchFragmentBinding.tabLayoutId.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    searchFragmentBinding.deleteAllStoreId.setVisibility(View.VISIBLE);
                    searchFragmentBinding.deleteAllProductId.setVisibility(View.GONE);
                    getOldSearches();
                    searchFragmentBinding.storesLinearId.setVisibility(View.VISIBLE);
                    searchFragmentBinding.productLinearId.setVisibility(View.GONE);
                    searchFragmentBinding.searchTextId.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (TextUtils.isEmpty(searchFragmentBinding.searchTextId.getText())) {
                                //history search
                                Log.i("HEREEEE", "?");
                                getOldSearches();
                                searchFragmentBinding.pastSearcheslinearId.setVisibility(View.VISIBLE);
                                searchFragmentBinding.storesRecyclerId.setVisibility(View.GONE);
                            } else {
                                Log.i("LANGUAGEEE", GoldenNoLoginSharedPreference.getUserLanguage(context) + "//" + searchFragmentBinding.searchTextId.getText().toString());
                                String locale = GoldenNoLoginSharedPreference.getUserLanguage(context);
                                storesListAdapter = new SearchStoresListAdapter(context, listener);
                                searchFragmentBinding.storesRecyclerId.setVisibility(View.VISIBLE);
                                getSearchStore(locale, searchFragmentBinding.searchTextId.getText().toString(), locale);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                } else if (tab.getPosition() == 1) {
                    getOldProductSearches();
                    searchFragmentBinding.deleteAllStoreId.setVisibility(View.GONE);
                    searchFragmentBinding.deleteAllProductId.setVisibility(View.VISIBLE);
                    searchFragmentBinding.storesLinearId.setVisibility(View.GONE);
                    searchFragmentBinding.productLinearId.setVisibility(View.VISIBLE);
                    searchFragmentBinding.searchTextId.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (TextUtils.isEmpty(searchFragmentBinding.searchTextId.getText())) {
                                //history search
                                Log.i("HEREEEE", "?");
                                getOldProductSearches();
                                searchFragmentBinding.pastSearcheslinearId.setVisibility(View.VISIBLE);
                                searchFragmentBinding.productPastSearchId.setVisibility(View.GONE);
                            } else {
                                Log.i("LANGUAGEEE", GoldenNoLoginSharedPreference.getUserLanguage(context) + "//" + searchFragmentBinding.searchTextId.getText().toString());
                                storeProduct = new SearchProductAdapter(context, listener);
                                searchFragmentBinding.productPastSearchId.setVisibility(View.VISIBLE);
                                getProducts(GoldenNoLoginSharedPreference.getUserCountryId(context), searchFragmentBinding.searchTextId.getText().toString());
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

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

    private void getSearchStore(String lang, String word, String locale) {
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
                        String userToken;
                        if (GoldenSharedPreference.isLoggedIn(context)) {
                            userToken = "Bearer" + GoldenSharedPreference.getToken(context);
                        } else {
                            userToken = "";
                        }
                        apiInterface.getSearchStore(userToken, lang, device, word, locale).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 200 && response.body() != null) {
                                        if (!response.body().getStores().isEmpty()) {
                                            searchFragmentBinding.pastSearcheslinearId.setVisibility(View.GONE);
                                            storesListAdapter = new SearchStoresListAdapter(context, listener);
                                            storesListAdapter.setStores(response.body().getStores());
                                            searchFragmentBinding.storesRecyclerId.setAdapter(storesListAdapter);
                                            storesListAdapter.setOnItemClickListener(new SearchStoresListAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(View viewItem, int position, int id, String store) {
                                                    Log.i("SEARCHKEY", store);
                                                }
                                            });
                                        } else {
//                                            Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    SearchViewModel.Listener listener = new SearchViewModel.Listener() {
        @Override
        public void click(int click) {
            click = click;
            if (click == 1) {
                if (GoldenSharedPreference.isLoggedIn(context)) {
                    storesListAdapter = new SearchStoresListAdapter(context, listener);
                    String locale = GoldenNoLoginSharedPreference.getUserLanguage(context);
                    getSearchStore(locale, searchFragmentBinding.searchTextId.getText().toString(), locale);
                } else {
                    storesListAdapter = new SearchStoresListAdapter(context, listener);
                    String locale = GoldenNoLoginSharedPreference.getUserLanguage(context);
                    getSearchStore(locale, searchFragmentBinding.searchTextId.getText().toString(), locale);
                }
            }
            if (click == 2) {
                loginCheckDialog();
            }

        }

        @Override
        public void clickProduct(int click, int position, String id, String c) {
            if (click == 1) {
                getSingleProducts(GoldenNoLoginSharedPreference.getUserCountryId(context), GoldenNoLoginSharedPreference.getUserLanguage(context),
                        id);

            }
        }

    };

    public interface Listener {
        void click(int click);

        void clickProduct(int click, int position, String id, String c);

    }

    private void loginCheckDialog() {
        final View view2 = loginCheckDialogBinding.getRoot();
        loginCheckDialog.setContentView(view2);
        loginCheckDialog.setCancelable(true);
        loginCheckDialog.show();
        loginCheckDialogBinding.signId.setOnClickListener(v -> {
            loginCheckDialog.dismiss();
            Navigation.findNavController(searchFragmentBinding.getRoot()).navigate(R.id.loginFragment);
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

    private void getProducts(int country, String search) {
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
                        searchFragmentBinding.progress.setVisibility(View.VISIBLE);
                        String lang = GoldenNoLoginSharedPreference.getUserLanguage(context);
                        apiInterface.getSearchStoreProducts(search, lang, token, country, lang).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 200 && response.body() != null) {
                                        searchFragmentBinding.progress.setVisibility(View.GONE);
                                        if (!response.body().getProducts().getData().isEmpty()) {
                                            searchFragmentBinding.pastSearcheslinearId.setVisibility(View.GONE);
                                            storeProduct = new SearchProductAdapter(context, listener);
                                            storeProduct.setStores(response.body().getProducts().getData());
                                            searchFragmentBinding.productPastSearchId.setAdapter(storeProduct);
                                        } else {
                                            searchFragmentBinding.productPastSearchId.setVisibility(View.GONE);
                                            searchFragmentBinding.progress.setVisibility(View.GONE);
                                        }
                                    } else {
                                        Toast.makeText(context, R.string.somethingwentwrongmessage, Toast.LENGTH_SHORT).show();
                                        searchFragmentBinding.progress.setVisibility(View.GONE);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.i("onFailure", t.toString());
                                searchFragmentBinding.progress.setVisibility(View.GONE);
                                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void getOldSearches() {
        searchFragmentBinding.pastSearcheslinearId.setVisibility(View.VISIBLE);
        class GetTasks extends AsyncTask<Void, Void, List<SavedData>> {

            @Override
            protected List<SavedData> doInBackground(Void... voids) {
                List<SavedData> taskList = DatabaseClient
                        .getInstance(context)
                        .getAppDatabase()
                        .taskDao()
                        .getPastSearches("store");
                DatabaseClient golden = DatabaseClient.getInstance(context);
                deleteAllDublicates(golden.getAppDatabase().taskDao());
                return taskList;

            }

            @Override
            protected void onPostExecute(List<SavedData> tasks) {
                super.onPostExecute(tasks);
                TasksAdapter adapter = new TasksAdapter(context, tasks,listener);
                searchFragmentBinding.storePastSearchId.setAdapter(adapter);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void getOldProductSearches() {
        Log.i("PRODUCTDATABASE", "here");
        searchFragmentBinding.pastSearcheslinearId.setVisibility(View.VISIBLE);
        class GetTasks extends AsyncTask<Void, Void, List<SavedData>> {

            @Override
            protected List<SavedData> doInBackground(Void... voids) {
                List<SavedData> taskList = DatabaseClient
                        .getInstance(context)
                        .getAppDatabase()
                        .taskDao()
                        .getPastSearches("product");
                DatabaseClient golden = DatabaseClient.getInstance(context);
                deleteAllDublicates(golden.getAppDatabase().taskDao());
                return taskList;

            }

            @Override
            protected void onPostExecute(List<SavedData> tasks) {
                super.onPostExecute(tasks);
                TasksAdapter adapter = new TasksAdapter(context, tasks,listener);
                searchFragmentBinding.storePastSearchId.setAdapter(adapter);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private static class deleteAllWordsStoreAsyncTask extends AsyncTask<Void, Void, Void> {
        private Golden mAsyncTaskDao;

        deleteAllWordsStoreAsyncTask(Golden dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll("store");
            return null;
        }
    }

    private static class deleteAllWordsProductAsyncTask extends AsyncTask<Void, Void, Void> {
        private Golden mAsyncTaskDao;

        deleteAllWordsProductAsyncTask(Golden dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll("product");
            return null;
        }
    }

    //saveItemsInDataBase
    private void saveTask(String name, int productId) {
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                //creating a task
                SavedData task = new SavedData();
                task.setWord(name);
                task.setStoreId(productId);
                task.setType("product");
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

    public void deleteAllDublicates(Golden mWordDao) {
        new deleteAllDublicatesWordsAsyncTask(mWordDao).execute();
    }

    public void deleteAllStore(Golden mWordDao) {
        new deleteAllWordsStoreAsyncTask(mWordDao).execute();
        searchFragmentBinding.pastSearcheslinearId.setVisibility(View.GONE);
    }

    public void deleteAllProduct(Golden mWordDao) {
        new deleteAllWordsProductAsyncTask(mWordDao).execute();
        searchFragmentBinding.pastSearcheslinearId.setVisibility(View.GONE);
    }

    private static class deleteAllDublicatesWordsAsyncTask extends AsyncTask<Void, Void, Void> {
        private Golden mAsyncTaskDao;

        deleteAllDublicatesWordsAsyncTask(Golden dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteDuplicates();
            Log.i("DELETED", "?");
            return null;
        }
    }

    private void getSingleProducts(int country, String lang, String id) {
        showProductDetailsDialog();
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
                        apiInterface.getStoreProducts(id, token, country, lang, "", "", "", "", "", 0).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 200 && response.body() != null) {
                                        //product dialog
                                        saveTask(response.body().getSingleProduct().getTitle(), Integer.parseInt(id));
                                        searchFragmentBinding.pastSearcheslinearId.setVisibility(View.GONE);
                                        productDetailsDialogBinding.newPriceId.setText(Utility.fixNullString(response.body().getSingleProduct().getProductCountry().getDiscountPrice()) + "");
                                        productDetailsDialogBinding.currentPriceId.setPaintFlags(productDetailsDialogBinding.currentPriceId.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        productDetailsDialogBinding.discountId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getProductCountry().getDiscount())) + "");
                                        productDetailsDialogBinding.currentPriceId.setText(Utility.fixNullString(String.valueOf(response.body().getSingleProduct().getProductCountry().getPrice())) + "");
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
                                                Toast.makeText(context, R.string.code_copy, Toast.LENGTH_SHORT).show();
                                                myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                                                String text;
                                                text = secondProductDetailsDialogBinding.couponValueId.getText().toString();
                                                myClip = ClipData.newPlainText("text", text);
                                                myClipboard.setPrimaryClip(myClip);
                                                openUrl(response.body().getSingleProduct().getProductLink());
                                            }
                                        });
                                        if (response.body().getSingleProduct().getVideoLink() == null) {
                                            productDetailsDialogBinding.goToYoutube2Id.setVisibility(View.GONE);
                                            secondProductDetailsDialogBinding.goToYoutube2Id.setVisibility(View.GONE);

                                        } else {
                                            secondProductDetailsDialogBinding.goToYoutube2Id.setVisibility(View.VISIBLE);
                                            productDetailsDialogBinding.goToYoutube2Id.setVisibility(View.VISIBLE);
                                        }
                                        productDetailsDialogBinding.goToYoutube2Id.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                openUrl(response.body().getSingleProduct().getVideoLink());
                                            }
                                        });
                                        secondProductDetailsDialogBinding.goToYoutube2Id.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                openUrl(response.body().getSingleProduct().getVideoLink());
                                            }
                                        });
                                        secondProductDetailsDialogBinding.noActiveId.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                sendNoActive(secondProductDetailsDialogBinding.couponValueId.getText().toString(),
                                                        response.body().getSingleProduct().getStore().getTitle(), response.body().getSingleProduct().getWhatsapp()
                                                );
                                            }
                                        });
                                        ////////////////
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
                });
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
        social2();
    }

    private void showSecondProductDetailsDialog() {
        final View view5 = secondProductDetailsDialogBinding.getRoot();
        secondShowProductDetailsDialog.setContentView(view5);
        secondShowProductDetailsDialog.setCancelable(true);
        secondShowProductDetailsDialog.show();
        secondProductDetailsDialogBinding.cancelLogId.setOnClickListener(v8 -> secondShowProductDetailsDialog.dismiss());
        if (GoldenNoLoginSharedPreference.getUserLanguage(context).equals("ar")) {
            secondProductDetailsDialogBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        } else {
            secondProductDetailsDialogBinding.discountId.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        social3();
        secondProductDetailsDialogBinding.yesActiveId.setOnClickListener(v7 -> rateDialog());

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
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = store
                + " " + url;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_via)));
    }

    private void openUrl(String url) {
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    private void social3() {
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

    private void social2() {
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
}