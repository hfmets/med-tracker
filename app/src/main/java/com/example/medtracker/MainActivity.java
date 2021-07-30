package com.example.medtracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import com.example.medtracker.fragments.DoctorsFragment;
import com.example.medtracker.fragments.HomeFragment;
import com.example.medtracker.fragments.HomeFragmentDirections;
import com.example.medtracker.fragments.MedsFragment;
import com.example.medtracker.fragments.ReportsFragment;
import com.example.medtracker.fragments.SettingsFragment;
import com.example.medtracker.fragments.SignInDialogFragment;
import com.example.medtracker.models.Doctor;
import com.example.medtracker.models.User;
import com.example.medtracker.util.AlarmReceiver;
import com.example.medtracker.util.MedicationManagerWorker;
import com.example.medtracker.util.ResetReciever;
import com.example.medtracker.util.TimePickerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    public User loggedInUser = null;
    AppRepository repo;
    Toolbar toolbar = null;
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOGIN_STATUS = "loginStatus";
    private static final int RESET_CODE = 5;
    NavController navController;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up toolbar/actionbar
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        onCreateOptionsMenu(toolbar.getMenu());
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        // Get bottom nav
        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottom_nav);

        repo = new AppRepository(getApplication());

        FragmentManager mainFragmentManager = getSupportFragmentManager();
        NavHostFragment mainNavHost = (NavHostFragment) mainFragmentManager.findFragmentById(R.id.nav_host_fragment);
        navController = mainNavHost.getNavController();
        NavigationUI.setupWithNavController(bottomNav, navController);

        if (sharedPreferences.getInt(LOGIN_STATUS, 0) == 0) {
            SignInDialogFragment signInDialogFragment = new SignInDialogFragment(navController.getCurrentDestination().getId());
            signInDialogFragment.show(getSupportFragmentManager(), null);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), RESET_CODE,
                new Intent(getApplicationContext(), ResetReciever.class), 0);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }



    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        NavController navController = ((NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();
        switch (itemId) {
            case R.id.addUser: {
                NavDirections action = NavGraphDirections.actionGlobalSignUpFragment();
                navController.navigate(action);
                return true;
            }
            case R.id.switchUser: {
                SignInDialogFragment signInDialogFragment = new SignInDialogFragment(navController.getCurrentDestination().getId());
                signInDialogFragment.show(getSupportFragmentManager(), null);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Inflates the menu for the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar, menu);
        return true;
    }

    public void userChanged(int fragmentId) {
        navController.popBackStack(fragmentId, true);
        navController.navigate(fragmentId);
    }

}