package com.example.medtracker.models;

import androidx.lifecycle.LiveData;
import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class DoctorsWithMedications {
    @Embedded public Doctor doctor;
    @Relation(
            parentColumn = "doctorId",
            entityColumn = "medicationId"
    )
    public Medication medication;
}
