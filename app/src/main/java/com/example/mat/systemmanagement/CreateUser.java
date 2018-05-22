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

public class CreateUser extends AppCompatActivity{

    private static final String TAG = "CreateUser";

    EditText firstName, lastName, email;
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);

        firstName = (EditText)findViewById(R.id.first_name);
        lastName = (EditText)findViewById(R.id.last_name);
        email = (EditText)findViewById(R.id.email);
        button = (Button)findViewById(R.id.button);

        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production")
                .allowMainThreadQueries()
                .build();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 5/21/18 Save to database
                Log.d(TAG, "onClick: firstName: " + firstName.getText().toString());

                User user = new User(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString());
                db.userDao().insertAll(user);
                Intent intent = new Intent(CreateUser.this, RecyclerActivity.class);
                startActivity(intent);
            }
        });
    }
}
