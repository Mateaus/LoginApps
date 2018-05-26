package com.example.mat.systemmanagement.FireBaseAccountActivity.UserInterface.ManagerUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mat.systemmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManagerConfirmingAccount extends AppCompatActivity {

    private static final String TAG = "MainLoginActivity";

    private EditText emailEt, passwordEt;
    private Button loginBtn;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    private String name, email, password, phone, role;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_confirming_account);

        emailEt = (EditText)findViewById(R.id.emailReg);
        passwordEt = (EditText)findViewById(R.id.passwordReg);
        loginBtn = (Button)findViewById(R.id.loginregBtn);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        password = intent.getStringExtra("password");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        role = intent.getStringExtra("role");

        emailEt.setText(email);
        passwordEt.setText(password);

        if (user != null) {
            // signed in
            userId = user.getUid();
        } else {
            Log.e(TAG, "You aren't logged in");
            finish();
        }
        myRef = FirebaseDatabase.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.d(TAG, "Listener is Connected to the Database" + myRef);
                if (user != null ) {
                    user.isEmailVerified();
                } else {
                    FirebaseAuth.getInstance().signOut();
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "Listener is Connected to the Database");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Listener was disconnected from the Database");
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                (mAuth.signInWithEmailAndPassword(emailEt.getText().toString(), passwordEt.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "THIS IS INSIDER HERE: " + userId);
                            Intent intent = new Intent(ManagerConfirmingAccount.this, ManagerAuthenticationActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("password", password);
                            intent.putExtra("email", email);
                            intent.putExtra("phone", phone);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        } else {
                            Log.e("ERROR", task.getException().toString());
                        }
                    }
                });

            }
        });
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
}
