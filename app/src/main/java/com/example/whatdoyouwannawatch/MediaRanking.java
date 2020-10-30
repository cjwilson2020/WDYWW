package com.example.whatdoyouwannawatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MediaRanking extends AppCompatActivity {

    private static ArrayList<Media> mediaList = new ArrayList<Media>(5);
    private String genreList = null;
    private String streamingServiceList = null;
    private static final String TAG = "MediaRanking";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_ranking);

        Bundle extras = getIntent().getExtras();

        if (extras != null) { //extra passed into this
            genreList = extras.getString("genreList");
            streamingServiceList = extras.getString("streamingServiceList");
        }

        retrieveData();
    }

    private void initRecyclerView(ArrayList<String> titles) {
        Log.d(TAG, "titles: " + titles.toString());

        ArrayList<String> fill = new ArrayList<String>();
        fill.add("title1");
        fill.add("title2");
        fill.add("title3");
        fill.add("title4");
        fill.add("title5");

        RecyclerView recyclerView = findViewById(R.id.ranking_recycler);
        MediaRankingAdapter mediaRankingAdapter = new MediaRankingAdapter(fill, this);

        ItemTouchHelper.Callback callback = new MediaRankingTouchHelper(mediaRankingAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        mediaRankingAdapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(mediaRankingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String res = "";
        for (String s : titles) {
            res = res + s + ", ";
        }
        res = res.trim();
        Toast.makeText(this, res, Toast.LENGTH_LONG);
    }

    private void retrieveData() {
        // ArrayList<String> titleList = new ArrayList<>(5);
        getMediaList(genreList, streamingServiceList, new MediaCallback() {
            @Override
            public void onCallback(final ArrayList<Media> m) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Log.d(TAG, "retrieveData: mediaList size: " + m.size());
                        Iterator i = mediaList.iterator();
                        ArrayList<String> tmp = new ArrayList<>(5);
                        while (i.hasNext()) {
                            Media j = (Media) i.next();
                            tmp.add(j.getTitle());
                        }
                        Log.d(TAG, "retrieveDate: " + tmp.toString());

                        initRecyclerView(tmp);
                    }
                });
            }
        });
    }

    public void onClickRanking(View v) {
        // TODO push mediaList to Theatre
        Toast.makeText(MediaRanking.this, "" + mediaList.toString(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ResultActivity.class);
        startActivity(intent);
    }


    public static void getMediaList(String genreList, String streamingServiceList, final MediaCallback mcb) {
        ArrayList<Media> mediaList = new ArrayList<Media>();
        try {
            MainActivity.apiCallSearch(genreList, streamingServiceList, new ApiCallback() {
                @Override
                public void onCallback(String res) throws JSONException, IOException {
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
                                    m.setGenres((List<String>) genres);
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
                                        m.setCast((List<String>) c);
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

                                    MainActivity.apiCallImage(img, new ApiCallback() {
                                        @Override
                                        public void onCallback(String res) throws JSONException {
                                            Log.d("img", res);
                                        }
                                    });
                                }
                                if (img != null)
                                    info.put((String) entry.getKey(), img);
                                else
                                    info.put((String) entry.getKey(), "No Images available");
                            }
                            Log.d("search", (String) entry.getKey() + ": " + info.get(entry.getKey()));
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        Log.d("MainActivity.getMediaList()", m.getTitle());
                        MediaRanking.mediaList.add(m);
                    }
                    Log.d("MainActivity.getMediaList()", " returned Medias: " + MediaRanking.mediaList.size());
                    mcb.onCallback(MediaRanking.mediaList);
                }
            });
            Log.d("search", mediaList.toString());
            // return mediaList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return null;
    }
}


class MediaRankingAdapter extends RecyclerView.Adapter<MediaRankingAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private static final String TAG = "RankingAdapter";

    private ArrayList<String> mediaList;
    private Context context;
    private ItemTouchHelper itemTouchHelper;


    public MediaRankingAdapter(ArrayList<String> list, Context con) {
        mediaList = list;
        context = con;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_media_ranking_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder called");

        holder.text.setText(mediaList.get(position));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + mediaList.get(position));
            }
        });
    }

    public ArrayList<String> getMediaList() {
        return mediaList;
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        String media = mediaList.get(fromPosition);
        mediaList.remove(fromPosition);
        mediaList.add(toPosition, media);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void setTouchHelper(ItemTouchHelper helper) {
        this.itemTouchHelper = helper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener {
        TextView text;
        Button btn;
        RelativeLayout layout;
        GestureDetector gestureDetector;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.text_candidate_media);
            btn = itemView.findViewById(R.id.btn_media_details);
            layout = itemView.findViewById(R.id.media_ranking_layout);

            gestureDetector = new GestureDetector(itemView.getContext(), this);
            itemView.setOnTouchListener(this);
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            itemTouchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            gestureDetector.onTouchEvent(motionEvent);
            return true;
        }
    }
}

class MediaRankingTouchHelper extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter adapter;

    public MediaRankingTouchHelper(ItemTouchHelperAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.cardview_shadow_end_color));
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.colorAccent));
        }
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.UP | ItemTouchHelper.DOWN);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
