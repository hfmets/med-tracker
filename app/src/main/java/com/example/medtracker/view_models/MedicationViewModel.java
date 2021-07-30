package com.example.medtracker.view_models;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.medtracker.AppRepository;
import com.example.medtracker.models.Medication;

import java.util.List;

public class MedicationViewModel extends AndroidViewModel {

    private final AppRepository repo;
    private final LiveData<List<Medication>> allMedications;
    Observer<List<Medication>> observer = new Observer<List<Medication>>() {
        @Override
        public void onChanged(List<Medication> medications) {
            for (Medication medication : medications) {
                updateTakenStatus(false, medication.getMedicationId());
            }
        }
    };

    public MedicationViewModel(@NonNull Application application) {
        super(application);
        repo = new AppRepository(application);
        allMedications = repo.getAllMedications();
    }

    public LiveData<List<Medication>> getAllMedications() { return allMedications; }

    public void incrementMedCount(int medId) { repo.incrementMedAmount(medId); }

    public void decrementMedCount(int medId) { repo.decrementMedAmount(medId); }

    public void updateTakenStatus(boolean taken, int medId) { repo.updateTakenStatus(taken, medId); }

    public void setAllTakenStatusFalse() {
        Log.i("NOTICEMESENPAI", "setAllTakenStatusFalse: doing it");
        allMedications.observeForever(observer);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        allMedications.removeObserver(observer);
    }
}
