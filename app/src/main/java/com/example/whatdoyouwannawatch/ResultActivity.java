package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    FirebaseUser fbUser;
    String theatreID;
    private static ArrayList<Media> mediaList = new ArrayList<Media>(5);
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
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
        Intent intent = new Intent(ResultActivity.this, UserHomeActivity.class);
        startActivity(intent);

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
                        updateWatchHistories(t);
                        TextView displayTitle = findViewById(R.id.textView19);
                        displayTitle.setText(t.getResult().getFinalDecision().getTitle());
                    }
                }
            });
        }
    }

    private void updateWatchHistories(Theatre theatre){
        Result result = theatre.getResult();
        ArrayList<User> users = (ArrayList<User>) theatre.getUsers();
        for (User u : users) {
            Toast.makeText(ResultActivity.this,"User History: " + u.getHistory(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(ResultActivity.this,"guest? " + u.isGuest(), Toast.LENGTH_SHORT).show();
            if (u.getHistory() == null) u.setHistory(new ArrayList<Result>());
            u.addHistory(result);
            MainActivity.pushData(u);
        }
    }
}