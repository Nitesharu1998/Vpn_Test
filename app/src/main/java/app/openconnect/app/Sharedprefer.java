package app.openconnect.app;

import android.content.Context;
import android.content.SharedPreferences;

public class Sharedprefer {


    Context context;
    private SharedPreferences default_prefence;

    public Sharedprefer(Context context) {
        this.context = context;
        default_prefence = context.getSharedPreferences("uptoo_vpn", Context.MODE_PRIVATE);
    }


    public Boolean userlogin() {
        return default_prefence.getBoolean("userslogin", false);
    }

    public void userlogin(Boolean userlogin) {
        default_prefence.edit().putBoolean("userslogin", userlogin).apply();
    }


    public String getUsername() {
        return default_prefence.getString("username", "");
    }

    public void setUsername(String username) {
        default_prefence.edit().putString("username", username).apply();
    }






    public String getPassword() {
        return default_prefence.getString("password", "");
    }

    public void setPassword(String password) {
        default_prefence.edit().putString("password", password).apply();
    }




}
