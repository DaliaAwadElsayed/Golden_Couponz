package com.goldencouponz.utility;

public class Utility {
    private static String TAG = "Utility";

    public static String fixNullString(String str) {
        if (str == null||str.equals("null"))
            return " ";
        return str;
    }

}
