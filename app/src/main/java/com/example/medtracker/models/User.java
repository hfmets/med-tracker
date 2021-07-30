package com.example.medtracker.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.medtracker.util.Converters;

import java.time.LocalTime;
import java.util.List;

@Entity(tableName = "users")
public class User extends Person{
    @PrimaryKey(autoGenerate = true)
    private int userId = 0;
    private int age;
    private String password;
    private Gender gender;

    private boolean morningOn;

    private boolean afternoonOn;
    private boolean eveningOn;

    private LocalTime morningAlert, afternoonAlert, eveningAlert;

    public User(String first, String last, String password, Gender gender, int age) {
        super(first, last);
        this.password = password;
        this.gender = gender;
        this.morningAlert = null;
        this.afternoonAlert = null;
        this.eveningAlert = null;
        this.age = age;
        this.morningOn = false;
        this.afternoonOn = false;
        this.eveningOn = false;
    }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() { return gender; }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean isMorningOn() {
        return morningOn;
    }

    public void setMorningOn(boolean morningOn) {
        this.morningOn = morningOn;
    }

    public boolean isAfternoonOn() {
        return afternoonOn;
    }

    public void setAfternoonOn(boolean afternoonOn) {
        this.afternoonOn = afternoonOn;
    }

    public boolean isEveningOn() {
        return eveningOn;
    }

    public void setEveningOn(boolean eveningOn) {
        this.eveningOn = eveningOn;
    }

    public String getPassword() { return password; }

    public LocalTime getMorningAlert() {
        return morningAlert;
    }

    public void setMorningAlert(LocalTime morningAlert) {
        this.morningAlert = morningAlert;
    }

    public LocalTime getAfternoonAlert() {
        return afternoonAlert;
    }

    public void setAfternoonAlert(LocalTime afternoonAlert) { this.afternoonAlert = afternoonAlert; }

    public LocalTime getEveningAlert() {
        return eveningAlert;
    }

    public void setEveningAlert(LocalTime eveningAlert) {
        this.eveningAlert = eveningAlert;
    }

    public static enum Gender {
        MALE,
        FEMALE
    }
}
