package com.example.medtracker.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.medtracker.NavGraphDirections;
import com.example.medtracker.R;
import com.example.medtracker.adapters.UserAdapter;
import com.example.medtracker.models.User;
import com.example.medtracker.util.DialogDismissListener;
import com.example.medtracker.view_models.UsersViewModel;

import java.util.ArrayList;
import java.util.List;


public class SignInDialogFragment extends DialogFragment {

    private static User selectedUser;
    private int destinationId;

    public SignInDialogFragment(int destinationId) {
        this.destinationId = destinationId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        UsersViewModel usersViewModel = new UsersViewModel(requireActivity().getApplication());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View content = inflater.inflate(R.layout.fragment_sign_in_dialog, null);
        RecyclerView recyclerView = content.findViewById(R.id.user_recyler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        UserAdapter userAdapter = new UserAdapter(new ArrayList<User>());
        recyclerView.setAdapter(userAdapter);

        usersViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                userAdapter.updateUserList(users);
            }
        });


        builder.setView(content).setTitle("Create New User or Sign In.").setNeutralButton("Create User", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                NavController navController = ((NavHostFragment)requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();
                NavDirections action = NavGraphDirections.actionGlobalSignUpFragment();
                navController.navigate(action);
            }
        }).setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EnterPasswordFragment enterPasswordFragment = new EnterPasswordFragment(selectedUser, destinationId);
                enterPasswordFragment.show(requireActivity().getSupportFragmentManager(), null);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();



        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UsersViewModel usersViewModel = new UsersViewModel(requireActivity().getApplication());
        View root = inflater.inflate(R.layout.fragment_sign_in_dialog, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.user_recyler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        UserAdapter userAdapter = new UserAdapter(new ArrayList<User>());
        recyclerView.setAdapter(userAdapter);

        usersViewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                userAdapter.updateUserList(users);
            }
        });



        return root;
    }

    public static void setSelectedUser(User user) {
        selectedUser = user;
    }
}