package com.example.medtracker.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.medtracker.AppRepository;
import com.example.medtracker.models.Medication;
import com.example.medtracker.models.MedicationWithDoctor;

import java.util.List;

public class MedicationsWithDoctorsViewModel extends AndroidViewModel {

    private AppRepository repo;
    private final LiveData<List<MedicationWithDoctor>> allMedications;
    private LiveData<List<MedicationWithDoctor>> allMedicationsForUser;
    private LiveData<List<MedicationWithDoctor>> allMedicationsForUserMorning;
    private LiveData<List<MedicationWithDoctor>> allMedicationsForUserAfternoon;
    private LiveData<List<MedicationWithDoctor>> allMedicationsForUserEvening;

    public MedicationsWithDoctorsViewModel(@NonNull Application application) {
        super(application);
        repo = new AppRepository(application);
        allMedications = repo.getMedicationsWithDoctors();
    }

    public LiveData<List<MedicationWithDoctor>> getAllMedicationsForUser(int userId) { return repo.getMedicationsWithDoctorsForUser(userId); }

    public LiveData<List<MedicationWithDoctor>> getAllMedicationsForUserMorning(int userId) { return repo.getMedicationsWithDoctorsForUserForTimeOfDay(userId, Medication.TakeTimeCategory.MORNING); }

    public LiveData<List<MedicationWithDoctor>> getAllMedicationsForUserAfternoon(int userId) { return repo.getMedicationsWithDoctorsForUserForTimeOfDay(userId, Medication.TakeTimeCategory.AFTERNOON); }

    public LiveData<List<MedicationWithDoctor>> getAllMedicationsForUserEvening(int userId) { return repo.getMedicationsWithDoctorsForUserForTimeOfDay(userId, Medication.TakeTimeCategory.EVENING); }
}
