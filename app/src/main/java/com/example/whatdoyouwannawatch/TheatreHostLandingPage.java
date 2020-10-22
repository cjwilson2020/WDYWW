package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TheatreHostLandingPage extends AppCompatActivity {
    private int theatreID = 12345;
    private int code = 98765;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theatre_host_landing_page);

        displayTheatreID();
        displayCode();


    }

    public void onClickCopyCode(View v) {
        int code = getCode();
        // TODO: copy to clipboard


    }

    // TODO: Implement once theatre id generation set up in FireBase
    private int getTheatreID() {
        return theatreID;
    }

    // TODO: Implement once code generation set up in FireBase
    private int getCode() {
        return code;
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