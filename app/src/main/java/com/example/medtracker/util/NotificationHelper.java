package com.example.medtracker.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.medtracker.R;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager notificationManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationHelper(Context base) {
        super(base);
        createChannel();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public NotificationCompat.Builder getMorningAlert() {
        return new NotificationCompat.Builder(getApplicationContext(), channelID).setContentTitle("Medication Time").setContentText("It's time to take your morning medications.").setSmallIcon(R.drawable.heart_purple);
    }

    public NotificationCompat.Builder getAfternoonAlert() {
        return new NotificationCompat.Builder(getApplicationContext(), channelID).setContentTitle("Medication Time").setContentText("It's time to take your afternoon medications.").setSmallIcon(R.drawable.heart_purple);
    }

    public NotificationCompat.Builder getEveningAlert() {
        return new NotificationCompat.Builder(getApplicationContext(), channelID).setContentTitle("Medication Time").setContentText("It's time to take your evening medications.").setSmallIcon(R.drawable.heart_purple);
    }
}
