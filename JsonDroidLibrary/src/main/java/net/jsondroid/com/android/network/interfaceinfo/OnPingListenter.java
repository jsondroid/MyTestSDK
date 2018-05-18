package net.jsondroid.com.android.network.interfaceinfo;

/**
 * Created by wenbaohe on 2018/5/18.
 */

public interface OnPingListenter {

    public void onSuccess(int status,int nettype);

    public void onFail(int status, Throwable throwable);
}
