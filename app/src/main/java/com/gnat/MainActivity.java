package com.gnat;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    WifiManager mainWifi;

    List<String> ssids = new ArrayList<>();
    ListView wifiListView;
    List<WifiConfiguration> wifiList;
    ArrayAdapter<String> listAdapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        wifiListView = (ListView) findViewById(R.id.wifiList);
        listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if(!mainWifi.isWifiEnabled()) {
            mainWifi.setWifiEnabled(true);
        }

        new AsyncConnection().execute();

    }

    public void refresh(){
        new AsyncConnection().execute();
        updateNetworkList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                // User chose the "Refesh" item, refresh the network list...
                refresh();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    /*
     * Listview and ssids list need to be cleared to prevent duplication
     */
    public void clearNetworkList() {
        listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, new ArrayList<String>());
        ssids = new ArrayList<>();
    }

    public void updateNetworkList() {

        clearNetworkList();

        wifiListView.setAdapter(listAdapter);


        for (WifiConfiguration conf : wifiList) {
            ssids.add(conf.SSID.substring(1,conf.SSID.length()-1));
        }

        for (String ssid : ssids) {
            listAdapter.add(ssid);
        }

        wifiListView.setAdapter(listAdapter);
    }


    private class AsyncConnection extends AsyncTask<Void, Void, Void> {

        public AsyncConnection() {
            super();
            mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            wifiList = new ArrayList<>();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            wifiList = mainWifi.getConfiguredNetworks();
            return null;
        }

        @Override
        protected void onPostExecute(Void results){
            updateNetworkList();
        }
    }

}
