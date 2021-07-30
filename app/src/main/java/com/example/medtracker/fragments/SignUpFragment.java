package com.example.medtracker.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medtracker.AppRepository;
import com.example.medtracker.MainActivity;
import com.example.medtracker.R;
import com.example.medtracker.models.User;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;


public class SignUpFragment extends Fragment {



    public SignUpFragment() {
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
        View root = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Set up variables needed
        MainActivity mainActivity = (MainActivity)getActivity();
        TextView firstNameInput = root.findViewById(R.id.new_user_first_name);
        TextView lastNameInput = root.findViewById(R.id.new_user_last_name);
        RadioGroup genderRadioGroup = root.findViewById(R.id.gender_radio_grp);
        TextView ageInput = root.findViewById(R.id.new_user_age);
        TextView passwordInput = root.findViewById(R.id.new_user_password);
        Button submitButton = root.findViewById(R.id.sign_up_submit);
        int maleButtonId = R.id.male_radio;
        int femaleButtonId = R.id.female_radio;
        SharedPreferences sharedPreferences = getActivity().getApplication().getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Submit click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onClick(View v) {
                String firstNameText = firstNameInput.getText().toString();
                String lastNameText = lastNameInput.getText().toString();
                int age = Integer.parseInt(ageInput.getText().toString());
                String passwordText = passwordInput.getText().toString();
                int genderSelectedId = genderRadioGroup.getCheckedRadioButtonId();
                User.Gender genderCode = null;

                if (genderSelectedId == maleButtonId) {
                    genderCode = User.Gender.MALE;
                } else if (genderSelectedId == femaleButtonId) {
                    genderCode = User.Gender.FEMALE;
                }

                if (validateData(firstNameText, lastNameText, age, genderCode, passwordText)) {
                    User newUser = new User(firstNameText, lastNameText, passwordText, genderCode, age);
                    AppRepository repo = new AppRepository(requireActivity().getApplication());
                    try {
                        Long userId = repo.insertUser(newUser).get();
                        editor.putInt(MainActivity.LOGIN_STATUS, userId.intValue());
                        editor.apply();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    Navigation.findNavController(root).popBackStack();
                } else {
                    String toastMessage = "Something is wrong with your form!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getContext(), toastMessage, duration);
                    toast.show();
                }
            }
        });

        return root;
    }

    public boolean validateData(String firstName, String lastName, int age, User.Gender gender, String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || (age <= 0) || (gender == null)) {
            return false;
        }
        return true;
    }
}