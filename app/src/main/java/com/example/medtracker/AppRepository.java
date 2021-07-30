package com.example.medtracker;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.medtracker.dao.DoctorDao;
import com.example.medtracker.dao.MedicationDao;
import com.example.medtracker.dao.RecordsDao;
import com.example.medtracker.dao.UserDao;
import com.example.medtracker.models.Doctor;
import com.example.medtracker.models.Medication;
import com.example.medtracker.models.MedicationWithDoctor;
import com.example.medtracker.models.Record;
import com.example.medtracker.models.User;
import com.example.medtracker.models.UserWithDoctors;
import com.google.common.util.concurrent.ListenableFuture;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AppRepository {
    private UserDao userDao;
    private DoctorDao doctorDao;
    private MedicationDao medicationDao;
    private RecordsDao recordsDao;
    private LiveData<List<Record>> allRecords;
    private LiveData<List<User>> allUsers;
    private LiveData<List<Doctor>> allDoctors;
    private LiveData<List<Medication>> allMedications;

    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application.getApplicationContext());
        userDao = db.userDao();
        doctorDao = db.doctorDao();
        medicationDao = db.medicationDao();
        recordsDao = db.recordsDao();
        allUsers = userDao.getAll();
        allDoctors = doctorDao.getAll();
        allMedications = medicationDao.getAll();
        allRecords = recordsDao.getAll();
    }

    public ListenableFuture<Integer> updateAlertTimesForUser(LocalTime morning, LocalTime afternoon, LocalTime evening, int userId) { return userDao.updateAlertTimesForUser(morning, afternoon, evening, userId); }

    public ListenableFuture<Integer> updateSwitchStatus(boolean morning, boolean afternoon, boolean evening, int userId) { return userDao.updateSwitchStatusForUser(morning, afternoon, evening, userId); }

    public LiveData<List<User>> getAllUsers() { return allUsers; }

    public List<Medication> getAllMedicationsNotLive() throws ExecutionException, InterruptedException {
        Future<List<Medication>> result = AppDatabase.database_executor.submit(new Callable<List<Medication>>() {
            @Override
            public List<Medication> call() throws Exception {
                return medicationDao.getAllNotLive();
            }
        });
        return result.get();
    }

    public Record getRecordByDate(String date, int userId) throws ExecutionException, InterruptedException {
        Future<Record> result = AppDatabase.database_executor.submit(new Callable<Record>() {
            @Override
            public Record call() throws Exception {
                return recordsDao.getRecordByDate(date, userId);
            }
        });
        return result.get();
    }

    public ListenableFuture<Integer> deleteMed(Medication medication) { return medicationDao.deleteMed(medication);}

    public ListenableFuture<Integer> deleteDoctor(Doctor doctor) { return doctorDao.deleteDoctor(doctor);}

    public List<Record> getAllNotLive() throws ExecutionException, InterruptedException {
        Future<List<Record>> result = AppDatabase.database_executor.submit(new Callable<List<Record>>() {
            @Override
            public List<Record> call() throws Exception {
                return recordsDao.getAllNotLive();
            }
        });
        return result.get();
    }

    public List<Record> getAllNotLiveFromUser(int userId) throws ExecutionException, InterruptedException {
        Future<List<Record>> result = AppDatabase.database_executor.submit(new Callable<List<Record>>() {
            @Override
            public List<Record> call() throws Exception {
                return recordsDao.getAllNotLiveFromUser(userId);
            }
        });
        return result.get();
    }

    public ListenableFuture<Long> addRecord(Record record) { return recordsDao.insertRecord(record); }

    public ListenableFuture<Integer> updateRecord(Record record) { return recordsDao.updateRecord(record); }

    public LiveData<List<Doctor>> getAllDoctors() { return doctorDao.getAll(); }

    public LiveData<List<Medication>> getAllMedications() { return allMedications; }

    public ListenableFuture<Long> insertUser(User user) {
        return userDao.insertUser(user);
    }

    public ListenableFuture<Long> insertMedication(Medication medication) { return medicationDao.insertMedication(medication); }

    public ListenableFuture<Integer> updateTakenStatus(boolean taken, int medId) { return medicationDao.updateTakenStatus(taken, medId); }

    public ListenableFuture<Long> insertDoctor(Doctor doctor) { return doctorDao.insertDoctor(doctor); }

    public ListenableFuture<Integer> incrementMedAmount(int medId) {return medicationDao.incrementMedAmount(medId); }

    public ListenableFuture<Integer> decrementMedAmount(int medId) { return medicationDao.decrementMedAmount(medId); }

    public ListenableFuture<User> getUserById(int userId) { return userDao.getUserById(userId); }

    public ListenableFuture<Doctor> getDoctorById(int doctorId) { return doctorDao.getDoctorById(doctorId); }

    public LiveData<List<Medication>> getMedicationsForUser(int userId) { return medicationDao.getMedicationsForUser(userId); }

    public LiveData<List<Medication>> getMedicationsForUserForTimeOfDay(int userId, Medication.TakeTimeCategory takeTimeCategory) { return medicationDao.getMedicationsForUserForTimeOfDay(userId, takeTimeCategory); }

    public LiveData<UserWithDoctors> getUserWithDoctors(int userId) { return userDao.getUserWithDoctors(userId); }

    public LiveData<List<MedicationWithDoctor>> getMedicationsWithDoctors() { return medicationDao.getAllWithDoctors(); }

    public LiveData<List<MedicationWithDoctor>> getMedicationsWithDoctorsForUser(int userId) { return medicationDao.getAllWithDoctorsForUser(userId); }

    public LiveData<List<MedicationWithDoctor>> getMedicationsWithDoctorsForUserForTimeOfDay(int userId, Medication.TakeTimeCategory takeTimeCategory) { return medicationDao.getAllWithDoctorsForUserForTimeOfDay(userId, takeTimeCategory); }

    public ListenableFuture<Integer> updateDoctor(Doctor doctor) { return doctorDao.updateDoctor(doctor); }

    public ListenableFuture<Medication> getMedicationById(int medicationId) { return medicationDao.getMedicationById(medicationId); }

    public ListenableFuture<Integer> updateMedication(Medication medication) { return medicationDao.updateMedication(medication); }
}
