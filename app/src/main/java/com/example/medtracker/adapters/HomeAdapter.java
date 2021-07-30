package com.example.medtracker.adapters;

import android.app.Application;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medtracker.AppRepository;
import com.example.medtracker.R;
import com.example.medtracker.models.Medication;
import com.example.medtracker.models.Record;
import com.example.medtracker.view_models.MedicationViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<Medication> medicationList;
    private final List<Medication> medicationListFull;
    private final MedicationViewModel medicationViewModel;
    private final AppRepository repo;


    public HomeAdapter(List<Medication> dataset, Application application) {
        this.medicationList = dataset;
        medicationListFull = new ArrayList<>(dataset);
        medicationViewModel = new MedicationViewModel(application);
        repo = new AppRepository(application);
    }

    public void setMedicationList(List<Medication> medicationList) {
        this.medicationList = medicationList;
        medicationListFull.addAll(this.medicationList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final CheckBox medicationCheckBox;
        public ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.medication_name);
            medicationCheckBox = view.findViewById(R.id.medication_checkbox);
        }
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_medication_card, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        Medication item = medicationList.get(position);
        holder.name.setText(item.getName());
        holder.medicationCheckBox.setChecked(item.isTaken());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M - d - uuuu");

        holder.medicationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    medicationViewModel.decrementMedCount(item.getMedicationId());
                    medicationViewModel.updateTakenStatus(true, item.getMedicationId());
                    Record record = null;
                    try {
                        if (repo.getRecordByDate(LocalDateTime.now().format(dtf), item.getUserId()) != null) {
                            record = repo.getRecordByDate(LocalDateTime.now().format(dtf), item.getUserId());
                        } else {
                            record = new Record(LocalDateTime.now().format(dtf), item.getUserId());
                            repo.addRecord(record);
                        }
                        switch (item.getTakeTimeCategory()) {
                            case MORNING: {
                                record.setMorningMedicationsTaken(record.getMorningMedicationsTaken().concat(" " + item.getName()));
                            } break;
                            case AFTERNOON: {
                                record.setAfternoonMedicationsTaken(record.getAfternoonMedicationsTaken().concat(" " + item.getName()));
                            } break;
                            case EVENING: {
                                record.setEveningMedicationsTaken(record.getEveningMedicationsTaken().concat(" " + item.getName()));
                            } break;
                        }
                        repo.updateRecord(record);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    medicationViewModel.incrementMedCount(item.getMedicationId());
                    medicationViewModel.updateTakenStatus(false, item.getMedicationId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    public void filter(String text) {
        medicationList.clear();
        if (text.isEmpty()) {
            medicationList.addAll(medicationListFull);
        } else {
            text = text.toLowerCase();
            for (Medication item : medicationListFull) {
                if (item.getName().toLowerCase().contains(text)) {
                    medicationList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

}
