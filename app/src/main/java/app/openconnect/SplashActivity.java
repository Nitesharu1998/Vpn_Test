package app.openconnect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;

import app.openconnect.Ad_Module.Ad;
import app.openconnect.Ad_Module.Cache_Adds;
import app.openconnect.core.OpenConnectManagementThread;
import app.openconnect.core.OpenVpnService;
import com.vpnpro.dubaivpn.R;


import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

public class SplashActivity extends AppCompatActivity {
    private int mConnectionState = OpenConnectManagementThread.STATE_DISCONNECTED;
    public static SharedPreferences global_sharedPreferences;
    SharedPreferences splash_sharedPreferences;
    boolean isLogin_first_time;
    public static int FirstScreenCounter_Value;
    Boolean isAppInstalled_first_time;
    private BillingClient mBillingClient;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init_data();
        do_next();
    }

    private void do_next() {

        if (splash_sharedPreferences != null) {


                global_sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = splash_sharedPreferences.edit();

                new Handler().postDelayed(this::checkAdAndNavigate_next_screen, 7000);





        }
    }


    private void init_data() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //init billing..
        mBillingClient = BillingClient.newBuilder(this)
                .setListener((billingResult, purchases) -> {
                    // To be implemented in a later section.
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                        for (Purchase purchase : purchases) {
                            handlePurchase(purchase);
                        }
                    } else if (billingResult.getResponseCode() ==
                            BillingClient.BillingResponseCode.USER_CANCELED) {
                        // Handle an error caused by a user cancelling the purchase flow.
                    } else {
                        // Handle any other error codes.
                    }

                })
                .enablePendingPurchases()
                .build();

        global_sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        splash_sharedPreferences = getSharedPreferences("DATA", MODE_PRIVATE);

        // check if vpn connection..!!
        isUserHasSubscription();



    }



    //check add and move to next screen if add exist..!!
    private void checkAdAndNavigate_next_screen() {



       /* if (Cache_Adds.nnativeAd_ != null) {
            Toast.makeText(SplashActivity.this, "Loaded", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SplashActivity.this, Ad.class);
            startActivity(intent);

        } else {

           Toast.makeText(SplashActivity.this, "Not Loaded", Toast.LENGTH_SHORT).show();

        }*/
        Intent intent = new Intent(SplashActivity.this, GetStartedActivity.class);
        startActivity(intent);
        finish();
    }


    private void handlePurchase(Purchase purchase) {

        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {

            AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = billingResult -> {

                // the user's purchase has been successful
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)

                    //TODO set the user's premium status to true
                    Log.d("Acknowledged", "successfully acknowledged product");
            };

            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();

                Log.d("Not_Acknowledged", "no");
                mBillingClient.acknowledgePurchase(acknowledgePurchaseParams,
                        acknowledgePurchaseResponseListener);
            }
        }
    }

    private void isUserHasSubscription() {
        if (mBillingClient == null) {
            mBillingClient = BillingClient.newBuilder(this)
                    .enablePendingPurchases()
                    .build();
        }

        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NotNull BillingResult billingResult) {

                Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.SUBS);

                mBillingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, (billingResult1, list) -> {

                    Log.d("billingprocess", "purchasesResult.getPurchasesList():" + purchasesResult.getPurchasesList());
                    if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK && !Objects.requireNonNull(purchasesResult.getPurchasesList()).isEmpty()) {
                        //google tells use that subscription already done..!!
                        Log.d("Already_Availed", "true" + " " + billingResult1.getResponseCode());
                        if (splash_sharedPreferences != null) {
                            SharedPreferences.Editor editor = splash_sharedPreferences.edit();
                            if (editor != null) {
                                editor.putBoolean("payment_status", true).apply();
                                DataManager.ADMOB_ENABLE = false;
                            }
                        }
                    } else {
                        Log.d("no_Already_Availed", "false");
                        if (splash_sharedPreferences != null) {
                            SharedPreferences.Editor editor = splash_sharedPreferences.edit();
                            if (editor != null) {
                                editor.putBoolean("payment_status", false).apply();
                                DataManager.ADMOB_ENABLE = true;
                            }
                        }
                    }
                });
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.d("billingprocess", "onBillingServiceDisconnected");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}