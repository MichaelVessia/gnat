package com.gnat;


import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NetworkListManager {

    List<String> ssids = new ArrayList<>();
    List<WifiConfiguration> configuredWifiList;
    ArrayAdapter<String> configuredWifiListAdapter;
    ListView configuredWifiListView;


    List<ScanResult> localWifiList;
    ArrayAdapter<String> localWifiListAdapter;
    ListView localWifiListView;
    Context mContext;

    public NetworkListManager(Context c){
        this.mContext = c;
    }


    /*
   * Listview and ssids list need to be cleared to prevent duplication
   */
    public void clearNetworkList() {

        configuredWifiListAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_list_item_1, new ArrayList<String>());

        localWifiListAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_list_item_1, new ArrayList<String>());

        ssids = new ArrayList<>();
    }

    public void updateNetworkList(WifiManager main) {

        clearNetworkList();

        configuredWifiListView.setAdapter(configuredWifiListAdapter);
        localWifiListView.setAdapter(localWifiListAdapter);

        List<String> configuredSSID = new ArrayList<>();

        for(WifiConfiguration conf : configuredWifiList) {
            configuredSSID.add(conf.SSID.substring(1, conf.SSID.length()-1));
        }

        WifiInfo currentConnection = main.getConnectionInfo();
        String currentSSID = main.getConnectionInfo().getSSID();
        if(currentSSID.length() >=2) {
            currentSSID = currentSSID.substring(1, currentSSID.length() - 1);
        }

        for(ScanResult result : localWifiList) {
            localWifiListAdapter.add(result.SSID + " " + result.BSSID);
            if(configuredSSID.contains(result.SSID)) {
                if(result.BSSID.equals(currentConnection.getBSSID())) {
                    ssids.add(result.SSID + " " + "(Connected)");
                } else {
                    ssids.add(result.SSID + " " + result.BSSID);
                }
            }
        }

        if(ssids.size() == 0) {
            configuredWifiListAdapter.add("No networks configured.");
        } else {
            for (String ssid : ssids) {
                configuredWifiListAdapter.add(ssid);
            }
        }


        configuredWifiListView.setAdapter(configuredWifiListAdapter);
        localWifiListView.setAdapter(localWifiListAdapter);

    }


}
