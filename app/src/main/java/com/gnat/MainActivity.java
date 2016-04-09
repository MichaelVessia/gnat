package com.gnat;


import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity {

    WifiManager mWifiManager;
    NetworkListManager mNetworkListManager = new NetworkListManager(this);
    NetworkLogger mNetworkLogger = new NetworkLogger(this);
    ConnectionInfo connectionInfo = ConnectionInfo.getInstance(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mNetworkListManager.configuredWifiListView = (ListView) findViewById(R.id.configuredWifiList);
        mNetworkListManager.configuredWifiListAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);

        mNetworkListManager.localWifiListView= (ListView) findViewById(R.id.localWifiList);
        mNetworkListManager.configuredWifiListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);


        LoggingService.setServiceAlarm(this, true);

        new AsyncConnection().execute();

    }

    public void refresh(){
        new AsyncConnection().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                // User chose the "Refresh" item, refresh the network list...
                refresh();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, MyPreferenceActivity.class));
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
    

    private class AsyncConnection extends AsyncTask<Void, Void, Void> {

        public AsyncConnection() {
            super();
            mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);


            // TODO: Add condition here to check if force wifi on is enabled in settings
            if(!mWifiManager.isWifiEnabled()) {
                mWifiManager.setWifiEnabled(true);
            }

        }
        @Override
        protected Void doInBackground(Void... voids) {
            mNetworkListManager.setConfiguredWifiList(mWifiManager.getConfiguredNetworks());
            mNetworkListManager.setLocalWifiList(mWifiManager.getScanResults());

            connectionInfo.retrieveAllInfo();
            return null;
        }

        @Override
        protected void onPostExecute(Void results){
            mNetworkListManager.updateNetworkList(mWifiManager);
            mNetworkLogger.logConnection(connectionInfo);
        }
    }

}
