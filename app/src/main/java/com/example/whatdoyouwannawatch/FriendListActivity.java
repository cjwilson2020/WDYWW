
package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        listView = (ListView) findViewById(R.id.listview);

        MainActivity.pullData('u', user.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if (obj != null) {
                    User u = (User) obj;
                    final ArrayList<User> fList = (ArrayList<User>) u.getFriends();
                    List<String> nameList = new ArrayList<String>();
                    if (fList != null && fList.size()>0) {
                        for (int i = 0; i < fList.size(); i++) {
                            nameList.add(fList.get(i).getUsername());
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(FriendListActivity.this,
                                android.R.layout.simple_list_item_1, nameList);
                        listView.setAdapter(arrayAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                                onClickFriend(fList, i);
                            }
                        });
                    }else{
                        ArrayList <String> noFriends = new ArrayList<String>();
                        noFriends.add("You currently have no friends. Click the add button to find them!");
                        ArrayAdapter arrayAdapter = new ArrayAdapter(FriendListActivity.this,
                                android.R.layout.simple_list_item_1, noFriends);
                        listView.setAdapter(arrayAdapter);
                    }
                }
            }
        });
    }


    protected void onResume() {
        super.onResume();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        listView = (ListView) findViewById(R.id.listview);

        MainActivity.pullData('u', user.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if (obj != null) {
                    User u = (User) obj;
                    final ArrayList<User> fList = (ArrayList<User>) u.getFriends();
                    List<String> nameList = new ArrayList<String>();
                    if (fList != null && fList.size()>0) {
                        for (int i = 0; i < fList.size(); i++) {
                            nameList.add(fList.get(i).getUsername());
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(FriendListActivity.this,
                                android.R.layout.simple_list_item_1, nameList);
                        listView.setAdapter(arrayAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                                onClickFriend(fList, i);
                            }
                        });
                    }else{
                        ArrayList <String> noFriends = new ArrayList<String>();
                        noFriends.add("You currently have no friends. Click the add button to find them!");
                        ArrayAdapter arrayAdapter = new ArrayAdapter(FriendListActivity.this,
                                android.R.layout.simple_list_item_1, noFriends);
                        listView.setAdapter(arrayAdapter);
                    }
                }
            }
        });
    }

    public void onClickAddFriend(View v) {
        Intent intent = new Intent(FriendListActivity.this, AddFriendActivity.class);
        startActivity(intent);
    }

    public void onClickFriend(List<User> fList, int i) {
        Intent intent = new Intent(FriendListActivity.this, FriendProfileActivity.class);
        intent.putExtra("username", fList.get(i).getUsername());
        startActivity(intent);
    }

}