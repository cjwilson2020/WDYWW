package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.florescu.android.rangeseekbar.RangeSeekBar;

public class DurationSelection extends AppCompatActivity {

    RangeSeekBar bar;
    TextView feedback;
    int minimum = 40;
    int maximum = 180;
    FirebaseUser fbUser;
    String genreList;
    String streamingServiceList;
    int minTime;
    int maxTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration_selection);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        Bundle extras = getIntent().getExtras();

        if (extras != null) { //extra passed into this
            genreList = extras.getString("genreList");
            streamingServiceList = extras.getString("streamingServiceList");
            minTime = extras.getInt("minTime");
            minTime = extras.getInt("minTime");
        }
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
                minimum = min.intValue();
                maximum  = max.intValue();

                feedback.setText(min + " minutes ~ " + max + "minutes");
            }
        });
    }

    public void onClickNext(View v){
        MainActivity.pullData('u', fbUser.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if(obj!= null){
                    User u = (User) obj;
                    u.setMinLength(minimum);
                    u.setMaxLength(maximum);
                    MainActivity.pushData(u);
                }
            }
        });
        Intent intent = new Intent(DurationSelection.this, MediaRanking.class);
        intent.putExtra("genreList", genreList);
        intent.putExtra("streamingServiceList", streamingServiceList);
        intent.putExtra("minTime", minimum);
        intent.putExtra("maxTime", maximum);
        startActivity(intent);
    }
}