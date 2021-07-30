package com.example.medtracker.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.medtracker.AppRepository;
import com.example.medtracker.models.Medication;

import java.util.List;

public class UserWithMedicationsViewModel extends AndroidViewModel {

    private AppRepository repo;
    private final LiveData<List<Medication>> allMedications;
    private LiveData<List<Medication>> medicationsFromUser;
    private LiveData<List<Medication>> medicationsFromUserMorning;
    private LiveData<List<Medication>> medicationsFromUserAfternoon;
    private LiveData<List<Medication>> medicationsFromUserEvening;

    public UserWithMedicationsViewModel(@NonNull Application application) {
        super(application);
        repo = new AppRepository(getApplication());
        allMedications = repo.getAllMedications();
    }

    public LiveData<List<Medication>> getAllMedications() { return allMedications; }

    public LiveData<List<Medication>> getMedicationsForUser(int userId) { return repo.getMedicationsForUser(userId); }

    public LiveData<List<Medication>> getMedicationsMorning(int userId) { return repo.getMedicationsForUserForTimeOfDay(userId, Medication.TakeTimeCategory.MORNING); }

    public LiveData<List<Medication>> getMedicationsAfternoon(int userId) { return repo.getMedicationsForUserForTimeOfDay(userId, Medication.TakeTimeCategory.AFTERNOON); }

    public LiveData<List<Medication>> getMedicationsEvening(int userId) {return repo.getMedicationsForUserForTimeOfDay(userId, Medication.TakeTimeCategory.EVENING); }
}
