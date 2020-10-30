package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateJoinTheatre extends AppCompatActivity {
    /*
        Create theatre objects that act as a hub for users to rank movie recommendations
        Upon clicking create theatre:
        √ a theatre roomNumber should be generated, and
        - the Theatre’s hostID field should be set to that of the host.
        - the User should be added to the theatre’s users field.
        Upon successful generation of theatre, it should be pushed to firebase so that other Users can Join.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_theatre);

    }

    public void onClickCreateTheatre(View v) {
        final FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser != null) {
            MainActivity.pullData('t', fbUser.getDisplayName(), new DataCallback() {
                @Override
                public void onCallback(Object usr) {
                    if (usr == null) {
                        Theatre theatre1 = new Theatre(fbUser.getDisplayName());
                        MainActivity.pushData(theatre1);
                    }
                }
            });
        } else {
            MainActivity.pullData('t', fbUser.getDisplayName(), new DataCallback() {
                @Override
                public void onCallback(Object usr) {
                    if (usr == null) {
                        Theatre theatre1 = new Theatre("GuestTheatre");
                        MainActivity.pushData(theatre1);
                    }
                }
            });
        }

        Intent intent = new Intent(CreateJoinTheatre.this, TheatreHostLandingPage.class);
        startActivity(intent);
    }

    public void onClickCreateTheatreTesting(View v){
//        Theatre theatre;
        String name;

        MainActivity.pullData('t', "0", new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                Theatre theatre = (Theatre) obj;

                Toast.makeText( CreateJoinTheatre.this, "hey I'm a message", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void onClickJoinTheatre(View v) {
        Intent intent = new Intent(CreateJoinTheatre.this, JoinTheatre.class);
        startActivity(intent);
    }
}