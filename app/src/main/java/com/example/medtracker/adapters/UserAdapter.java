package com.example.medtracker.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.example.medtracker.R;
import com.example.medtracker.fragments.SignInDialogFragment;
import com.example.medtracker.models.User;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import static com.example.medtracker.R.*;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> userList;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    public void updateUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout.user_card_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User item = userList.get(position);
        String name = item.getFirst() + " " + item.getLast();
        holder.userName.setText(name);
        holder.itemView.setActivated(selectedPosition == position);


        holder.userCardView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedPosition);
                selectedPosition = holder.getLayoutPosition();
                notifyItemChanged(selectedPosition);
                SignInDialogFragment.setSelectedUser(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView userName;
        private final MaterialCardView userCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(id.user_name);
            userCardView = itemView.findViewById(id.user_card_view);

        }
    }
}
