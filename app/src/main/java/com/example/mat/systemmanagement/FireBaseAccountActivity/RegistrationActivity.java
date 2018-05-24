package com.example.mat.systemmanagement.FireBaseAccountActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mat.systemmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "AddUserstoDatabase";

    private EditText nameregEt, passwordregEt, emailregEt, phoneregEt;
    private Button registerBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        nameregEt = (EditText)findViewById(R.id.nameregEt);
        emailregEt = (EditText)findViewById(R.id.emailregEt);
        passwordregEt = (EditText)findViewById(R.id.passwordregEt);
        phoneregEt = (EditText)findViewById(R.id.phoneregEt);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            userID = user.getUid();
        }


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Added information to database: \n" + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user != null){
                    String name = nameregEt.getText().toString();
                    String email = emailregEt.getText().toString();
                    String password = passwordregEt.getText().toString();
                    String phoneNum = phoneregEt.getText().toString();

                    Log.d(TAG, "onClick: Attempting to submit to database \n" +
                            "name: " + name + "\n" +
                            "email: " + email + "\n" +
                            "phone number: " + phoneNum + "\n"
                    );
                    // handle the exceptions if the EditText fields are null
                    if (!name.equals("") && !email.equals("") && !phoneNum.equals("")){
                        UserInformation userInformation = new UserInformation(name, email, phoneNum);
                        myRef.child("users").child(userID).push().setValue(userInformation);
                        Toast.makeText(RegistrationActivity.this,"New Information has been saved.", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(RegistrationActivity.this, "Fill out all the fields", Toast.LENGTH_SHORT).show();
                    }

                }

                final ProgressDialog progressDialog = ProgressDialog.show(RegistrationActivity.this, "Please wait...", "Processing...", true);
                (mAuth.createUserWithEmailAndPassword(emailregEt.getText().toString(), passwordregEt.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            Toast.makeText(RegistrationActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                            sendVerificationEmail();
                            finishActivity(0);
                            /*Intent intent = new Intent(RegistrationActivity.this, MainLoginActivity.class);
                            startActivity(intent);*/
                        } else {
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this, "Email verification sent", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(RegistrationActivity.this, MainLoginActivity.class);
                    finish();
                } else {
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0 , 0);
                    startActivity(getIntent());
                }
            }
        });
    }
}