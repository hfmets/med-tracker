package com.example.medtracker.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "doctors")
public class Doctor extends Person{
    @PrimaryKey(autoGenerate = true)
    private int doctorId = 0;

    private String address;

    private String phone;
    public Doctor(String first, String last, String address, String phone) {
        super(first, last);
        this.address = address;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public int getDoctorId() { return doctorId; }

    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return getFirst() + " " + getLast();
    }
}
