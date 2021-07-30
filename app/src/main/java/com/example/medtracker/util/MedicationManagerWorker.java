package com.example.medtracker.util;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.medtracker.models.Medication;
import com.example.medtracker.view_models.MedicationViewModel;

import java.util.List;

public class MedicationManagerWorker extends Worker {
    private MedicationViewModel medicationViewModel;


    public MedicationManagerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        medicationViewModel = new MedicationViewModel((Application)context.getApplicationContext());
    }

    @NonNull
    @Override
    public Result doWork() {
        medicationViewModel.setAllTakenStatusFalse();

        Log.i("SUCCESS", "doWork: Success");
        return Result.success();
    }
}
