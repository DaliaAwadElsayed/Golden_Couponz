package com.goldencouponz.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.ActivityMainBinding;
import com.goldencouponz.interfaces.ToolbarInterface;
import com.goldencouponz.utility.GoldenSharedPreference;
import com.goldencouponz.utility.Local;
import com.goldencouponz.utility.LocaleHelper;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements ToolbarInterface {
    ActivityMainBinding activityMainBinding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Local.Companion.updateResources(this);
        LocaleHelper.setLocale(this, GoldenSharedPreference.getSelectedLanguageValue(this));
        super.onCreate(savedInstanceState);
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

    //Internet ConnectionCheck
//    public void checkInternet() {
//        if (isInternetAvailable()) {
//            new IsInternetActive().execute();
//        } else {
//            Toast.makeText(getApplicationContext(), R.string.no_internet_message, Toast.LENGTH_LONG).show();
//        }
//    }

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

    class IsInternetActive extends AsyncTask<Void, Void, String> {
        InputStream is = null;
        String json = "Fail";

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL strUrl = new URL("http://icons.iconarchive.com/icons/designbolts/handstitch-social/24/Android-icon.png");
                //Here I have taken one android small icon from server, you can put your own icon on server and access your URL, otherwise icon may removed from another server.

                URLConnection connection = strUrl.openConnection();
                connection.setDoOutput(true);
                is = connection.getInputStream();
                json = "Success";

            } catch (Exception e) {
                e.printStackTrace();
                json = "Fail";
            }
            return json;

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (result.equals("Fail")) {
                    Log.e("isInternetAvailable:", "Internet Not Active");
                } else {
                    Log.e("isInternetAvailable:", "Internet  Active");
                }
            } else {
                Log.e("isInternetAvailable:", "Internet Not Active");
            }
        }

        @Override
        protected void onPreExecute() {
            Log.e("isInternetAvailable:", "Validating Internet");
            super.onPreExecute();
        }
    }
}