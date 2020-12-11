package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Iterator;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    FirebaseUser fbUser;
    String theatreID;
    private static ArrayList<Media> mediaList = new ArrayList<Media>(5);
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = database.getReference();
    ImageView resultImg;
    ProgressDialog p;
    TextView titleDisplay;
    TextView text;
    Button btn;
    Boolean allResult;

    private void refresh(int miliseconds) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content();
            }
        };
        handler.postDelayed(runnable, miliseconds);
    }

    public void content() {
        MainActivity.pullData('t', theatreID, new DataCallback() {
            @Override
            public void onCallback(final Object obj) {
                if (obj != null) {
                    final Theatre t = (Theatre) obj; //Theatre
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!allResult) {
                                List<User> list = t.getUsers();
                                Log.d("Display", "List of users: " + list.toString());
                                int cntRanked = 0;
                                int cntTheatre = list.size();
                                Log.d("Display", "Number of users in theatre: " + cntTheatre);
                                User us = new User();
                                for (int i = 0; i < cntTheatre; i++) {
                                    us = list.get(i);
                                    if (us.getRankings().size() > 0) {
                                        cntRanked = cntRanked + 1;
                                    }
                                }
                                if (((double) cntRanked / (double) cntTheatre) == 1.00) { //All users finished ranking
                                    allResult = true;
                                    Log.d("Result", "allResult: " + allResult);

                                    //If Host
                                    if (fbUser.getDisplayName().equals(t.getHostID())) {
                                        Log.d("Result", "Host Username: " + theatreID);
                                        btn.setVisibility(View.VISIBLE);
                                        Log.d("Result", "btn Visibility: " + btn.getVisibility());
                                        text.setText("Ready to calculate result!");
                                    } else {
                                        text.setText("All members finished, waiting for Host to Calculate Result");
                                    }
                                } else { //resets displays if they are visible
                                    if (btn.getVisibility() == View.VISIBLE)
                                        btn.setVisibility(View.GONE);
                                    if (!text.getText().equals("Waiting for others..."))
                                        text.setText("Waiting for others...");
                                }
                            }
                            // Updates media poster and title
                            if (t.getResult() != null) {
                                titleDisplay.setText(t.getResult().getFinalDecision().getTitle());
                                Media m = t.getResult().getFinalDecision();
                                try {
                                    MainActivity.apiCallImage(m.getPoster(), new ApiCallback() {
                                        @Override
                                        public void onCallback(final Bitmap result) throws JSONException, IOException {
                                            if (result != null) {
                                                //         Log.d("search", "Image found, downloading from API");
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        resultImg.setImageBitmap(result);
                                                        //   p.dismiss();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                } else {
                    Log.i("Null", "Null");
                }

            }
        });

        while (allResult) {
            refresh(1000);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        allResult = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        resultImg = findViewById(R.id.result_poster);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        titleDisplay = findViewById(R.id.textView19);
        titleDisplay.setVisibility(View.GONE);
        text = findViewById(R.id.textView15);
        text.setText("Waiting for others...");
        btn = findViewById(R.id.calcResultButton);
        btn.setVisibility(View.GONE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) { //extra passed into this
            mediaList = (ArrayList<Media>) extras.getSerializable("mediaList");
            theatreID = extras.getString("theatreID");
        }


        content();

    }


    public void onClickDone(View v) {
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
                } else {
                    Log.i("Guest", "Guest is null");
                }
            }
        });


        // TODO add result to history
    }

    public void onClickCalcResult(View v) {
        p = new ProgressDialog(ResultActivity.this);
        p.setMessage("Getting Media details...");
        p.setCancelable(false);
        p.show();
        if (btn.getVisibility() == View.VISIBLE)
            btn.setVisibility(View.GONE);
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