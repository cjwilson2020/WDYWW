package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ChooseGenresActivity extends AppCompatActivity {
    private String genres[] = {"Horror", "Comedy", "Romance", "Action", "True Crime", "Fantasy",
        "Sports", "Drama", "Historical"};
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    private ArrayList<String> selectedGenres;

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
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        selectedGenres = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            int position = checked.keyAt(i);
            // add genre if checked
            if (checked.valueAt(i)) {
                selectedGenres.add(arrayAdapter.getItem(position));
            }
        }
        final ArrayList<String> userGenres = selectedGenres;
        MainActivity.pullData('u', fbUser.getDisplayName(), new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if(obj != null){
                    User u = (User) obj;
                    u.setPreferences(userGenres);
                    MainActivity.pushData(u);
                }
            }
        });

        String genreList = "";
        for (String genre: selectedGenres) {
            genreList += genre + ", ";
        }
        genreList = genreList.trim();
 //       if (genreList.length() > 4);
//            genreList = genreList.substring(0, genreList.length()-2);
        Toast.makeText(this, genreList, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ChooseStreamingServicesActivity.class);
        intent.putExtra("genreList", genreList);
        startActivity(intent);

    }
}

