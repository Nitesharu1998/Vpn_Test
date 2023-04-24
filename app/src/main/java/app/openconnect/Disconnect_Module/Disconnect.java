package app.openconnect.Disconnect_Module;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.vpnpro.dubaivpn.R;
import com.bumptech.glide.Glide;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;



import app.openconnect.DataManager;
import app.openconnect.MainActivity;
import app.openconnect.core.OpenVpnService;
import app.openconnect.utils.vpn_connect_time;

public class Disconnect extends AppCompatActivity {
    Toolbar toolbar_disconnected;
    String success_connection;
    TextView disconnected_county_name, disconnected_download_speed, disconnected_upload_speed, Duration;
    String hrSize;
    ImageView ratingBar_2;
    ImageView toolbar_back_button;
    int position = 0;
    int lastPosition = 0;
    boolean isRunning = true;
    ImageView country_image_connected;
    RatingBar ratingBar;
    LinearLayout dis_linear_value;
    SharedPreferences dis_sharedPreferences;
    private RelativeLayout layout;
    private AdView mAdView;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.disconnect);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // NativeAdNew.showNativeAd(this, findViewById(R.id.nativeAddisconnect));
        Initbanner();

        loadinit();


        stopService(new Intent(Disconnect.this, vpn_connect_time.class));
        dis_linear_value.setVisibility(View.VISIBLE);
        Duration.setText(MainActivity.time);


        disconnected_download_speed.setText(
                OpenVpnService.humanReadableByteCount(MainActivity.mConn.deltaStats.rxBytes, true)
        );
        disconnected_upload_speed.setText(
                OpenVpnService.humanReadableByteCount(MainActivity.mConn.deltaStats.txBytes, true));


        toolbar_back_button.setOnClickListener(view -> onBackPressed());


        Glide.with(Disconnect.this).load(R.drawable.my_rating).into(ratingBar_2);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                if (ratingBar.getRating() >= 4) {
                    Disconnect.this.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + Disconnect.this.getPackageName())));
                }


            }
        });
    }

    private void loadinit() {
        ratingBar_2 = findViewById(R.id.ratingBar_2);
        dis_linear_value = findViewById(R.id.dis_linear_value);
        ratingBar = findViewById(R.id.ratingbar);
        toolbar_disconnected = findViewById(R.id.toolbar_disconnected);
        toolbar_disconnected.setTitleTextColor(Color.WHITE);
        Duration = findViewById(R.id.Duration);
        disconnected_county_name = findViewById(R.id.disconnected_county_name);
        country_image_connected = findViewById(R.id.country_image_connected);
        toolbar_back_button = findViewById(R.id.toolbar_back_button);
        disconnected_download_speed = findViewById(R.id.disconnected_download_speed);
        disconnected_upload_speed = findViewById(R.id.disconnected_upload_speed);

        dis_sharedPreferences = getSharedPreferences("DATA", MODE_PRIVATE);

        disconnected_county_name.setText(DataManager.Server_NameS[MainActivity.SpinerIndex]);
        country_image_connected.setImageResource(DataManager.FlagIds[MainActivity.SpinerIndex]);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onPause() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        ratingBar.setRating(0);
        super.onRestart();
    }


    @Override
    public void onBackPressed() {
        Disconnect.this.finish();
        super.onBackPressed();
    }
    public void Initbanner() {

        if (DataManager.ADMOB_ENABLE) {

            View adContainer_admob = findViewById(R.id.bannerAddisconnect);
            mAdView = new AdView(this);
            mAdView.setAdSize(AdSize.LARGE_BANNER);
            mAdView.setAdUnitId(DataManager.Banner_Ad_ID);
            ((RelativeLayout) adContainer_admob).addView(mAdView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

        }

    }
}
