package com.example.mat.systemmanagement.RoomActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mat.systemmanagement.R;
import com.example.mat.systemmanagement.User.User;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private static final String TAG = "CreateUserActivity";
    private Context context;

    List<User> users;

    public UserAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.ViewHolder holder, final int position) {
        holder.firstName.setText("Name: " + users.get(position).getFirstName() + " " + users.get(position).getLastName());
        holder.email.setText("Email: " + users.get(position).getEmail());
        holder.updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: id: " + users.get(position).getFirstName());
                Intent intent = new Intent(context.getApplicationContext(), UpdateUserActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", String.valueOf(users.get(position).getId()));
                intent.putExtra("name", users.get(position).getFirstName());
                intent.putExtra("lastname", users.get(position).getLastName());
                intent.putExtra("email", users.get(position).getEmail());

                context.getApplicationContext().startActivity(intent);
            }
        });

        holder.deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: id: " + users.get(position).getId());

                int userId = users.get(position).getId();

                User user = new User();
                user.setId(userId);
                RecyclerActivity.appDatabase.userDao().delete(user);
                removeItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView firstName;
        public TextView email;
        public Button deletebutton;
        public Button updatebutton;
        public ViewHolder(View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.first_name);
            email = itemView.findViewById(R.id.email);
            deletebutton = itemView.findViewById(R.id.delBtn);
            updatebutton = itemView.findViewById(R.id.updateBtn);

        }
    }

    public void removeItem(int position) {
        users.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, users.size());
    }
}
