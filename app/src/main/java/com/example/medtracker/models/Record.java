package com.example.medtracker.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "records")
public class Record {
    @PrimaryKey(autoGenerate = true)
    private int recordsId = 0;

    private int userId;


    private String date;

    private String morningMedicationsTaken;
    private String afternoonMedicationsTaken;
    private String eveningMedicationsTaken;
    public Record(String date, int userId) {
        this.date = date;
        this.morningMedicationsTaken = "";
        this.afternoonMedicationsTaken = "";
        this.eveningMedicationsTaken = "";
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getMorningMedicationsTaken() {
        return morningMedicationsTaken;
    }

    public void setMorningMedicationsTaken(String morningMedicationsTaken) {
        this.morningMedicationsTaken = morningMedicationsTaken;
    }

    public String getAfternoonMedicationsTaken() {
        return afternoonMedicationsTaken;
    }

    public void setAfternoonMedicationsTaken(String afternoonMedicationsTaken) {
        this.afternoonMedicationsTaken = afternoonMedicationsTaken;
    }

    public String getEveningMedicationsTaken() {
        return eveningMedicationsTaken;
    }

    public void setEveningMedicationsTaken(String eveningMedicationsTaken) {
        this.eveningMedicationsTaken = eveningMedicationsTaken;
    }


    public int getRecordsId() {
        return recordsId;
    }

    public void setRecordsId(int recordsId) {
        this.recordsId = recordsId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
