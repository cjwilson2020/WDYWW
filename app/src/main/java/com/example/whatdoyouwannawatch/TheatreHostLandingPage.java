package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class TheatreHostLandingPage extends AppCompatActivity {
    private FirebaseUser fbUser;

    Button qButton;
    TextView qTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theatre_host_landing_page);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        qButton = (Button) findViewById(R.id.query_button);
        qTextView = (TextView) findViewById(R.id.query_editText);
        qButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add text to QueryActivity's extras
                Intent intent = new Intent(TheatreHostLandingPage.this, QueryActivity.class);
                String q = (String) qTextView.getText().toString();
                intent.putExtra("KEY", q);
                startActivity(intent);
            }
        });

       // displayTheatreID();
       // displayCode();


    }

    public void onClickCopyCode(View v) {
        String code = getCode();
        // TODO: copy to clipboard

    }

    public void onClickInviteFriend(View v) {

    }

    // TODO: Figure out how to pull theatre data from FireBase
    private String getTheatreID() {
        final String[] theatreID = new String[1];
        MainActivity.pullData('t', fbUser.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object theatre) {
                if(theatre != null){
                    Theatre currTheatre = (Theatre) theatre;
                    theatreID[0] = currTheatre.getHostID();
                }
            }
        });
        return theatreID[0];
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

    private void displayTheatreID() {
        TextView displayTheatreID = (TextView) findViewById(R.id.textView_displayTheatreID);
        displayTheatreID.setText("You are now the host of Theatre #" + getTheatreID());
    }

    private void displayCode() {
        TextView displayCode = (TextView) findViewById(R.id.textView_displayCode);
        displayCode.setText("Invitation code: " + getCode());
    }
}