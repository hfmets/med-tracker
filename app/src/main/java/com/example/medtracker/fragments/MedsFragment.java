package com.example.medtracker.fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.medtracker.AppRepository;
import com.example.medtracker.MainActivity;
import com.example.medtracker.NavGraphDirections;
import com.example.medtracker.R;
import com.example.medtracker.adapters.MedicationAdapter;
import com.example.medtracker.adapters.PageObjectMedsFragmentAdapter;
import com.example.medtracker.models.Medication;
import com.example.medtracker.models.MedicationWithDoctor;
import com.example.medtracker.models.User;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class MedsFragment extends Fragment {

    AppRepository repo;
    SharedPreferences sharedPreferences;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    PageObjectMedsFragmentAdapter pageAdapter;

    public MedsFragment() {
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
        View root = inflater.inflate(R.layout.fragment_meds, container, false);
        MainActivity mainActivity = (MainActivity)requireActivity();
        repo = new AppRepository(requireActivity().getApplication());
        sharedPreferences = getActivity().getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        User loggedInUser = updateLoggedInUser();
        Button addMedicationButton = root.findViewById(R.id.add_medication_btn);
        viewPager = root.findViewById(R.id.meds_view_pager);
        tabLayout = root.findViewById(R.id.meds_tab_layout);
        pageAdapter = new PageObjectMedsFragmentAdapter(this);
        viewPager.setAdapter(pageAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: {
                    tab.setText("Morning");
                } break;
                case 1: {
                    tab.setText("Afternoon");
                } break;
                case 2: {
                    tab.setText("Evening");
                } break;
                case 3: {
                    tab.setText("All");
                }
            }
        }).attach();





        addMedicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(MedsFragmentDirections.actionMedsFragmentToNewMedicationFragment());
            }
        });



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