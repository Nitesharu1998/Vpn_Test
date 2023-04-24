
package app.openconnect;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vpnpro.dubaivpn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import app.openconnect.Ad_Module.Ad;
import app.openconnect.Ad_Module.Cache_Adds;
import app.openconnect.Disconnect_Module.Disconnect;
import app.openconnect.Premium_Feature.Premium_dialog;
import app.openconnect.core.OpenConnectManagementThread;
import app.openconnect.core.OpenVpnService;
import app.openconnect.core.ProfileManager;
import app.openconnect.core.VPNConnector;
import app.openconnect.helper.IabBroadcastReceiver;
import app.openconnect.helper.SharedPreferencesManager;

@SuppressLint("MissingPermission")
public class MainActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener, IabBroadcastReceiver.IabBroadcastListener,
        DialogInterface.OnClickListener {

    int clickPosition;

    public static Dialog err_dialog;
    private ImageView cursor;
    public static String time;


    private NativeAd nativeAd_;

    private LinearLayout mainLayout;
    private ProgressBar progressBar;

    private TextView LocationView, title_tv, des_tv;
    public static final String TAG = "OpenConnect";

    private String selectedServerType = "FREE";
    private TextView tvSafe;


    public View view;
    // Will the subscription auto-renew?
    private boolean shouldShowTimer = false;


    private int mConnectionState = OpenConnectManagementThread.STATE_DISCONNECTED;
    public static VPNConnector mConn;

    private PrefManager prefManager;
    private Dialog LoadingDialog, LocationDialog, PTDiaglogbox, ConnectedDialog, DisconnectDialog, RateDialog, CloseDialog, ProDialog;
    private Animation PlayButtonAnim;
    private ImageView PlayButton;
    String SelectedUUID = "";
    public static int SpinerIndex = 0;
    public static int retryCount = 1;
    private OpenVpnService OpenService;
    private boolean ConnectCommand = false;
    private Boolean OnOpen = false;
    private String[] FlagIds;
    private Boolean ActiveAnimation;
    private int RateIndex = 0;
    private Boolean CanClose = false;

    private TextView UpInfo, TimeInfo, DownInfo;
    private TextView Status;
    private ImageView Status2;
    private ImageView Img_Flg;
    private RelativeLayout board;
    private RecyclerView RV;
    private TextView btnFreeServers;
    private TextView btnPremiumServers;
    private ImageView Tap_Cnct;
    private int Call_Index;
    Button Dis_disconnect;
    Button Dis_Cancle;
    AdView LC_Banner;
    private boolean InAdShown = false;
    private int AdNetwork = 0;
    private boolean RequestForDisconnect = false;
    private boolean PusedByAd = false;
    private boolean AllowInAd = false;
    private int OpencCount = 0;
    private Button btnRetry;
    private RequestQueue requestQueue;

    RelativeLayout layout;
    com.google.android.gms.ads.AdView adView;
    private AdView mAdView;
    LinearLayout btnVIP;
    ImageView btnrate;
    ImageView btnshare;

    private NavigationView navigationView;
    // private LottieAnimationView mLottieAnimationView;
    private ObjectAnimator anim;
    private LinearLayout subscription_ll;
    DrawerLayout navDrawer;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferencesManager.init(MainActivity.this);
        if (Cache_Adds.nnativeAd_ != null) {
            Intent intent = new Intent(MainActivity.this, Ad.class);
            startActivity(intent);
        }


        OnOpen = true;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        btnshare = findViewById(R.id.share_us_tv);
        //btnrate = findViewById(R.id.rate_us_tv);
        PlayButton = findViewById(R.id.img_stts);
        Status = findViewById(R.id.play);
        tvSafe = findViewById(R.id.textView23);

        DownInfo = findViewById(R.id.textView7);
        TimeInfo = findViewById(R.id.time_info_tv);
        UpInfo = findViewById(R.id.textView22);
        LocationView = findViewById(R.id.location_view);
        Img_Flg = findViewById(R.id.img_flg);
        board = findViewById(R.id.imageView7);
        mainLayout = findViewById(R.id.main_layout);
        progressBar = findViewById(R.id.progress_bar);
        btnRetry = findViewById(R.id.btnRetry);
        //  mLottieAnimationView = findViewById(R.id.animationView);
        subscription_ll = findViewById(R.id.subscription_ll);


        Initbanner();
        //Get_Update();


        navDrawer = findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, navDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        navDrawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        Log.d(TAG, "Creating IAB helper.");


        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDataFromServer();
                progressBar.setVisibility(View.VISIBLE);
                mainLayout.setVisibility(View.GONE);
                btnRetry.setVisibility(View.GONE);
            }
        });


        fetchDataFromServer();
        ActiveAnimation = false;


      /*  btnrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateUs();

            }
        });*/
        btnshare.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                // If the navigation drawer is not open then open it, if its already open then close it.
                if (!navDrawer.isDrawerOpen(GravityCompat.START))
                    navDrawer.openDrawer(Gravity.START);
                else navDrawer.closeDrawer(Gravity.END);
            }
        });
        subscription_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Premium_dialog dialog = new Premium_dialog(MainActivity.this, R.style.AppTheme);
                dialog.show();

            }
        });

        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainApplication.mInterstitialAd != null) {
                    MainApplication.mInterstitialAd.show(MainActivity.this);
                } else {
                    MainApplication.load_rinterstitial_Ad();
                    if (Cache_Adds.nnativeAd_ != null) {
                        Intent intent = new Intent(MainActivity.this, Ad.class);
                        startActivity(intent);
                    }
                }
                ShowLocationsWindow();
            }
        });

        PlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (OpenService.getConnectionState() == OpenConnectManagementThread.STATE_CONNECTED) {
                    // mLottieAnimationView.setVisibility(View.INVISIBLE);
                    Disconnect();
                    if (MainApplication.mInterstitialAd != null) {
                        MainApplication.mInterstitialAd.show(MainActivity.this);
                    } else {
                        MainApplication.load_rinterstitial_Ad();
                        if (Cache_Adds.nnativeAd_ != null) {
                            Intent intent = new Intent(MainActivity.this, Ad.class);
                            startActivity(intent);
                        }
                    }
                } else {
                    // mLottieAnimationView.setVisibility(View.VISIBLE);
                  /*  if (MainApplication.mInterstitialAd != null) {
                        MainApplication.mInterstitialAd.show(MainActivity.this);
                    } else {
                        MainApplication.load_rinterstitial_Ad();
                        if (Cache_Adds.nnativeAd_ != null) {
                            Intent intent = new Intent(MainActivity.this, Ad.class);
                            startActivity(intent);
                        }
                    }
*/
                    ConnectCommand = true;
                    if (ActiveAnimation) {
                        PlayButtonAnimation(1);
                    }
                    startVPN();
                }
            }
        });
        prefManager = new PrefManager(getBaseContext(), PrefManager.PRF_APP_DATA, PrefManager.MODE_READ);
        OpencCount = prefManager.ReadInt(PrefManager.KEY_OPEN_COUNT);
        RateIndex = prefManager.ReadInt(PrefManager.KEY_RATE_INDEX);
        if (OpencCount != 0) {
        }
        OpencCount++;
        prefManager = new PrefManager(getBaseContext(), PrefManager.PRF_APP_DATA, PrefManager.MODE_WRITE);
        prefManager.SaveIntData(PrefManager.KEY_OPEN_COUNT, OpencCount);
        AllowInAd = true;
    }


    private void initDrawer(Toolbar toolbar) {
//        final DrawerLayout drawer = findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, navDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        navDrawer.addDrawerListener(toggle);

        navDrawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }
        });
        toggle.syncState();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_prem:
                Premium_dialog dialog = new Premium_dialog(MainActivity.this, R.style.AppTheme);
                dialog.show();
                break;
            case R.id.nav_contact_us:
                contactUs();
                break;

