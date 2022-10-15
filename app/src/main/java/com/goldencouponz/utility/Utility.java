package com.goldencouponz.utility;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

public class Utility {
    private static String TAG = "Utility";
    public static void updateResources(Context mContext) {
        String lang = GoldenSharedPreference.getSelectedLanguageValue(mContext);
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources res = mContext.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public static Context updateResourcess(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }
    public static String fixNullString(String str) {
        if (str == null)
            return "";
        return str;
    }

}
