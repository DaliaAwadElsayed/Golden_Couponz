package com.goldencouponz.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat.Builder;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.com.dtag.livia.utility.Local;
import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.ActivitySkipBinding;
import com.goldencouponz.utility.LocaleHelper;

import org.jetbrains.annotations.NotNull;

import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public class SkipActivity extends AppCompatActivity {
    ActivitySkipBinding activityMainBinding;
    private ActivityResultLauncher requestPermissionLauncher;
    private final Lazy notificationManager$delegate = LazyKt.lazy((Function0)(new Function0() {

        @NotNull
        public final NotificationManager invoke() {
            Object var10000 = getSystemService(Context.NOTIFICATION_SERVICE);
            if (var10000 == null) {
                throw new NullPointerException("null cannot be cast to non-null type android.app.NotificationManager");
            } else {
                return (NotificationManager)var10000;
            }
        }
    }));
    @NotNull
    public static final String CHANNEL_ID = "dummy_channel";
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker)null);

    private final NotificationManager getNotificationManager() {
        Lazy var1 = this.notificationManager$delegate;
        Object var3 = null;
        return (NotificationManager)var1.getValue();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_skip);
        ActivityResultLauncher var10001 = this.registerForActivityResult((ActivityResultContract)(new ActivityResultContracts.RequestPermission()),
                (ActivityResultCallback)(new ActivityResultCallback() {
                    // $FF: synthetic method
                    // $FF: bridge method
                    public void onActivityResult(Object var1) {
                        this.onActivityResult((Boolean)var1);
                    }

                    public final void onActivityResult(Boolean it) {
                        Intrinsics.checkNotNullExpressionValue(it, "it");
                        if (it) {
                  showDummyNotification();
                        } else {
                      openAppNotificationSettings((Context)SkipActivity.this);
                        }

                    }
                }));
        Intrinsics.checkNotNullExpressionValue(var10001, "registerForActivityResul…)\n            }\n        }");
        this.requestPermissionLauncher = var10001;
        this.createNotificationChannel();

        Bundle extras = getIntent().getExtras();
        Log.i("LANGUAGESKIP",extras.getString("language"));
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
    }
    public final void openAppNotificationSettings(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intent var3 = new Intent();
        boolean var5 = false;
        if (Build.VERSION.SDK_INT >= 26) {
            var3.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            Intrinsics.checkNotNullExpressionValue(var3.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName()), "putExtra(Settings.EXTRA_…AGE, context.packageName)");
        } else if (Build.VERSION.SDK_INT >= 21) {
            var3.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            var3.putExtra("app_package", context.getPackageName());
            Intrinsics.checkNotNullExpressionValue(var3.putExtra("app_uid", context.getApplicationInfo().uid), "putExtra(\"app_uid\", context.applicationInfo.uid)");
        } else {
            var3.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            var3.addCategory("android.intent.category.DEFAULT");
            var3.setData(Uri.parse("package:" + context.getPackageName()));
        }

        context.startActivity(var3);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private final void createNotificationChannel() {
        NotificationChannel var2 = new NotificationChannel("dummy_channel", (CharSequence)"Important Notification Channel", 4);
        boolean var4 = false;
        var2.setDescription("This notification contains important announcement, etc.");
        this.getNotificationManager().createNotificationChannel(var2);
    }

    private final void showDummyNotification() {
        Builder var10000 = (new Builder((Context)this, "dummy_channel")).setSmallIcon(700000).setContentTitle((CharSequence)"Congratulations! \ud83c\udf89\ud83c\udf89\ud83c\udf89").setContentText((CharSequence)"You have post a notification to Android 13!!!").setPriority(1);
        Intrinsics.checkNotNullExpressionValue(var10000, "NotificationCompat.Build…tionCompat.PRIORITY_HIGH)");
        Builder builder = var10000;
        NotificationManagerCompat var2 = NotificationManagerCompat.from((Context)this);
        boolean var4 = false;
        var2.notify(1, builder.build());
    }

    // $FF: synthetic method
    public final ActivityResultLauncher access$getRequestPermissionLauncher$p(SkipActivity $this) {
        ActivityResultLauncher var10000 =this.requestPermissionLauncher;
        if (var10000 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("requestPermissionLauncher");
        }

        return var10000;
    }

    // $FF: synthetic method
    public final void access$setRequestPermissionLauncher$p(MainActivity $this, ActivityResultLauncher var1) {
        this.requestPermissionLauncher = var1;
    }


    public static final class Companion {
        private Companion() {
        }

        // $FF: synthetic method
        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
public void notification(){
    if (ContextCompat.checkSelfPermission((Context)SkipActivity.this, "android.permission.POST_NOTIFICATIONS") == 0) {
     showDummyNotification();
    } else {
       access$getRequestPermissionLauncher$p(SkipActivity.this).launch("android.permission.POST_NOTIFICATIONS");
    }
}
}