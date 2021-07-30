package com.example.medtracker.models;

import androidx.room.Embedded;
import androidx.room.Relation;

public class MedicationWithDoctor {
    @Embedded public Medication medication;
    @Relation(
            parentColumn = "doctorId",
            entityColumn = "doctorId"
    )
    public Doctor doctor;
}
