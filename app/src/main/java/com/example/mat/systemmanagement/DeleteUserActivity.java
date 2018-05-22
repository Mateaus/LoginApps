package com.example.mat.systemmanagement;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DeleteUserActivity extends AppCompatActivity {

    EditText enterId;
    Button deleteBtn, cancelBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        enterId = (EditText)findViewById(R.id.idEt);
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    int id = Integer.parseInt(enterId.getText().toString());
                    User user = new User();
                    user.setId(id);

                    RecyclerActivity.appDatabase.userDao().delete(user);

                    Toast.makeText(getApplication(), "User successfully removed..", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DeleteUserActivity.this, RecyclerActivity.class));

                } catch (NumberFormatException e) {
                    Toast.makeText(getApplication(), "Input is not a number", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeleteUserActivity.this, RecyclerActivity.class));
            }
        });
    }
}
