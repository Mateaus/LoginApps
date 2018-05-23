package com.example.mat.systemmanagement.FireBaseAccountActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mat.systemmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainLoginActivity extends AppCompatActivity {

    private EditText emailEt, passwordEt;
    private Button loginBtn, registrationBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEt = (EditText)findViewById(R.id.emailEt);
        passwordEt = (EditText)findViewById(R.id.passwordEt);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        registrationBtn = (Button)findViewById(R.id.registrationBtn);
        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = ProgressDialog.show(MainLoginActivity.this, "Please wait...", "Processing...", true);

                (mAuth.signInWithEmailAndPassword(emailEt.getText().toString(), passwordEt.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()){
                            Toast.makeText(MainLoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainLoginActivity.this, ProfileActivity.class);
                            intent.putExtra("Email", mAuth.getCurrentUser().getEmail());
                            startActivity(intent);
                        } else {
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(MainLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainLoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }


}
