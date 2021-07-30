package com.example.medtracker.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medtracker.R;
import com.example.medtracker.fragments.MedsFragmentDirections;
import com.example.medtracker.models.Medication;
import com.example.medtracker.models.MedicationWithDoctor;

import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {
    private List<MedicationWithDoctor> dataset;

    public MedicationAdapter(List<MedicationWithDoctor> dataset, Context appContext) {
        this.dataset = dataset;
    }

    public void setMedications(List<MedicationWithDoctor> medicationList) {
        this.dataset = medicationList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView medicationName;
        private final TextView doctorName;
        private final CardView medicationCard;
        private final ImageView editButton;
        public ViewHolder(View view) {
            super(view);

            medicationName = view.findViewById(R.id.medication_name);
            doctorName = view.findViewById(R.id.med_card_doctor_name);
            medicationCard = view.findViewById(R.id.medication_card);
            editButton = view.findViewById(R.id.edit_icon);
        }
    }

    @NonNull
    @Override
    public MedicationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medication_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MedicationAdapter.ViewHolder holder, int position) {
        holder.medicationName.setText(dataset.get(position).medication.getName());
        holder.doctorName.setText(dataset.get(position).doctor.getFirst() + " " + dataset.get(position).doctor.getLast());
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedsFragmentDirections.ActionMedsFragmentToEditMedicationFragment action = MedsFragmentDirections.actionMedsFragmentToEditMedicationFragment(dataset.get(position).medication.getMedicationId());
                Navigation.findNavController(holder.itemView).navigate(action);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataset != null) {
            return dataset.size();
        }
        return 0;
    }
}
