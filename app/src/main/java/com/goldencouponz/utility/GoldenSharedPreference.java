package com.goldencouponz.utility;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GoldenSharedPreference {
    private static final String IS_LOGGED_IN = "IsLoggedIn";
    private static final String KEY_NAME = "name";
    private static final String PREF_KEY = "PREF_KEY";
    private static final String KEY_EMAIL = "email";
    private static final String TOKEN = "token_pref";
    private static final String UID = "user_id_pref";
    private static final String LANG_KEY = "chosen_lang_key";
    private static final String ADDRESS = "address";
    private static final String PHONE = "phone";
    private static final String NOLOGGING = "NOLOGIN";
    private static Application application;
    SharedPreferences sharedPref = application.getApplicationContext().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    public SharedPreferences.Editor editor = sharedPref.edit();

    public GoldenSharedPreference() {

    }
    /**
     * @created by Dalia
     * */
    /**
     * save the user data into shared preference manager (APPLICATION TO APP PREFERENCE MANAGER)
     *
     * @param context  the context to access the shared preference
     * @param userData the user data from the login response
     */
//    public static void saveUser(Context context, User userData, int id) {
//        PreferenceManager.getDefaultSharedPreferences(context)
//                .edit()
//                .putInt(UID, id)
//                .putString(USERROLE, "CLIENT")
//                .putInt(NOLOGGING, 1)
//                .putString(KEY_NAME, userData.getName())
//                .putString(KEY_EMAIL, userData.getEmail())
//                .putString(PHONE, userData.getPhone())
//                .putString(ADDRESS, userData.getAddress())
//                .putBoolean(IS_LOGGED_IN, true)
//                .apply();
//    }

    /**
     * @save the user token
     */
//    public static void saveToken(Context context, ApiResponse userData) {
//        PreferenceManager.getDefaultSharedPreferences(context)
//                .edit()
//                .putString(TOKEN, userData.getAccessToken())
//                .apply();
//    }

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
     * @return the user Address
     */
    public static String getAddress(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(ADDRESS, "");
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
    }


    /**
     * get Activity
     */
    public static void setActivity(Application activity) {
        application = activity;
    }

    public static Application getActivity() {
        return application;
    }

    private static SharedPreferences getDefaultSharedPreference(Context context) {
        if (PreferenceManager.getDefaultSharedPreferences(context) != null)
            return PreferenceManager.getDefaultSharedPreferences(context);
        else
            return null;
    }

    public static int getSelectedLanguage(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(LANG_KEY, "ar").equals("en") ? 0 : 1;
    }

    public static String getSelectedLanguageValue(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(LANG_KEY, "ar");
    }

    /**
     * @param language 0 for english and 1 for arabic
     */
    public static void changeLanguage(Context context, int language) {
        // PreferenceManager.getDefaultSharedPreferences(context).edit().putString(LANG_KEY, language == 0 ? "en" : "ar").apply();
        if (language == 0) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(LANG_KEY, "en").apply();
        } else {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(LANG_KEY, "ar").apply();
        }
    }
}
