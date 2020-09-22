package com.example.pushup_reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class AlertReceiver extends BroadcastReceiver {
    public static final String SHARED_PREFS = "sharedprefs";
    public static final String ALARM_COUNT = "alarmCount";
    public static final String TIME_DELAY = "timeDelay";
    int alarmCountShared;
//    Calendar calendar = Calendar.getInstance();
//    int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
    MainActivity mainAct = null;
    private MyCallback callback;

//    public AlertReceiver(MyCallback callback) {
//        this.callback = callback;
//    }

//void setMainActivityHandler(MainActivity main){
//    mainAct = main;
//}

    @Override
    public void onReceive(Context context, Intent intent) {
        StartAlarm startAlarmObj = new StartAlarm(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        alarmCountShared = sharedPreferences.getInt(ALARM_COUNT, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel1")
                .setContentText("Please do your Push Ups, Reminder No. " + alarmCountShared).setContentTitle("Push Up Reminder")
                .setSmallIcon(R.drawable.ic_baseline_favorite_border_24);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());

        int timeDelay = sharedPreferences.getInt(TIME_DELAY, 0 );


        startAlarmObj.startAlarm(timeDelay);


    }
}
