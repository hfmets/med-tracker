package com.example.medtracker.util;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.medtracker.models.Medication;
import com.example.medtracker.view_models.MedicationViewModel;

import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    public final int MORNING_NOTIFICATION_ID = 0;
    public final int AFTERNOON_NOTIFICATION_ID = 1;
    public final int EVENING_NOTIFICATION_ID = 2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        int timeCategory = intent.getIntExtra("timeCategory", -1);
        NotificationCompat.Builder nb;

        switch (timeCategory) {
            case 0: {
                nb = notificationHelper.getMorningAlert();
                notificationHelper.getManager().notify(MORNING_NOTIFICATION_ID, nb.build());
            } break;
            case 1: {
                nb = notificationHelper.getAfternoonAlert();
                notificationHelper.getManager().notify(AFTERNOON_NOTIFICATION_ID, nb.build());
            } break;
            case 2: {
                nb = notificationHelper.getEveningAlert();
                notificationHelper.getManager().notify(EVENING_NOTIFICATION_ID, nb.build());
            } break;
        }
    }
}
