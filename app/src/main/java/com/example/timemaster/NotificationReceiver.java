package com.example.timemaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("MESSAGE");
        Log.d("NotificationReceiver", "Received message: " + message);
        NotificationHelper.createNotification(context, "Напоминание", message);
    }
}
