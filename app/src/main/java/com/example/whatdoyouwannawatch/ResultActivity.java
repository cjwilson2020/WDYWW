package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    FirebaseUser fbUser;
    String theatreID;
    private static ArrayList<Media> mediaList = new ArrayList<Media>(5);
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = database.getReference();
    ImageView resultImg;
    ProgressDialog p;

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
        if(theatreID!= null && !theatreID.equalsIgnoreCase(fbUser.getDisplayName())){
            Button button = (Button) findViewById(R.id.calcResultButton);
            button.setVisibility(View.GONE);
            TextView displayTitle = findViewById(R.id.textView19);
            displayTitle.setText("Please wait for the result to be calculated");
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
                        /*
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
                        */
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
        p = new ProgressDialog(ResultActivity.this);
        p.setMessage("Getting Media details...");
        p.setCancelable(false);
        p.show();
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

                        Media m = t.getResult().getFinalDecision();
                        try {
                            MainActivity.apiCallImage(m.getPoster(), new ApiCallback() {
                                @Override
                                public void onCallback(final Bitmap result) throws JSONException, IOException {
                                    if (result != null) {
                                        Log.d("search", "Image found, downloading from API");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                resultImg.setImageBitmap(result);
                                                p.dismiss();
                                            }
                                        });
                                    } else {
                                        Log.d("search", "No image downloaded");
                                        p.dismiss();
                                    }
                                }
                            });
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
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
                            ArrayList<String> history = new ArrayList<String>();
                            history.add(result.getFinalDecision().getTitle());
                            us.setHistory(history);
                        } else {
                            us.addHistory(result.getFinalDecision().getTitle());
                        }
                        MainActivity.pushData(us);
                    }
                }
            });
            //Toast.makeText(ResultActivity.this,"User History: " + u.getHistory().toString(), Toast.LENGTH_SHORT).show();

        }
    }
}