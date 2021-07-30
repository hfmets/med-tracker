package com.example.medtracker.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medtracker.MainActivity;
import com.example.medtracker.NavGraphDirections;
import com.example.medtracker.R;
import com.example.medtracker.models.User;
import com.example.medtracker.util.DialogDismissListener;
import com.example.medtracker.view_models.UsersViewModel;


public class EnterPasswordFragment extends DialogFragment {

    private User selectedUser;
    private int destinationId;

    public EnterPasswordFragment(User selectedUser, int destinationId) {
        this.selectedUser = selectedUser;
        this.destinationId = destinationId;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        UsersViewModel usersViewModel = new UsersViewModel(requireActivity().getApplication());
        String name = selectedUser.getFirst() + " " + selectedUser.getLast();
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View content = inflater.inflate(R.layout.fragment_enter_password, null);



        builder.setTitle(name).setMessage("Please enter your PIN to login.").setView(content).setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (((TextView)content.findViewById(R.id.user_pin)).getText().toString().equals(selectedUser.getPassword())) {
                    MainActivity ma = (MainActivity) requireActivity();
                    editor.putInt(MainActivity.LOGIN_STATUS, selectedUser.getUserId());
                    editor.apply();
                    ma.userChanged(destinationId);
                    dialog.dismiss();
                } else {
                    String toastMessage = "The PIN you entered is incorrect, try again or log in to a different account.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getContext(), toastMessage, duration);
                    toast.show();
                }
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
        View root = inflater.inflate(R.layout.fragment_enter_password, container, false);


        return root;
    }
}