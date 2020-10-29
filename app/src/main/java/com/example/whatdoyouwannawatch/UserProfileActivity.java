package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        final FirebaseUser FBuser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = FBuser.getUid();
        user_id = uid;

        // Toast.makeText(UserHomeActivity.this, "Welcome " + FBuser.getDisplayName(), Toast.LENGTH_SHORT).show();

        TextView name = findViewById(R.id.textView_Name);
        name.setText(FBuser.getDisplayName());

    }


}