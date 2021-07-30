package com.example.medtracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.medtracker.AppRepository;
import com.example.medtracker.MainActivity;
import com.example.medtracker.R;
import com.example.medtracker.models.Doctor;
import com.example.medtracker.models.Medication;
import com.example.medtracker.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EditMedicationFragment extends Fragment {

    int newSelectedDoctor;
    AppRepository repo;
    SharedPreferences sharedPreferences;
    Medication medicationBeingUpdated;
    ArrayAdapter<Doctor> spinnerAdapter;

    public EditMedicationFragment() {
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
        View root = inflater.inflate(R.layout.fragment_edit_medication, container, false);

        // Set up variables needed
        MainActivity mainActivity = (MainActivity)getActivity();
        TextView medicationNameInput = root.findViewById(R.id.edit_medication_name);
        TextView medicationDosageInput = root.findViewById(R.id.edit_medication_dosage);
        TextView medicationSideEffectsInput = root.findViewById(R.id.edit_medication_side_effects);
        TextView medicationAmountInput = root.findViewById(R.id.edit_medication_amount);
        TextView medicationReminderCountInput = root.findViewById(R.id.edit_medication_threshold);
        RadioGroup takeTimeRadioGroup = root.findViewById(R.id.edit_medication_take_time_radio_group);
        final int morningRadio = R.id.morning_radio;
        final int afternoonRadio = R.id.afternoon_radio;
        final int eveningRadio = R.id.evening_radio;
        Button saveButton = root.findViewById(R.id.save_medication);
        Button deleteButton = root.findViewById(R.id.delete_medication);
        repo = new AppRepository(requireActivity().getApplication());
        sharedPreferences = getActivity().getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        User loggedInUser = updateLoggedInUser();
        Spinner spinner = root.findViewById(R.id.doctor_selection_spinner);
        medicationBeingUpdated = updateMedicationBeingUpdated(EditMedicationFragmentArgs.fromBundle(getArguments()));
        Doctor selectedDoctor = setSelectedDoctor(medicationBeingUpdated.getDoctorId());
        newSelectedDoctor = -1;
        List<Doctor> doctorPickerList = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<Doctor>(getActivity(), android.R.layout.simple_spinner_item, doctorPickerList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        repo.getAllDoctors().observe(getViewLifecycleOwner(), new Observer<List<Doctor>>() {
            @Override
            public void onChanged(List<Doctor> doctorList) {
                for (Doctor doctor : doctorList) {
                    doctorPickerList.add(doctor);
                }
                spinnerAdapter.notifyDataSetChanged();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Doctor doctorSelected = (Doctor)parent.getSelectedItem();
                setNewSelectedDoctorId(doctorSelected.getDoctorId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        medicationNameInput.setText(medicationBeingUpdated.getName());
        medicationDosageInput.setText(medicationBeingUpdated.getDosage());
        medicationSideEffectsInput.setText(medicationBeingUpdated.getSideEffects());
        medicationAmountInput.setText(String.valueOf(medicationBeingUpdated.getAmount()));
        medicationReminderCountInput.setText(String.valueOf(medicationBeingUpdated.getLowThresh()));
        switch (medicationBeingUpdated.getTakeTimeCategory().ordinal()) {
            case 0: {
                takeTimeRadioGroup.check(morningRadio);
            } break;
            case 1: {
                takeTimeRadioGroup.check(afternoonRadio);
            } break;
            case 2: {
                takeTimeRadioGroup.check(eveningRadio);
            } break;
        }
        int spinnerPosition = spinnerAdapter.getPosition(selectedDoctor);
        spinner.setSelection(spinnerPosition);



        saveButton.setOnClickListener(new View.OnClickListener() {
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
                        medicationBeingUpdated.setAmount(amount);
                        medicationBeingUpdated.setDoctorId(doctorId);
                        medicationBeingUpdated.setDosage(dosage);
                        medicationBeingUpdated.setSideEffects(sideEffects);
                        medicationBeingUpdated.setLowThresh(reminder);
                        medicationBeingUpdated.setName(name);
                        medicationBeingUpdated.setTakeTimeCategory(takeTimeCategory);
                        repo.updateMedication(medicationBeingUpdated);
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

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repo.deleteMed(medicationBeingUpdated);
                Navigation.findNavController(root).popBackStack();
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

    public Medication updateMedicationBeingUpdated(EditMedicationFragmentArgs editMedicationFragmentArgs) {
        try {
            return repo.getMedicationById(editMedicationFragmentArgs.getMedicationId()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Doctor setSelectedDoctor(int doctorId) {
        try {
            return repo.getDoctorById(doctorId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setNewSelectedDoctorId(int doctorId) {
        newSelectedDoctor = doctorId;
    }

    public boolean validateData(String name, String dosage, String sideEffects, int amount, int reminder, Medication.TakeTimeCategory takeTimeCategory, User loggedInUser, int doctorId) {
        if (name.isEmpty() || dosage.isEmpty() || sideEffects.isEmpty() || (amount < 0) || (reminder < 0) || (takeTimeCategory == null) || (loggedInUser == null) || (doctorId < 1)){
            return false;
        }
        return true;
    }
}