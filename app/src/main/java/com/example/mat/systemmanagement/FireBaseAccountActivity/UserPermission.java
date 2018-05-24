package com.example.mat.systemmanagement.FireBaseAccountActivity;

import android.arch.core.util.Function;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    public void getUserPermission(String user) {

        if (user.equalsIgnoreCase("user")){
            Firebase myFirebase = new Firebase("https://fir-accountap.firebaseio.com/user");

            myFirebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String myString = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "onClick: id: " + myString);

                    if (myString.equalsIgnoreCase("regular")){
                        Toast.makeText(context, "Worked", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context.getApplicationContext(), ProfileActivity.class);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    } else {
                        Toast.makeText(context, "NotWorked", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
