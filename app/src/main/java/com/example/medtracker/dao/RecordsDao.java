package com.example.medtracker.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.medtracker.models.Record;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface RecordsDao {
    @Insert
    public ListenableFuture<Long> insertRecord(Record record);

    @Query("SELECT * FROM records")
    public LiveData<List<Record>> getAll();

    @Query("SELECT * FROM records")
    public List<Record> getAllNotLive();

    @Query("SELECT * FROM records WHERE userId = :userId")
    public List<Record> getAllNotLiveFromUser(int userId);

    @Query("SELECT * FROM records WHERE date = :date AND userId = :userId")
    public Record getRecordByDate(String date, int userId);

    @Update
    public ListenableFuture<Integer> updateRecord(Record record);
}
