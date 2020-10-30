package com.example.whatdoyouwannawatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Context;
import android.media.Image;
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
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.whatdoyouwannawatch.Media;

public class QueryActivity extends AppCompatActivity {
    NestedScrollView nsv;
    public Context c = this;
    public static ArrayList<Media> mediaList = new ArrayList<Media>();

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
        Log.d("search", value);

        try {
            MainActivity.apiCallSearch(value, new ApiCallback() {
                @Override
                public void onCallback(String res) throws JSONException {
                    //Here is where we will update the view
                    // res is a JSON string containing the search results

                    JSONObject obj = new JSONObject(res); //make it a JSON object
                    JSONArray hits = obj.getJSONArray("Hits"); //The hits are the actual result listings
                    Log.d("search", hits.toString(2));

                    // A For-Loop to iterate through each result from the query
                    // For each result, we are going to extract:

                    // public Media(String id, String title, List<String> genres, List<String> cast, LocalTime length, String director, String writer, String description, Image poster, Double rating) {
                    for (int i = 0; i < hits.length() - hits.length() - 1; i++) {
                        Media m = new Media("", "", new ArrayList<String>(), new ArrayList<String>(), 0, "", "", "", (Image) null, 0.0);
                        JSONObject result_info = hits.getJSONObject(i).getJSONObject("Source"); //all the info for this movie
                        if (i == 0)
                            Log.d("search", result_info.toString());
                        //I will make a HashMap to store key value pairs.
                        HashMap<String, String> info;
                        info = new HashMap<String, String>();

                        // valuating
                        //String id, String title, List < String > genres, List < String > cast, Time length, String director, String writer, String description, Image poster, Double rating}
                        info.put("Id", "");
                        info.put("Title", "");
                        info.put("Genres", ""); // a JSON ARRAY
                        info.put("Cast", ""); //List<String> if (Contributors.getJSONObject(j).getString("Job").equals("Writer"))
                        info.put("Runtime", ""); //Time length
                        info.put("Director", ""); //PersonName if (Contributors.getJSONObject(j).getString("Job").equals("Director"))
                        info.put("Description", "");
                        info.put("Image", ""); //Image
                        info.put("IvaRating", "");

                        Iterator it = info.entrySet().iterator();
                        while (it.hasNext()) { //for each piece of info, load it into the hashmap and Media object
                            Map.Entry entry = (Map.Entry) it.next(); // first is ID, then Title, and so on..
                            Log.d("search", "");
                            //If the key in the hashmap it Description,
                            if ("Id".equals((String) entry.getKey())) {
                                if (result_info.has((String) entry.getKey())) {
                                    //Translate String to desired datatype
                                    m.setId((String) result_info.getString((String) entry.getKey()));

                                } else {
                                    m.setId("No " + (String) entry.getKey() + " available");
                                }
                            } else if ("Title".equals((String) entry.getKey())) {
                                if (result_info.has((String) entry.getKey())) {
                                    m.setTitle((String) result_info.getString((String) entry.getKey()));

                                    info.put((String) entry.getKey(), result_info.getString((String) entry.getKey()));
                                } else {
                                    info.put((String) entry.getKey(), "No " + (String) entry.getKey() + " available");
                                }
                            } else if ("IvaRating".equals((String) entry.getKey())) {
                                if (result_info.has((String) entry.getKey())) {
                                    m.setRating((double) Integer.parseInt(result_info.getString((String) entry.getKey())));
                                    info.put((String) entry.getKey(), result_info.getString((String) entry.getKey()));
                                } else {
                                    info.put((String) entry.getKey(), "No " + (String) entry.getKey() + " available");
                                }
                            } else if ("Description".equals((String) entry.getKey())) { // do this for Description, Genre, and Contributors
                                //  Log.d("search", "Has Descriptions: " + result_info.getJSONArray("Descriptions"));
                                if (result_info.has("Descriptions")) {
                                    m.setDescription((String) result_info.getString((String) entry.getKey()));
                                    info.put((String) entry.getKey(), (String) result_info.getJSONArray("Descriptions").getJSONObject(0).getString("Description"));
                                } else {
                                    info.put((String) entry.getKey(), "No Description available");
                                }
                            } else if ("Genres".equals((String) entry.getKey())) {

                                String g = ""; //comma separated string with list of genres that this result matches
                                //List of Strings
                                ArrayList<String> genres = new ArrayList<String>();
                                if (result_info.has("Genres")) {
                                    JSONArray temp = result_info.getJSONArray("Genres");
                                    for (int j = 0; j < temp.length(); j++) { // for each genre listed, add it to g
                                        genres.add(temp.getString(j));
                                        g.concat(temp.getString(j) + ",");
                                    }

                                    m.setGenres((List<String>)genres);
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
                                        ArrayList<String> c = new ArrayList<String>();
                                        c.add(cast);
                                        m.setCast((List<String>)c);
                                    } else if (cast == null && ((String) entry.getKey()).equals("Cast")) {
                                        info.put((String) entry.getKey(), "No Genres available");
                                    }

                                    if (director != null && ((String) entry.getKey()).equals("Director")) {
                                        info.put((String) entry.getKey(), director);
                                        m.setDirector(director);
                                    } else if (director == null && ((String) entry.getKey()).equals("Director")) {
                                        info.put((String) entry.getKey(), "No Genres available");
                                    }
                                } else {
                                    info.put((String) entry.getKey(), "No " + (String) entry.getKey() + "available");
                                }
                            } else if ("Image".equals((String) entry.getKey())) { //TODO implement the image getter from this API in ITERATION 2
                                String img = null;
                                if (result_info.has("Images")) {
                                    JSONArray temp = result_info.getJSONArray("Images");

                                    if (temp.getJSONObject(0) != null)
                                        img = temp.getJSONObject(0).getString("FilePath");
                                }
                                if (img != null) {
                                    info.put((String) entry.getKey(), img);
                                } else {
                                    info.put((String) entry.getKey(), "No Images available");
                                }
                            }
                            Log.d("search", (String) entry.getKey() + ": " + info.get(entry.getKey()));

                            it.remove(); // avoids a ConcurrentModificationException
                        }

                        //Now info is loaded with all the info for this result

                        mediaList.add(m);
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("search", mediaList.toString());
    }
}