package com.example.whatdoyouwannawatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TheatreHostLandingPage extends AppCompatActivity {
    private FirebaseUser fbUser;

    Button qButton;
    TextView qTextView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theatre_host_landing_page);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();



            displayTheatreID();
            int test = 5;



       // displayCode();


    }

    public void onClickCopyCode(View v) {
        String code = getCode();
        // TODO: copy to clipboard

    }

    public void onClickInviteFriend(View v) {

    }

    // TODO: Figure out how to pull theatre data from FireBase
    public void displayTheatreID() {
        MainActivity.pullData('t', fbUser.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object theatre) {
                if(theatre != null){
                    Theatre currTheatre = (Theatre) theatre;
                    TextView displayTheatreID = (TextView) findViewById(R.id.textView_displayTheatreID);
                    displayTheatreID.setText("You are in " + currTheatre.getHostID() + "'s theatre ");
                }
            }
        });
    }


    // TODO: Implement once code generation set up in FireBase
    private String getCode() {
        final String[] code = new String[1];
        MainActivity.pullData('t', fbUser.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object theatre) {
                if(theatre != null){
                    Theatre currTheatre = (Theatre) theatre;
                    code[0] = currTheatre.getHostID();
                }
            }
        });
        return code[0];
    }



    private void displayCode() {
        TextView displayCode = (TextView) findViewById(R.id.textView_displayCode);
        displayCode.setText("Invitation code: ");
    }
}