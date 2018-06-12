package net.jsondroid.com.mytestsdk;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by wenbaohe on 2018/6/11.
 */

public class APNetUtils {

    //获取热点ssid
    public static String getValidApSsid(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
            method.setAccessible(true);
            WifiConfiguration configuration = (WifiConfiguration) method.invoke(wifiManager);
            return configuration.SSID;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取热点密码
    public static String getValidPassword(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration configuration = (WifiConfiguration) method.invoke(wifiManager);
            return configuration.preSharedKey;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
