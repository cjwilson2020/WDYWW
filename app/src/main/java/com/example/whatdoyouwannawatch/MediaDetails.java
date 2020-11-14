package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MediaDetails extends AppCompatActivity {
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_details);

        String json = getIntent().getStringExtra("data");
        Toast.makeText(this, json, Toast.LENGTH_LONG);

        Media data = new Gson().fromJson(json, Media.class);

        ImageView poster = findViewById(R.id.detail_poster);
        TextView title = findViewById(R.id.detail_title);
        TextView year = findViewById(R.id.detail_year_type);
        TextView length = findViewById(R.id.detail_length);
        TextView lang = findViewById(R.id.detail_language);
        TextView by = findViewById(R.id.detail_directed_by);
        TextView director = findViewById(R.id.detail_director);
        TextView description = findViewById(R.id.detail_description);

        Log.d("image", data.getPosterImg().toString());
        poster.setImageBitmap(getBitmapFromURL(data.getPosterImg().toString()));

        title.setText(data.getTitle());
//        year.setText(data.getYear() + " " + getType());
//        length.setText(Integer.toString(data.getLength()));
//        lang.setText(data.getLanguage());
        if (data.getDirector().equals("no Director available")){
            by.setText("");
            director.setText("");
        } else
            director.setText(data.getDirector());

        description.setText(data.getDescription());
    }



    public static Bitmap getBitmapFromURL(String src) {
        try {

            //uncomment below line in image name have spaces.
            //src = src.replaceAll(" ", "%20");

            URL url = new URL(src);

            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            Log.d("vk21", e.toString());
            return null;
        }
    }

}