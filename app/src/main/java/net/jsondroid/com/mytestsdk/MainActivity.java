package net.jsondroid.com.mytestsdk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import net.jsondroid.com.android.network.NetWorkUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @RequiresApi(api = 26)
    public void clickBtn(View view) {

        startWifiAp();
//        getwifi();
//        NetWorkUtil.getInstance().setOnWifiListenter(new OnWifiListenter() {
//            @Override
//            public void onConnet(int nettype, String wifiname, String ip) {
//
//                Log.e("onConnet--->", "" + nettype + "-----" + wifiname + "-----" + ip);
//            }
//
//            @Override
//            public void ondisConnet(int nettype) {
//                Log.e("ondisConnet--->", "" + nettype);
//            }
//
//            @Override
//            public void onNetAvailable(int status, int nettype) {
//                Log.e("onNetAvailable--->", "" + nettype + "-----" + status);
//            }
//
//            @Override
//            public void onNetDislable(int status) {
//                Log.e("onNetDislable--->", "-----" + status);
//            }
//        });
    }

    WifiAPManager wifiAPManager;

    private void getwifi() {
//        List<ScanResult> results = WiFiUtils.getSSIDs(this);
//        for (ScanResult s : results) {
//            Log.e("ssid---->", "" + s.SSID);
//        }
//
//        String custr = WiFiUtils.getCurrtWifiSSID(this);
//        Log.e("当前连接------->", "" + custr);


//        WifiAPUtil.getInstance(this).startAP();


        wifiAPManager = new WifiAPManager(this);
        wifiAPManager.startWifiAp("测试", "12d345687s6", false);

//        String sid1=wifiAPManager.getApSSID();
//        Log.e("sid1------->", "" + sid1);
//
//        String sid = APNetUtils.getValidApSsid(this);
//        Log.e("sid------->", "" + sid);
//        String pwd = APNetUtils.getValidPassword(this);
//        Log.e("pwd------->", "" + pwd);

    }


    @RequiresApi(api = 26)
    private void startWifiAp() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiManager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {
            @Override
            public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                super.onStarted(reservation);
                Log.e("测试---->",reservation.getWifiConfiguration().preSharedKey+"------"+reservation.getWifiConfiguration().SSID);
            }

            @Override
            public void onStopped() {
                super.onStopped();
            }

            @Override
            public void onFailed(int reason) {
                super.onFailed(reason);
            }
        }, new Handler());


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetWorkUtil.getInstance().unWifiBroadcastReceiver(MyApplication.instance);
    }
}
