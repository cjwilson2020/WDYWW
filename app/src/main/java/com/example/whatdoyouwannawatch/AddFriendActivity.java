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

import java.util.ArrayList;

public class AddFriendActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
    }

    // TODO: Implement once FireBase set up for theatre/code linking
    public void onClickAddFriend(View v) {
        TextView warning = findViewById(R.id.textView_Warning);
        warning.setText("");
        EditText editTextTheatreCode = (EditText) findViewById(R.id.editTextNumber_theatreCode);
        final String username = editTextTheatreCode.getText().toString();
        MainActivity.pullData('u', username, new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if (obj == null) {
                    TextView warning = findViewById(R.id.textView_Warning);
                    warning.setText("Warning: User " + username + " does not exist.");
                }else {
                    final User friend = (User) obj;
                    final FirebaseUser FBuser = FirebaseAuth.getInstance().getCurrentUser();
                    final String userName = FBuser.getDisplayName();
                    MainActivity.pullData('u', userName, new DataCallback() {
                        @Override
                        public void onCallback(Object obj) {
                            if(obj != null){
                                User us = (User) obj;
                                if(us.getFriends()== null || us.getFriends().size()<1) {
                                    us.addFriend(friend);
                                    MainActivity.pushData(us);
                                    Toast.makeText(getApplicationContext(), "Friend successfully added", Toast.LENGTH_SHORT).show();
                                }else{
                                    ArrayList<User> friends = (ArrayList<User>) us.getFriends();
                                    boolean found =false;
                                    for(int i = 0; i< friends.size(); i++){
                                        if(friends.get(i).getUsername().equals(friend.getUsername())){
                                            found =true;
                                        }
                                    }
                                    if(found) {
                                        Toast.makeText(getApplicationContext(), "You are already friends with this user", Toast.LENGTH_SHORT).show();
                                    }else {
                                        us.addFriend(friend);
                                        MainActivity.pushData(us);
                                        Toast.makeText(getApplicationContext(), "Friend successfully added", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }
                return;
            }
        });
    }
}