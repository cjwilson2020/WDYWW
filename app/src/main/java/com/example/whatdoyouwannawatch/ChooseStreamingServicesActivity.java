package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChooseStreamingServicesActivity extends AppCompatActivity {
    private String streamingServices[] = {"Hulu", "Netflix", "HBO Max", "Prime Video", "Showtime",
            "Disney+", "STARZ", "Crunchyroll", "Tubi"};
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_streaming_services);

        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, streamingServices);
        listView = findViewById(R.id.listView_displayStreamingServices);
        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    public void onClickSelectStreamingServices(View v) {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        ArrayList<String> selectedStreamingServices = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            int position = checked.keyAt(i);
            // add genre if checked
            if (checked.valueAt(i)) {
                selectedStreamingServices.add(arrayAdapter.getItem(position));
            }
        }

        String streamingServiceList = "";
        for (String genre: selectedStreamingServices) {
            streamingServiceList += genre + ", ";
        }
        Toast.makeText(this, streamingServiceList, Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(this, ChooseStreamingServicesActivity.class);
        //startActivity(intent);
    }
}