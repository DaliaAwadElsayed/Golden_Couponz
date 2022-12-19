package com.goldencouponz.utility.sharedPrefrence;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.goldencouponz.models.wrapper.ApiResponse;

public class GoldenSharedPreference {
    private static final String IS_LOGGED_IN = "IsLoggedIn";
    private static final String KEY_NAME = "name";
    private static final String PREF_KEY = "PREF_KEY";
    private static final String KEY_EMAIL = "email";
    private static final String TOKEN = "token_pref";
    private static final String UID = "user_id_pref";
    private static final String STATUS = "status";
    private static final String PROFILE = "profile";
    private static final String PHONE = "phone";
    private static final String NOLOGGING = "no_login";
    private static final String SHOW = "show";
    private static Application application;
    SharedPreferences sharedPref = application.getApplicationContext().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    public SharedPreferences.Editor editor = sharedPref.edit();

    public GoldenSharedPreference() {

    }
    /**
     * @created by Dalia Awad
     * */
    /**
     * save the user data into shared preference manager (APPLICATION TO APP PREFERENCE MANAGER)
     *
     * @param context  the context to access the shared preference
     * @param userData the user data from the login response
     */
    public static void saveUser(Context context, ApiResponse userData) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_NAME, userData.getName())
                .putString(KEY_EMAIL, userData.getEmail())
                .putString(PHONE, userData.getPhone())
                .putString(PROFILE, userData.getProfilePhotoUrl())
                .putString(TOKEN, userData.getToken())
                .putBoolean(IS_LOGGED_IN, true)
                .putInt(NOLOGGING, 1)
                .apply();
    }

    public static void saveUserCloseLogIn(Context context, String show) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(SHOW, show)
                .apply();
    }

    /**
     * get Activity
     */
    public static void setActivity(Application activity) {
        application = activity;
    }
    /**
     * @return the user show Login Screen
     */
    public static String getUserShowLogin(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SHOW, "");
    }

    /**
     * @return the user token
     */
    public static String getToken(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(TOKEN, "");
    }

    /**
     * @return the user login status
     */
    public static int getLOGIN(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(NOLOGGING, 1);
    }

    /**
     * @return the user Name
     */
    public static String getName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_NAME, "");
    }

    /**
     * @return the user Email
     */
    public static String getKeyEmail(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_EMAIL, "");
    }

    /**
     * @return the user Phone
     */
    public static String getPhone(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PHONE, "");
    }

    /**
     * @return the user id
     */
    public static int getUid(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(UID, 0);
    }

    /**
     * @return the login state if the user logged in will return true, else will return false
     */
    public static boolean isLoggedIn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(IS_LOGGED_IN, false);
    }

    /**
     * use this to clear the shared preference (when the user log out the account)
     */
    public static void clearSharedPreference(Context context) {
       PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
//        SharedPreferences settings = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
//        settings.edit().remove(KEY_EMAIL).apply();
    }


}
