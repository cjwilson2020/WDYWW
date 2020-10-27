package com.example.whatdoyouwannawatch;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.*;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class MainActivity extends AppCompatActivity {
    // Write info to the Realtime database
    private static final String TAG = "MainActivity";
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = database.getReference("users");
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");



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

    static void pushData(User user) {
        // A HashMap is used to upload information to firebase, the String is the location in
        // firebase and the Object is the SongQueue to be put in firebase
        HashMap<String, Object> map = new HashMap<>();
        String folder = "/users/" + user.getUID();
        map.put(folder, user);
        myRef.updateChildren(map)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("pushData", "Error Adding User", e);
                    }
                });
    }

    static void pullData(final DataCallback dbc, final String uid) {
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
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        //TODO Implement once FireBase is set up
    }

    public void onClickSignUp(View v) {
        //TODO Implement once FireBase is set up
    }

    //This is a method that I am creating to asynchronously call our API and wait for a response
    //To implement this method, I need to use a callback function

    public static void apiCallSearch(String title, final ApiCallback acb) throws IOException {
        OkHttpClient client = new OkHttpClient(); //A client for networking with the Api online

        Request request = new Request.Builder()
                .url("https:\\\\ivaee-internet-video-archive-entertainment-v1.p.rapidapi.com\\entertainment\\search\\")
                .get()
                .addHeader("x-rapidapi-host", "ivaee-internet-video-archive-entertainment-v1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "0781c4e67fmsh14845fdab783a92p1a799ejsna0098cb737dd")
                .addHeader("content-type", "application/json")
                .addHeader("title", title) //String title
                .addHeader("sortby", "Relevance") //Options: Relevance, Timestamp, IvaRating, ReleaseDate
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }
                    //Here is where I get the query results
                    String results = responseBody.string();
                    try {
                        acb.onCallback(results);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}