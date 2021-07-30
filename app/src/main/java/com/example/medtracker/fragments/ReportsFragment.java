package com.example.medtracker.fragments;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.medtracker.AppRepository;
import com.example.medtracker.MainActivity;
import com.example.medtracker.R;
import com.example.medtracker.models.Record;
import com.example.medtracker.models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportsFragment extends Fragment {
    public static final int CREATE_FILE = 1;
    private AppRepository repo;
    User loggedInUser;
    SharedPreferences sharedPreferences;
    public ReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reports, container, false);
        repo = new AppRepository(requireActivity().getApplication());
        sharedPreferences = getActivity().getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        loggedInUser = updateLoggedInUser();


        Button generateReportBtn = root.findViewById(R.id.generate_report_btn);

        generateReportBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                File dir = new File(getContext().getExternalFilesDir(null).toString(), "MedTrackerReports");

                if (!dir.exists() && !dir.isDirectory()) {
                    dir.mkdirs();
                }

                try {
                    if (loggedInUser != null) {
                        createFile(dir);
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    private void createFile(File directory) throws IOException, ExecutionException, InterruptedException {

        SimpleDateFormat sdf = new SimpleDateFormat("M_d_yyyy_h_m_s");
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());
        FileOutputStream fos = new FileOutputStream(new File(directory, "medtracker_report_" + date + ".txt"));
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        List<Record> recordList = repo.getAllNotLiveFromUser(loggedInUser.getUserId()-1);
        osw.write("Report for " + loggedInUser.getFirst() + " " + loggedInUser.getLast());
        osw.write("\n     Date     |     Morning     |     Afternoon     |     Evening     ");
        osw.append("\n");
        int j;
        if (recordList.size() >= 30) {
            j = 30;
        } else {
            j = recordList.size();
        }
        Log.i("SIZE IS THIS", "onClick: recordList size is " + recordList.size());
        for (int i = 1; i <= j; i++) {
            osw.append(recordList.get(recordList.size() - i).getDate()).append(" | ");
            osw.append(recordList.get(recordList.size() - i).getMorningMedicationsTaken()).append(" | ");
            osw.append(recordList.get(recordList.size() - i).getAfternoonMedicationsTaken()).append(" | ");
            osw.append(recordList.get(recordList.size() - i).getEveningMedicationsTaken()).append(" \n");
        }
        osw.close();
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