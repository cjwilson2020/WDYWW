package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class TheatreUserLandingPage extends AppCompatActivity {

    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    String theatreCode = "-111";
    FirebaseUser fbUser;

    private void refresh(int miliseconds){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content();
            }
        };
        handler.postDelayed(runnable, miliseconds);
    }

    public void content(){
        MainActivity.pullData('t', theatreCode, new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if(obj!=null) {
                    Log.i("Null", "Not null");
                    Theatre t = (Theatre)obj;
                    final List<User> uList = t.getUsers();
                    int x = uList.size();
                    String [] users = new String [x];
                    for(int i = 0; i< uList.size(); i++){
                        users[i] = uList.get(i).getUsername();
                        Log.i("Ulist",users[i]);
                    }
                    arrayAdapter = new ArrayAdapter<String>(TheatreUserLandingPage.this, android.R.layout.simple_list_item_1, users);
                    listView = findViewById(R.id.listView_displayUsers);
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                            onClickFriend(uList, i);
                        }
                    });
                }
                else{
                    Log.i("Null", "Null");
                }

            }
        });
        TextView youAreNowInTheatre= findViewById(R.id.textView_youAreNowInTheatre);
        youAreNowInTheatre.setText("You are in "   + getTheatreId() + "'s theatre" );

        refresh(1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theatre_user_landing_page);
        Intent intent = getIntent();
        theatreCode = (intent.getStringExtra("theatreCode"));
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        content();
    }

    private void onClickFriend(List<User> uList, int i) {
        Intent intent = new Intent(TheatreUserLandingPage.this, FriendProfileActivity.class);
        intent.putExtra("username", uList.get(i).getUsername());
        startActivity(intent);
    }

    public void onClickImAllSet(View v) {
        Intent intent = new Intent(this, ChooseGenresActivity.class);
        intent.putExtra("theatreID", getTheatreId());
        startActivity(intent);
    }

    public void onClickLeaveTheatre(View v) {
        // if guest, redirect to theatre selection. Else go to user home page
        MainActivity.pullData('u', fbUser.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if (obj != null) {
                    User u = (User) obj;
                    Log.i("Guest", u.getUsername());
                    Log.i("Guest", Boolean.toString(u.isGuest()));
                    MainActivity.removeUserFromTheatre(theatreCode, fbUser.getDisplayName());
                    if (u.isGuest()) {
                        Intent intent = new Intent(TheatreUserLandingPage.this, JoinTheatre.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(TheatreUserLandingPage.this, UserHomeActivity.class);
                        startActivity(intent);
                    }
                }
                else{
                    Log.i("Guest", "Guest is null");
                }
            }
        });
    }

    private String getTheatreId() {
        return theatreCode;
    }
}