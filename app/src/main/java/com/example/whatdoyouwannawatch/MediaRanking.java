package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import java.util.ArrayList;
import java.util.Arrays;

public class MediaRanking extends AppCompatActivity {
    private ArrayList<Media> mediaList = new ArrayList<Media>(5);

    private static final String TAG = "MediaRanking";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_ranking);

        initRecyclerView();
    }

    public void onClickRanking(View v){
        Intent intent = new Intent(this, ResultActivity.class);
        startActivity(intent);
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView. ");

        RecyclerView recyclerView = findViewById(R.id.ranking_recycler);
        MediaRankingAdapter adapter = new MediaRankingAdapter(retrieveData(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private ArrayList<String> retrieveData(){
        // TODO Dummy data, needs to be replaced
        return new ArrayList<String>(
                Arrays.asList("Kill Bill", "TENET", "Mulan", "Lord of the Rings", "Grand Budapest Hotel"));
    }
}