package com.example.mat.systemmanagement.FireBaseAccountActivity.UserInformation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.mat.systemmanagement.FireBaseAccountActivity.UserInterface.AdminUI.AdminProfileActivity;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

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

        Log.d(TAG, "THIS IS INSIDER GETUSERPERMISSION FUNCTION! userID: " + userId);
            Firebase myFirebase = new Firebase("https://fir-accountap.firebaseio.com/users/"+userId+"/role");
        Log.d(TAG, "THIS IS INSIDER GETUSERPERMISSION FUNCTION! userID: " + myFirebase);
            myFirebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String myString = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "onClick: id: " + myString);

                    if (myString.equalsIgnoreCase("Admin")){
                        Toast.makeText(context, "Admin Section", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context.getApplicationContext(), AdminProfileActivity.class);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else if (myString.equalsIgnoreCase("Manager")) {
                        Toast.makeText(context, "Manager Section", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context.getApplicationContext(), AdminProfileActivity.class);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else if (myString.equalsIgnoreCase("Worker")){
                        Toast.makeText(context, "Worker Section", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context.getApplicationContext(), AdminProfileActivity.class);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else {
                        Toast.makeText(context, "Unauthorized person!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

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
