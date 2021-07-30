package com.example.medtracker.util;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import com.example.medtracker.models.Medication;
import com.example.medtracker.models.User;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Converters {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static LocalTime toTime(String timeString) {
        if (timeString == null) {
            return null;
        } else {
            return LocalTime.parse(timeString);
        }
    }

    @TypeConverter
    public static String toTimeString(LocalTime time) {
        if (time == null) {
            return null;
        } else {
            return time.toString();
        }
    }

    @TypeConverter
    public static int categoryToInt(Medication.TakeTimeCategory category) {
        return category.ordinal();
    }

    @TypeConverter
    public static Medication.TakeTimeCategory takeTimeCategoryFromInt(int categoryInt) {
        return Medication.TakeTimeCategory.values()[categoryInt];
    }

    @TypeConverter
    public static int genderToInt(User.Gender gender) {
        return gender.ordinal();
    }

    @TypeConverter
    public static User.Gender genderFromInt(int genderInt) {
        return User.Gender.values()[genderInt];
    }

    /*@RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static String toDate(String dateString) {
        if (dateString == null) {
            return null;
        } else {
            return LocalDateTime.parse(dateString);
        }
    }

    @TypeConverter
    public static String toDateString(String dateTime) {
        if (dateTime == null) {
            return null;
        } else {
            return dateTime.toString();
        }
    }*/
}
