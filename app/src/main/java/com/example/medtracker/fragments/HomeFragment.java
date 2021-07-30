package com.example.medtracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.medtracker.AppRepository;
import com.example.medtracker.MainActivity;
import com.example.medtracker.R;
import com.example.medtracker.adapters.HomeAdapter;
import com.example.medtracker.adapters.PageObjectHomeFragmentAdapter;
import com.example.medtracker.models.Medication;
import com.example.medtracker.models.User;
import com.example.medtracker.util.DialogDismissListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {

    View root = null;
    AppRepository repo;
    User loggedInUser;
    SharedPreferences sharedPreferences;
    List<Medication> medicationList;
    HomeAdapter medicationAdapter;
    ViewPager2 viewPager;
    PageObjectHomeFragmentAdapter pageAdapter;
    TabLayout tabLayout;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        repo = new AppRepository(getActivity().getApplication());
        sharedPreferences = getActivity().getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        loggedInUser = updateLoggedInUser();
        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.home_pager);
        pageAdapter = new PageObjectHomeFragmentAdapter(this);
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

        if (loggedInUser != null) {
            ((TextView)root.findViewById(R.id.logged_in_user)).setText(loggedInUser.getFirst() + " " + loggedInUser.getLast());
        } else if (loggedInUser == null){
            ((TextView)root.findViewById(R.id.logged_in_user)).setText("No user created.");
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