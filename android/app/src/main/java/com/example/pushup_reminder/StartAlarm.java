package com.example.pushup_reminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class StartAlarm {

    public static final String SHARED_PREFS = "sharedprefs";
    public static final String ALARM_COUNT = "alarmCount";
    public static final String TIME_DELAY = "timeDelay";
    private static int alarmCountShared;
    int alarmCounter;
    int timeDelayLocal;
    Context context ;

    public  StartAlarm(Context context){
       this.context = context;
   }

    public void saveData(int alarmCounter, int timeDelayLocal) {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ALARM_COUNT, alarmCounter);
        editor.putInt(TIME_DELAY, timeDelayLocal);
        editor.apply();
    }

    public void startAlarm(int timeDelay) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        alarmCountShared = sharedPreferences.getInt(ALARM_COUNT, 0);
        Log.d(TAG, "first tag: alarm counter  = " + alarmCounter + " alarm counter shared = " + alarmCountShared);


        timeDelayLocal = timeDelay;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this.context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long time = System.currentTimeMillis();
        long timePeriod = 1000 * 60 * timeDelayLocal;
        long timePeriod2 = 1000 * 10;
                //timePeriod;




        if (alarmCountShared >= 3) {
            alarmCounter = 0;
            timeDelayLocal = 0;
            saveData(alarmCounter, timeDelayLocal);


            SharedPreferences flutterSharedPrefs = this.context.getSharedPreferences("FlutterSharedPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = flutterSharedPrefs.edit();
            editor.putBoolean("flutter."+"timerStatus", false);
            editor.apply();
            Log.d(TAG, "timer status has been set to false");


            alarmCountShared = sharedPreferences.getInt(ALARM_COUNT, 0);

            alarmManager.cancel(pendingIntent);
            alarmCountShared = sharedPreferences.getInt(ALARM_COUNT, 0);
            Log.d(TAG, "if statement: alarm has been canceled because count is more than 3 and alarm count shared =" + alarmCountShared);



        } else {
            alarmCounter = alarmCountShared;
            alarmCounter += 1;
            saveData(alarmCounter, timeDelayLocal);

            alarmCountShared = sharedPreferences.getInt(ALARM_COUNT, 0);
            Log.d(TAG, "else statement: alarm counter  = " + alarmCounter + " alarm counter shared = " + alarmCountShared + "time period = "+ timePeriod);

            alarmManager.set(AlarmManager.RTC_WAKEUP, time + timePeriod2, pendingIntent);

            alarmCountShared = sharedPreferences.getInt(ALARM_COUNT, 0);
            Log.d(TAG, "after setting alarm: alarm counter  = " + alarmCounter + " alarm counter shared = " + alarmCountShared);

            Toast.makeText(this.context, "Reminder is set, Reminder number is " + alarmCounter, Toast.LENGTH_LONG).show();
        }
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel1", "pushup", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            Log.d(TAG, "createNotificationChannel: this is the channel in main activity");
        }
    }
}
