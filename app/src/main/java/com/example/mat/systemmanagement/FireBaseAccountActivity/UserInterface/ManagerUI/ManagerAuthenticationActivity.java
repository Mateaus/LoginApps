package com.example.mat.systemmanagement.FireBaseAccountActivity.UserInterface.ManagerUI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mat.systemmanagement.FireBaseAccountActivity.UserInformation.UserInformation;
import com.example.mat.systemmanagement.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManagerAuthenticationActivity extends AppCompatActivity {

    private static String TAG = "ManagerAuthenticateActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    private TextView confirmname, confirmemail, confirmpassword, confirmphone, confirmrole;
    private Button createBtn;

    private String name, email, password, phone, role;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_authentication);

        confirmname = (TextView) findViewById(R.id.confirmnameEt);
        confirmemail = (TextView)findViewById(R.id.confirmemailEt);
        confirmpassword = (TextView)findViewById(R.id.confirmpassEt);
        confirmphone = (TextView)findViewById(R.id.confirmphoneEt);
        confirmrole = (TextView) findViewById(R.id.confirmroleEt);

        createBtn = (Button)findViewById(R.id.createBtn);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        password = intent.getStringExtra("password");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        role = intent.getStringExtra("role");

        confirmname.setText(name);
        confirmemail.setText(email);
        confirmpassword.setText(password);
        confirmphone.setText(phone);
        confirmrole.setText(role);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


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
                Log.d(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Attempting to submit to database \n" +
                        "name: " + name + "\n" +
                        "email: " + email + "\n" +
                        "phone number: " + phone + "\n" +
                        "role: " + role
                );
                if (!name.equals("") && !email.equals("") && !phone.equals("") && !role.equals("")){
                    UserInformation userInformation = new UserInformation(name, email, phone, role);
                    myRef.child("users").child(userId).setValue(userInformation);
                    Intent intent = new Intent(ManagerAuthenticationActivity.this, ManagerProfileActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(ManagerAuthenticationActivity.this, "Fill out all the fields", Toast.LENGTH_SHORT).show();
                }
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
