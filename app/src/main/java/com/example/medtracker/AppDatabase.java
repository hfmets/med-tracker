package com.example.medtracker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.medtracker.dao.DoctorDao;
import com.example.medtracker.dao.MedicationDao;
import com.example.medtracker.dao.RecordsDao;
import com.example.medtracker.dao.UserDao;
import com.example.medtracker.models.Doctor;
import com.example.medtracker.models.Medication;
import com.example.medtracker.models.Record;
import com.example.medtracker.models.User;
import com.example.medtracker.util.Converters;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Doctor.class, User.class, Medication.class, Record.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DoctorDao doctorDao();
    public abstract UserDao userDao();
    public abstract MedicationDao medicationDao();
    public abstract RecordsDao recordsDao();

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService database_executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context appContext) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(appContext.getApplicationContext(), AppDatabase.class, "medtracker_database").build();
                }
            }
        }
        return instance;
    }

    public static RoomDatabase.Callback deleteAll = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            instance.userDao().deleteAllUsers();
            instance.medicationDao().deleteAllMedications();
            instance.doctorDao().deleteAllDoctors();
        }
    };
}
