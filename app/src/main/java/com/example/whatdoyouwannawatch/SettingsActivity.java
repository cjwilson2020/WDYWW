package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void goToContactUsOnClick(View view) {
        Intent intent = new Intent(SettingsActivity.this, ContactUsActivity.class);
        startActivity(intent);
    }

    public void goToWatchPreferencesOnClick(View view) {
        Intent intent = new Intent(SettingsActivity.this, WatchPreferencesActivity.class);
        startActivity(intent);
    }

}