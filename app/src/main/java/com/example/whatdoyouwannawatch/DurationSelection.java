package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.florescu.android.rangeseekbar.RangeSeekBar;

public class DurationSelection extends AppCompatActivity {

    RangeSeekBar bar;
    TextView feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration_selection);

        bar = findViewById(R.id.rang_seek_bar);
        feedback = findViewById(R.id.textView5);
        bar.setRangeValues(0,300);
        bar.setSelectedMinValue(40);
        bar.setSelectedMaxValue(180);

        bar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                Number min = bar.getSelectedMinValue();
                Number max = bar.getSelectedMaxValue();

                feedback.setText(min + " minutes ~ " + max + "minutes");
            }
        });
    }

    public void onClickDurationSelection(View v){
        Intent intent = new Intent(DurationSelection.this, MediaRanking.class);
        startActivity(intent);
    }
}