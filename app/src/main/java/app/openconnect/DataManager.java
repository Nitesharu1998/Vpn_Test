package app.openconnect;

import android.content.SharedPreferences;

public class DataManager {
    public static SharedPreferences.Editor editor;
    public static SharedPreferences sharedPreferences;

    public static String[] username;
    public static String[] password;
    public int USA_Servers = 0;

    public static Boolean connected = false;
    public static Boolean connecting = false;


    public static final String Main_Api = "https://codingcafe.in/Uae/128.90.143/";

   
    public static final String Backup_Api = "https://codingcafe.in/Uae/128.90.143/";

    public static final String Servers_API = Main_Api + "api.php?action=get_servers";
    public static final String Settings_API = Main_Api + "api.php?action=get_settings";
    public static final String Update_API = Main_Api + "api.php?action=get_Update";


    public static final String Servers_API_Backup = Backup_Api + "api.php?action=get_servers";
    public static final String Settings_API_Backup = Backup_Api + "api.php?action=get_settings";
    public static final String Update_API_Backup = Backup_Api + "api.php?action=get_Update";


    public static boolean ADMOB_ENABLE = true;
    public static Boolean readyToPurchase =false;

    public static String[] Server_IPS;
    public static String[] Server_NameS;
    public static String[] city_NameS;
    public static int[] FlagIds;
    public static int[] Server_Types;



    public static String Package_Name = "";

    //Ads

/* public static String Interstitial_Ad_ID = "";
    public static String Banner_Ad_ID = "";
    public static String Native_Ad_ID = "";
    public static String Publisher_Ad_ID = "";



    public static Boolean Banner_Ad = true;
    public static Boolean Native_Ad = true;
    public static Boolean Interstitial_Ad = true;*/


    //Ads

    /*public static String Interstitial_Ad_ID = "ca-app-pub-3940256099942544/8691691433";
    public static String Banner_Ad_ID = "ca-app-pub-3940256099942544/6300978111";
    public static String Native_Ad_ID = "ca-app-pub-3940256099942544/2247696110";
    public static String Publisher_Ad_ID = "ca-app-pub-8563781473494124~3019394242";*/


    public static String Interstitial_Ad_ID = "/21753324030,22857996732/com.vpnpro.unblockproxy.dubaivpn_Interstitial";
    public static String Banner_Ad_ID = "/21753324030,22857996732/com.vpnpro.unblockproxy.dubaivpn_Banner";
    public static String Native_Ad_ID = "/21753324030,22857996732/com.vpnpro.unblockproxy.dubaivpn_Native";


    //Privacy Policy

    public static String Privacy_Policy = "";

    //About

    public static String Contact = "";
    public static String Email = "";
    public static String Website = "";
    public static String Company = "";


    //Update

    public static int Version;
    public static String Version_Name = "";
    public static String Link = "";
    public static String descrption = "";



    public static void setIsFirst(Boolean flag) {
        editor.putBoolean("firstopen", flag);
        editor.apply();
    }

    public static Boolean getIsFirst() {
        return sharedPreferences.getBoolean("firstopen", true);
    }

}