/*            case R.id.nav_privacy:

                Intent intent = new Intent(getApplicationContext(), PrivacyPolicyActivity.class);
                startActivity(intent);
                break;*/
            case R.id.nav_rate:
                rateUs();
                break;
            case R.id.nav_about_us:
                Intent intent2 = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent2);
                break;

            case R.id.nav_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Download " + getResources().getString(R.string.app_name) + " : " + "https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            default:
                return true;
        }

//        DrawerLayout drawer = findViewById(R.id.activity_main);
        navDrawer.closeDrawer(GravityCompat.START);
        return true;

    }


    private void contactUs() {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.Email)});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));


        try {
            startActivity(Intent.createChooser(intent, getString(R.string.sendmail)));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, getResources().getString(R.string.nomailappfound) + "", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this, getResources().getString(R.string.unexpected) + "", Toast.LENGTH_SHORT).show();
        }
    }

    CountDownTimer timer_connecting;

    public static boolean isConnected() {

        String iface = "";
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    iface = networkInterface.getName();
                if (iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                    return true;
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }

        return false;
    }


    public void fetchDataFromServer() {

        progressBar.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = DataManager.Servers_API;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response ->
        {
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    JSONArray serverArray = jsonObject.getJSONArray("servers");
                    DataManager.Server_NameS = new String[serverArray.length()];
                    DataManager.city_NameS = new String[serverArray.length()];
                    DataManager.Server_IPS = new String[serverArray.length()];
                    DataManager.FlagIds = new int[serverArray.length()];
                    DataManager.Server_Types = new int[serverArray.length()];
                    DataManager.username = new String[serverArray.length()];
                    DataManager.password = new String[serverArray.length()];

                    for (int i = 0; i < serverArray.length(); i++) {
                        JSONObject serverObject = serverArray.getJSONObject(i);
                        String hostName = serverObject.getString("HostName");
                        String cityName = serverObject.getString("city");
                        String ip = serverObject.getString("IP");
                        String flag = serverObject.getString("certificate");
                        int type = serverObject.getInt("type");
                        String username = serverObject.getString("username");
                        String password = serverObject.getString("password");

                        // Toast.makeText(MainActivity.this, String.valueOf(DataManager.Server_NameS.length), Toast.LENGTH_SHORT).show();


                        DataManager.Server_NameS[i] = hostName;
                        DataManager.city_NameS[i] = cityName;
                        DataManager.Server_IPS[i] = ip;
                        DataManager.FlagIds[i] = getResources().getIdentifier(flag, "drawable", getPackageName());
                        DataManager.Server_Types[i] = type;
                        DataManager.username[i] = username;
                        DataManager.password[i] = password;


                    }

                    mainLayout.setVisibility(View.VISIBLE);
                    btnRetry.setVisibility(View.GONE);
                    InitiateLocationWindow();

                } else {
                    Toast.makeText(MainActivity.this, "Unable to process server response", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Unable to process server response", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            fetchDataFromBackupServer();
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 3, 3));
        queue.add(stringRequest);

    }

    public void fetchDataFromBackupServer() {

        progressBar.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = DataManager.Servers_API_Backup;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response ->
        {
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    JSONArray serverArray = jsonObject.getJSONArray("servers");
                    DataManager.Server_NameS = new String[serverArray.length()];
                    DataManager.city_NameS = new String[serverArray.length()];
                    DataManager.Server_IPS = new String[serverArray.length()];
                    DataManager.FlagIds = new int[serverArray.length()];
                    DataManager.Server_Types = new int[serverArray.length()];
                    DataManager.username = new String[serverArray.length()];
                    DataManager.password = new String[serverArray.length()];

                    for (int i = 0; i < serverArray.length(); i++) {
                        JSONObject serverObject = serverArray.getJSONObject(i);
                        String hostName = serverObject.getString("HostName");
                        String cityName = serverObject.getString("city");
                        String ip = serverObject.getString("IP");
                        String flag = serverObject.getString("certificate");
                        int type = serverObject.getInt("type");
                        String username = serverObject.getString("username");
                        String password = serverObject.getString("password");


                        DataManager.Server_NameS[i] = hostName;
                        DataManager.city_NameS[i] = cityName;
                        DataManager.Server_IPS[i] = ip;
                        DataManager.FlagIds[i] = getResources().getIdentifier(flag, "drawable", getPackageName());
                        DataManager.Server_Types[i] = type;
                        DataManager.username[i] = username;
                        DataManager.password[i] = password;
                    }

                    mainLayout.setVisibility(View.VISIBLE);
                    btnRetry.setVisibility(View.GONE);
                    InitiateLocationWindow();

                } else {
                    Toast.makeText(MainActivity.this, "Unable to process server response", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Unable to process server response", Toast.LENGTH_SHORT).show();
            }
        }, error -> check_cache_data());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 3, 3));
        queue.add(stringRequest);

    }

    private void check_cache_data() {
        progressBar.setVisibility(View.GONE);
        btnRetry.setVisibility(View.VISIBLE);
        Toast.makeText(MainActivity.this, "No Internet Connection, Please Check And RETRY", Toast.LENGTH_SHORT).show();

    }
