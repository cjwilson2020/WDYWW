package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

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

public class WatchPreferencesActivity extends AppCompatActivity {
    private String genres[] = {"Horror", "Comedy", "Romance", "Action", "True Crime", "Fantasy",
            "Sports", "Drama", "Historical"};
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
                User user = (User) obj;
                existingUserPreferences = user.getPreferences();
                for(int i = 0; i < arrayAdapter.getCount(); i++) {
                    if(existingUserPreferences.contains(listView.getItemAtPosition(i))) {
                        listView.setItemChecked(i, true);
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
                    u.setPreferences(userGenres);
                    MainActivity.pushData(u);
                }
            }
        });
    }
}
