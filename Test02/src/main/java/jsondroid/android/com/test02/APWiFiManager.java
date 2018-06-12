package jsondroid.android.com.test02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by wenbaohe on 2018/6/12.
 */

public class APWiFiManager {

    /**
     * 热点开启状态
     */
    public static final int WIFI_AP_STATE_DISABLING = 10;
    public static final int WIFI_AP_STATE_DISABLED = 11; //关闭
    public static final int WIFI_AP_STATE_ENABLED = 13; //开启成功
    public static final int WIFI_AP_STATE_FAILED = 14;//开启失败

    private static APWiFiManager instance;

    public static APWiFiManager getInstance() {
        if (instance == null) {
            synchronized (APWiFiManager.class) {
                if (instance == null) {
                    instance = new APWiFiManager();
                }
            }
        }
        return instance;
    }

    private WeakReference<Context> contextw;
    private WifiManager wifiManager;

    public void init(Context context) {
        if (contextw == null) {
            contextw = new WeakReference<Context>(context);
            wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            /**api小于26时使用广播的形式监听*/
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

                IntentFilter filter = new IntentFilter();
                filter.addAction(WIFI_AP_STATE_CHANGED_ACTION);
                context.registerReceiver(mWifiStateBroadcastReceiver, filter);

                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case WIFI_AP_STATE_ENABLED:
                                if (onHotspotCallback != null) {
                                    onHotspotCallback.onStarted(getWifiApConfiguration());
                                }
                                break;
                            case WIFI_AP_STATE_DISABLED:
                                if (onHotspotCallback != null) {
                                    onHotspotCallback.onStopped();
                                }
                                break;
                            case WIFI_AP_STATE_FAILED:
                                if (onHotspotCallback != null) {
                                    onHotspotCallback.onFailed();
                                }
                                break;
                        }
                    }
                };
            }

        }
    }

    public void release() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            if (contextw.get() != null) {
                contextw.get().unregisterReceiver(mWifiStateBroadcastReceiver);
            }
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    public WifiConfiguration getWifiApConfiguration() {
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
            method.setAccessible(true);
            WifiConfiguration configuration = (WifiConfiguration) method.invoke(wifiManager);
            return configuration;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取热点ssid(8.0以下使用)
     */
    public String getValidApSsid() {
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
            method.setAccessible(true);
            WifiConfiguration configuration = (WifiConfiguration) method.invoke(wifiManager);
            return configuration.SSID;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取热点密码(8.0以下使用)
     */
    public String getValidPassword() {
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration configuration = (WifiConfiguration) method.invoke(wifiManager);
            return configuration.preSharedKey;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭WiFi热点
     */
    public void closeNativeAP() {
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
            method.setAccessible(true);
            WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);
            Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method2.invoke(wifiManager, config, false);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取热点状态
     */
    public int getWifiAPState() {
        int state = -1;
        try {
            Method method2 = wifiManager.getClass().getMethod("getWifiApState");
            state = (Integer) method2.invoke(wifiManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }


    /**
     * 创建配置信息（8.0以下可用）
     */
    public WifiConfiguration getAPConfiguration(String mSSID, String mPasswd, boolean isPublic) {

        WifiConfiguration netConfig = new WifiConfiguration();
        netConfig.SSID = mSSID;
        netConfig.preSharedKey = mPasswd;
        netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        if (isPublic) {
            netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        } else {
            netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        }
        netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

        return netConfig;
    }

    /**
     * 8.0关闭Ap方法
     */
    public synchronized void  close26WifiAp() {
        if (getWifiAPState() == WIFI_AP_STATE_ENABLED) {
            try {
                Method method = wifiManager.getClass().getDeclaredMethod("cancelLocalOnlyHotspotRequest");
                method.setAccessible(true);
                method.invoke(wifiManager);
                if (onHotspotCallback != null) {
                    onHotspotCallback.onStopped();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }

    @RequiresApi(api = 26)
    private void startWifiAp() {
        wifiManager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {
            @Override
            public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                super.onStarted(reservation);
                if (onHotspotCallback != null) {
                    onHotspotCallback.onStarted(reservation.getWifiConfiguration());
                }
                Log.e("TAG--->", "---" + reservation.getWifiConfiguration().SSID + "-----" + reservation.getWifiConfiguration().preSharedKey);
            }

            @Override
            public void onStopped() {
                super.onStopped();
                Log.e("TAG--->", "已关闭");
                if (onHotspotCallback != null) {
                    onHotspotCallback.onStopped();
                }
            }

            @Override
            public void onFailed(int reason) {
                super.onFailed(reason);
                Log.e("TAG--->", "开启失败");
                if (onHotspotCallback != null) {
                    onHotspotCallback.onFailed();
                }
            }
        }, new Handler());
    }

    private boolean startNativeAp() {
        boolean b = false;
        try {
            Method method1 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            b = (boolean) method1.invoke(wifiManager, null, true);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return b;
    }

    public void startWIFIAP() {
        if (getWifiAPState() != WIFI_AP_STATE_ENABLED) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
                startWifiAp();
            } else {
                startNativeAp();
            }
        }
    }

    public void closeWifiAp() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            close26WifiAp();
        } else {
            closeNativeAP();
        }
    }

    public void setOnHotspotCallback(OnHotspotCallback onHotspotCallback) {
        this.onHotspotCallback = onHotspotCallback;
    }

    private OnHotspotCallback onHotspotCallback;

    public interface OnHotspotCallback {
        public void onStarted(WifiConfiguration configuration);

        public void onStopped();

        public void onFailed();
    }


    public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    public static final String EXTRA_WIFI_AP_STATE = "wifi_state";
    private Handler handler;
    //监听wifi热点状态变化
    private BroadcastReceiver mWifiStateBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WIFI_AP_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int cstate = intent.getIntExtra(EXTRA_WIFI_AP_STATE, -1);
                if (cstate == WIFI_AP_STATE_ENABLED) {
                    if (handler != null) {
                        handler.sendEmptyMessage(WIFI_AP_STATE_ENABLED);
                    }
                }
                if (cstate == WIFI_AP_STATE_DISABLED) {
                    if (handler != null) {
                        handler.sendEmptyMessage(WIFI_AP_STATE_DISABLED);
                    }
                }
                if (cstate == WIFI_AP_STATE_FAILED) {
                    if (handler != null) {
                        handler.sendEmptyMessage(WIFI_AP_STATE_FAILED);
                    }

                }
            }
        }
    };
}
