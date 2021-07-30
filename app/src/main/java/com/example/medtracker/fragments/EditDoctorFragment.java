package com.example.medtracker.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medtracker.AppRepository;
import com.example.medtracker.R;
import com.example.medtracker.models.Doctor;

import java.util.concurrent.ExecutionException;


public class EditDoctorFragment extends Fragment {

    public AppRepository repo;

    public EditDoctorFragment() {
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
        View root = inflater.inflate(R.layout.fragment_edit_doctor, container, false);

        // Set up variables needed
        TextView firstName = root.findViewById(R.id.edit_doctor_first_name);
        TextView lastName = root.findViewById(R.id.edit_doctor_last_name);
        TextView addressText = root.findViewById(R.id.edit_doctor_address);
        TextView phoneText = root.findViewById(R.id.edit_doctor_phone_number);
        Button saveButton = root.findViewById(R.id.save_doctor_btn);
        Button deleteButton = root.findViewById(R.id.delete_btn);
        repo = new AppRepository(requireActivity().getApplication());
        Doctor doctorBeingUpdated = updateDoctorBeingUpdated(EditDoctorFragmentArgs.fromBundle(getArguments()));

        firstName.setText(doctorBeingUpdated.getFirst());
        lastName.setText(doctorBeingUpdated.getLast());
        addressText.setText(doctorBeingUpdated.getAddress());
        phoneText.setText(doctorBeingUpdated.getPhone());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first = firstName.getText().toString();
                String last = lastName.getText().toString();
                String address = addressText.getText().toString();
                String phone = phoneText.getText().toString();


                if (validateData(first, last, address, phone)) {
                    doctorBeingUpdated.setFirst(firstName.getText().toString());
                    doctorBeingUpdated.setLast(lastName.getText().toString());
                    doctorBeingUpdated.setAddress(addressText.getText().toString());
                    doctorBeingUpdated.setPhone(phoneText.getText().toString());
                    repo.updateDoctor(doctorBeingUpdated);
                    Navigation.findNavController(root).popBackStack();
                } else {
                    String toastMessage = "Something is wrong with your form!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getContext(), toastMessage, duration);
                    toast.show();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repo.deleteDoctor(doctorBeingUpdated);
                Navigation.findNavController(root).popBackStack();
            }
        });





        return root;
    }

    public Doctor updateDoctorBeingUpdated(EditDoctorFragmentArgs editDoctorFragmentArgs) {
        try {
            return repo.getDoctorById(editDoctorFragmentArgs.getDoctorId()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean validateData(String first, String last, String address, String phone) {
        if (first.isEmpty() || last.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            return false;
        }
        return true;
    }
}