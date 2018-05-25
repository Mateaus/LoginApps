package com.example.mat.systemmanagement.FireBaseAccountActivity.LogInSystemActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mat.systemmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RetrievePasswordActivity extends AppCompatActivity {

    EditText retrievemailEt;
    Button retrievepasswordButton, cancelButton;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_password);

        retrievemailEt = (EditText)findViewById(R.id.retrievemailEt);
        retrievepasswordButton = (Button)findViewById(R.id.retrievepasswordBtn);
        cancelButton = (Button)findViewById(R.id.cancelBtn);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.GONE);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RetrievePasswordActivity.this, MainLoginActivity.class);
                startActivity(intent);
            }
        });

        retrievepasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = retrievemailEt.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RetrievePasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RetrievePasswordActivity.this, MainLoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RetrievePasswordActivity.this, "Failed to send reset instructions to email", Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                });


            }
        });

    }
}
