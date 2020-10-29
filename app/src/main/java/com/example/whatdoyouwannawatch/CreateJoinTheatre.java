package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class CreateJoinTheatre extends AppCompatActivity {
    private User user = new User("jbond@mi6.com","James","007");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_theatre);
    }

    public void onClickCreateTheatre(View v) {
        Theatre theatre = new Theatre(user.getUID());

        Intent intent = new Intent(CreateJoinTheatre.this, TheatreHostLandingPage.class);
        startActivity(intent);
    }

    public void onClickCreateTheatreTesting(View v){

        Toast.makeText(this, "hey I'm a message", Toast.LENGTH_SHORT).show();
    }

    public void onClickJoinTheatre(View v) {
        Intent intent = new Intent(CreateJoinTheatre.this, JoinTheatre.class);
        startActivity(intent);
    }
}