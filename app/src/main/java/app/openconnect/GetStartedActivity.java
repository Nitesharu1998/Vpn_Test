package app.openconnect;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vpnpro.dubaivpn.R;

import java.util.ArrayList;
import java.util.List;


public class GetStartedActivity extends AppCompatActivity {

    AdView adView;
    private AdView mAdView;
    boolean mobileDataEnabled = false;
    NetworkInfo wifiCheck;
    private NetworkInfo mWifi;
    int clickPosition;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstarted);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TextView txtbtn = findViewById(R.id.getstarted_btn);
        TextView rateus = findViewById(R.id.rateus_btn);
        Initbanner();
        Application application = getApplication();
        if (!(application instanceof MainApplication)) {

            return;
        }
        ((MainApplication) application)
                .showAdIfAvailable(
                        this,
                        new MainApplication.OnShowAdCompleteListener() {
                            @Override
                            public void onShowAdComplete() {

                            }
                        });
        /*if (mobileDataEnabled || mWifi.isConnected()){
            BottomSheetDialog dialog = new BottomSheetDialog(GetStartedActivity.this);
            View bottom_sheet = getLayoutInflater().inflate(R.layout.rate_app_bottomsheet,null);{
                dialog.setContentView(bottom_sheet);
                dialog.show();
            }
        }
        else {
            startActivity(new Intent(GetStartedActivity.this,Nointernet.class));

        }*/

        // checking internter conncetion

        // Assume disabled

//        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(txtbtn, "alpha",  1f, .3f);
//        fadeOut.setDuration(1600);
//        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(txtbtn, "alpha", .3f, 1f);
//        fadeIn.setDuration(1600);
//        final AnimatorSet mAnimationSet = new AnimatorSet();
//        mAnimationSet.play(fadeIn).after(fadeOut);
//        mAnimationSet.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                mAnimationSet.start();
//            }
//        });
//        mAnimationSet.start();



       /* Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the blinking time with this parameter
        anim.setStartOffset(200);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        txtbtn.startAnimation(anim);*/



        List<Animator> animList = new ArrayList<>();

        ObjectAnimator anim = ObjectAnimator.ofFloat(txtbtn, "scaleX", 0.92f);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        animList.add(anim);

        anim = ObjectAnimator.ofFloat(txtbtn, "scaleY", 0.92f);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        animList.add(anim);

        AnimatorSet animButton = new AnimatorSet();
        animButton.playTogether(animList);
        animButton.setDuration(400);
        animButton.start();
        txtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetStartedActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    BottomSheetDialog dialog = new BottomSheetDialog(GetStartedActivity.this,R.style.SheetDialog);
                    View bottom_sheet = getLayoutInflater().inflate(R.layout.rate_app_bottomsheet, null);

                    ImageView imageView = bottom_sheet.findViewById(R.id.rateIcon);


                    ImageView star1 = bottom_sheet.findViewById(R.id.star1);
                    ImageView star2 = bottom_sheet.findViewById(R.id.star2);
                    ImageView star3 = bottom_sheet.findViewById(R.id.star3);
                    ImageView star4 = bottom_sheet.findViewById(R.id.star4);
                    ImageView star5 = bottom_sheet.findViewById(R.id.star5);

                    star1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //chnge emojis
                            clickPosition = 1;
                            imageView.setImageResource(R.drawable.ic_rate1_icon);
                            star1.setImageResource(R.drawable.star_icon_fill);
                            star2.setImageResource(R.drawable.star_icon_border);
                            star3.setImageResource(R.drawable.star_icon_border);
                            star4.setImageResource(R.drawable.star_icon_border);
                            star5.setImageResource(R.drawable.star_icon_border);
                            bottom_sheet.findViewById(R.id.rate_app_btn).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
                        }
                    });

                    star2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clickPosition = 2;
                            imageView.setImageResource(R.drawable.ic_rate2_icon);
                            star1.setImageResource(R.drawable.star_icon_fill);
                            star2.setImageResource(R.drawable.star_icon_fill);
                            star3.setImageResource(R.drawable.star_icon_border);
                            star4.setImageResource(R.drawable.star_icon_border);
                            star5.setImageResource(R.drawable.star_icon_border);
                            bottom_sheet.findViewById(R.id.rate_app_btn).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));


                        }
                    });

                    star3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clickPosition = 3;
                            imageView.setImageResource(R.drawable.ic_rate3_icon);
                            star1.setImageResource(R.drawable.star_icon_fill);
                            star2.setImageResource(R.drawable.star_icon_fill);
                            star3.setImageResource(R.drawable.star_icon_fill);
                            star4.setImageResource(R.drawable.star_icon_border);
                            star5.setImageResource(R.drawable.star_icon_border);

                            bottom_sheet.findViewById(R.id.rate_app_btn).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));

                        }
                    });

                    star4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clickPosition = 4;
                            imageView.setImageResource(R.drawable.ic_rate4_icon);
                            star1.setImageResource(R.drawable.star_icon_fill);
                            star2.setImageResource(R.drawable.star_icon_fill);
                            star3.setImageResource(R.drawable.star_icon_fill);
                            star4.setImageResource(R.drawable.star_icon_fill);
                            star5.setImageResource(R.drawable.star_icon_border);

                            bottom_sheet.findViewById(R.id.rate_app_btn).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));


                        }
                    });

                    star5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clickPosition = 5;
                            imageView.setImageResource(R.drawable.ic_rate5_icon);
                            star1.setImageResource(R.drawable.star_icon_fill);
                            star2.setImageResource(R.drawable.star_icon_fill);
                            star3.setImageResource(R.drawable.star_icon_fill);
                            star4.setImageResource(R.drawable.star_icon_fill);
                            star5.setImageResource(R.drawable.star_icon_fill);
                            bottom_sheet.findViewById(R.id.rate_app_btn).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
                        }
                    });

                    bottom_sheet.findViewById(R.id.remind_later_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });


                    bottom_sheet.findViewById(R.id.rate_app_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (clickPosition == 1) {
                                dialog.dismiss();
                                Toast.makeText(GetStartedActivity.this, "Rating Submitted Successfully", Toast.LENGTH_SHORT).show();
                            } else if (clickPosition == 2) {
                                dialog.dismiss();
                                Toast.makeText(GetStartedActivity.this, "Rating Submitted Successfully", Toast.LENGTH_SHORT).show();
                            } else if (clickPosition == 3) {
                                dialog.dismiss();
                                Toast.makeText(GetStartedActivity.this, "Rating Submitted Successfully", Toast.LENGTH_SHORT).show();
                            } else if (clickPosition == 4) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                                dialog.dismiss();

                            } else if (clickPosition == 5) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                                dialog.dismiss();

  try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                                }


                            }

                        }
                    });

                    dialog.setContentView(bottom_sheet);
                    dialog.show();
                }

        });


    }

    public void Initbanner() {

        if (DataManager.ADMOB_ENABLE) {


                View adContainer_admob = findViewById(R.id.banner_getstarted);
                mAdView = new AdView(this);
                mAdView.setAdSize(AdSize.LARGE_BANNER);
                mAdView.setAdUnitId(DataManager.Banner_Ad_ID);
                ((RelativeLayout) adContainer_admob).addView(mAdView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);



        }

    }
}