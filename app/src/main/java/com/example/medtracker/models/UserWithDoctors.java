package com.example.medtracker.models;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class UserWithDoctors {
    @Embedded public User user;
    @Relation(
            parentColumn = "userId",
            entity = Doctor.class,
            entityColumn = "doctorId"
    )
    public List<Doctor> doctors;
}
