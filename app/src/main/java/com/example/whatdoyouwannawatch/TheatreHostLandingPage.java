package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class TheatreHostLandingPage extends AppCompatActivity {
    private FirebaseUser fbUser;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
//    private String theatreCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theatre_host_landing_page);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        MainActivity.pullData('t', fbUser.getDisplayName(), new DataCallback() {
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
                    arrayAdapter = new ArrayAdapter<String>(TheatreHostLandingPage.this, android.R.layout.simple_list_item_1, users);
                    listView = findViewById(R.id.listView_displayUsersInHost);
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
        displayTheatreID();

//
    }

    private void onClickFriend(List<User> uList, int i) {
        Intent intent = new Intent(TheatreHostLandingPage.this, FriendProfileActivity.class);
        intent.putExtra("username", uList.get(i).getUsername());
        startActivity(intent);
    }


    public void onClickNextHost(View v) {
        Intent intent = new Intent(this, ChooseGenresActivity.class);
        intent.putExtra("theatreCode", fbUser.getDisplayName());
        startActivity(intent);
    }

    public void onClickRemoveTheatre(View v) {
        MainActivity.pullData('t', fbUser.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object theatre) {
                if(theatre != null){
                    Theatre currTheatre = (Theatre) theatre;
                    MainActivity.removeTheatre(currTheatre.getHostID());
                }
            }
        });
        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);
    }

    public void displayTheatreID() {
        MainActivity.pullData('t', fbUser.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object theatre) {
                if(theatre != null){
                    Theatre currTheatre = (Theatre) theatre;
                    TextView displayTheatreID = (TextView) findViewById(R.id.textView_displayTheatreID);
                    displayTheatreID.setText("You are in " + currTheatre.getHostID() + "'s theatre ");
//                    theatreCode = currTheatre.getHostID();
                }
            }
        });
    }



}