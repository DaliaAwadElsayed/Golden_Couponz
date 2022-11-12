package com.goldencouponz.utility;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.e.goldencouponz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class Utility {
    private static String TAG = "Utility";

    public static String fixNullString(String str) {
        if (str == null || str.equals("null"))
            return " ";
        return str;
    }

}
