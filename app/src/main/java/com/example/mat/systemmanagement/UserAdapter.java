package com.example.mat.systemmanagement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private static final String TAG = "CreateUser";

    List<User> users;
    int number;

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.ViewHolder holder, final int position) {
        //holder.id.setText(String.valueOf(users.get(position).getId())); // if id ever needs to be shown
        //holder.firstName.setText("First Name: " + users.get(position).getFirstName());
        holder.firstName.setText("Name: " + users.get(position).getFirstName() + " " + users.get(position).getLastName());
        //holder.lastName.setText("Last Name: " + users.get(position).getLastName());
        holder.email.setText("Email: " + users.get(position).getEmail());
        holder.deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: id: " + users.get(position).getId());
                //int userId = Integer.parseInt(holder.id.getText().toString()); // if id ever needs to be shown

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
        //public TextView id; // if id ever needs to be shown
        public TextView firstName;
        //public TextView lastName;
        public TextView email;
        public Button deletebutton;
        public ViewHolder(View itemView) {
            super(itemView);
            //id = itemView.findViewById(R.id.id);  // if id ever needs to be shown
            firstName = itemView.findViewById(R.id.first_name);
            //lastName = itemView.findViewById(R.id.last_name);
            email = itemView.findViewById(R.id.email);
            deletebutton = itemView.findViewById(R.id.delBtn);

        }
    }

    public void removeItem(int position) {
        users.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, users.size());
    }
}
