package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ServicePreferencesActivity extends AppCompatActivity {
    private String streamingServices[] = {"HBO", "Hulu", "Amazon Prime Video", "Netflix"};
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    private List<String> existingUserServices;
    private ArrayList<String> selectedServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_preferences);


        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, streamingServices);
        listView = findViewById(R.id.listView_displayServices);
        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        final FirebaseUser FBuser = FirebaseAuth.getInstance().getCurrentUser();
        final String username = FBuser.getDisplayName();

        MainActivity.pullData('u', username, new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                User user = (User) obj;
                existingUserServices = user.getServices();
                for(int i = 0; i < arrayAdapter.getCount(); i++) {
                    if(existingUserServices.contains(listView.getItemAtPosition(i))) {
                        listView.setItemChecked(i, true);
                    }
                }
            }
        });
    }

    public void saveServices(View view) {
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
                if(obj != null){
                    User u = (User) obj;
                    u.setServices(userServices);
                    MainActivity.pushData(u);

                    // tell user their preferences have been saved
                    android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(ServicePreferencesActivity.this);
                    builder1.setMessage("Your service preferences have been saved.");
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