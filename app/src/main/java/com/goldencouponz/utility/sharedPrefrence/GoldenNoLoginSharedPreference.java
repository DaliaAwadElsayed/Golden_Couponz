package com.goldencouponz.utility.sharedPrefrence;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GoldenNoLoginSharedPreference {

    private static final String PREF_KEY = "PREF_KEY2";
    private static final String LANG_KEY = "chosen_lang_key";
    private static final String COUNTRY_NAME = "chosen_country";
    private static final String COUNTRY_ID = "chosen_country_id";
    private static Application application;
    SharedPreferences sharedPref = application.getApplicationContext().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    public SharedPreferences.Editor editor = sharedPref.edit();
    public GoldenNoLoginSharedPreference() {

    }
    public static void saveUserLang(Context context, String lang) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(LANG_KEY, lang)
                .apply();
    }

    public static void saveUserCountry(Context context, int countryId, String country) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(COUNTRY_NAME, country)
                .putInt(COUNTRY_ID, countryId)
                .apply();
    }
    /**
     * get Activity
     */
    public static void setActivity(Application activity) {
        application = activity;
    }
    /**
     * @return the user country name choice
     */
    public static String getUserCountryName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(COUNTRY_NAME, "");
    }

    /**
     * @return the user country id choice
     */
    public static int getUserCountryId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(COUNTRY_ID, 0);
    }

    /**
     * @return the user language choice
     */
    public static String getUserLanguage(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(LANG_KEY, "");
    }

    public static String getSelectedLanguageValue(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(LANG_KEY, "ar");
    }


}
