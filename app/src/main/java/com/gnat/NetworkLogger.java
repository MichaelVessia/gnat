package com.gnat;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.util.JsonWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.Date;

public class NetworkLogger {


    private Context context;

    public NetworkLogger(Context context){
        this.context = context;
    }



    public void logConnection(ConnectionInfo connection){

        File file = new File(this.context.getExternalCacheDir(),
                System.currentTimeMillis()+"_"+connection.getSSID()+".json");

        Log.w("gnat", this.context.getExternalCacheDir()+"");

        try
        {
            FileOutputStream out = new FileOutputStream(file);
            writeJson(connection, out);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void writeJson(ConnectionInfo connection, OutputStream out) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("    ");

        writer.beginObject();
        writer.name("DateTime").value(DateFormat.getDateTimeInstance().format(new Date()));
        writer.name("SSID").value(connection.getSSID());
        writer.name("BSSID").value(connection.getBSSID());
        writer.endObject();
        writer.close();
    }

}