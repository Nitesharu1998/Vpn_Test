/*
 * Copyright (c) 2013, Kevin Cernekee
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * In addition, as a special exception, the copyright holders give
 * permission to link the code of portions of this program with the
 * OpenSSL library.
 */

package app.openconnect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;


import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.onesignal.OneSignal;
import com.vpnpro.dubaivpn.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

import app.openconnect.Ad_Module.Cache_Adds;
import app.openconnect.core.FragCache;
import app.openconnect.core.ProfileManager;

public class MainApplication extends Application implements Application.ActivityLifecycleCallbacks, LifecycleObserver
{
    private static Context mContext;
    public static InterstitialAd mInterstitialAd;
    private RequestQueue requestQueue;
    private static final String ONESIGNAL_APP_ID = "fb7397ab-fb7d-42d9-bd03-46ca6cb26827";



    @SuppressLint("StaticFieldLeak")
    private static  Context context;
    @SuppressLint("StaticFieldLeak")
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;

    ///////////
    public AppOpenAdManager appOpenAdManager;
    private Activity currentActivity;
    public static int i = 1;
    ///////////////

    public  Context getsContext() {
        return context;
    }

    private static MainApplication mAppInstance;
    public static synchronized MainApplication getAppInstance() {
        return mAppInstance;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void onCreate() {
        super.onCreate();


        mAppInstance = this;
        context = getApplicationContext();
        this.registerActivityLifecycleCallbacks(this);
        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(
                            @NonNull InitializationStatus initializationStatus) {}
                });


        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        appOpenAdManager = new AppOpenAdManager();


        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        mContext = getApplicationContext();


