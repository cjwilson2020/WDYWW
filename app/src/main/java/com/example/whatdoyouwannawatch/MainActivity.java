package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    public void onClickWatchNow(View v) {
        Intent intent = new Intent(MainActivity.this, CreateJoinTheatre.class);
        startActivity(intent);
    }

    public void onClickLogIn(View v) {
        //TODO Implement once FireBase is set up
    }

    public void onClickSignUp(View v) {
        //TODO Implement once FireBase is set up
    }
}