/*

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


                            myupdate();


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

    @SuppressLint("SetTextI18n")
    public void myupdate() {
        final Dialog dialog;
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update);

        final ImageView imageView = dialog.findViewById(R.id.cancel);
        final TextView app_name = dialog.findViewById(R.id.app_name);
        final TextView t1 = dialog.findViewById(R.id.t1);
        final TextView t2 = dialog.findViewById(R.id.t2);
        final TextView t3 = dialog.findViewById(R.id.t3);
        final TextView textView = dialog.findViewById(R.id.text_view_go_pro);

        app_name.setText(getResources().getString(R.string.app_name));
        t1.setText("Version Code " + DataManager.Version);
        t2.setText("Version Name " + DataManager.Version_Name);
        t3.setText(DataManager.descrption);

        imageView.setOnClickListener(view -> dialog.cancel());

        textView.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(DataManager.Link))));

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (BuildConfig.VERSION_CODE != DataManager.Version) {
            if (!BuildConfig.VERSION_NAME.equals(DataManager.Version_Name)) {
                dialog.show();
            }

        }
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }
*/


    public void ShowLocationsWindow() {
        LocationDialog.show();
    }

    public void InitiateLocationWindow() {
        LocationDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
        LocationDialog.setContentView(R.layout.location_window);
        LocationDialog.findViewById(R.id.upgrade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Premium_dialog dialog = new Premium_dialog(MainActivity.this, R.style.AppTheme);


                dialog.show();

            }

        });

        RV = LocationDialog.findViewById(R.id.rv);
        RV.setLayoutManager(new LinearLayoutManager(this));
        RV.setAdapter(new LocationRecyclerAdapter(DataManager.Server_NameS, DataManager.city_NameS, DataManager.FlagIds, new LocationRecyclerAdapter.OnItemListener() {
            @Override
            public void OnItemClick(int index) {
                if (DataManager.Server_Types[index] == 2) {
                    if (DataManager.ADMOB_ENABLE) {
                        Premium_dialog dialog = new Premium_dialog(MainActivity.this, R.style.AppTheme);
                        dialog.show();
                    } else {

                        prefManager = new PrefManager(getBaseContext(), PrefManager.PRF_APP_DATA, PrefManager.MODE_WRITE);
                        SpinerIndex = index;
                        prefManager.SaveIntData(PrefManager.KEY_SPINER_INDEX, SpinerIndex);

                        LocationView.setText(DataManager.Server_NameS[SpinerIndex]);
                        Img_Flg.setImageResource(DataManager.FlagIds[SpinerIndex]);
                        ConnectCommand = true;
                        if (ActiveAnimation) {
                            PlayButtonAnimation(1);
                        }
                        startVPN();
                        LocationDialog.dismiss();
                    }
                } else {
                    prefManager = new PrefManager(getBaseContext(), PrefManager.PRF_APP_DATA, PrefManager.MODE_WRITE);
                    SpinerIndex = index;
                    prefManager.SaveIntData(PrefManager.KEY_SPINER_INDEX, SpinerIndex);

                    LocationView.setText(DataManager.Server_NameS[SpinerIndex]);
                    Img_Flg.setImageResource(DataManager.FlagIds[SpinerIndex]);
                    ConnectCommand = true;
                    if (ActiveAnimation) {
                        PlayButtonAnimation(1);
                    }
                    startVPN();
                    //    LC_Banner.destroy();
                    LocationDialog.dismiss();
                }
            }
        }));


        // LC_Banner = LocationDialog.findViewById(R.id.lc_banner);
        ImageView btnclose2 = LocationDialog.findViewById(R.id.ic_back_tv);

        btnclose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationDialog.dismiss();
            }
        });

    }

    public void Disconnect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.disconnectdialog);
        builder.setCancelable(false);

        View alertView = View.inflate(MainActivity.this, R.layout.dialog_medium_banner_layout, null);
        showNativeAd(alertView);
        builder.setTitle("Do you want to disconnect?")

                .setView(alertView);

        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    mConn.service.stopVPN();
                    Intent intent = new Intent(MainActivity.this, Disconnect.class);
                    startActivity(intent);
                    loadNativeAd();
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loadNativeAd();
                    }
                });
        builder.show();

    }


