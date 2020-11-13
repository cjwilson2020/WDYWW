package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {
    FirebaseUser fbUser;
    Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        delete = findViewById(R.id.delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.pullData('u', fbUser.getDisplayName(), new DataCallback() {
                    @Override
                    public void onCallback(Object obj1) {
                        if (obj1 != null) {
                            //delete user in Database
                            User u = (User) obj1;
                            MainActivity.deleteData(u);
                            
                            //delete User in FB Auth
                            FirebaseAuth.getInstance().getCurrentUser().delete();

                            MainActivity.pullData('t', u.getUsername(), new DataCallback() {
                                @Override
                                public void onCallback(Object obj2) {
                                    Theatre t = (Theatre) obj2;
                                    if (t != null){
                                        //If this user is the host of a theatre, delete that too
                                        MainActivity.deleteData(obj2);
                                    }
                                }
                            });

                            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                            startActivity(intent);

                        }
                    }
                });

            }
        });
    }


    public void goToContactUsOnClick(View view) {
        Intent intent = new Intent(SettingsActivity.this, ContactUsActivity.class);
        startActivity(intent);
    }

    public void goToWatchPreferencesOnClick(View view) {
        Intent intent = new Intent(SettingsActivity.this, WatchPreferencesActivity.class);
        startActivity(intent);
    }

}