package com.example.medtracker.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.medtracker.AppRepository;
import com.example.medtracker.MainActivity;
import com.example.medtracker.R;
import com.example.medtracker.models.User;
import com.example.medtracker.util.AlarmReceiver;
import com.example.medtracker.util.DialogDismissListener;
import com.example.medtracker.util.TimePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class SettingsFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {

    int selectedTimeCategory;
    EditText morningInput;
    EditText afternoonInput;
    EditText eveningInput;
    AppRepository repo;
    User loggedInUser;
    SharedPreferences sharedPreferences;

    public SettingsFragment() {

        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        repo = new AppRepository(requireActivity().getApplication());
        sharedPreferences = getActivity().getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        loggedInUser = updateLoggedInUser();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm");
        morningInput = root.findViewById(R.id.morning_alert_box);
        afternoonInput = root.findViewById(R.id.afternoon_alert_box);
        eveningInput = root.findViewById(R.id.evening_alert_box);
        Switch morningSwitch = root.findViewById(R.id.morning_switch);
        Switch afternoonSwitch = root.findViewById(R.id.afternoon_switch);
        Switch eveningSwitch = root.findViewById(R.id.evening_switch);
        Button saveSettings = root.findViewById(R.id.save_settings_btn);
        if (loggedInUser != null) {
            if (loggedInUser.getMorningAlert() != null) {
                morningInput.setText(loggedInUser.getMorningAlert().format(dtf));
            }

            if (loggedInUser.getAfternoonAlert() != null) {
                afternoonInput.setText(loggedInUser.getAfternoonAlert().format(dtf));
            }

            if (loggedInUser.getEveningAlert() != null) {
                eveningInput.setText(loggedInUser.getEveningAlert().format(dtf));
            }

            if (loggedInUser.isMorningOn()) {
                morningSwitch.setChecked(true);
            }

            if (loggedInUser.isAfternoonOn()) {
                afternoonSwitch.setChecked(true);
            }

            if (loggedInUser.isEveningOn()) {
                eveningSwitch.setChecked(true);
            }
        }


        morningInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTimeCategory = 0;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getChildFragmentManager(), "time picker");
            }
        });

        afternoonInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTimeCategory = 1;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getChildFragmentManager(), "time picker");
            }
        });

        eveningInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTimeCategory = 2;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getChildFragmentManager(), "time picker");
            }
        });

        morningSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
                Calendar c = Calendar.getInstance();

                if (isChecked) {
                    try {
                        repo.updateSwitchStatus(true, loggedInUser.isAfternoonOn(), loggedInUser.isEveningOn(), loggedInUser.getUserId());
                        loggedInUser.setMorningOn(true);
                        c.setTime(sdf.parse(morningInput.getText().toString()));
                        createAlarm(0, c);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    cancelAlarm(0);
                    repo.updateSwitchStatus(false, loggedInUser.isAfternoonOn(), loggedInUser.isEveningOn(), loggedInUser.getUserId());
                    loggedInUser.setMorningOn(false);
                }
            }
        });

        afternoonSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
                Calendar c = Calendar.getInstance();
                if (isChecked) {
                    try {
                        repo.updateSwitchStatus(loggedInUser.isMorningOn(), true, loggedInUser.isEveningOn(), loggedInUser.getUserId());
                        loggedInUser.setAfternoonOn(true);
                        c.setTime(sdf.parse(afternoonInput.getText().toString()));
                        createAlarm(1, c);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    cancelAlarm(1);
                    repo.updateSwitchStatus(loggedInUser.isMorningOn(), false, loggedInUser.isEveningOn(), loggedInUser.getUserId());
                    loggedInUser.setAfternoonOn(false);
                }
            }
        });

        eveningSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
                Calendar c = Calendar.getInstance();
                if (isChecked) {
                    try {
                        repo.updateSwitchStatus(loggedInUser.isMorningOn(), loggedInUser.isAfternoonOn(), true, loggedInUser.getUserId());
                        loggedInUser.setEveningOn(true);
                        c.setTime(sdf.parse(eveningInput.getText().toString()));
                        createAlarm(2, c);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    cancelAlarm(2);
                    repo.updateSwitchStatus(loggedInUser.isMorningOn(), loggedInUser.isAfternoonOn(), false, loggedInUser.getUserId());
                    loggedInUser.setEveningOn(false);
                }
            }
        });

        /*saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
                Calendar c = Calendar.getInstance();
                if (morningSwitch.isChecked()) {
                    try {
                        c.setTime(sdf.parse(morningInput.getText().toString()));
                        createAlarm(0, c);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }
        });*/


        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        LocalTime time = LocalTime.of(hourOfDay, minute);
        SimpleDateFormat df = new SimpleDateFormat("h:mm");
        String timeString = df.format(c.getTime());
        switch (selectedTimeCategory) {
            case 0: {
                morningInput.setText(timeString);
                loggedInUser.setMorningAlert(time);
                repo.updateAlertTimesForUser(time, loggedInUser.getAfternoonAlert(), loggedInUser.getEveningAlert(), loggedInUser.getUserId());
            } break;
            case 1: {
                afternoonInput.setText(timeString);
                loggedInUser.setAfternoonAlert(time);
                repo.updateAlertTimesForUser(loggedInUser.getMorningAlert(), time, loggedInUser.getEveningAlert(), loggedInUser.getUserId());
            } break;
            case 2: {
                eveningInput.setText(timeString);
                loggedInUser.setEveningAlert(time);
                repo.updateAlertTimesForUser(loggedInUser.getMorningAlert(), loggedInUser.getAfternoonAlert(), time, loggedInUser.getUserId());
            } break;
        }
    }

    public void createAlarm(int alertCategory, Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("timeCategory", alertCategory);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), alertCategory, intent, 0);
        calendar.add(Calendar.DATE, 1);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        /*switch (alertCategory) {
            case 0: {
                Intent intent = new Intent(getContext(), AlarmReceiver.class);
                intent.putExtra("timeCategory", alertCategory);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), alertCategory, intent, 0);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            } break;
            case 1: {
                Intent intent = new Intent(getContext(), AlarmReceiver.class);
                intent.putExtra("timeCategory", alertCategory);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), alertCategory, intent, 0);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }*/

    }

    public void cancelAlarm(int alertCategory) {
        AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
        switch (alertCategory) {
            case 0: {
                Intent intent = new Intent(getContext(), AlarmReceiver.class);
                intent.putExtra("timeCategory", 0);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), alertCategory, intent, 0);
                alarmManager.cancel(pendingIntent);
            }
        }
    }

    public User updateLoggedInUser() {
        try {
            return repo.getUserById(sharedPreferences.getInt(MainActivity.LOGIN_STATUS, -1)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}