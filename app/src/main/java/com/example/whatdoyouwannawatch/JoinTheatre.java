package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class JoinTheatre extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_theatre);
    }

    // TODO: Implement once FireBase set up for theatre/code linking
    public void onClickJoinTheatreByCode(View v) {
        EditText editTextTheatreCode = (EditText) findViewById(R.id.editTextNumber_theatreCode);
        String code = editTextTheatreCode.getText().toString();
        Intent intent = new Intent(JoinTheatre.this, TheatreUserLandingPage.class);
        // startActivity(intent);
        // Toast.makeText(this, "Code entered: " + code, Toast.LENGTH_SHORT).show();



        MainActivity.pullData('t', code, new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                final FirebaseUser FBuser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = FBuser.getDisplayName();
                final User[] u = new User[1];
                MainActivity.pullData('u', uid, new DataCallback(){

                    public void onCallback(Object usr) {
                        if (usr != null) {
                            u[0] = (User) usr;
                        }
                    }

                });

                if(obj!=null) {
                    Theatre theatre = (Theatre) obj;
                    if(u[0]!= null) {
                        theatre.addUser(u[0]);
                    }
                    MainActivity.pushData(theatre);
                }
            }
        });

        startActivity(intent);
    }
}