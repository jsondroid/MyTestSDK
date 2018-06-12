package net.jsondroid.com.android.network.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by wenbaohe on 2018/6/11.
 */

public class WiFiUtils {

    /**
     * 获取附近所有的ssid
     */
    public static List<ScanResult> getSSIDs(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            return wifiManager.getScanResults();
        }
        return null;
    }

    /**
     * 获取附近所有的ssid
     */
    public static boolean isOpenWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }


    /**
     * 是否连接WIFI
     */
    public static  boolean isConnectedWifi(Context context) {
        if (!isOpenWifi(context)) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetworkInfo.isConnected();
    }

    /**
     * 获取当前连接wifi的SSID
     */
    public static String getCurrtWifiSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String ssid = "";
        if (isOpenWifi(context) && isConnectedWifi(context)) {
            ssid = wifiManager.getConnectionInfo().getSSID();
        }
        return replace(ssid);
    }

    /**
     * 去除前后双引号
     */
    private static  String replace(String str) {
        if (str != null && !str.isEmpty()) {
            if ((str.startsWith("\"") && str.endsWith("\"")) || (str.startsWith("\\“") && str.endsWith("\\”"))) {
                if (str.indexOf("\"") == 0) str = str.substring(1, str.length());   //去掉第一个 "
                String sstr = str.substring(0, str.length() - 1);  //去掉最后一个 "
                return sstr;
            }
        }
        return str;
    }
}
