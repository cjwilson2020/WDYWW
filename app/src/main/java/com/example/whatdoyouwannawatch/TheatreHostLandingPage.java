package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TheatreHostLandingPage extends AppCompatActivity {
    private FirebaseUser fbUser;
    Button qButton;
    TextView qTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theatre_host_landing_page);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        displayTheatreID();
    }


    public void onClickNextHost(View v) {
        Intent intent = new Intent(this, TheatreUserLandingPage.class);
        intent.putExtra("theatreCode", fbUser.getDisplayName());
        startActivity(intent);
    }

    public void onClickRemoveTheatre(View v) {
        MainActivity.pullData('t', fbUser.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object theatre) {
                if(theatre != null){
                    Theatre currTheatre = (Theatre) theatre;
                    MainActivity.removeTheatre(currTheatre.getHostID());
                }
            }
        });
        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);
    }

    public void displayTheatreID() {
        MainActivity.pullData('t', fbUser.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object theatre) {
                if(theatre != null){
                    Theatre currTheatre = (Theatre) theatre;
                    TextView displayTheatreID = (TextView) findViewById(R.id.textView_displayTheatreID);
                    displayTheatreID.setText("You are in " + currTheatre.getHostID() + "'s theatre ");
                }
            }
        });
    }



}