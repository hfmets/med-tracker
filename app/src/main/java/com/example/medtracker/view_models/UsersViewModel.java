package com.example.medtracker.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.medtracker.AppRepository;
import com.example.medtracker.models.User;

import java.util.List;

public class UsersViewModel extends AndroidViewModel {

    private AppRepository repo;
    private final LiveData<List<User>> allUsers;


    public UsersViewModel(@NonNull Application application) {
        super(application);
        repo = new AppRepository(application);
        allUsers = repo.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() { return allUsers; }
}
