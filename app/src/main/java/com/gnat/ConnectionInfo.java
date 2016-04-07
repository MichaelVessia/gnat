package com.gnat;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class ConnectionInfo {

    private Context context;
    private WifiInfo wifiInfo;
    private NetworkInfo networkInfo;
    private WifiManager mainWifi;

    private static ConnectionInfo instance = null;
    private ConnectionInfo(Context context) {
        this.context = context;
    }

    public static ConnectionInfo getInstance(Context context){
        if(instance == null){
            instance = new ConnectionInfo(context);
        }
        return instance;
    }

    public void retrieveAllInfo(){
        this.mainWifi = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
        this.wifiInfo = mainWifi.getConnectionInfo();
        ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.networkInfo = connectivityManager.getActiveNetworkInfo();
    }

    public String getSSID(){
        String ssid = this.wifiInfo.getSSID();
        return ssid.replace("\"", "");
    }

    public String getBSSID(){
        String bssid = this.wifiInfo.getBSSID();
        return bssid;
    }

    public String getSignalStrength(){
        int signal = this.wifiInfo.getRssi();
        signal = this.mainWifi.calculateSignalLevel(signal, 101);

        return signal + "%";
    }

    public String getMacAddress(){
        return this.wifiInfo.getMacAddress();
    }


}
