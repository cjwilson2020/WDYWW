package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class JoinTheatre extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_theatre);
    }

    // TODO: Implement once FireBase set up for theatre/code linking
    public void onClickJoinTheatreByCode(View v) {
        EditText editTextTheatreCode = (EditText) findViewById(R.id.editTextNumber_theatreCode);
        String code = editTextTheatreCode.getText().toString(); // change to int
        Intent intent = new Intent(JoinTheatre.this, TheatreUserLandingPage.class);
        startActivity(intent);
       // Toast.makeText(this, "Code entered: " + code, Toast.LENGTH_SHORT).show();
    }
}