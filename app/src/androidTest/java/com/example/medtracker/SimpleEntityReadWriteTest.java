package com.example.medtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.medtracker.dao.DoctorDao;
import com.example.medtracker.dao.MedicationDao;
import com.example.medtracker.dao.UserDao;
import com.example.medtracker.models.Medication;
import com.example.medtracker.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {
    private UserDao userDao;
    private MedicationDao medicationDao;
    private DoctorDao doctorDao;
    private AppDatabase db;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = db.userDao();
        medicationDao = db.medicationDao();
        doctorDao = db.doctorDao();
        sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @After
    public void closeDb() throws IOException {
        //db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        User user = new User("George", "of The Jungle", "", User.Gender.MALE, 34);
        userDao.insert(user);
        List<User> byName = userDao.getUserByName("George");
        assertThat(byName.get(0).getFirst(), equalTo("George"));
    }


    // need to make sure form checks for this
    @Test
    public void createMedicationWithoutDoctor() {
        Medication medication = new Medication(30, 0, 0, 0, "Concerta", "None", "36 mg", Medication.TakeTimeCategory.MORNING);
        medicationDao.insert(medication);
        List<Medication> medicationList = medicationDao.getAllMedications();
        assertThat(doctorDao.getById(medicationList.get(0).getDoctorId()).isEmpty(), equalTo(true));
    }

    @Test
    public void loggedInUserTest() {
        User user = new User("George", "of The Jungle", "", User.Gender.MALE, 34);
        User user2 = new User("Tarzan", "of The Jungle", "", User.Gender.MALE, 37);
        userDao.insert(user);
        editor.putInt(MainActivity.LOGIN_STATUS, user.getUserId());
        assertThat(getLoggedInUser().get(0).getUserId(), equalTo(user.getUserId()+1));
        userDao.insert(user2);
        editor.putInt(MainActivity.LOGIN_STATUS, user2.getUserId());
        assertThat(getLoggedInUser().get(0).getUserId(), equalTo(user2.getUserId()+1));
    }

    public List<User> getLoggedInUser() {
        return userDao.getById(sharedPreferences.getInt(MainActivity.LOGIN_STATUS, 0));
    }
}