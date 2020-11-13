package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class WatchHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_history);

        List<Result> watchHistory = new ArrayList<>();
        //add test result
//        Media m1 = new Media("1");
//        Media m2 = new Media("2");
//        Result r1 = new Result(m1);
//        Result r2 = new Result(m2);
//        watchHistory.add(r1);
//        watchHistory.add(r2);
        //delete the test later

        final FirebaseUser FBuser = FirebaseAuth.getInstance().getCurrentUser();
        MainActivity.pullData('u', FBuser.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object obj) {

                if(obj!=null) {
                    TextView title = (TextView)findViewById(R.id.textView1);
                    ListView historyView = (ListView)findViewById(R.id.listView_history);
                    User u = (User) obj;
                    List<Result> watchHistory = u.getHistory();
                    if (watchHistory == null || watchHistory.size() == 0){
                        title.setText("No history available");

                    } else{
                        title.setText("Watch History");
                        ArrayAdapter historyAdapter = new ArrayAdapter<Result>(WatchHistoryActivity.this, android.R.layout.simple_list_item_1, watchHistory);
                        historyView.setAdapter(historyAdapter);}
                }
            }
        });





    }
}