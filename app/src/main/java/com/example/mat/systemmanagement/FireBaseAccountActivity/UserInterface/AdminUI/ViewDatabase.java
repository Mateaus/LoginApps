package com.example.mat.systemmanagement.FireBaseAccountActivity.UserInterface.AdminUI;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;

public class ViewDatabase extends AppCompatActivity {

    private static final String TAG = "ViewDatabase";

    private ListView mListView;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_database);

        mListView = (ListView)findViewById(R.id.listview);

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
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
                Log.d(TAG, "Listener is Connected to the Databaselol1 " + myRef);
                Log.d(TAG, "Listener is Connected to the Databaselol2 " + dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Listener was disconnected from the Database");
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

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showData(DataSnapshot dataSnapshot) {
        Log.d(TAG, "We are inside the information before database is pushed");
        Log.d(TAG, "DataSnapshot : " + dataSnapshot.getKey());
        Log.d(TAG, "DataSnapshot getChildren: " + dataSnapshot.getChildren());
        Log.d(TAG, "DataSnapshot value: " + dataSnapshot.child(userId));
        for (DataSnapshot ds :dataSnapshot.getChildren()) {
            UserInformation uInfo = new UserInformation();
            uInfo.setId(userId);
            uInfo.setName(ds.child(userId).getValue(UserInformation.class).getName());
            uInfo.setEmail(ds.child(userId).getValue(UserInformation.class).getEmail());
            uInfo.setPhone(ds.child(userId).getValue(UserInformation.class).getPhone());
            uInfo.setRole(ds.child(userId).getValue(UserInformation.class).getRole());

            Log.d(TAG, "showData: ID: " + uInfo.getId());
            Log.d(TAG, "showData: name: " + uInfo.getName());
            Log.d(TAG, "showData: email: " + uInfo.getEmail());
            Log.d(TAG, "showData: phone: " + uInfo.getPhone());
            Log.d(TAG, "showData: role: " + uInfo.getRole());

            ArrayList<String> array = new ArrayList<>();
            array.add("UID: " + uInfo.getId());
            array.add("Name: " + uInfo.getName());
            array.add("Email: " + uInfo.getEmail());
            array.add("Phone: " + uInfo.getPhone());
            array.add("Role: " + uInfo.getRole());
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
            mListView.setAdapter(adapter);
        }
    }
}
