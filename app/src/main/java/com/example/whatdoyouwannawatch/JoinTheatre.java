package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class JoinTheatre extends AppCompatActivity {
    private Theatre theatre;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_theatre);
    }

    // TODO: Implement once FireBase set up for theatre/code linking
    public void onClickJoinTheatreByCode(View v) {
        EditText editTextTheatreCode = (EditText) findViewById(R.id.editTextNumber_theatreCode);
        final String code = editTextTheatreCode.getText().toString();
        final Intent intent = new Intent(JoinTheatre.this, TheatreUserLandingPage.class);
        intent.putExtra("theatreCode", code);
        MainActivity.pullData('t', code, new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                Theatre theatre = (Theatre) obj;
                setTheatre(theatre);

                // Check to make sure theatre exists, TODO: alert user
                if (theatre == null) {
                    TextView warning = findViewById(R.id.textView_Warning);
                    warning.setText("Warning: Theatre " + code + " does not exist.");
                    return;
                }

                final FirebaseUser FBuser = FirebaseAuth.getInstance().getCurrentUser();
                final String userName = FBuser.getDisplayName();

                MainActivity.pullData('u', userName, new DataCallback() {
                    @Override
                    public void onCallback(Object obj) {
                        User user = (User) obj;
                        setUser(user);
                        addUserToTheatre();
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void addUserToTheatre() {
        theatre.addUser(user);
        MainActivity.pushData(theatre);
    }

    public void setTheatre(Theatre theatre) {
        this.theatre = theatre;
    }

    public void setUser(User user) {
        this.user = user;
    }
}