package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class StreamingPreferencesActivity extends AppCompatActivity {

    private String genres[] = {"Amazon Instant Video", "NBC", "ABC", "FOX",
            "Fandango Movies", "Google Play", "Nickelodeon", "Discovery Channel",
            "HBO", "Hulu", "Amazon Prime Video", "Netflix"};
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    private List<String> existingUserPreferences;
    private ArrayList<String> selectedGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_preferences);
        Intent intent = getIntent();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, genres);
        listView = findViewById(R.id.listView_displayPreferences);
        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        final FirebaseUser FBuser = FirebaseAuth.getInstance().getCurrentUser();
        final String username = FBuser.getDisplayName();

        MainActivity.pullData('u', username, new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if(obj!= null) {
                    User user = (User) obj;
                    existingUserPreferences = user.getServices();
                    for (int i = 0; i < arrayAdapter.getCount(); i++) {
                        if (existingUserPreferences.contains(listView.getItemAtPosition(i))) {
                            listView.setItemChecked(i, true);
                        }
                    }
                }
            }
        });

    }

    public void savePreferences(View view) {
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        selectedGenres = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            int position = checked.keyAt(i);
            // add genre if checked
            if (checked.valueAt(i)) {
                selectedGenres.add(arrayAdapter.getItem(position));
            }
        }
        final ArrayList<String> userGenres = selectedGenres;
        MainActivity.pullData('u', fbUser.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if(obj != null){
                    User u = (User) obj;
                    u.setServices(userGenres);
                    MainActivity.pushData(u);

                    // tell user their preferences have been saved
                    android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(StreamingPreferencesActivity.this);
                    builder1.setMessage("Your genre preferences have been saved.");
                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });
    }
}