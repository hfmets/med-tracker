package com.example.medtracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medtracker.AppRepository;
import com.example.medtracker.MainActivity;
import com.example.medtracker.R;
import com.example.medtracker.adapters.HomeAdapter;
import com.example.medtracker.adapters.MedicationAdapter;
import com.example.medtracker.models.Medication;
import com.example.medtracker.models.MedicationWithDoctor;
import com.example.medtracker.models.User;
import com.example.medtracker.view_models.MedicationsWithDoctorsViewModel;
import com.example.medtracker.view_models.UserWithMedicationsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class PageObjectMedsFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    AppRepository repo;
    User loggedInUser;
    SharedPreferences sharedPreferences;
    List<MedicationWithDoctor> medicationList;
    MedicationAdapter medicationAdapter;


    public PageObjectMedsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_page_object_meds, container, false);
        repo = new AppRepository(getActivity().getApplication());
        MedicationsWithDoctorsViewModel medsDoctorsViewModel = new ViewModelProvider(requireActivity()).get(MedicationsWithDoctorsViewModel.class);
        sharedPreferences = getActivity().getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        loggedInUser = updateLoggedInUser();

        medicationList = new ArrayList<MedicationWithDoctor>();

        RecyclerView recyclerView = root.findViewById(R.id.medication_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        medicationAdapter = new MedicationAdapter(medicationList, getContext());
        recyclerView.setAdapter(medicationAdapter);

        Bundle args = getArguments();

        if (loggedInUser != null) {
            switch (args.getInt(ARG_OBJECT)) {
                case 1: {
                    medsDoctorsViewModel.getAllMedicationsForUserMorning(loggedInUser.getUserId()).observe(getViewLifecycleOwner(), new Observer<List<MedicationWithDoctor>>() {
                        @Override
                        public void onChanged(List<MedicationWithDoctor> medications) {
                            medicationAdapter.setMedications(medications);
                        }
                    });
                } break;
                case 2: {
                    medsDoctorsViewModel.getAllMedicationsForUserAfternoon(loggedInUser.getUserId()).observe(getViewLifecycleOwner(), new Observer<List<MedicationWithDoctor>>() {
                        @Override
                        public void onChanged(List<MedicationWithDoctor> medications) {
                            medicationAdapter.setMedications(medications);
                        }
                    });
                } break;
                case 3: {
                    medsDoctorsViewModel.getAllMedicationsForUserEvening(loggedInUser.getUserId()).observe(getViewLifecycleOwner(), new Observer<List<MedicationWithDoctor>>() {
                        @Override
                        public void onChanged(List<MedicationWithDoctor> medications) {
                            medicationAdapter.setMedications(medications);
                        }
                    });
                } break;
                case 4: {
                    medsDoctorsViewModel.getAllMedicationsForUser(loggedInUser.getUserId()).observe(getViewLifecycleOwner(), new Observer<List<MedicationWithDoctor>>() {
                        @Override
                        public void onChanged(List<MedicationWithDoctor> medications) {
                            medicationAdapter.setMedications(medications);
                        }
                    });
                } break;
            }
        }


        return root;
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