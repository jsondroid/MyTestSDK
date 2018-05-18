package net.jsondroid.com.mytestsdk;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.jsondroid.com.android.network.NetWorkUtil;
import net.jsondroid.com.android.network.interfaceinfo.OnWifiListenter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void clickBtn(View view) {

        NetWorkUtil.getInstance().setOnWifiListenter(new OnWifiListenter() {
            @Override
            public void onConnet(int nettype, String wifiname, String ip) {

                Log.e("onConnet--->", "" + nettype + "-----" + wifiname + "-----" + ip);
            }

            @Override
            public void ondisConnet(int nettype) {
                Log.e("ondisConnet--->", "" + nettype);
            }

            @Override
            public void onNetAvailable(int status, int nettype) {
                Log.e("onNetAvailable--->", "" + nettype + "-----" + status);
            }

            @Override
            public void onNetDislable(int status) {
                Log.e("onNetDislable--->", "-----" + status);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetWorkUtil.getInstance().unWifiBroadcastReceiver(MyApplication.instance);
    }
}
