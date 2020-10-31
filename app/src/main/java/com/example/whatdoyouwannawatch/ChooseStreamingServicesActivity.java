package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChooseStreamingServicesActivity extends AppCompatActivity {
    private String streamingServices[] = {"Amazon Instant Video", "NBC", "ABC", "FOX",
        "Fandango Movies", "Google Play", "CBS", "The CW", "TBS", "Nickelodon", "Discovery Channel",
            "HBO", "Disney XD", "USA Network", "Hulu", "Amazon Prime Video", "iTunes", "Netflix",
            "Atom Tickets"};
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    String genreList = null;
    String theatreID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_streaming_services);

        Bundle extras = getIntent().getExtras();

        if (extras != null) { //extra passed into this
            genreList = extras.getString("genreList");
            theatreID = extras.getString("theatreID");
        }
        Log.d("search", genreList);

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

        streamingServiceList = streamingServiceList.trim();
//        if (streamingServiceList.length() > 2);
 //           streamingServiceList = streamingServiceList.substring(0, streamingServiceList.length()-2);
        Toast.makeText(this, streamingServiceList, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, DurationSelection.class);
        intent.putExtra("genreList", genreList);
        intent.putExtra("streamingServiceList", streamingServiceList);
        intent.putExtra("theatreID", theatreID);
        startActivity(intent);
    }
}