/*    public void errorDialog() {


        err_dialog = new Dialog(MainActivity.this);
        err_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        err_dialog.setContentView(R.layout.error_dialog);
        err_dialog.setCancelable(false);

        final ImageView imageView = err_dialog.findViewById(R.id.cancel);
        final TextView textView = err_dialog.findViewById(R.id.text_view_go_pro);


        imageView.setOnClickListener(view -> err_dialog.cancel());

        textView.setOnClickListener(view -> {
            err_dialog.dismiss();
            LocationDialog.show();
        });


        Window window = err_dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

         // err_dialog.show();
    }*/


    public void Initbanner() {

        if (DataManager.ADMOB_ENABLE) {

            View adContainer_admob = findViewById(R.id.banner_main);
            mAdView = new AdView(this);
            mAdView.setAdSize(AdSize.LARGE_BANNER);
            mAdView.setAdUnitId(DataManager.Banner_Ad_ID);
            ((RelativeLayout) adContainer_admob).addView(mAdView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void UpdateUI(OpenVpnService service) {
        int state = service.getConnectionState();
        service.startActiveDialog(this);

        if (mConnectionState != state) {
            if (state == OpenConnectManagementThread.STATE_DISCONNECTED) {
                //mLottieAnimationView.setVisibility(View.INVISIBLE);
                PlayButton.setImageResource(R.drawable.new_ic_disconnect_svg);
                Status.setVisibility(View.VISIBLE);
                Status.setText(R.string.connect);
                tvSafe.setVisibility(View.GONE);

                PlayButton.setEnabled(true);
                if (!ActiveAnimation)
                    PlayButton.setImageAlpha(255);

                LocationView.setEnabled(true);
                Img_Flg.setEnabled(true);
                Img_Flg.setImageAlpha(255);
                shouldShowTimer = false;
                DataManager.connected = false;

            } else if (state == OpenConnectManagementThread.STATE_CONNECTED) {

                if (ConnectCommand) {
                    DataManager.connected = true;
                    //  mLottieAnimationView.setVisibility(View.INVISIBLE);
//                    intervalInterstitialAd.show();
                    Status.setText(R.string.state_connected);
                    if (MainApplication.mInterstitialAd != null) {
                        MainApplication.mInterstitialAd.show(MainActivity.this);
                    } else {
                        MainApplication.load_rinterstitial_Ad();
                        if (Cache_Adds.nnativeAd_ != null) {
                            Intent intent = new Intent(MainActivity.this, Ad.class);
                            startActivity(intent);
                        }
                    }
                    Status.setVisibility(View.VISIBLE);
                    tvSafe.setVisibility(View.VISIBLE);
                    DataManager.connecting = false;
                    Status.setTextColor(getResources().getColor(R.color.white));
                    PlayButton.setEnabled(true);
                    ConnectCommand = false;
                    LocationView.setEnabled(true);
                    Img_Flg.setEnabled(true);
                    Img_Flg.setImageAlpha(255);

                    PlayButton.setImageAlpha(255);
                    PlayButton.setImageResource(R.drawable.ic_new_svg_connect);
                    //starttimer();
                    service.startTime = new Date();
                    shouldShowTimer = true;


                    if (ActiveAnimation)
                        PlayButtonAnimation(0);
                    //InitAds();
//                    Initbanner();
                }


            } else if (state == OpenConnectManagementThread.STATE_AUTHENTICATING || state == OpenConnectManagementThread.STATE_CONNECTING) {
                PlayButton.setEnabled(false);
                Status.setText(R.string.state_connecting);
                DataManager.connecting = true;

                // showTioast();


                //  mLottieAnimationView.setVisibility(View.VISIBLE);

                PlayButton.setImageResource(R.drawable.new_ic_disconnect_svg);


                PlayButton.setImageAlpha(100);
                LocationView.setEnabled(false);
                Img_Flg.setEnabled(false);
                Img_Flg.setImageAlpha(100);
            }
            mConnectionState = state;
        }


        if (state == OpenConnectManagementThread.STATE_CONNECTED) {
            if (shouldShowTimer) {
                DownInfo.setText(
                        OpenVpnService.humanReadableByteCount(mConn.deltaStats.rxBytes, true)
                );
                UpInfo.setText(
                        OpenVpnService.humanReadableByteCount(mConn.deltaStats.txBytes, true));
                TimeInfo.setText(OpenVpnService.formatElapsedTime(service.startTime.getTime()));
                TimeInfo.setTextColor(getResources().getColor(R.color.white));
                time = TimeInfo.getText().toString();

            }


        } else if (mConnectionState == OpenConnectManagementThread.STATE_DISCONNECTED) {
            shouldShowTimer = false;
            TimeInfo.setText("00:00");
            TimeInfo.setTextColor(getResources().getColor(R.color.white));
            DownInfo.setText("0 Mbps");
            UpInfo.setText("0 Mbps");
        }

    }

    @SuppressLint("WrongConstant")
    private void manageBlinkEffect() {
        anim = ObjectAnimator.ofInt(Status, "backgroundColor", Color.WHITE, Color.RED,
                Color.WHITE);
        anim.setDuration(1500);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();
    }

    private void loadNativeAd() {

        if (DataManager.ADMOB_ENABLE) {

            AdLoader.Builder builder = new AdLoader.Builder(MainActivity.this, DataManager.Native_Ad_ID);

            builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                @Override
                public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                    if (nativeAd_ != null) {
                        nativeAd_.destroy();
                    }
                    nativeAd_ = nativeAd;
                }


            });

            VideoOptions videoOptions = new VideoOptions.Builder()
                    .setStartMuted(true)
                    .build();

            NativeAdOptions adOptions = new NativeAdOptions.Builder()
                    .setVideoOptions(videoOptions)
                    .build();

            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(new AdListener() {


                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                }
            }).build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }

    }


    private void showNativeAd(View rootView) {
        if (nativeAd_ != null) {


            NativeAdView adView = (NativeAdView) getLayoutInflater()
                    .inflate(R.layout.ad_unified_large, null);
            populateUnifiedNativeAdView(nativeAd_, adView);

            FrameLayout frameLayout = rootView.findViewById(R.id.fl_adplaceholder);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        }
    }

    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        MediaView mediaView = adView.findViewById(R.id.ad_media);

        //mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }


    public void PlayButtonAnimation(int index) {
        if (index == 0) {
            PlayButtonAnim = new ScaleAnimation(1f, 1.02f, 1f, 1.02f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 1f);
            PlayButtonAnim.setDuration(350);
            PlayButtonAnim.setRepeatCount(Animation.INFINITE);
            PlayButtonAnim.setRepeatMode(Animation.REVERSE);
        } else if (index == 1) {
            PlayButtonAnim = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            PlayButtonAnim.setDuration(500);
            PlayButtonAnim.setRepeatCount(Animation.INFINITE);
        }

        PlayButton.startAnimation(PlayButtonAnim);

    }


    public void s(String s) {

        Toast.makeText(getApplicationContext(), s,
                Toast.LENGTH_SHORT).show();

    }

    private void startVPN() {

        Intent prepIntent;
        try {
            prepIntent = VpnService.prepare(this);
        } catch (Exception e) {
            //reportBadRom(e);
            return;
        }

        if (prepIntent != null) {
            try {
                startActivityForResult(prepIntent, 0);
            } catch (Exception e) {
                //reportBadRom(e);
                return;
            }
        } else {
            onActivityResult(0, RESULT_OK, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode);
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        if (resultCode == RESULT_OK) {
            int n;
            if (SpinerIndex == 0) {
                Random rand = new Random();
                n = rand.nextInt(DataManager.Server_IPS.length);
            } else {
                n = SpinerIndex;
            }
            ProfileManager.mProfiles.clear();
            String s = ProfileManager.create(DataManager.Server_IPS[n]).getUUID().toString();
            Intent intent = new Intent(getBaseContext(), OpenVpnService.class);
            intent.putExtra(OpenVpnService.EXTRA_UUID, s);
            startService(intent);

        } else {

            if (ActiveAnimation) {
                PlayButtonAnimation(0);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // AppOpenAdManager.showAdIfAvailable(MainActivity.this);
        // checkPurchases();
        mConn = new VPNConnector(this, false) {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onUpdate(OpenVpnService service) {
                OpenService = service;
                UpdateUI(service);

                if (OnOpen) {
                    if (OpenService.getConnectionState() == OpenConnectManagementThread.STATE_CONNECTED) {
                        prefManager = new PrefManager(getBaseContext(), PrefManager.PRF_APP_DATA, PrefManager.MODE_READ);
                        SpinerIndex = prefManager.ReadInt(PrefManager.KEY_SPINER_INDEX);
                        Status.setText(R.string.state_connected);
                        if (MainApplication.mInterstitialAd != null) {
                            MainApplication.mInterstitialAd.show(MainActivity.this);
                        } else {
                            MainApplication.load_rinterstitial_Ad();
                            if (Cache_Adds.nnativeAd_ != null) {
                                Intent intent = new Intent(MainActivity.this, Ad.class);
                                startActivity(intent);
                            }
                        }
                        Status.setVisibility(View.VISIBLE);
                        DataManager.connected = true;
                        DataManager.connecting = false;
                        PlayButton.setImageResource(R.drawable.ic_new_svg_connect);
                        //Status.setTextColor(getResources().getColor(R.color.green_text));
                        shouldShowTimer = true;

                    } else {
                        prefManager = new PrefManager(getBaseContext(), PrefManager.PRF_APP_DATA, PrefManager.MODE_WRITE);
                        SpinerIndex = 0;
                        DataManager.connected = false;
                        DataManager.connecting = false;
                        Status.setVisibility(View.VISIBLE);
                        prefManager.SaveIntData(PrefManager.KEY_SPINER_INDEX, SpinerIndex);
                        shouldShowTimer = false;
                    }
                    if (DataManager.Server_NameS != null) {
                        LocationView.setText(DataManager.Server_NameS[SpinerIndex]);
                        Img_Flg.setImageResource(DataManager.FlagIds[SpinerIndex]);
                    }
                    OnOpen = false;
                }

            }

        };

    }

    @Override
    protected void onPause() {
        mConn.stopActiveDialog();
        mConn.unbind();
        super.onPause();
    }

    public void onBackPressed() {
        if (navDrawer.isDrawerOpen(GravityCompat.START)) {
            navDrawer.closeDrawer(GravityCompat.START);
        } else {
            setResult(Activity.RESULT_CANCELED);

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.disconnectdialog);
        builder.setCancelable(false);

        View alertView = View.inflate(MainActivity.this, R.layout.dialog_medium_banner_layout, null);
        showNativeAd(alertView);
        builder.setTitle("Do you want to Exit?")

                .setView(alertView);

        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    finishAffinity();
                    loadNativeAd();
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loadNativeAd();
                    }
                });
        builder.show();


    }

    private void rateUs() {


        BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this, R.style.SheetDialog);
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
                    Toast.makeText(MainActivity.this, "Rating Submitted Successfully", Toast.LENGTH_SHORT).show();
                } else if (clickPosition == 2) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Rating Submitted Successfully", Toast.LENGTH_SHORT).show();
                } else if (clickPosition == 3) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Rating Submitted Successfully", Toast.LENGTH_SHORT).show();
                } else if (clickPosition == 4) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    dialog.dismiss();

                } else if (clickPosition == 5) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    dialog.dismiss();


                }

            }
        });

        dialog.setContentView(bottom_sheet);
        dialog.show();
    }


    public void Rate() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
        return;
    }


    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}