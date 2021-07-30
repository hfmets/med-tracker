package com.example.medtracker.util;

import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.example.medtracker.AppDatabase;
import com.example.medtracker.AppRepository;
import com.example.medtracker.AppSingleton;
import com.example.medtracker.dao.MedicationDao;
import com.example.medtracker.models.Medication;
import com.example.medtracker.models.Record;
import com.example.medtracker.view_models.MedicationViewModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.ContentValues.TAG;

public class ResetReciever extends BroadcastReceiver {


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        /*ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        List<Medication> medicationList = new ArrayList<>();*/
        AppRepository repo = new AppRepository((Application)context.getApplicationContext());
        try {
            List<Medication> medicationList = repo.getAllMedicationsNotLive();
            for (Medication medication : medicationList) {
                repo.updateTakenStatus(false, medication.getMedicationId());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //final PendingResult pendingResult = goAsync();
        Log.i("NOTICEMESENPAI", "onReceive: doing it");
        /*executor.execute(() -> {
            for (Medication medication : medicationList) {
                db.medicationDao().updateTakenStatus(false, medication.getMedicationId());
            }
            handler.post(pendingResult::finish);
        });*/
    }
}
