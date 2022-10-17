package com.goldencouponz.utility;

public class Utility {
    private static String TAG = "Utility";

    public static String fixNullString(String str) {
        if (str == null)
            return "";
        return str;
    }

}
