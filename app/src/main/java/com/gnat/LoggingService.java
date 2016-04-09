package com.gnat;


import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class LoggingService extends IntentService {

    private static final String TAG = "LoggingService";
    private static final int LOG_INTERVAL = 1000 * 60; // 60 seconds
    private Context context;
    private ConnectionInfo connectionInfo = ConnectionInfo.getInstance(context);

    public LoggingService() {
        super(TAG);
    }

    public LoggingService(Context context) {
        super(TAG);
        // We get application context here because the service exists after the activity is gone
        this.context = context.getApplicationContext();
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
                    SystemClock.elapsedRealtime(), LOG_INTERVAL, pi);
        }
        else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }



    @Override
    protected void onHandleIntent(Intent intent){
        Log.i(TAG, "Recieved an intent: " + intent);


    }
}
