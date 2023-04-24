package app.openconnect.Ad_Module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.vpnpro.dubaivpn.R;

import java.util.Objects;

import app.openconnect.MainActivity;
import app.openconnect.SplashActivity;


public class Ad extends AppCompatActivity {

    Intent intent;
    NativeAdView adView;
    ImageView mSkipBtn;
    TextView mSkipTimerBtn;
    public static boolean isAlreadyClicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_layout);


        init_add();

        mSkipBtn.setOnClickListener(view -> {

            goNext();
            if (Cache_Adds.nnativeAd_ != null) {
                Cache_Adds.nnativeAd_.destroy();
            }
            Cache_Adds.loadAd();
        });



        if (Cache_Adds.nnativeAd_ != null) {

            populateAdView(Cache_Adds.nnativeAd_, adView);

        } else {
            goNext();
        }
    }

    private void init_add() {
        adView = findViewById(R.id.parent_ad_view);
        mSkipTimerBtn = findViewById(R.id.ad_skip_bt_timer);
        mSkipBtn = findViewById(R.id.ad_skip_bt);
    }





    @Override
    protected void onResume() {
        overridePendingTransition(0, 0);
        if (isAlreadyClicked) {
            overridePendingTransition(0,0);
            goNext();
            isAlreadyClicked = false;
        }

        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
      //  goNext();
    }


    private void populateAdView(NativeAd ad, NativeAdView adView) {
        MediaView mediaView = adView.findViewById(R.id.ad_media);

        //mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        ((TextView) adView.getHeadlineView()).setText(Cache_Adds.nnativeAd_.getHeadline());
        if (Cache_Adds.nnativeAd_.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(Cache_Adds.nnativeAd_.getBody());
        }

        if (Cache_Adds.nnativeAd_.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(Cache_Adds.nnativeAd_.getCallToAction());
        }

        if (Cache_Adds.nnativeAd_.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    Cache_Adds.nnativeAd_.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(Cache_Adds.nnativeAd_);
    }

    public static int pxFromDp(final Context context, final float dp) {

        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }


    private void setMediaViewScale(MediaView mediaView) {

        mediaView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                if (child instanceof ImageView) {
                    ImageView imageView = (ImageView) child;
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
            }
        });
    }


    public void goNext() {

      /*  try {
            Intent intent = new Intent(Ad.this, MainActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.d("goNext_Exception", "" + e.getMessage());
        }*/
        finish();
    }





}



