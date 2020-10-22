package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CreateJoinTheatre extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_theatre);
    }

    public void onClickCreateTheatre(View v) {
        Intent intent = new Intent(CreateJoinTheatre.this, TheatreHostLandingPage.class);
        startActivity(intent);
    }

    public void onClickJoinTheatre(View v) {
        Intent intent = new Intent(CreateJoinTheatre.this, JoinTheatre.class);
        startActivity(intent);
    }
}