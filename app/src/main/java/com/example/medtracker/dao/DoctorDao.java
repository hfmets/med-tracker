package com.example.medtracker.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.medtracker.models.Doctor;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface DoctorDao {
    @Query("SELECT * FROM doctors")
    LiveData<List<Doctor>> getAll();

    @Query("SELECT * FROM doctors WHERE doctorId = :doctorId")
    List<Doctor> getById(int doctorId);

    @Insert
    public ListenableFuture<Long> insertDoctor(Doctor doctor);

    @Query("DELETE FROM doctors")
    public void deleteAllDoctors();

    @Query("SELECT * FROM doctors WHERE doctorId = :doctorId")
    public ListenableFuture<Doctor> getDoctorById(int doctorId);

    @Update
    public ListenableFuture<Integer> updateDoctor(Doctor doctor);

    @Delete
    public ListenableFuture<Integer> deleteDoctor(Doctor doctor);
}
