package com.example.whatdoyouwannawatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.*;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    // Write info to the Realtime database
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Writing to Realtime Database
        myRef.setValue("123"); //value pair for "key"

        //We use a callback function if we are in a different Activity class
        //Steps are as follows
        /*
          1. Define the methods in an interface that we want to invoke after callback.
          2. Define a class that will implement the callback methods of the interface.
          3. Define a reference in other class to register the callback interface.
          4. Use that reference to invoke the callback method.
         */
        // 1. Look at DataCallback interface class
        // 2. This is the class that will implement the callback method
        //     Refer to the pullData function in this class
        // 3. The reference will be in UserHomeActivity
        // 4. Look at UserHomeActivity
    }

    static void pullData(final DataCallBack dbc, final String uid) {
        // Somehow this allows the action String to work as intended
        DatabaseReference qReference = database.getReference();
        Log.i("Fetch", qReference.toString());

        qReference.child(String.format("users/%s", uid));
        qReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                dbc.onCallback(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting User failed, log a message
                Log.i("PullData", "Failed to Load User from Firebase", databaseError.toException());
            }
        });
        // Use the uID to access the correct user
        //   return null;
    }


    public void onClickWatchNow(View v) {
        Intent intent = new Intent(MainActivity.this, CreateJoinTheatre.class);
        startActivity(intent);
    }

    public void onClickLogIn(View v) {
        //TODO Implement once FireBase is set up
    }

    public void onClickSignUp(View v) {
        //TODO Implement once FireBase is set up
    }
}