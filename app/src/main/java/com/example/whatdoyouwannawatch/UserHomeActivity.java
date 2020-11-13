package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class UserHomeActivity extends AppCompatActivity {
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        final FirebaseUser FBuser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = FBuser.getUid();
        user_id = uid;

        //Toast.makeText(UserHomeActivity.this, "Welcome " + FBuser.getDisplayName(), Toast.LENGTH_SHORT).show();

        TextView welcome = findViewById(R.id.textView_Welcome);

        welcome.setText("Welcome, " + FBuser.getDisplayName());


    }

    public void goToProfileOnClick(View view) {
        Intent intent = new Intent(UserHomeActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }

    public void goToSettingsOnClick(View view) {
        Intent intent = new Intent(UserHomeActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void goToCreateJoinTheatreOnClick(View view) {
        Intent intent = new Intent(UserHomeActivity.this, CreateJoinTheatre.class);
        startActivity(intent);
    }

    public void goToFriendsListOnClick(View view) {
        Intent intent = new Intent(UserHomeActivity.this, FriendListActivity.class);
        startActivity(intent);
    }

    public void goToWatchHistoryOnClick(View view) {
        Intent intent = new Intent(UserHomeActivity.this, WatchHistoryActivity.class);
        startActivity(intent);
    }

    public void onClickLogOut(View w) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}