package com.example.whatdoyouwannawatch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123; //what's this?


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("login", "LoginActivity");
        createSignInIntent();
        //Maybe here save User to fire

    }

    /*
        Onclick function which redirects user to their profile
     */
    public void goToProfileOnClick(View view) {
        // TODO: if new user, push new user data to DB
        
        Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
        startActivity(intent);

    }

    /*
        Create and launch sign-in intent
     */
    public void createSignInIntent() {
        startActivityForResult( AuthUI.getInstance().createSignInIntentBuilder().build(), RC_SIGN_IN );

    }


}