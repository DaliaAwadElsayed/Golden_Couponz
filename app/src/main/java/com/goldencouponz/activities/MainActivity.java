package com.goldencouponz.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.com.dtag.livia.utility.Local;
import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.ActivityMainBinding;
import com.goldencouponz.interfaces.ToolbarInterface;
import com.goldencouponz.utility.LocaleHelper;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;

import io.github.vejei.bottomnavigationbar.BottomNavigationBar;

public class MainActivity extends AppCompatActivity implements ToolbarInterface {
    ActivityMainBinding activityMainBinding;
    NavController navController;

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.home_nav_fragment);
        return super.onSupportNavigateUp();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (GoldenNoLoginSharedPreference.getUserLanguage(MainActivity.this).equals("en")) {
                LocaleHelper.setLocale(this, "en");
                GoldenNoLoginSharedPreference.saveUserLang(MainActivity.this, "en");
                activityMainBinding.relativBottomId.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                Local.Companion.updateResources(MainActivity.this);

            } else {
                LocaleHelper.setLocale(this, "ar");
                GoldenNoLoginSharedPreference.saveUserLang(MainActivity.this, "ar");
                activityMainBinding.relativBottomId.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                Local.Companion.updateResources(MainActivity.this);

            }
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (GoldenNoLoginSharedPreference.getUserLanguage(MainActivity.this).equals("en")) {
                LocaleHelper.setLocale(this, "en");
                GoldenNoLoginSharedPreference.saveUserLang(MainActivity.this, "en");
                activityMainBinding.relativBottomId.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                Local.Companion.updateResources(MainActivity.this);
            } else {
                LocaleHelper.setLocale(this, "ar");
                GoldenNoLoginSharedPreference.saveUserLang(MainActivity.this, "ar");
                activityMainBinding.relativBottomId.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                Local.Companion.updateResources(MainActivity.this);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("language");
            String country = GoldenNoLoginSharedPreference.getUserCountryName(MainActivity.this);
            int id = GoldenNoLoginSharedPreference.getUserCountryId(MainActivity.this);
            Log.i("COUNTRY", country + "");
            GoldenNoLoginSharedPreference.saveUserCountry(MainActivity.this, 0, id, country);
            Local.Companion.updateResources(this);
            LocaleHelper.setLocale(this, value);
            if (value.equals("ar") || GoldenNoLoginSharedPreference.getUserLanguage(this).equals("ar")) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            } else if (value.equals("en") || GoldenNoLoginSharedPreference.getUserLanguage(this).equals("en")) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        navController = Navigation.findNavController(this, R.id.home_nav_fragment);
        bottomClickListener();
        GoldenSharedPreference.setActivity(getApplication());
        GoldenNoLoginSharedPreference.setActivity(getApplication());
    }


    private void bottomClickListener() {
        activityMainBinding.relativBottomId.setOnNavigationItemSelectedListener(
                new BottomNavigationBar.OnNavigationItemSelectedListener() {
                    @Override
                    public void onNavigationItemSelected(MenuItem item) {
                        Fragment selectedFragment = null;
                        int itemId = item.getItemId();
                        if (itemId == R.id.homeFragment) {
                            navController.navigate(R.id.homeFragment);
                        } else if (itemId == R.id.favouriteStoresFragment) {
                            navController.navigate(R.id.homeFragment);
                        } else if (itemId == R.id.product) {
                            navController.navigate(R.id.homeFragment);
                        } else if (itemId == R.id.profileFragment) {
                            navController.navigate(R.id.profileFragment);
                        }

                    }
                });
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