package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChooseGenresActivity extends AppCompatActivity {
    private String genres[] = {"Horror", "Comedy", "Romance", "Action", "True Crime", "Fantasy",
        "Sports", "Drama", "Historical"};
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_genres);

        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, genres);
        listView = findViewById(R.id.listView_displayGenres);
        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    public void onClickSelectGenres(View v) {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        ArrayList<String> selectedGenres = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            int position = checked.keyAt(i);
            // add genre if checked
            if (checked.valueAt(i)) {
                selectedGenres.add(arrayAdapter.getItem(position));
            }
        }

        String genreList = "";
        for (String genre: selectedGenres) {
            genreList += genre + ", ";
        }
        Toast.makeText(this, genreList, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ChooseStreamingServicesActivity.class);
        startActivity(intent);

    }
}

