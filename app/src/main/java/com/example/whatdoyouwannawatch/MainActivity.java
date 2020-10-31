package com.example.whatdoyouwannawatch;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.*;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class
MainActivity extends AppCompatActivity {
    // Write info to the Realtime database
    private static final String TAG = "MainActivity";
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = database.getReference();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    static void removeUserFromTheatre(String hostID, final String username){
        myRef = database.getReference();
        pullData('t', hostID, new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if(obj != null) {
                    Theatre t = (Theatre) obj;
                    t.removeUser(username);
                    pushData(t);
                }
            }
        });
    }

    static void removeTheatre(String hostID){
        myRef = database.getReference();
        DatabaseReference tRef =myRef.child("theatres").child(hostID);
        tRef.removeValue();
    }

    static void pushData(Object obj) {
        // A HashMap is used to upload information to firebase, the String is the location in
        // firebase and the Object is the Object to be put in firebase
        HashMap<String, Object> map = new HashMap<>();
        myRef = database.getReference();
        if(obj.getClass().getName().equals("com.example.whatdoyouwannawatch.User")){
            User tmp = (User)obj;
            String u = "users";

            String folder = u + "/" + (tmp).getUsername();
            map.put(folder.toLowerCase(), obj);
        } else if(obj.getClass().getName().equals("com.example.whatdoyouwannawatch.Theatre")){
            Theatre tmp = (Theatre) obj;
            String t = "theatres";

            String folder = t + "/" + (tmp).getHostID();
            map.put(folder.toLowerCase(), obj);
        }
        myRef.updateChildren(map)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("pushData", "Error Adding User/Theatre", e);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.d("pushData", "Successfully added user to firebase");
                    }
                });
    }

    static Object pullData(char type, String id, final DataCallback dcb){
        String t = "theatres";
        String u = "users";
        id = id.toLowerCase().trim();

        if (type == 'u'){
            myRef = database.getReference().child(u).child(id);
            Log.d("pull", myRef.toString());

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user == null) {
                        dcb.onCallback(null);
                    } else {
                        if (user.getUID() == null) {
                            dcb.onCallback(null);
                        } else {
                            dcb.onCallback(user);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting User failed, log a message
                    Log.i("PullData", "Failed to Load User from Firebase", databaseError.toException());
                }
            });
        } else if (type == 't'){
            myRef = database.getReference().child(t).child(id);
            Log.d("pull", myRef.toString());
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("pull", "theatre datasnapshot: " + dataSnapshot.toString());
                    Theatre theatre = dataSnapshot.getValue(Theatre.class);

                    if (theatre == null) {
                        dcb.onCallback(null);
                    } else {
                        if (theatre.getHostID() == null) {
                            dcb.onCallback(null);
                        } else {
                            dcb.onCallback(theatre);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting User failed, log a message
                    Log.i("PullData", "Failed to Load Theatre from Firebase", databaseError.toException());
                }
            });
        }
        return null;
    }

    public void onClickLogIn(View v) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void onClickSignUp(View v) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    // This is a method to asynchronously call our API, Entertainment Data Hub on RapidAPI,
    // https://rapidapi.com/IVALLC/api/entertainment-data-hub and wait for a response
    // To implement this method, I need to use a callback function
    public static void apiCallSearch(String genres, String providers , final ApiCallback acb)   throws IOException {

        OkHttpClient client = new OkHttpClient(); //A client for networking with the Api online
        //Log.d("search", "Title: " + title );
        genres = (genres.toLowerCase()).replaceAll("\\s+","");
        providers = (providers.toLowerCase()).replaceAll("\\s+","");

        Request.Builder builder = new Request.Builder();
        builder.url("https://ivaee-internet-video-archive-entertainment-v1.p.rapidapi.com/entertainment/search/");
        builder.get();
        builder.addHeader("x-rapidapi-host", "ivaee-internet-video-archive-entertainment-v1.p.rapidapi.com");
        builder.addHeader("x-rapidapi-key", "0781c4e67fmsh14845fdab783a92p1a799ejsna0098cb737dd");
        builder.addHeader("content-type", "application/json");
        builder.addHeader("Providers", providers);
        builder.addHeader("Genres", genres);
        builder.addHeader("sortby", "Relevance");// This is the query we build
//Program Types
        Request request = builder //Options: Relevance, Timestamp, IvaRating, ReleaseDate
                .build();

        Log.d("search", request.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //A Response has a headers and a body
                //Headers just contain info or metadata about the response like number of calls left for the free trial
                // or the Access control methods allowed like GET, POST, PUT, etc

                //The body has all of the data about the shows and movies found, if any..
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
//                    Headers responseHeaders = response.headers();
//                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                        Log.d("search",responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }

                    //Here is where we get the query results
                    String results = responseBody.string();
                    try {
                        acb.onCallback(results);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }catch (IOException i){
                    i.printStackTrace();
                }
            }
        });
    }

    public static void apiCallImage(String path , final ApiCallback acb)   throws IOException {
        OkHttpClient client = new OkHttpClient(); //A client for networking with the Api online
        //Log.d("search", "Title: " + title );
        Request request = new Request.Builder() // This is the query we build
                .url("https://ivaee-internet-video-archive-entertainment-v1.p.rapidapi.com/Images/%7Bfilepath%7D/Redirect?Redirect=false")
                .get()
                .addHeader("x-rapidapi-host", "ivaee-internet-video-archive-entertainment-v1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "0781c4e67fmsh14845fdab783a92p1a799ejsna0098cb737dd")
                .addHeader("accept", "application/json")
                .addHeader("filepath", path) //String title
                .addHeader("providers", "Netflix,Hulu,AmazonPrimeVideo,HBO,GooglePlay,iTunes")
                .addHeader("expirationminutes", "Relevance") //Options: Relevance, Timestamp, IvaRating, ReleaseDate
                .build();

        Log.d("search", request.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //A Response has a headers and a body
                //Headers just contain info or metadata about the response like number of calls left for the free trial
                // or the Access control methods allowed like GET, POST, PUT, etc

                //The body has all of the data about the shows and movies found, if any..
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
//
                    //Here is where we get the query results
                    String results = responseBody.string();
                    try {
                        acb.onCallback(results);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }catch (IOException i){
                    i.printStackTrace();
                }
            }
        });
    }


}