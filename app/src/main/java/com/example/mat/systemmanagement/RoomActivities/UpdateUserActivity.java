package com.example.mat.systemmanagement.RoomActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mat.systemmanagement.R;
import com.example.mat.systemmanagement.User.User;

public class UpdateUserActivity extends AppCompatActivity {
    private static final String TAG = "CreateUserActivity";
    EditText firstName, lastName, email;
    Button updateBtn, cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        firstName = (EditText)findViewById(R.id.first_name);
        lastName = (EditText)findViewById(R.id.last_name);
        email = (EditText)findViewById(R.id.email);
        updateBtn = (Button)findViewById(R.id.updateBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);

        final String id = getIntent().getExtras().getString("id");
        String fname = getIntent().getExtras().getString("name");
        String lname = getIntent().getExtras().getString("lastname");
        String emal = getIntent().getExtras().getString("email");

        firstName.setText(fname);
        lastName.setText(lname);
        email.setText(emal);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = firstName.getText().toString();
                String lastname = lastName.getText().toString();
                String eml = email.getText().toString();

                User user = new User();
                user.setId(Integer.parseInt(id));
                user.setFirstName(name);
                user.setLastName(lastname);
                user.setEmail(eml);

                RecyclerActivity.appDatabase.userDao().updateUser(user);

                Intent intent = new Intent(UpdateUserActivity.this, RecyclerActivity.class);
                startActivity(intent);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateUserActivity.this, RecyclerActivity.class);
                startActivity(intent);
            }
        });

    }
}
