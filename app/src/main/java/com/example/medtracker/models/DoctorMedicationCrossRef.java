package com.example.medtracker.models;

import androidx.room.Entity;

@Entity(primaryKeys = {"doctorId", "medicationId"})
public class DoctorMedicationCrossRef {
    public int doctorId;
    public int medicationId;
}
