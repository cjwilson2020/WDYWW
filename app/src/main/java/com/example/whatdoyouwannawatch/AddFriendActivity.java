package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddFriendActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
    }

    // TODO: Implement once FireBase set up for theatre/code linking
    public void onClickAddFriend(View v) {
        EditText editTextTheatreCode = (EditText) findViewById(R.id.editTextNumber_theatreCode);
        final String username = editTextTheatreCode.getText().toString();
        MainActivity.pullData('u', username, new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                // Check to make sure theatre exists, TODO: alert user
                if (obj == null) {
                    TextView warning = findViewById(R.id.textView_Warning);
                    warning.setText("Warning: User " + username + " does not exist.");
                    return;
                }else {
                    final User friend = (User) obj;
                    final FirebaseUser FBuser = FirebaseAuth.getInstance().getCurrentUser();
                    final String userName = FBuser.getDisplayName();
                    MainActivity.pullData('u', userName, new DataCallback() {
                        @Override
                        public void onCallback(Object obj) {
                            if(obj != null){
                                User us = (User) obj;
                                us.addFriend(friend);
                                MainActivity.pushData(us);
                            }
                        }
                    });
                }
            }
        });
    }
}