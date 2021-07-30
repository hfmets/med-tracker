package com.example.medtracker.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.medtracker.models.Medication;
import com.example.medtracker.models.MedicationWithDoctor;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface MedicationDao {
    @Query("SELECT * FROM medications")
    public LiveData<List<Medication>> getAll();

    @Query("SELECT * FROM medications")
    public List<Medication> getAllNotLive();

    @Insert
    public void insert(Medication medication);

    @Query("SELECT * FROM medications")
    public List<Medication> getAllMedications();

    @Query("SELECT * FROM medications WHERE userId = :userId")
    public LiveData<List<Medication>> getMedicationsForUser(int userId);

    @Query("SELECT * FROM medications WHERE userId = :userId AND takeTimeCategory = :takeTimeCategory")
    public LiveData<List<Medication>> getMedicationsForUserForTimeOfDay(int userId, Medication.TakeTimeCategory takeTimeCategory);

    @Query("DELETE FROM medications")
    public void deleteAllMedications();

    @Transaction
    @Query("SELECT * FROM medications")
    public LiveData<List<MedicationWithDoctor>> getAllWithDoctors();

    @Transaction
    @Query("SELECT * FROM medications WHERE userId = :userId")
    public LiveData<List<MedicationWithDoctor>> getAllWithDoctorsForUser(int userId);

    @Query("UPDATE medications SET amount = amount + 1 WHERE medicationId = :medId")
    public ListenableFuture<Integer> incrementMedAmount(int medId);

    @Query("UPDATE medications SET amount = amount - 1 WHERE medicationId = :medId")
    public ListenableFuture<Integer> decrementMedAmount(int medId);

    @Transaction
    @Query("SELECT * FROM medications WHERE userId = :userId AND takeTimeCategory = :takeTimeCategory")
    public LiveData<List<MedicationWithDoctor>> getAllWithDoctorsForUserForTimeOfDay(int userId, Medication.TakeTimeCategory takeTimeCategory);

    @Query("SELECT * FROM medications WHERE medicationId = :medicationId")
    public ListenableFuture<Medication> getMedicationById(int medicationId);

    @Query("Update medications SET taken = :taken WHERE medicationId = :medId")
    public ListenableFuture<Integer> updateTakenStatus(boolean taken, int medId);

    @Delete
    public ListenableFuture<Integer> deleteMed(Medication medication);

    @Insert
    public ListenableFuture<Long> insertMedication(Medication medication);

    @Update
    ListenableFuture<Integer> updateMedication(Medication medication);
}
