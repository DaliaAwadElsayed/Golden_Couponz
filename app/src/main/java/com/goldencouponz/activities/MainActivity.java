package com.goldencouponz.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.com.dtag.livia.utility.Local;
import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.ActivityMainBinding;
import com.goldencouponz.interfaces.ToolbarInterface;
import com.goldencouponz.utility.LocaleHelper;

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
            Local.Companion.updateResources(this);
            LocaleHelper.setLocale(this, value);
            if (value.equals("ar")) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        navController = Navigation.findNavController(this, R.id.home_nav_fragment);
        NavigationUI.setupWithNavController(activityMainBinding.bottomId, navController);
        activityMainBinding.toolBarId.setClickable(true);

    }

    @Override
    public void showToolbar() {
        activityMainBinding.toolBarId.setSystemUiVisibility(View.VISIBLE);
        activityMainBinding.toolBarId.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideToolbar() {
        activityMainBinding.toolBarId.setSystemUiVisibility(View.GONE);
        activityMainBinding.toolBarId.setVisibility(View.GONE);
    }

    @Override
    public void hideBottomMenu() {
        activityMainBinding.bottomId.setVisibility(View.GONE);
    }

    @Override
    public void showBottomMenu() {
        activityMainBinding.bottomId.setVisibility(View.VISIBLE);

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