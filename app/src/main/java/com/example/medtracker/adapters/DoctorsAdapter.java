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
import com.example.medtracker.fragments.DoctorsFragmentDirections;
import com.example.medtracker.models.Doctor;

import java.util.ArrayList;
import java.util.List;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {

    private List<Doctor> dataset;
    private Context appContext;

    public DoctorsAdapter(List<Doctor> dataset, Context appContext) {
        this.dataset = dataset;
        this.appContext = appContext;
    }

    public void setDoctors(List<Doctor> doctorList) {
        this.dataset = doctorList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView doctorName;
        private final TextView doctorPhone;
        private final CardView doctorCard;
        public ViewHolder(View view) {
            super(view);

            doctorName = view.findViewById(R.id.doctor_name);
            doctorPhone = view.findViewById(R.id.doctor_phone);
            doctorCard = view.findViewById(R.id.doctor_card_item);
        }
    }

    @NonNull
    @Override
    public DoctorsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DoctorsAdapter.ViewHolder holder, int position) {
        holder.doctorName.setText(dataset.get(position).getFirst() + " " + dataset.get(position).getLast());
        holder.doctorPhone.setText(dataset.get(position).getPhone());

        holder.doctorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorsFragmentDirections.ActionDoctorsFragmentToEditDoctorFragment action = DoctorsFragmentDirections.actionDoctorsFragmentToEditDoctorFragment(dataset.get(position).getDoctorId());
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
