package com.goldencouponz.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.e.goldencouponz.R;
import com.e.goldencouponz.databinding.ActivityMainBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.goldencouponz.adapters.countries.CountriesAdapter;
import com.goldencouponz.interfaces.Api;
import com.goldencouponz.interfaces.ToolbarInterface;
import com.goldencouponz.models.appsetting.Country;
import com.goldencouponz.models.wrapper.ApiResponse;
import com.goldencouponz.models.wrapper.RetrofitClient;
import com.goldencouponz.utility.Local;
import com.goldencouponz.utility.LocaleHelper;
import com.goldencouponz.utility.sharedPrefrence.GoldenNoLoginSharedPreference;
import com.goldencouponz.utility.sharedPrefrence.GoldenSharedPreference;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import io.github.vejei.bottomnavigationbar.BottomNavigationBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ToolbarInterface {
    ActivityMainBinding activityMainBinding;
    NavController navController;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 1000;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.home_nav_fragment);
        return super.onSupportNavigateUp();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        languageData();
        if (!isInternetAvailable()) {
            Toast.makeText(this, R.string.no_internet_message, Toast.LENGTH_SHORT).show();
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (GoldenNoLoginSharedPreference.getUserLanguage(MainActivity.this).equals("en")) {
                LocaleHelper.setLocale(this, "en");
                GoldenNoLoginSharedPreference.saveUserLang(MainActivity.this, "en");
                activityMainBinding.relativBottomId.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                activityMainBinding.bottomAppBar.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                Local.Companion.updateResources(MainActivity.this);

            } else {
                LocaleHelper.setLocale(this, "ar");
                GoldenNoLoginSharedPreference.saveUserLang(MainActivity.this, "ar");
                activityMainBinding.relativBottomId.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                activityMainBinding.bottomAppBar.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                Local.Companion.updateResources(MainActivity.this);

            }
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (GoldenNoLoginSharedPreference.getUserLanguage(MainActivity.this).equals("en")) {
                LocaleHelper.setLocale(this, "en");
                GoldenNoLoginSharedPreference.saveUserLang(MainActivity.this, "en");
                activityMainBinding.bottomAppBar.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                activityMainBinding.relativBottomId.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                Local.Companion.updateResources(MainActivity.this);
            } else {
                LocaleHelper.setLocale(this, "ar");
                GoldenNoLoginSharedPreference.saveUserLang(MainActivity.this, "ar");
                activityMainBinding.bottomAppBar.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                activityMainBinding.relativBottomId.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                Local.Companion.updateResources(MainActivity.this);
            }
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNewAppVersionState();
    }

    @Override
    protected void onDestroy() {
        unregisterInstallStateUpdListener();
        super.onDestroy();
    }


    private void checkForAppUpdate() {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Create a listener to track request state updates.
        installStateUpdatedListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState installState) {
                // Show module progress, log state, or install the update.
                if (installState.installStatus() == InstallStatus.DOWNLOADED)
                    // After the update is downloaded, show a notification
                    // and request user confirmation to restart the app.
                    popupSnackbarForCompleteUpdateAndUnregister();
            }
        };

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // Request the update.
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                    // Before starting an update, register a listener for updates.
                    appUpdateManager.registerListener(installStateUpdatedListener);
                    // Start an update.
                    startAppUpdateFlexible(appUpdateInfo);
                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Start an update.
                    startAppUpdateImmediate(appUpdateInfo);
                }
            }
        });
    }

    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    MainActivity.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    MainActivity.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            unregisterInstallStateUpdListener();
        }
    }

    /**
     * Displays the snackbar notification and call to action.
     * Needed only for Flexible app update
     */
    private void popupSnackbarForCompleteUpdateAndUnregister() {
        Snackbar snackbar =
                Snackbar.make(activityMainBinding.mainContainer, getString(R.string.update_downloaded), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.restart, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.basic_color));
        snackbar.show();

        unregisterInstallStateUpdListener();
    }

    /**
     * Checks that the update is not stalled during 'onResume()'.
     * However, you should execute this check at all app entry points.
     */
    private void checkNewAppVersionState() {
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            //FLEXIBLE:
                            // If the update is downloaded but not installed,
                            // notify the user to complete the update.
                            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                                popupSnackbarForCompleteUpdateAndUnregister();
                            }

                            //IMMEDIATE:
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                startAppUpdateImmediate(appUpdateInfo);
                            }
                        });

    }

    /**
     * Needed only for FLEXIBLE update
     */
    private void unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        checkForAppUpdate();
        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                navController.navigate(R.id.updateFragment);
            }
        });

        languageData();
        String serverClientId = getString(R.string.server_client_id);
        //facebook
        printHashKey();
        FacebookSdk.sdkInitialize(MainActivity.this);
        callbackManager = CallbackManager.Factory.create();
        facebookLogin();
        /////
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken((serverClientId))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        navController = Navigation.findNavController(this, R.id.home_nav_fragment);
        int release = Integer.parseInt(Build.VERSION.RELEASE);
        Log.i("RELEASEEE", release + "?");
        if (release >= 11) {
            bottomClickListener();
        } else {
            bottom2ClickListener();
        }

        GoldenSharedPreference.setActivity(getApplication());
        GoldenNoLoginSharedPreference.setActivity(getApplication());

    }

    public Country findMemberByName(int id, List<Country> countries) {
        // go through list of members and compare name with given name

        for (Country member : countries) {
            if (member.getId() == (id)) {
                Log.i("MEMBER", member.getTitle() + "//" + member.getCurrency() + "?");
                GoldenNoLoginSharedPreference.saveUserCountry(this, 0, id, member.getTitle(), member.getCurrency());
                return member; // return member when name found
            }
        }
        return null; // return null when no member with given name could be found
    }

    private void getCountryAndCurrencyWithId(String lang) {
        int countryId = GoldenNoLoginSharedPreference.getUserCountryId(this);
        CountriesAdapter countriesAdapter = new CountriesAdapter(this);
        apiInterface.getCountries(lang).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() && response.body() != null) {
                        if (!response.body().getCountries().isEmpty()) {
                            countriesAdapter.setCountries(response.body().getCountries());
                            findMemberByName(countryId, response.body().getCountries());

                            countriesAdapter.setOnItemClickListener(new CountriesAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View viewItem, int position, int id, String code, String currency) {

                                }
                            });

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });

    }

    private void languageData() {
        if (GoldenNoLoginSharedPreference.getUserLanguage(this).equals("en")) {
            getCountryAndCurrencyWithId("en");
        } else {
            getCountryAndCurrencyWithId("ar");
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("language");
            String country = GoldenNoLoginSharedPreference.getUserCountryName(MainActivity.this);
            String currency = GoldenNoLoginSharedPreference.getUserCurrency(MainActivity.this);
            int id = GoldenNoLoginSharedPreference.getUserCountryId(MainActivity.this);
            Log.i("VALUE?", value + "//" + currency + "/" + country + "?");
            Log.i("COUNTRY", currency + "");
            GoldenNoLoginSharedPreference.saveUserCountry(MainActivity.this, 0, id, country, currency);
            Local.Companion.updateResources(this);
            LocaleHelper.setLocale(this, value);
            if (value.equals("ar")) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            } else if (value.equals("en")) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        } else {

            String value = GoldenNoLoginSharedPreference.getUserLanguage(MainActivity.this);
            String country = GoldenNoLoginSharedPreference.getUserCountryName(MainActivity.this);
            String currency = GoldenNoLoginSharedPreference.getUserCurrency(MainActivity.this);
            int id = GoldenNoLoginSharedPreference.getUserCountryId(MainActivity.this);
            Log.i("VALUE??", value + "//" + currency + "/" + country + "?");
            Local.Companion.updateResources(this);
            LocaleHelper.setLocale(this, value);
            if (value.equals("ar")) {

                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            } else if (value.equals("en")) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }

    }

    public void facebookLogin() {
        loginManager
                = LoginManager.getInstance();
        callbackManager
                = CallbackManager.Factory.create();

        loginManager
                .registerCallback(
                        callbackManager,
                        new FacebookCallback<LoginResult>() {

                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                GraphRequest request = GraphRequest.newMeRequest(
                                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(JSONObject object,
                                                                    GraphResponse response) {
                                                if (object != null) {
                                                    try {
                                                        String name = object.getString("name");
                                                        String email = object.getString("email");
                                                        String fbUserID = object.getString("id");
                                                        disconnectFromFacebook();
                                                        Log.i("FACEBOOKDATA", name + "//" + email + "//" + fbUserID);
                                                        if (GoldenNoLoginSharedPreference.getUserEmail(MainActivity.this).equals(email)) {
                                                            loginWithGoogleApi(loginResult.getAccessToken().getToken(), "facebook", "second");

                                                        } else {
                                                            loginWithGoogleApi(loginResult.getAccessToken().getToken(), "facebook", "first");

                                                        }
                                                        // do action after Facebook login success
                                                        // or call your API
                                                    } catch (NullPointerException | JSONException e) {
                                                        e.printStackTrace();
                                                        Log.i("FACEBOOKEXCEPTION", e.toString());
                                                    }
                                                }
                                            }
                                        });

                                Bundle parameters = new Bundle();
                                parameters.putString(
                                        "fields",
                                        "id, name, email, gender, birthday");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }

                            @Override
                            public void onCancel() {
                                Log.v("LoginScreen", "---onCancel");
                            }

                            @Override
                            public void onError(FacebookException error) {
                                // here write code when get error
                                Log.v("LoginScreen", "----onError: "
                                        + error.getMessage());
                            }
                        });

    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                    }
                })
                .executeAsync();
    }

    public void faceBookLogin() {
        loginManager.logInWithReadPermissions(
                MainActivity.this,
                Arrays.asList(
                        "email",
                        "public_profile",
                        "user_birthday"));
    }

    public void printHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info
                    = getPackageManager().getPackageInfo(
                    "com.e.goldencouponz",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {

                MessageDigest md
                        = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash",
                        Base64.encodeToString(
                                md.digest(),
                                Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }


    private void loginWithGoogleApi(String accessToken, String driver, String login) {
        apiInterface.socialLogin(accessToken, driver).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    if (login.equals("second")) {
                        navController.navigate(R.id.homeFragment);
                    } else {
                        navController.navigate(R.id.favouriteStoresFragment);

                    }
                    GoldenSharedPreference.saveUser(MainActivity.this, response.body(), response.body().getId());

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // add this line
        callbackManager.onActivityResult(
                requestCode,
                resultCode,
                data);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        switch (requestCode) {

            case REQ_CODE_VERSION_UPDATE:
                if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
                    Log.d("Update flow failed!: ", "" + resultCode);
                    // If the update is cancelled or fails,
                    // you can request to start the update again.
                    unregisterInstallStateUpdListener();
                }

                break;
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (GoldenNoLoginSharedPreference.getUserEmail(MainActivity.this).equals(acct.getEmail())) {
                loginWithGoogleApi(account.getIdToken(), "apple", "second");
            } else {
                loginWithGoogleApi(account.getIdToken(), "apple", "first");
            }
            // Signed in successfully, show authenticated UI.


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("massageGOOGLE", e.toString());

        }
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void bottomClickListener() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) activityMainBinding.fragmentId.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        activityMainBinding.fragmentId.setLayoutParams(params);
        activityMainBinding.fragmentId.setLayoutParams(params);
        activityMainBinding.fragmentId.setLayoutParams(params);
        activityMainBinding.fab.setVisibility(View.GONE);
        activityMainBinding.bottomAppBar.setVisibility(View.GONE);
        activityMainBinding.relativBottomId.setVisibility(View.VISIBLE);
        activityMainBinding.relativBottomId.getActionView().findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.whatsAppFragment);
            }
        });
        activityMainBinding.relativBottomId.setOnNavigationItemSelectedListener(
                new BottomNavigationBar.OnNavigationItemSelectedListener() {
                    @Override
                    public void onNavigationItemSelected(MenuItem item) {
                        Fragment selectedFragment = null;
                        int itemId = item.getItemId();
                        if (itemId == R.id.homeFragment) {
                            navController.navigate(R.id.homeFragment);
                        } else if (itemId == R.id.favouriteFragment) {
                            navController.navigate(R.id.favouriteFragment);
                        } else if (itemId == R.id.productsFragment) {
                            navController.navigate(R.id.productsFragment);
                        } else if (itemId == R.id.profileFragment) {
                            navController.navigate(R.id.profileFragment);
                        }

                    }
                });
    }

    private void bottom2ClickListener() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) activityMainBinding.fragmentId.getLayoutParams();
        params.setMargins(0, 0, 0, 200);
        activityMainBinding.fragmentId.setLayoutParams(params);
        activityMainBinding.fragmentId.setLayoutParams(params);
        activityMainBinding.fab.setVisibility(View.VISIBLE);
        activityMainBinding.bottomAppBar.setVisibility(View.VISIBLE);
        activityMainBinding.relativBottomId.setVisibility(View.GONE);
        NavigationUI.setupWithNavController(activityMainBinding.bottomNavigation, navController);
        activityMainBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.whatsAppFragment);
            }
        });
        activityMainBinding.bottomNavigation.getMenu().findItem(R.id.homeFragment).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                navController.navigate(R.id.homeFragment);
                return true;
            }
        });
        activityMainBinding.bottomNavigation.getMenu().findItem(R.id.productsFragment).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                navController.navigate(R.id.productsFragment);
                return true;
            }
        });
        activityMainBinding.bottomNavigation.getMenu().findItem(R.id.favouriteFragment).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                navController.navigate(R.id.favouriteFragment);
                return true;
            }
        });
        activityMainBinding.bottomNavigation.getMenu().findItem(R.id.profileFragment).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                navController.navigate(R.id.profileFragment);
                return true;
            }
        });
    }

    public void googleLogOut() {
        LoginManager.getInstance().logOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    @Override
    public void hideBottomMenu() {
        activityMainBinding.relativBottomId.setVisibility(View.GONE);
    }

    @Override
    public void showBottomMenu() {
        activityMainBinding.relativBottomId.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideBottomMenu2() {
        activityMainBinding.fab.setVisibility(View.GONE);
        activityMainBinding.bottomAppBar.setVisibility(View.GONE);
    }

    @Override
    public void showBottomMenu2() {
        activityMainBinding.bottomAppBar.setVisibility(View.VISIBLE);
        activityMainBinding.fab.setVisibility(View.VISIBLE);

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