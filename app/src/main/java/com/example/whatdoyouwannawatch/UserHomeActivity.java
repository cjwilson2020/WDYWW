package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import sun.applet.Main;

public class UserHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        // getting user from Firebase
        // 4. Use that reference to invoke the callback method.
        MainActivity.pullData(new DataCallBack()  //this is the reference to the callback with the
                                      // onCallback method implemented to print the returned user
        {   @Override
            public void onCallback(User user) {
                // Here where we can read and edit info from the user object
               System.out.println(user.toString());

            }
        }
        , "1234");
    }
}