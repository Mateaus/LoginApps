package com.example.mat.systemmanagement.FireBaseAccountActivity.UserInformation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.mat.systemmanagement.FireBaseAccountActivity.UserInterface.AdminUI.AdminProfileActivity;
import com.example.mat.systemmanagement.FireBaseAccountActivity.UserInterface.ManagerUI.ManagerProfileActivity;
import com.example.mat.systemmanagement.FireBaseAccountActivity.UserInterface.WorkerUI.WorkerProfileActivity;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class UserPermission extends AppCompatActivity{

    private static final String TAG = "UserPermission";

    private static Context context;

    private static String name;

    @Override
    protected void onCreate(Bundle saveInstaceState) {
        super.onCreate(saveInstaceState);
        Firebase.setAndroidContext(this);


    }

    public UserPermission(Context c){
        context = c;
    }

    public void getUserPermission(String userId) {

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("role");

        myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String myString = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "onClick: id: " + myString);

                    if (myString.equalsIgnoreCase("Admin")){
                        Log.d(TAG, "You are logged or redirected to the Admin user interface");
                        Intent intent = new Intent(context.getApplicationContext(), AdminProfileActivity.class);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else if (myString.equalsIgnoreCase("Manager")) {
                        Log.d(TAG, "You are logged or redirected to the Manager user interface");
                        Intent intent = new Intent(context.getApplicationContext(), ManagerProfileActivity.class);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else if (myString.equalsIgnoreCase("Worker")){
                        Log.d(TAG, "You are logged or redirected to the Worker user interface");
                        Intent intent = new Intent(context.getApplicationContext(), WorkerProfileActivity.class);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else {
                        Toast.makeText(context, "Unauthorized person!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
