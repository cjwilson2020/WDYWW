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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class QueryActivity extends AppCompatActivity {
    NestedScrollView nsv;
    public Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        nsv = (NestedScrollView) findViewById(R.id.q_nestedScrollView);
        String value = null;
        final String result = null;
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
                    for (int i = 0; i < hits.length(); i++) {
                        JSONObject result_info = hits.getJSONObject(i).getJSONObject("Source"); //all the info for this movie

                        //I will make a HashMap to store key value pairs.
                        HashMap<String, String> info;
                        info = new HashMap<String, String>();

                        // valuating
                        //String id, String title, List < String > genres, List < String > cast, Time length, String director, String writer, String description, Image poster, Double rating}
                        info.put("Id", "");
                        info.put("Title", "");
                        info.put("Genres", "");
                        info.put("Cast", ""); //List<String> if (Contributors.getJSONObject(j).getString("Job").equals("Writer"))
                        info.put("Runtime", ""); //Time length
                        info.put("Director", ""); //PersonName if (Contributors.getJSONObject(j).getString("Job").equals("Director"))
                        info.put("Description", "");
                        info.put("Image", ""); //Image
                        info.put("IvaRating", "");

                        Iterator it = info.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            System.out.println("");
                            if ("Description".equals((String) entry.getKey())) { // do this for Description, Genre, and Contributors

                                if (result_info.has("Descriptions")) {
                                    info.put((String) entry.getKey(), (String) result_info.getJSONArray("Descriptions").getJSONObject(0).getString("Description"));
                                } else {
                                    info.put((String) entry.getKey(), "No Description available");
                                }
                            } else if ("Genres".equals((String) entry.getKey())) {
                                String g = ""; //comma separated string with list of genres that this result matches
                                if (result_info.has("Genres")) {
                                    JSONArray temp = result_info.getJSONArray("Genres");
                                    for (int j = 0; j < temp.length(); j++) { // for each genre listed, add it to g
                                        g.concat(temp.getString(j) + ",");
                                    }
                                }
                                //add g as the string for Genres
                                if (g.length() > 0) {
                                    info.put((String) entry.getKey(), g);
                                } else {
                                    info.put((String) entry.getKey(), "No Genres available");
                                }
                            } else if ("Cast".equals((String) entry.getKey()) || "Director".equals((String) entry.getKey())) {
                                String cast = null;
                                String director = null;

                                if (result_info.has("Contributors")) {
                                    JSONArray temp = result_info.getJSONArray("Contributors");
                                    for (int j = 0; j < temp.length(); j++) { // for each contributor listed, add its PersonName if it satosfies condition
                                        if (temp.getJSONObject(j).getString("Job").equals("Director")) {
                                            director.concat(temp.getJSONObject(j).getString("PersonName") + ",");
                                        } else if (temp.getJSONObject(j).getString("Job").equals("Actor")) {
                                            cast.concat(temp.getJSONObject(j).getString("PersonName" + ","));
                                        }
                                    }

                                    // cast should be of format "<person>,<person>,
                                    // director should be of format <person>"
                                    if (cast != null && ((String) entry.getKey()).equals("Cast")) {
                                        info.put((String) entry.getKey(), cast);
                                    } else if (cast == null && ((String) entry.getKey()).equals("Cast")) {
                                        info.put((String) entry.getKey(), "No Genres available");
                                    }

                                    if (director != null && ((String) entry.getKey()).equals("Director")) {
                                        info.put((String) entry.getKey(), cast);
                                    } else if (director == null && ((String) entry.getKey()).equals("Director")) {
                                        info.put((String) entry.getKey(), "No Genres available");
                                    }
                                }else{
                                    info.put((String) entry.getKey(), "No " + (String)entry.getKey() + "available");
                                }
                            } else if ("Image".equals((String) entry.getKey())) {
                                String img = null;
                                if (result_info.has("Images")) {
                                    JSONArray temp = result_info.getJSONArray("Images");

                                    if (temp.getJSONObject(0) != null)
                                        img = temp.getJSONObject(0).getString("FilePath");
                                }
                                if (img!= null) {
                                    info.put((String) entry.getKey(), img);
                                } else {
                                    info.put((String) entry.getKey(), "No Images available");
                                }
                            } else {
                                if (result_info.has((String) entry.getKey())) {
                                    info.put((String) entry.getKey(), result_info.getString((String) entry.getKey()));
                                } else {
                                    info.put((String) entry.getKey(), "No " + (String) entry.getKey() + " available");
                                }
                            }
                            System.out.println((String)entry.getKey() + ": " + info.get(entry.getKey()));
                            it.remove(); // avoids a ConcurrentModificationException
                        }

                        //Now info is loaded with all the info for this result

                        // retrieving
                        // int index = info.get("123");
                        // index is 1



                        //Todo: For each result, we want to create it as a Media object and display it with the Scrollview
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