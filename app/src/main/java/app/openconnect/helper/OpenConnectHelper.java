package app.openconnect.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import app.openconnect.VpnProfile;
import app.openconnect.api.GrantPermissionsActivity;
import app.openconnect.core.ProfileManager;

public class OpenConnectHelper {
    private static Context activity;
    private static String PREFNAME = "openConnectGate";

    public OpenConnectHelper(Activity activity) {
        this.activity = activity;
    }

    public OpenConnectHelper(Context activity) {
        this.activity = activity;
    }

    public OpenConnectHelper setUserCredential(String username,String password){
        SharedPreferences p = activity.getSharedPreferences(PREFNAME, Activity.MODE_PRIVATE);
        p.edit().putString("username", username).apply();
        p.edit().putString("password", password).apply();
        return this;
    }

    public static String getUsername(){
        SharedPreferences p = activity.getSharedPreferences(PREFNAME, Activity.MODE_PRIVATE);
        return p.getString("username","");
    }

    public static String getPassword(){
        SharedPreferences p = activity.getSharedPreferences(PREFNAME, Activity.MODE_PRIVATE);
        return p.getString("password","");
    }


    //start vpn
    public void startVPN() {
        VpnProfile profile = ProfileManager.create("");
        Intent intent = new Intent(activity, GrantPermissionsActivity.class);
        String pkg = activity.getPackageName();

        intent.putExtra(pkg + GrantPermissionsActivity.EXTRA_UUID, profile.getUUID().toString());
        intent.setAction(Intent.ACTION_MAIN);
        activity.startActivity(intent);
    }

}
