package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ChooseStreamingServicesActivity extends AppCompatActivity {
    private String streamingServices[] = {"Amazon Instant Video", "NBC", "ABC", "FOX",
            "Fandango Movies", "Google Play", "Nickelodeon", "Discovery Channel",
            "HBO", "Hulu", "Amazon Prime Video", "Netflix"};
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    String genreList = null;
    ArrayList<String> selectedServices;
    String theatreID;
    private List<String> existingStreamingServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_streaming_services);
        //Sorting the streamingServices
        for (int i = 0; i < streamingServices.length; i++) {
            for (int j = i + 1; j < streamingServices.length; j++) {
                if (streamingServices[i].compareTo(streamingServices[j]) > 0) {
                    String temp = streamingServices[i];
                    streamingServices[i] = streamingServices[j];
                    streamingServices[j] = temp;
                }
            }
        }
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

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        String username = fbUser.getDisplayName();
        MainActivity.pullData('u', username, new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if (obj != null) {
                    final User user = (User) obj;
                    if (!user.isGuest()) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ChooseStreamingServicesActivity.this);
                        builder1.setMessage("Would you like to use your saved streaming service preferences?");
                        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                existingStreamingServices = user.getServices();
                                for (int i = 0; i < arrayAdapter.getCount(); i++) {
                                    if (existingStreamingServices.contains(listView.getItemAtPosition(i))) {
                                        listView.setItemChecked(i, true);
                                    }
                                }
                                Button button = findViewById(R.id.button_selectStreamingServices);
                                button.performClick();
                                existingStreamingServices = user.getServices();
                                dialog.cancel();
                            }
                        });

                        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
            }
        });
    }

    public void onClickSelectStreamingServices(View v) {
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        selectedServices = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            int position = checked.keyAt(i);
            // add genre if checked
            if (checked.valueAt(i)) {
                selectedServices.add(arrayAdapter.getItem(position));
            }
        }
        final ArrayList<String> userServices = selectedServices;
        MainActivity.pullData('u', fbUser.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if (obj != null) {
                    User u = (User) obj;
                    u.setServices(userServices);
                    MainActivity.pushData(u);
                }
            }
        });
        String streamingServiceList = "";
        for (String genre : selectedServices) {
            streamingServiceList += genre + ", ";
        }
        streamingServiceList = streamingServiceList.trim();
        if (streamingServiceList.length() > 0) {
            if (streamingServiceList.substring(streamingServiceList.length() - 1).equals(",")) {
                streamingServiceList = streamingServiceList.substring(0, streamingServiceList.length() - 1);
            }
        }
        //Toast.makeText(this, streamingServiceList, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DurationSelection.class);
        intent.putExtra("genreList", genreList);
        intent.putExtra("streamingServiceList", streamingServiceList);
        intent.putExtra("theatreID", theatreID);
        startActivity(intent);
    }
}