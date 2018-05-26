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

import com.example.mat.systemmanagement.FireBaseAccountActivity.UserInterface.AdminUI.AdminRegistrationActivity;
import com.example.mat.systemmanagement.FireBaseAccountActivity.UserInformation.UserPermission;
import com.example.mat.systemmanagement.R;
import com.example.mat.systemmanagement.RoomActivities.RecyclerActivity;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManagerProfileActivity extends AppCompatActivity {

    private static final String TAG = "UserPermission";

    private TextView emailTv;
    private Button customerBtn, registerBtn, signoutBtn;
    public static Button calculateBtn;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_profile);
        Firebase.setAndroidContext(this);

        customerBtn = (Button)findViewById(R.id.customerBtn);
        emailTv = (TextView)findViewById(R.id.emailTv);
        calculateBtn = (Button)findViewById(R.id.calcBtn);
        registerBtn = (Button)findViewById(R.id.createBtn);
        signoutBtn = (Button)findViewById(R.id.signoutBtn);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        UserPermission userPermission = new UserPermission(ManagerProfileActivity.this);
        emailTv.setText(userPermission.getName());

        if (user != null) {
            // signed in
            userId = user.getUid();
        } else {
            Log.e(TAG, "You aren't logged in");
            finishActivity(0);
        }
        myRef = FirebaseDatabase.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "Listener is Connected to the Database" + myRef);
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    finish();
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

        customerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerProfileActivity.this, RecyclerActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerProfileActivity.this, ManagerRegistrationActivity.class);
                startActivity(intent);
            }
        });

        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getInstance().signOut();
            }
        });
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
