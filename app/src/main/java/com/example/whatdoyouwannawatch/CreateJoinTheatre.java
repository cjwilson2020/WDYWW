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
            MainActivity.pullData('u', fbUser.getDisplayName(), new DataCallback() {
                @Override
                public void onCallback(Object usr) {
                    if (usr != null) {
                        Theatre theatre1 = new Theatre(fbUser.getDisplayName());
                        User host = (User) (usr);
                        theatre1.addUser(host);
                        MainActivity.pushData(theatre1);
                    }
                }
            });


            Intent intent = new Intent(CreateJoinTheatre.this, TheatreHostLandingPage.class);
            startActivity(intent);
        }
    }

    public void onClickCreateTheatreTesting(View v){
        String name;

//        MainActivity.pullData('t', "0", new DataCallback() {
//            @Override
//            public void onCallback(Object obj) {
//                Theatre theatre = (Theatre) obj;
//
//                Toast.makeText( CreateJoinTheatre.this, theatre.getHostID(), Toast.LENGTH_SHORT).show();
//            }
//        });

        Intent toRanking = new Intent(this, MediaRanking.class);
        startActivity(toRanking);

    }

    public void onClickJoinTheatre(View v) {
        Intent intent = new Intent(CreateJoinTheatre.this, JoinTheatre.class);
        startActivity(intent);
    }
}