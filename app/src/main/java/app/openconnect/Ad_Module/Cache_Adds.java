package app.openconnect.Ad_Module;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAd;

import app.openconnect.MainApplication;
import app.openconnect.DataManager;


public class Cache_Adds {

    public static NativeAd nnativeAd_;


    public static void loadAd() {


        AdLoader.Builder builder = new AdLoader.Builder(MainApplication.getContext(), DataManager.Native_Ad_ID);

        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                if (nnativeAd_ != null) {
                    nnativeAd_.destroy();
                }
                nnativeAd_ = nativeAd;
            }


        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        com.google.android.gms.ads.formats.NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {


            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);


            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                //    Toast.makeText(MainApplication.getContext(), "nativeAdLoaded2", Toast.LENGTH_SHORT).show();
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());


    }


    public NativeAd getAd() {

        if (nnativeAd_ != null)
            return nnativeAd_;
        else {
            return null;
        }
    }

    public static Boolean containsAd() {
        return nnativeAd_ != null;
    }


}