        MobileAds.initialize(MainApplication.getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // appOpenAdManager = new AppOpenAdManager();



                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus status = statusMap.get(adapterClass);
                    Log.d("MyApp", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status.getDescription(), status.getLatency()));
                }

                // Start loading ads here...




            }
        });

        Get_Settings();
        Get_Update();





        String cpuABI;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cpuABI = Build.SUPPORTED_ABIS[0];
        } else {
            cpuABI = Build.CPU_ABI;
        }
        Log.e(String.valueOf(Build.VERSION.SDK_INT), cpuABI);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cpuABI.equals("arm64-v8a")) {
                System.loadLibrary("openconnect_10");
            } else {
                System.loadLibrary("openconnect");
            }
            System.loadLibrary("stoken");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ProfileManager.init(getApplicationContext());
        FragCache.init();







    }
    public static Context getContext()
    {
        return mContext;
    }

    private void Get_Settings() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, DataManager.Settings_API, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            DataManager.Package_Name = response.getString("package_name");

                            DataManager.Privacy_Policy = response.getString("app_privacy_policy");


                            DataManager.Contact = response.getString("contact");
                            DataManager.Email = response.getString("email");
                            DataManager.Website = response.getString("website");
                            DataManager.Company = response.getString("company");


                         /*   DataManager.Banner_Ad_ID = response.getString("banner_ad_id");
                            DataManager.Interstitial_Ad_ID = response.getString("interstital_ad_id");
                            DataManager.Native_Ad_ID = response.getString("admob_native_ad_id");
                            DataManager.Publisher_Ad_ID = response.getString("publisher_id");

                            DataManager.Banner_Ad = response.getBoolean("banner_ad");
                            DataManager.Interstitial_Ad = response.getBoolean("interstital_ad");
                            DataManager.Native_Ad = response.getBoolean("admob_nathive_ad");
*/
                            DataManager.Contact = response.getString("contact");
                            DataManager.Email = response.getString("email");
                            DataManager.Website = response.getString("website");
                            DataManager.Company = response.getString("company");


                            load_rinterstitial_Ad();
                            if(Cache_Adds.nnativeAd_ == null){
                                Cache_Adds.loadAd();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("Exception", error.toString());
                        Get_Settings_Backup();
                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    private void Get_Settings_Backup() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, DataManager.Settings_API_Backup, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            DataManager.Package_Name = response.getString("package_name");

                            DataManager.Privacy_Policy = response.getString("app_privacy_policy");


                            DataManager.Contact = response.getString("contact");
                            DataManager.Email = response.getString("email");
                            DataManager.Website = response.getString("website");
                            DataManager.Company = response.getString("company");

                          /*  DataManager.Banner_Ad_ID = response.getString("banner_ad_id");
                            DataManager.Interstitial_Ad_ID = response.getString("interstital_ad_id");
                            DataManager.Native_Ad_ID = response.getString("admob_native_ad_id");
                            DataManager.Publisher_Ad_ID = response.getString("publisher_id");

                            DataManager.Banner_Ad = response.getBoolean("banner_ad");
                            DataManager.Interstitial_Ad = response.getBoolean("interstital_ad");
                            DataManager.Native_Ad = response.getBoolean("admob_nathive_ad");
*/
                            load_rinterstitial_Ad();
                            if(Cache_Adds.nnativeAd_ == null){
                                Cache_Adds.loadAd();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("Exception", error.toString());

                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }


    private void Get_Update() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, DataManager.Update_API, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            DataManager.Version = response.getInt("version");
                            DataManager.Version_Name = response.getString("version_name");
                            DataManager.Link = response.getString("url");
                            DataManager.descrption = response.getString("description");






                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("Exception", error.toString());
                        Get_Update_Backup();
                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }
    private void Get_Update_Backup() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, DataManager.Update_API_Backup, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            DataManager.Version = response.getInt("version");
                            DataManager.Version_Name = response.getString("version_name");
                            DataManager.Link = response.getString("url");
                            DataManager.descrption = response.getString("description");






                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("Exception", error.toString());

                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }


    public static void load_rinterstitial_Ad() {

        if (DataManager.ADMOB_ENABLE){
            AdRequest adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(getContext(),DataManager.Interstitial_Ad_ID, adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;
                            //  Log.i(TAG, "onAdLoaded");
                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    // Called when fullscreen content is dismissed.
                                    Log.d("TAG", "The ad was dismissed.");
                                    load_rinterstitial_Ad();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    // Called when fullscreen content failed to show.
                                    Log.d("TAG", "The ad failed to show.");
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    // Called when fullscreen content is shown.
                                    // Make sure to set your reference to null so you don't
                                    // show it a second time.
                                    mInterstitialAd = null;
                                    Log.d("TAG", "The ad was shown.");
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            //   Log.i(TAG, loadAdError.getMessage());
                            mInterstitialAd = null;
                        }
                    });


        }

    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.

        appOpenAdManager.showAdIfAvailable(currentActivity);

    }
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {}
    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.

        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity;
        }

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {}

    @Override
    public void onActivityPaused(@NonNull Activity activity) {}

    @Override
    public void onActivityStopped(@NonNull Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {}


    public void showAdIfAvailable(
            @NonNull Activity activity,
            @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener);
    }



    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    /** Inner class that loads and shows app open ads. */

    public  class AppOpenAdManager {

        //   private static final String LOG_TAG = "AppOpenAdManager";
        //   private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294";

        private AppOpenAd appOpenAd = null;
        private boolean isLoadingAd = false;
        private boolean isShowingAd = false;

        /** Keep track of the time an app open ad is loaded to ensure you don't show an expired ad. */
        private long loadTime = 0;

        /** Constructor. */
        public AppOpenAdManager() {}

        /**
         * Load an ad.
         *
         * @param context the context of the activity that loads the ad
         */
        private void loadAd(Context context,String id) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return;
            }

            isLoadingAd = true;
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(
                    context,
                    id,
                    request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    new AppOpenAd.AppOpenAdLoadCallback() {
                        /**
                         * Called when an app open ad has loaded.
                         *
                         * @param ad the loaded app open ad.
                         */
                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            appOpenAd = ad;
                            isLoadingAd = false;
                            loadTime = (new Date()).getTime();

                            //    Log.d(LOG_TAG, "onAdLoaded.");
                            //    Toast.makeText(context, "onAdLoaded", Toast.LENGTH_SHORT).show();

                        }

                        /**
                         * Called when an app open ad has failed to load.
                         *
                         * @param loadAdError the error.
                         */
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            isLoadingAd = false;
                            //     Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
                            //     Toast.makeText(context, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        /** Check if ad was loaded more than n hours ago. */
        private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
            long dateDifference = (new Date()).getTime() - loadTime;
            long numMilliSecondsPerHour = 3600000;
            return (dateDifference < (numMilliSecondsPerHour * numHours));
        }

        /** Check if ad exists and can be shown. */
        private boolean isAdAvailable() {
            // Ad references in the app open beta will time out after four hours, but this time limit
            // may change in future beta versions. For details, see:
            // https://support.google.com/admob/answer/9341964?hl=en
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         */
        private void showAdIfAvailable(@NonNull final Activity activity) {


            showAdIfAvailable(
                    activity,
                    new OnShowAdCompleteListener() {
                        @Override
                        public void onShowAdComplete() {
                            // Empty because the user will go back to the activity that shows the ad.
                        }
                    });


        }
        private AdRequest getAdRequest() {
            return new AdRequest.Builder().build();
        }
        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
         */
        private void showAdIfAvailable(
                @NonNull final Activity activity,
                @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                //    Log.d(LOG_TAG, "The app open ad is already showing.");
                return;
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                //    Log.d(LOG_TAG, "The app open ad is not ready yet.");
                onShowAdCompleteListener.onShowAdComplete();
                //  if (i % 3 == 1) {
                loadAd(activity,getContext().getResources().getString(R.string.app_open));
               /* try {
                    SharedPreferences prefs = mAppInstance.getSharedPreferences("whatsapp_pref",
                            Context.MODE_PRIVATE);
                    loadAd(mAppInstance,prefs.getString("appOpen",""));

                } catch (Exception e) {
                    Log.i("exception", "fetchAd: $e");
                }*/



                //    i++;
//                } else {
//                    i++;
//                }
                return;
            }

            //  Log.d(LOG_TAG, "Will show ad.");

            appOpenAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        /** Called when full screen content is dismissed. */
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            appOpenAd = null;
                            isShowingAd = false;


                            //    Log.d(LOG_TAG, "onAdDismissedFullScreenContent.");
                            //     Toast.makeText(activity, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT).show();

                            onShowAdCompleteListener.onShowAdComplete();

                            //     loadAd(activity);
                        }

                        /** Called when fullscreen content failed to show. */
                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            appOpenAd = null;
                            isShowingAd = false;

                            //   Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
                            //    Toast.makeText(activity, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT).show();

                            onShowAdCompleteListener.onShowAdComplete();
                            //      loadAd(activity);

                        }

                        /** Called when fullscreen content is shown. */
                        @Override
                        public void onAdShowedFullScreenContent() {

                            //  Log.d(LOG_TAG, "onAdShowedFullScreenContent.");
                            //   Toast.makeText(activity, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT).show();

                        }
                    });

            isShowingAd = true;
            appOpenAd.show(activity);
        }

    }
}