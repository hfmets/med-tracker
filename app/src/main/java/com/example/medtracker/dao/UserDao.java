package com.example.medtracker.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.medtracker.models.User;
import com.example.medtracker.models.UserWithDoctors;
import com.google.common.util.concurrent.ListenableFuture;

import java.time.LocalTime;
import java.util.List;


@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    public LiveData<List<User>> getAll();

    @Query("SELECT * FROM users WHERE userId = :userId")
    public ListenableFuture<User> getUserById(int userId);

    @Insert
    public ListenableFuture<Long> insertUser(User user);

    @Insert
    public void insert(User user);

    @Query("SELECT * FROM users WHERE userId = :userId")
    public List<User> getById(int userId);

    @Query("DELETE FROM users")
    public void deleteAllUsers();

    @Query("SELECT * FROM users WHERE first = :first")
    public List<User> getUserByName(String first);

    @Transaction
    @Query("SELECT * FROM users WHERE userId = :userId")
    public LiveData<UserWithDoctors> getUserWithDoctors(int userId);

    @Query("UPDATE users SET morningAlert = :morning, afternoonAlert = :afternoon, eveningAlert = :evening WHERE userId = :userId")
    public ListenableFuture<Integer> updateAlertTimesForUser(LocalTime morning, LocalTime afternoon, LocalTime evening, int userId);

    @Query("UPDATE users SET morningOn = :morning, afternoonOn = :afternoon, eveningOn = :evening WHERE userId = :userId")
    public ListenableFuture<Integer> updateSwitchStatusForUser(boolean morning, boolean afternoon, boolean evening, int userId);
}
