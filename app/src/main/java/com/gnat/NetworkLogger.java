package com.gnat;


import android.content.Context;
import android.os.Build;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import com.loopj.android.http.*;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class NetworkLogger {


    private final String SERVER_URL = "https://gnat-rails.herokuapp.com/logs";
    private Context context;

    public NetworkLogger(Context context) {
        this.context = context;
    }


    public String getDeviceInfo() {
        return Build.BRAND
                + " " + Build.MANUFACTURER
                + " " + Build.MODEL
                + " running SDK " + Build.VERSION.SDK_INT;
    }

    public void logConnection(ConnectionInfo connection) {

        File file = new File(this.context.getExternalCacheDir(),
                System.currentTimeMillis() + "_" + connection.getSSID() + ".json");

        Log.w("gnat", this.context.getExternalCacheDir() + "");

        try {
            JSONObject log = makeLog(file.getName(), connection);
            writeJson(file, log);
            uploadLog(log, SERVER_URL);
        } catch (IOException e) {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    public void uploadLog(JSONObject log, String server) {

        SyncHttpClient client = new SyncHttpClient();


        StringEntity se = null;
        try {
            se = new StringEntity(log.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        client.post(null, server, se, "application/json", new JsonHttpResponseHandler());
    }
}