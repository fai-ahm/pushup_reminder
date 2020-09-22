package com.example.pushup_reminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;


import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity { 
    public static final String EXTRA_ALARM_COUNT = "com.example.pushup_reminder";
    public static final String SHARED_PREFS = "sharedprefs";
    public static final String ALARM_COUNT = "alarmCount";
    public static final String TIME_DELAY = "timeDelay";
    private static final String TAG = "-------->";

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);

        StartAlarm startAlarmObj = new StartAlarm(this);

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), "com.pushup_reminder.messages")
                .setMethodCallHandler(
                        (call, result) -> {
                            // Note: this method is invoked on the main thread.
                            String timePeriod = call.arguments.toString();
                            if (call.method.equals("startService")) {
                                startAlarmObj.createNotificationChannel();


                                int timeDelay = Integer.parseInt(timePeriod);

                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt(TIME_DELAY, timeDelay);
                                editor.apply();

                                // startAlarm();
                                startAlarmObj.startAlarm(timeDelay);

                                Log.d(TAG, "method argument = " + timeDelay);
                                result.success("alarm is set");
                            } else {
                                Log.d(TAG, "configureFlutterEngine: call is different");
                            }
                        }
                );
    }
}