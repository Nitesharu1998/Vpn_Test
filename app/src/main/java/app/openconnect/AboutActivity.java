package app.openconnect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.vpnpro.dubaivpn.BuildConfig;
import com.vpnpro.dubaivpn.R;

import java.util.Objects;



public class AboutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);




        Toolbar toolbarAbout = findViewById(R.id.toolbar_ab);
        setSupportActionBar(toolbarAbout);
        setTitle(getResources().getString(R.string.about));

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbarAbout.setNavigationOnClickListener(view -> onBackPressed());


        TextView company = findViewById(R.id.company);
        TextView email = findViewById(R.id.email);
        TextView website = findViewById(R.id.website);
        TextView contact = findViewById(R.id.contact);


        company.setText(DataManager.Company);
        email.setText(DataManager.Email);
        website.setText(DataManager.Website);
        contact.setText(DataManager.Contact);

        LinearLayout btn_email = findViewById(R.id.btn_email);
        btn_email.setOnClickListener(v -> {
            Intent intent=new Intent(Intent.ACTION_SEND);
            String[] recipients={DataManager.Email};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback for "+getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, "You can send problems or suggestions to us." + "\n" +
                    "VersionName" + ":" + "  " + Build.VERSION.RELEASE + "\n" +
                    "VersionCode" + ":" + "  " + Build.MODEL + "\n" +
                    "Device Brand/Model: " + "  " + Build.MODEL + "\n" +
                    "System Version");
            intent.putExtra(Intent.EXTRA_CC,"");
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            startActivity(Intent.createChooser(intent, "Send mail"));

          });

        LinearLayout ll_share = findViewById(R.id.ll_share);
        ll_share.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Download " + getResources().getString(R.string.app_name) + " : " + "https://play.google.com/store/apps/details?id=" + getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent); });

        LinearLayout ll_rate = findViewById(R.id.ll_rate);
        ll_rate.setOnClickListener(v -> rateUs());


        TextView version = findViewById(R.id.version);
        version.setText(BuildConfig.VERSION_NAME);
    }
    private void rateUs() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}