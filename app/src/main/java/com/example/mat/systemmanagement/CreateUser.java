package com.example.mat.systemmanagement;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateUser extends AppCompatActivity{

    private static final String TAG = "CreateUser";

    private TextView id;
    EditText firstName, lastName, email;
    Button addButton, cancelButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);

        id = (TextView)findViewById(R.id.id);
        firstName = (EditText)findViewById(R.id.first_name);
        lastName = (EditText)findViewById(R.id.last_name);
        email = (EditText)findViewById(R.id.email);
        addButton = (Button)findViewById(R.id.saveBtn);
        cancelButton = (Button)findViewById(R.id.cancelBtn);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 5/21/18 Save to database
                Log.d(TAG, "onClick: firstName: " + firstName.getText().toString());

                int userid = Integer.parseInt(id.getText().toString());
                String name = firstName.getText().toString();
                String lastnm = lastName.getText().toString();
                String eml = email.getText().toString();

                User user = new User();
                user.setId(userid);
                user.setFirstName(name);
                user.setLastName(lastnm);
                user.setEmail(eml);

                RecyclerActivity.appDatabase.userDao().insertAll(user);

                Intent intent = new Intent(CreateUser.this, RecyclerActivity.class);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateUser.this, RecyclerActivity.class);
                startActivity(intent);
            }
        });
    }
}
