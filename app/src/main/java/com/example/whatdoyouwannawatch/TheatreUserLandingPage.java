package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theatre_user_landing_page);
        Intent intent = getIntent();
        theatreCode = (intent.getStringExtra("theatreCode"));
        MainActivity.pullData('t', theatreCode, new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if(obj!=null) {
                    Log.i("Null", "Not null");
                    Theatre t = (Theatre)obj;
                    List<User> uList = t.getUsers();
                    int x = uList.size();
                    String [] users = new String [x];
                    for(int i = 0; i< uList.size(); i++){
                        users[i] = uList.get(i).getUsername();
                        Log.i("Ulist",users[i]);
                    }
                    arrayAdapter = new ArrayAdapter<String>(TheatreUserLandingPage.this, android.R.layout.simple_list_item_1, users);
                    listView = findViewById(R.id.listView_displayUsers);
                    listView.setAdapter(arrayAdapter);
                }
                else{
                    Log.i("Null", "Null");
                }

            }
        });
        TextView youAreNowInTheatre= findViewById(R.id.textView_youAreNowInTheatre);
        youAreNowInTheatre.setText("You are in "   + getTheatreId() + "'s theatre" );
    }

    public void onClickImAllSet(View v) {
        Intent intent = new Intent(this, ChooseGenresActivity.class);
        startActivity(intent);
    }

    public void onClickLeaveTheatre(View v) {
        FirebaseUser FBuser = FirebaseAuth.getInstance().getCurrentUser();
        MainActivity.removeUserFromTheatre(theatreCode, FBuser.getDisplayName());
        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);
    }

    private String getTheatreId() {
        return theatreCode;
    }
}