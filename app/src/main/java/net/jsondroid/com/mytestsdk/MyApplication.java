package net.jsondroid.com.mytestsdk;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import net.jsondroid.com.android.network.NetWorkUtil;
import net.jsondroid.com.android.network.PingUtils;

/**
 * Created by wenbaohe on 2018/5/18.
 */

public class MyApplication extends Application {
    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        NetWorkUtil.getInstance().registerWifiBroadcastReceiver(this);
        instance = this;
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

}
