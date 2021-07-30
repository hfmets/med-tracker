package com.example.medtracker.fragments;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.medtracker.AppRepository;
import com.example.medtracker.R;
import com.example.medtracker.adapters.DoctorsAdapter;
import com.example.medtracker.models.Doctor;
import com.example.medtracker.models.User;
import com.example.medtracker.util.DialogDismissListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class DoctorsFragment extends Fragment {
    private List<Doctor> doctorList;

    public DoctorsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_doctors, container, false);
        AppRepository repo = new AppRepository(requireActivity().getApplication());
        Button newDoctorButton = root.findViewById(R.id.add_doctor_btn);
        SharedPreferences sp = requireActivity().getApplicationContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        RecyclerView recyclerView = root.findViewById(R.id.doctors_card_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DoctorsAdapter doctorsAdapter = new DoctorsAdapter(Collections.emptyList(), getContext());
        recyclerView.setAdapter(doctorsAdapter);


        newDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(DoctorsFragmentDirections.actionDoctorsFragmentToNewDoctorFragment());
            }
        });


        repo.getAllDoctors().observe(getViewLifecycleOwner(), new Observer<List<Doctor>>() {
            @Override
            public void onChanged(List<Doctor> doctors) {
                if (doctorsAdapter != null) {
                    doctorsAdapter.setDoctors(doctors);
                }
            }
        });


        return root;
    }
}