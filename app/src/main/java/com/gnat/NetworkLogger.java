package com.gnat;


import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

public class NetworkLogger {


    private final String SERVER_URL = "https://gnat-rails.herokuapp.com";
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
            JSONObject log = makeLog(file.getName(), connection);
            writeJson(file, log);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public JSONObject makeLog(String fileName, ConnectionInfo connection) {

        JSONObject log = new JSONObject();

        try {
            log.put("file_name", fileName);
            log.put("date_time", DateFormat.getDateTimeInstance().format(new Date()));
            log.put("ssid", connection.getSSID());
            log.put("bssid", connection.getBSSID());
            log.put("signal_strength", connection.getSignalStrength());
            log.put("device_info", getDeviceInfo());
            log.put("device_mac", connection.getMacAddress());
        } catch (JSONException e) { e.printStackTrace(); }

        return log;
    }

    public void writeJson(File file, JSONObject log) throws IOException {

        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(log.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}