package com.gnat;


import android.content.Context;
import android.os.Build;
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




    public String getDeviceInfo() {
        return Build.BRAND
                + " " + Build.MANUFACTURER
                + " " + Build.MODEL
                + " running SDK " + Build.VERSION.SDK_INT;
    }

    public void logConnection(ConnectionInfo connection){

        File file = new File(this.context.getExternalCacheDir(),
                System.currentTimeMillis()+"_"+connection.getSSID()+".json");

        Log.w("gnat", this.context.getExternalCacheDir()+"");

        try
        {
            FileOutputStream out = new FileOutputStream(file);
            writeJson(file.getName(), connection, out);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void writeJson(String fileName, ConnectionInfo connection, OutputStream out) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("    ");

        writer.beginObject();
        writer.name("file_name").value(fileName);
        writer.name("date_time").value(DateFormat.getDateTimeInstance().format(new Date()));
        writer.name("ssid").value(connection.getSSID());
        writer.name("bssid").value(connection.getBSSID());
        writer.name("signal_strength").value(connection.getSignalStrength());
        writer.name("device_info").value(getDeviceInfo());
        writer.name("device_mac").value(connection.getMacAddress());
        writer.endObject();
        writer.close();
    }

}