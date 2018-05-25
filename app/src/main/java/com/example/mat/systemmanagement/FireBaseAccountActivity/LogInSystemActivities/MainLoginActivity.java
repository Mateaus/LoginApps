package com.example.mat.systemmanagement.FireBaseAccountActivity.LogInSystemActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mat.systemmanagement.FireBaseAccountActivity.UserInformation.UserPermission;
import com.example.mat.systemmanagement.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainLoginActivity extends AppCompatActivity {

    private static final String TAG = "MainLoginActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private EditText emailEt, passwordEt;
    private Button loginBtn, retrievepassBtn;

    private ProgressDialog progressDialog;
    private String email, password;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        emailEt = (EditText)findViewById(R.id.emailEt);
        passwordEt = (EditText)findViewById(R.id.passwordEt);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        retrievepassBtn = (Button)findViewById(R.id.retrievepassBtn);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();


         mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d(TAG, "THIS IS INSIDER adasdasda function! START ");
                    progressDialog = ProgressDialog.show(MainLoginActivity.this, "Please wait...", "Processing...", true, false);
                    (mAuth.signInWithEmailAndPassword(emailEt.getText().toString(), passwordEt.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            Log.d(TAG, "THIS IS INSIDER loginBtn function! START ");
                            if (task.isSuccessful()){
                                String userId = mAuth.getCurrentUser().getUid();
                                Log.d(TAG, "THIS IS INSIDER HERE!!! function! START ");
                                //Intent intent = new Intent(MainLoginActivity.this, AdminProfileActivity.class);
                                checkIfEmailVerified(userId);
                                resetLoginInformation(); // After login is successful, this function will erase the login inputs if someone backtracks back to the login screen.
                            } else {
                                Log.e("ERROR", task.getException().toString());
                                Toast.makeText(MainLoginActivity.this, "Incorrect Email or Password.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(MainLoginActivity.this, "Missing Email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        retrievepassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainLoginActivity.this, RetrievePasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkIfEmailVerified(String userId) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified()) {
            Toast.makeText(MainLoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            UserPermission userPermission = new UserPermission(MainLoginActivity.this);
            RegistrationActivity registrationActivity = new RegistrationActivity();
            userPermission.setName(mAuth.getCurrentUser().getEmail());
            userPermission.getUserPermission(userId);
            /*Intent intent = new Intent(MainLoginActivity.this, AdminProfileActivity.class);
            intent.putExtra("Email", mAuth.getCurrentUser().getEmail());
            startActivity(intent);*/
        } else {
            sendVerificationEmail();
            Toast.makeText(MainLoginActivity.this, "Email not verified, please verify email", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
        }
    }

    private void resetLoginInformation() {

        email = emailEt.getText().toString();
        password = passwordEt.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            emailEt.setText("");
            passwordEt.setText("");
        } else {
            finish();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainLoginActivity.this, "Email verification sent", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
            }
        });
    }
}
