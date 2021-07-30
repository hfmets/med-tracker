package com.example.medtracker.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalTime;

@Entity(tableName = "medications")
public class Medication {
    @PrimaryKey(autoGenerate = true)
    private int medicationId = 0;

    private int amount;
    private int lowThresh;
    private int doctorId;
    private int userId;
    private String name, sideEffects, dosage;
    private TakeTimeCategory takeTimeCategory;
    private boolean taken;

    public Medication(int amount, int lowThresh, int doctorId, int userId, String name, String sideEffects, String dosage, TakeTimeCategory takeTimeCategory) {
        this.amount = amount;
        this.lowThresh = lowThresh;
        this.doctorId = doctorId;
        this.userId = userId;
        this.name = name;
        this.sideEffects = sideEffects;
        this.dosage = dosage;
        this.takeTimeCategory = takeTimeCategory;
    }



    public int getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(int medicationId) {
        this.medicationId = medicationId;
    }

    public boolean isTaken() { return taken; }

    public void setTaken(boolean taken) { this.taken = taken; }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getLowThresh() {
        return lowThresh;
    }

    public void setLowThresh(int lowThresh) {
        this.lowThresh = lowThresh;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public TakeTimeCategory getTakeTimeCategory() { return takeTimeCategory; }

    public void setTakeTimeCategory(TakeTimeCategory takeTimeCategory) { this.takeTimeCategory = takeTimeCategory; }

    public void decrementAmount() {
        amount = amount - 1;
    }

    public void incrementAmount() {
        amount = amount + 1;
    }

    public enum TakeTimeCategory {
        MORNING,
        AFTERNOON,
        EVENING,
    }
}
