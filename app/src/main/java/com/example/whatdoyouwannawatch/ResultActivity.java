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

import static com.example.whatdoyouwannawatch.MediaDetails.getBitmapFromURL;

public class ResultActivity extends AppCompatActivity {
    private static ImageView resultImg;
    FirebaseUser fbUser;
    String theatreID;
    private static ArrayList<Media> mediaList = new ArrayList<Media>(5);
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = database.getReference();


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
        DatabaseReference tRef = myRef.child("theatres").child(theatreID).child("result");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshotRes = dataSnapshot.child("result");

                for (DataSnapshot valueRes : dataSnapshotRes.getChildren()) {
                    Result r = dataSnapshot.getValue(Result.class);
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

    public void onClickDone(View v) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        MainActivity.pullData('u', fbUser.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if (obj != null) {
                    User u = (User) obj;
                    if (u.isGuest()) {
                        // delete user
                        MainActivity.pullData('u', fbUser.getDisplayName(), new DataCallback() {
                            @Override
                            public void onCallback(Object obj) {
                                if (obj != null) {
                                    User u = (User) obj;
                                    MainActivity.deleteData(u);
                                    MainActivity.removeUserFromTheatre(theatreID, fbUser.getDisplayName());
                                }
                            }
                        });
                        //Delete guest in FB Auth
                        FirebaseAuth.getInstance().getCurrentUser().delete();

                        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        if (theatreID.equalsIgnoreCase(fbUser.getDisplayName())) {
                            MainActivity.pullData('t', fbUser.getDisplayName(), new DataCallback() {
                                @Override
                                public void onCallback(Object theatre) {
                                    if (theatre != null) {
                                        Theatre currTheatre = (Theatre) theatre;
                                        //MainActivity.removeTheatre(currTheatre.getHostID());
                                        MainActivity.deleteData(currTheatre);
                                    }
                                }
                            });
                            Intent intent = new Intent(ResultActivity.this, UserHomeActivity.class);
                            startActivity(intent);
                        } else {
                            MainActivity.pullData('u', fbUser.getDisplayName(), new DataCallback() {
                                @Override
                                public void onCallback(Object obj) {
                                    if (obj != null) {
                                        User u = (User) obj;
                                        MainActivity.removeUserFromTheatre(theatreID, fbUser.getDisplayName());
                                        Intent intent = new Intent(ResultActivity.this, UserHomeActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }

                    }
                } else {
                    Log.i("Guest", "Guest is null");
                }
            }
        });


        // TODO add result to history
    }

    public void onClickCalcResult(View v) {
        if (fbUser.getDisplayName().equals(theatreID)) {
            MainActivity.pullData('t', theatreID, new DataCallback() {
                @Override
                public void onCallback(Object obj) {
                    if (obj != null) {
                        Theatre t = (Theatre) obj;
                        BackStage b = new BackStage(t);
                        b.calcResult(mediaList);
                        MainActivity.pushData(t);
                        Log.i("User", "About to update");
                        updateWatchHistories(t);

                        TextView displayTitle = findViewById(R.id.textView19);
                        displayTitle.setText(t.getResult().getFinalDecision().getTitle());

                        URL url = null;
                        if (t.getResult().getFinalDecision().getPoster() != null) {
                            url = t.getResult().getFinalDecision().getPosterImg();
                            if (url != null) {
                                Log.d("result", url.toString());

                                ResultActivity.resultImg.setImageBitmap(getBitmapFromURL(url.toString()));

                            } else {
                                Log.d("result", "No poster available for this result.");
                            }
                        }
                    }
                }
            });
        }else{
            MainActivity.pullData('t', theatreID, new DataCallback() {
                @Override
                public void onCallback(Object obj) {
                    if(obj!=null){
                        Theatre t = (Theatre) obj;
                        TextView displayTitle = findViewById(R.id.textView19);
                        if(t.getResult().getFinalDecision().getId().equals("x")){
                            displayTitle.setText("Result Not Yet Calculated");
                        }else {
                            displayTitle.setText(t.getResult().getFinalDecision().getTitle());
                        }
                    }

                }
            });

        }
    }

    private void updateWatchHistories(Theatre theatre) {
        final Result result = theatre.getResult();
        ArrayList<User> users = (ArrayList<User>) theatre.getUsers();
        for (User u : users) {
            MainActivity.pullData('u', u.getUsername(), new DataCallback() {
                @Override
                public void onCallback(Object obj) {
                    if (obj != null) {
                        User us = (User) obj;
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