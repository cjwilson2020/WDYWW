package com.example.whatdoyouwannawatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    String theatreID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration_selection);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        Bundle extras = getIntent().getExtras();

        if (extras != null) { //extra passed into this
            genreList = extras.getString("genreList");
            streamingServiceList = extras.getString("streamingServiceList");
            theatreID = extras.getString("theatreID");
        }
        bar = findViewById(R.id.rang_seek_bar);
        feedback = findViewById(R.id.textView5);
        bar.setRangeValues(0, 300);
        bar.setSelectedMinValue(40);
        bar.setSelectedMaxValue(180);

        bar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                Number min = bar.getSelectedMinValue();
                Number max = bar.getSelectedMaxValue();
                minimum = min.intValue();
                maximum = max.intValue();

                feedback.setText(min + " minutes ~ " + max + "minutes");
            }
        });
    }

    public void onClickNext(View v) {
        MainActivity.pullData('u', fbUser.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if (obj != null) {
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
        String progTypes = "";
        if (maximum >= 55) {
            progTypes = progTypes.concat(",Movie");
        }
        if (minimum < 55) {
            progTypes = progTypes.concat(",Show");
        }


        if ( progTypes.endsWith(",")) {
            progTypes = progTypes.substring(0, progTypes.length() - 1);
        }
        if (progTypes.startsWith(",")) {
            progTypes = progTypes.substring(1);
        }
        Toast.makeText(this, "Minimum: " + minimum + ", Maximum: " + maximum, Toast.LENGTH_SHORT).show();
        intent.putExtra("progType", progTypes);
        intent.putExtra("theatreID", theatreID);
        startActivity(intent);
    }
}