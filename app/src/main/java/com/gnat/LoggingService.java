package com.gnat;


import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

public class LoggingService extends IntentService {

    private static final String TAG = "LoggingService";
    private static int logInterval = 1000 * 60; // 60 seconds
    private ConnectionInfo connectionInfo;
    private NetworkLogger networkLogger;
    SharedPreferences preferences;


    public LoggingService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.networkLogger = new NetworkLogger(this);
        this.connectionInfo = new ConnectionInfo(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int minutes = Integer.parseInt(preferences.getString("logging_freq", "1"));
        this.logInterval *= minutes;
    }

    public static Intent newIntent(Context context){
        return new Intent(context, LoggingService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn){
        Intent i = LoggingService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if(isOn){
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), logInterval, pi);
        }
        else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = LoggingService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(
                context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }


    @Override
    protected void onHandleIntent(Intent intent){
        Log.i(TAG, "Recieved an intent: " + intent);
        connectionInfo.retrieveAllInfo();
        networkLogger.logConnection(connectionInfo);
    }
}
