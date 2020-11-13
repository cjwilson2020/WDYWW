package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

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
                if (obj == null) {
                    TextView warning = findViewById(R.id.textView_Warning);
                    warning.setText("Warning: User " + username + " does not exist.");
                    return;
                }else {
                    final User friend = (User) obj;
                    Log.i("Friend", friend.getUsername());
                    final FirebaseUser FBuser = FirebaseAuth.getInstance().getCurrentUser();
                    final String userName = FBuser.getDisplayName();
                    MainActivity.pullData('u', userName, new DataCallback() {
                        @Override
                        public void onCallback(Object obj) {
                            if(obj != null){
                                User us = (User) obj;
                                Log.i("Friend", us.getUsername());
                                if(us.getFriends()!= null) {
                                    us.addFriend(friend);
                                }else{
                                    ArrayList<User> friends = new ArrayList<User>();
                                    friends.add(friend);
                                    us.setFriends(friends);
                                }
                                MainActivity.pushData(us);
                            }
                        }
                    });
                    Intent intent = new Intent(AddFriendActivity.this, FriendListActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}