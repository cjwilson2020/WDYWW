package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import java.io.IOException;

public class QueryActivity extends AppCompatActivity {
    NestedScrollView nsv;
    public Context c = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        nsv = (NestedScrollView) findViewById(R.id.q_nestedScrollView);
        String value = null;
        String result = null;
        Bundle extras = getIntent().getExtras();

        if (extras != null) { //extra passed into this
            value = extras.getString("KEY");
        }

        try {
            System.out.println("value going into search: " + value);
            MainActivity.apiCallSearch(value, new ApiCallback() {

                @Override
                public void onCallback(String res) throws JSONException {
                    //Here is where we will update the view
                    // res is a JSON string

                    JSONObject obj = new JSONObject(res);
                    JSONArray hits = obj.getJSONArray("Hits");

                    // TODO: A For-Loop to iterate through each result from the query
                    // For each result, we are going to extract String id, String title, List<String> genres, List<String> cast, Time length, String director, String writer, String description, Image poster, Double rating
                    // To do this, we need to look at the json example at
                    for(int i = 0; i < hits.length(); i++){
                        JSONObject result_info = hits.getJSONObject(i).getJSONObject("Source");
                        String title = null;
                        String description = null;
                        if (result_info.has("Title")){
                           title = result_info.getString("Title");
                        } else{
                            title = "No Title available";
                        }

                        if (result_info.has("Descriptions")){
                            description = (String)result_info.getJSONObject("Descriptions").getJSONObject("0").getString("Description");
                        }else{
                            description = "No Description available";
                        }

                        System.out.println("res: " + res);
                        System.out.println("Movie Reslts:\nTitle: " + title);
                        System.out.println("Description: " + description);

                        //Todo: For each result, we want to create it as a Media objecy and display it with the Scrollview
                        // Media( String id, String title, List<String> genres, List<String> cast, Time length, String director, String writer, String description, Image poster, Double rating ) ;

                    }

                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }


        if (result != null) {
            String s = result.toString();
            Log.d("inQA", s);
        } else {
            Log.d("inQA", "Nothing in result");
        }


    }
}