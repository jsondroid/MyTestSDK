package jsondroid.android.com.test02;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements APWiFiManager.OnHotspotCallback {
    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        APWiFiManager.getInstance().init(this);
        APWiFiManager.getInstance().setOnHotspotCallback(this);
    }

    public void clickClose(View view) {
        APWiFiManager.getInstance().closeWifiAp();
    }

    public void clickBtn(View view) {
        APWiFiManager.getInstance().startWIFIAP();
    }


    @Override
    public void onStarted(WifiConfiguration configuration) {
        Log.e("开启成功--", ""+configuration.SSID+"-----"+configuration.preSharedKey);
    }

    @Override
    public void onStopped() {
        Log.e("关闭--", "关闭");
    }

    @Override
    public void onFailed() {
        Log.e("开启失败", "开启失败");
    }

    @Override
    protected void onDestroy() {
        APWiFiManager.getInstance().release();
        super.onDestroy();
    }
}
