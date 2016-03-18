package com.gnat;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkLogger {

    public Context context;
    public NetworkInfo networkInfo;

    public NetworkLogger(Context context){
        this.context = context;
    }

    public void getNetworkInformation(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
    }

    public void logNetworkInfo(){
        Log.w("gnat", networkInfo.toString());
    }

}
