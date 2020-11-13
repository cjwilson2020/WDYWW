package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {
    Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        delete = findViewById(R.id.delete_button);
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