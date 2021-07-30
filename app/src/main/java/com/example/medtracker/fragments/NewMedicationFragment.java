package com.example.medtracker.fragments;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medtracker.AppRepository;
import com.example.medtracker.MainActivity;
import com.example.medtracker.R;
import com.example.medtracker.models.Doctor;
import com.example.medtracker.models.Medication;
import com.example.medtracker.models.User;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class NewMedicationFragment extends Fragment {

    int selectedDoctor;
    SharedPreferences sharedPreferences;
    AppRepository repo;

    public NewMedicationFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_new_medication, container, false);

        // Set up variables needed
        MainActivity mainActivity = (MainActivity)getActivity();
        TextView medicationNameInput = root.findViewById(R.id.new_medication_name);
        TextView medicationDosageInput = root.findViewById(R.id.new_medication_dosage);
        TextView medicationSideEffectsInput = root.findViewById(R.id.new_medication_side_effects);
        TextView medicationAmountInput = root.findViewById(R.id.new_medication_amount);
        TextView medicationReminderCountInput = root.findViewById(R.id.new_medication_threshold);
        RadioGroup takeTimeRadioGroup = root.findViewById(R.id.new_medication_take_time_radio_group);
        final int morningRadio = R.id.morning_radio;
        final int afternoonRadio = R.id.afternoon_radio;
        final int eveningRadio = R.id.evening_radio;
        Button addButton = root.findViewById(R.id.add_medication);
        repo = new AppRepository(requireActivity().getApplication());
        sharedPreferences = getActivity().getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        User loggedInUser = updateLoggedInUser();
        Spinner spinner = root.findViewById(R.id.doctor_selection_spinner);

        selectedDoctor = -1;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Doctor doctorSelected = (Doctor)parent.getSelectedItem();
                setSelectedDoctorId(doctorSelected.getDoctorId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        repo.getAllDoctors().observe(getViewLifecycleOwner(), new Observer<List<Doctor>>() {
            @Override
            public void onChanged(List<Doctor> doctorList) {
                ArrayAdapter<Doctor> spinnerAdapter = new ArrayAdapter<Doctor>(getActivity(), android.R.layout.simple_spinner_item, doctorList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
            }
        });




        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = medicationNameInput.getText().toString();
                String dosage = medicationDosageInput.getText().toString();
                String sideEffects = medicationSideEffectsInput.getText().toString();
                int amount = Integer.parseInt(medicationAmountInput.getText().toString());
                int reminder = Integer.parseInt(medicationReminderCountInput.getText().toString());
                int doctorId = ((Doctor)spinner.getSelectedItem()).getDoctorId();
                Medication.TakeTimeCategory takeTimeCategory = null;
                switch (takeTimeRadioGroup.getCheckedRadioButtonId()) {
                    case morningRadio: {
                        takeTimeCategory = Medication.TakeTimeCategory.MORNING;
                    } break;
                    case afternoonRadio: {
                        takeTimeCategory = Medication.TakeTimeCategory.AFTERNOON;
                    } break;
                    case eveningRadio: {
                        takeTimeCategory = Medication.TakeTimeCategory.EVENING;
                    } break;

                }

                try {
                    if (validateData(name, dosage, sideEffects, amount, reminder, takeTimeCategory, loggedInUser, doctorId)) {

                            Medication newMed = new Medication(amount, reminder, doctorId, loggedInUser.getUserId(), name, sideEffects, dosage, takeTimeCategory);
                            repo.insertMedication(newMed);
                            Navigation.findNavController(root).popBackStack();
                    }
                    else {
                        String toastMessage = "Something is wrong with your form!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(getContext(), toastMessage, duration);
                        toast.show();
                    }
                } catch (NumberFormatException e) {
                    String toastMessage = "Make sure your Amount left and Reminder counts are numeric values!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getContext(), toastMessage, duration);
                    toast.show();
                }
            }
        });

        return root;
    }

    public void setSelectedDoctorId(int doctorId) {
        selectedDoctor = doctorId;
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

    public boolean validateData(String name, String dosage, String sideEffects, int amount, int reminder, Medication.TakeTimeCategory takeTimeCategory, User loggedInUser, int doctorId) {
        if (name.isEmpty() || dosage.isEmpty() || sideEffects.isEmpty() || (amount < 0) || (reminder < 0) || (takeTimeCategory == null) || (loggedInUser == null) || (doctorId < 1)){
            return false;
        }
        return true;
    }
}