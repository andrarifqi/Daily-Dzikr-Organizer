package com.rifqiandra.dailydzikrorganizer.appconfig;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class AppConfig {
    static Context context;
    static String SERVER = "http://192.168.56.1/";
    static String SERVER_FOLDER = SERVER + "DailyDzikrOrganizer/";
    static String FOLDER_API = SERVER_FOLDER + "API/";
    public static String API_WRITE = FOLDER_API + "write.php";
    public static String API_CALL = FOLDER_API + "call.php";
    public static String DATA_SEND;

    public static String getDeviceId(Context context) {

        String deviceId;

        deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        return deviceId;
    }
}
