package com.example.medtracker.models;

import androidx.room.Entity;

@Entity(primaryKeys = {"userId", "medicationId"})
public class UserMedicationCrossRef {
    public int userId;
    public int medicationId;
}
