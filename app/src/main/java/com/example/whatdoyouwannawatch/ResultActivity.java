package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    FirebaseUser fbUser;
    String theatreID;
    private static ArrayList<Media> mediaList = new ArrayList<Media>(5);
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = database.getReference();
    ImageView resultImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        resultImg = findViewById(R.id.result_poster);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        Bundle extras = getIntent().getExtras();
        if (extras != null) { //extra passed into this
            mediaList = (ArrayList<Media>) extras.getSerializable("mediaList");
            theatreID = extras.getString("theatreID");
        }
         waitForResult();
    }

    private void waitForResult() {
        myRef = database.getReference();
        DatabaseReference tRef =myRef.child("theatres").child(theatreID).child("result");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshotRes = dataSnapshot.child("result");

                for (DataSnapshot valueRes : dataSnapshotRes.getChildren()){
                    Result r =dataSnapshot.getValue(Result.class);
                    TextView displayTitle = findViewById(R.id.textView19);

                    displayTitle.setText(r.getFinalDecision().getTitle());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    public void onClickDone(View v){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        MainActivity.pullData('u', fbUser.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if (obj != null) {
                    User u = (User) obj;
                    Log.i("Guest", u.getUsername());
                    Log.i("Guest", Boolean.toString(u.isGuest()));
                    if (u.isGuest()) {
                        // delete user
                        MainActivity.pullData('u', fbUser.getDisplayName(), new DataCallback() {
                            @Override
                            public void onCallback(Object obj) {
                                if (obj != null) {
                                    User u = (User) obj;
                                    MainActivity.deleteData(u);
                                }
                            }
                        });
                        //Delete guest in FB Auth
                        FirebaseAuth.getInstance().getCurrentUser().delete();

                        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(ResultActivity.this, UserHomeActivity.class);
                        startActivity(intent);
                    }
                }
                else{
                    Log.i("Guest", "Guest is null");
                }
            }
        });


        // TODO add result to history
    }

    public void onClickCalcResult(View v){
        if(fbUser.getDisplayName().equals(theatreID)) {
            MainActivity.pullData('t', theatreID, new DataCallback() {
                @Override
                public void onCallback(Object obj) {
                    if(obj!= null){
                        Theatre t = (Theatre)obj;
                        BackStage b = new BackStage(t);
                        b.calcResult(mediaList);
                        MainActivity.pushData(t);
                        Log.i("User", "About to update");
                        updateWatchHistories(t);

                        TextView displayTitle = findViewById(R.id.textView19);
                        displayTitle.setText(t.getResult().getFinalDecision().getTitle());

                        URL url = null;
                        if(t.getResult().getFinalDecision().getPoster() != null) {
                            try {
                                url = new URL(t.getResult().getFinalDecision().getPoster().toString());
                                if (url != null) {
                                    HttpURLConnection connection = null;
                                    connection = (HttpURLConnection) url.openConnection();
                                    connection.setDoInput(true);
                                    connection.connect();
                                    InputStream input = connection.getInputStream();
                                    Bitmap poster = BitmapFactory.decodeStream(input);
                                    resultImg.setImageBitmap(poster);
                                } else {
                                    Log.d("result", "No poster available for this result.");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }

    private void updateWatchHistories(Theatre theatre){
        final Result result = theatre.getResult();
        ArrayList<User> users = (ArrayList<User>) theatre.getUsers();
        for (User u : users) {
            MainActivity.pullData('u', u.getUsername(), new DataCallback() {
                @Override
                public void onCallback(Object obj) {
                    if (obj != null) {
                        User us = (User)obj;
                        if (us.getHistory() == null || us.getHistory().size() < 1) {
                            ArrayList<Result> history = new ArrayList<Result>();
                            history.add(result);
                            us.setHistory(history);
                        } else {
                            us.addHistory(result);
                        }
                        MainActivity.pushData(us);
                    }
                }
            });
            //Toast.makeText(ResultActivity.this,"User History: " + u.getHistory().toString(), Toast.LENGTH_SHORT).show();

        }
    }
}