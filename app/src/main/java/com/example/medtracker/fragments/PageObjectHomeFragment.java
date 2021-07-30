package com.example.medtracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.medtracker.AppRepository;
import com.example.medtracker.MainActivity;
import com.example.medtracker.R;
import com.example.medtracker.adapters.HomeAdapter;
import com.example.medtracker.models.Medication;
import com.example.medtracker.models.User;
import com.example.medtracker.view_models.UserWithMedicationsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PageObjectHomeFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    AppRepository repo;
    User loggedInUser;
    SharedPreferences sharedPreferences;
    List<Medication> medicationList;
    HomeAdapter medicationAdapter;

    public PageObjectHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_page_object, container, false);
        repo = new AppRepository(getActivity().getApplication());
        UserWithMedicationsViewModel userMedsViewModel = new ViewModelProvider(requireActivity()).get(UserWithMedicationsViewModel.class);
        sharedPreferences = getActivity().getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        loggedInUser = updateLoggedInUser();

        medicationList = new ArrayList<Medication>();

        RecyclerView recyclerView = root.findViewById(R.id.medication_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        medicationAdapter = new HomeAdapter(medicationList, requireActivity().getApplication());
        recyclerView.setAdapter(medicationAdapter);

        Bundle args = getArguments();
        if (loggedInUser != null) {
            switch (args.getInt(ARG_OBJECT)) {
                case 1: {
                    userMedsViewModel.getMedicationsMorning(loggedInUser.getUserId()).observe(getViewLifecycleOwner(), new Observer<List<Medication>>() {
                        @Override
                        public void onChanged(List<Medication> medications) {
                            medicationAdapter.setMedicationList(medications);
                        }
                    });
                } break;
                case 2: {
                    userMedsViewModel.getMedicationsAfternoon(loggedInUser.getUserId()).observe(getViewLifecycleOwner(), new Observer<List<Medication>>() {
                        @Override
                        public void onChanged(List<Medication> medications) {
                            medicationAdapter.setMedicationList(medications);
                        }
                    });
                } break;
                case 3: {
                    userMedsViewModel.getMedicationsEvening(loggedInUser.getUserId()).observe(getViewLifecycleOwner(), new Observer<List<Medication>>() {
                        @Override
                        public void onChanged(List<Medication> medications) {
                            medicationAdapter.setMedicationList(medications);
                        }
                    });
                } break;
                case 4: {
                    userMedsViewModel.getMedicationsForUser(loggedInUser.getUserId()).observe(getViewLifecycleOwner(), new Observer<List<Medication>>() {
                        @Override
                        public void onChanged(List<Medication> medications) {
                            medicationAdapter.setMedicationList(medications);
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

    @Override
    public void onResume() {
        super.onResume();
        updateLoggedInUser();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.search_action_bar, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                medicationAdapter.filter(newText);
                return false;
            }
        });
    }
}