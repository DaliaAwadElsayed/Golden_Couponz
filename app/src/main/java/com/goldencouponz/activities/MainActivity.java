package com.goldencouponz.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.com.dtag.livia.utility.Local;
import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.ActivityMainBinding;
import com.goldencouponz.interfaces.ToolbarInterface;
import com.goldencouponz.utility.LocaleHelper;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class MainActivity extends AppCompatActivity implements ToolbarInterface {
    ActivityMainBinding activityMainBinding;
    NavController navController;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("language");
            String country=GoldenNoLoginSharedPreference.getUserCountryName(MainActivity.this);
            Log.i("COUNTRY",country+"");
            GoldenNoLoginSharedPreference.saveUserCountry(MainActivity.this,0,country);
            Local.Companion.updateResources(this);
            LocaleHelper.setLocale(this, value);
            if (value.equals("ar")|| GoldenNoLoginSharedPreference.getUserLanguage(this).equals("ar")) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            } else  if (value.equals("en")|| GoldenNoLoginSharedPreference.getUserLanguage(this).equals("en")) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        navController = Navigation.findNavController(this, R.id.home_nav_fragment);
        activityMainBinding.bottomId.initWithSaveInstanceState(savedInstanceState);
        activityMainBinding.bottomId.setSaveEnabled(true);
        activityMainBinding.bottomId.addSpaceItem(new SpaceItem("", R.drawable.ic_store));
        activityMainBinding.bottomId.addSpaceItem(new SpaceItem("", R.drawable.ic_products));
        activityMainBinding.bottomId.addSpaceItem(new SpaceItem("", R.drawable.ic_fav));
        activityMainBinding.bottomId.addSpaceItem(new SpaceItem("", R.drawable.ic_user));
        bottomClickListener();
        GoldenSharedPreference.setActivity(getApplication());
        GoldenNoLoginSharedPreference.setActivity(getApplication());
    }

    public void askRatings() {
        ReviewManager manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(task2 -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                });
            } else {
                // There was some problem, continue regardless of the result.
            }
        });
    }

    private void bottomClickListener() {
        activityMainBinding.bottomId.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Toast.makeText(MainActivity.this, "whatsApp", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                if (itemIndex == 3) {
                    navController.navigate(R.id.profileFragment);
                } else if (itemIndex == 2) {
                    navController.navigate(R.id.favouriteStoresFragment);
                } else if (itemIndex == 1) {
                    //products
                } else if (itemIndex == 0) {
                    navController.navigate(R.id.homeFragment);
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                if (itemIndex == 3) {
                    navController.navigate(R.id.profileFragment);
                } else if (itemIndex == 2) {
                    navController.navigate(R.id.favouriteStoresFragment);
                } else if (itemIndex == 1) {
                    //products
                } else if (itemIndex == 0) {
                    navController.navigate(R.id.homeFragment);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        activityMainBinding.bottomId.onSaveInstanceState(outState);
    }

    @Override
    public void hideBottomMenu() {
        activityMainBinding.relativBottomId.setVisibility(View.GONE);
    }

    @Override
    public void showBottomMenu() {
        activityMainBinding.relativBottomId.setVisibility(View.VISIBLE);

    }

    public boolean isInternetAvailable() {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {

            Log.e("isInternetAvailable:", e.toString());
            return false;
        }
    }


}