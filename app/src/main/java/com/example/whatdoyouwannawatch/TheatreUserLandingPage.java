package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TheatreUserLandingPage extends AppCompatActivity {
    private String users[] = {"Chris", "Tressa", "Meghan", "Peter", "Yuheng", "Yuan"};
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theatre_user_landing_page);

        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, users);
        listView = findViewById(R.id.listView_displayUsers);
        listView.setAdapter(arrayAdapter);

        TextView youAreNowInTheatre= findViewById(R.id.textView_youAreNowInTheatre);
        youAreNowInTheatre.setText("You Are Now In Theatre #" + getTheatreId());
    }

    public void onClickImAllSet(View v) {
        Intent intent = new Intent(this, ChooseGenresActivity.class);
        startActivity(intent);
    }

    private int getTheatreId() {
        return 1234;
    }
}