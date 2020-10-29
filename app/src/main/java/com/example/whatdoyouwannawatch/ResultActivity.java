package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private final String decision = "Blade Runner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView displayTitle = findViewById(R.id.textView19);
        displayTitle.setText(decision);
    }

    public void onClickResult(View v){
        Intent intent = new Intent(ResultActivity.this, UserHomeActivity.class);
        startActivity(intent);

        // TODO add result to history
    }
}