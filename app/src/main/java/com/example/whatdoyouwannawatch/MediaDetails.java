package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;

public class MediaDetails extends AppCompatActivity {
    ProgressDialog p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_details);

        String json = getIntent().getStringExtra("data");
        Toast.makeText(this, json, Toast.LENGTH_LONG);

        Media data = new Gson().fromJson(json, Media.class);

        final ImageView poster = findViewById(R.id.detail_poster);
        TextView title = findViewById(R.id.detail_title);
        TextView year = findViewById(R.id.detail_year_type);
        TextView length = findViewById(R.id.detail_length);
        TextView lang = findViewById(R.id.detail_language);
        TextView by = findViewById(R.id.detail_directed_by);
        TextView cast = findViewById(R.id.detail_cast);
        TextView description = findViewById(R.id.detail_description);
        p = new ProgressDialog(MediaDetails.this);
        p.setMessage("Getting Media details...");
        p.setCancelable(false);
        p.show();

        try {
            MainActivity.apiCallImage(data.getPoster(), new ApiCallback() {
                @Override
                public void onCallback(final Bitmap result) throws JSONException, IOException {
                    if (result != null) {
                        Log.d("search", "Image found, downloading from API");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                poster.setImageBitmap(result);
                                p.dismiss();
                            }
                        });
                    } else {
                        Log.d("search", "No image downloaded");
                        p.dismiss();
                    }
                }
            });
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        title.setText(data.getTitle());
        year.setText(data.getYear() + " " + data.getType() );


        if(data.getLength() == 0)
            length.setText("No duration found");
        else
                length.setText(Integer.toString(data.getLength()) + " minutes");

        lang.setText(data.getLanguage());

        String c = "";
        for (String ca: data.getCast()) {
            if (!c.contains(ca))
                c = c.concat(ca + "\n");
        }

        cast.setText(c);

        description.setText(data.getDescription());
    }
}