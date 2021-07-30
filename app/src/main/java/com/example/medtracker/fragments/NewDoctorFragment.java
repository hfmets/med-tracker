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

public class NewDoctorFragment extends Fragment {

    public NewDoctorFragment() {
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
        View root = inflater.inflate(R.layout.fragment_new_doctor, container, false);

        // Set up variables needed
        AppRepository repo = new AppRepository(requireActivity().getApplication());
        TextView newDoctorFirstNameInput = root.findViewById(R.id.new_doctor_first_name);
        TextView newDoctorLastNameInput = root.findViewById(R.id.new_doctor_last_name);
        TextView newDoctorAddressInput = root.findViewById(R.id.new_doctor_address);
        TextView newDoctorPhoneInput = root.findViewById(R.id.new_doctor_phone_number);
        Button addDoctorButton = root.findViewById(R.id.add_doctor_btn);

        addDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstNameText = newDoctorFirstNameInput.getText().toString();
                String lastNameText = newDoctorLastNameInput.getText().toString();
                String addressText = newDoctorAddressInput.getText().toString();
                String phoneText = newDoctorPhoneInput.getText().toString();

                if (validateData(firstNameText, lastNameText, addressText, phoneText)) {
                    Doctor newDoc = new Doctor(firstNameText, lastNameText, addressText, phoneText);
                    repo.insertDoctor(newDoc);
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

    public boolean validateData(String first, String last, String address, String phone) {
        if (first.isEmpty() || last.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            return false;
        }
        return true;
    }
}