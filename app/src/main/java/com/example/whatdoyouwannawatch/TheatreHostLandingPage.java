package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TheatreHostLandingPage extends AppCompatActivity {
    private int theatreID = 12345;
    private int code = 98765;

    Button qButton;
    TextView qTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theatre_host_landing_page);
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