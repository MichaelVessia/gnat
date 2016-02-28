package com.gnat;


import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NetworkListManager {

    List<String> ssids = new ArrayList<>();
    ListView wifiListView;
    List<WifiConfiguration> configuredWifiList;
    List<ScanResult> localWifiList;
    ArrayAdapter<String> listAdapter;
    Context mContext;

    public NetworkListManager(Context c){
        this.mContext = c;
    }


    /*
   * Listview and ssids list need to be cleared to prevent duplication
   */
    public void clearNetworkList() {

        listAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_list_item_1, new ArrayList<String>());

        ssids = new ArrayList<>();
    }

    public void updateNetworkList(WifiManager main) {

        clearNetworkList();

        wifiListView.setAdapter(listAdapter);

        List<String> configuredSSID = new ArrayList<>();

        for(WifiConfiguration conf : configuredWifiList) {
            configuredSSID.add(conf.SSID.substring(1, conf.SSID.length()-1));
        }

        String currentConnection = main.getConnectionInfo().getSSID();
        if(currentConnection.length() >=2) {
            currentConnection = currentConnection.substring(1, currentConnection.length() - 1);
        }

        for(ScanResult result : localWifiList) {
            if(configuredSSID.contains(result.SSID)) {
                if(result.SSID.equals(currentConnection)) {
                    ssids.add(result.SSID + " " + "(Connected)");
                } else {
                    ssids.add(result.SSID + " " + result.BSSID);
                }
            }
        }

        for (String ssid : ssids) {
            listAdapter.add(ssid);
        }


        wifiListView.setAdapter(listAdapter);
    }


}
