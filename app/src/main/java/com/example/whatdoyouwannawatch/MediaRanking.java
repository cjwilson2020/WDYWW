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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MediaRanking extends AppCompatActivity {

    private static ArrayList<Media> mediaList = new ArrayList<Media>(5);
    private String genreList = null;
    private String streamingServiceList = null;
    private String theatreID;
    public static ArrayList<URL> im;

    FirebaseUser fbUser;

    private static final String TAG = "MediaRanking";
    String progTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_ranking);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        Bundle extras = getIntent().getExtras();

        if (extras != null) { //extra passed into this
            genreList = extras.getString("genreList");
            streamingServiceList = extras.getString("streamingServiceList");
            theatreID = extras.getString("theatreID");
            progTypes = extras.getString("progType");
        }
        extras.clear();

        retrieveData();
    }

    private void initRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.ranking_recycler);
        MediaRankingAdapter mediaRankingAdapter = new MediaRankingAdapter(mediaList, this);

        ItemTouchHelper.Callback callback = new MediaRankingTouchHelper(mediaRankingAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        mediaRankingAdapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(mediaRankingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void retrieveData() {
        getMediaList(progTypes, genreList, streamingServiceList, new MediaCallback() {
            @Override
            public void onCallback(final ArrayList<Media> m) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        initRecyclerView();
                    }
                });
            }
        });
    }

    public void onClickRanking(View v) {
        // TODO for testing purposes
        ArrayList<String> toasts = new ArrayList<String>();
        for (Media e : mediaList) {
            toasts.add(e.getTitle());
        }
        Toast.makeText(MediaRanking.this, "" + toasts.toString(), Toast.LENGTH_SHORT).show();

        MainActivity.pullData('t', theatreID, new DataCallback() {
            @Override
            public void onCallback(Object obj) {
                if (obj != null) {
                    Theatre t = (Theatre) obj;
                    List<User> users = t.getUsers();
                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).getUsername().equals(fbUser.getDisplayName())) {
                            users.get(i).setRankings(mediaList);
                        }
                    }
                    MainActivity.pushData(t);
                }
            }
        });

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("theatreID", theatreID);
        intent.putExtra("mediaList", (Serializable) mediaList);
        startActivity(intent);
    }

    public static void getMediaList(String progTypes, String genreList, String streamingServiceList, final MediaCallback mcb) {
        ArrayList<Media> mediaList = new ArrayList<Media>();

        try {
            MainActivity.apiCallSearch(progTypes, genreList, streamingServiceList, new ApiCallback() {
                @Override
                public void onCallback(String res) throws JSONException, IOException {
                    //Here is where we will update the view
                    // res is a JSON string containing the search results

                    JSONObject obj = new JSONObject(res); //make it a JSON object
                    JSONArray hits = obj.getJSONArray("Hits"); //The hits are the actual result listings
                    //Log.d("search", hits.toString(2));

                    // public Media(String id, String title, List<String> genres, List<String> cast, LocalTime length, String director, String writer, String description, Image poster, Double rating) {
                    final int len = hits.length();
                    Log.d("search", "number of results: " + len);

                    for (int i = 0; i < 5; i = i + 1) {
                        Log.d("search", "Result #" + (i + 1));
                        String iden = "";
                        String tit = "";
                        ArrayList<String> gens = new ArrayList<String>();
                        ArrayList<String> cas = new ArrayList<String>();
                        int dur = 0;
                        String director = "";
                        String writer = "";
                        String desc = "";

                        Double rat = 0.0;

                        JSONObject result_info = hits.getJSONObject(i).getJSONObject("Source"); //all the info for this lisiting
                        //Log.d("search", result_info.toString());

                        String[] info = {"Id", "Title", "Cast", "Runtime", "Director", "Description", "Image", "IvaRating"};
                        final int siz = info.length;
                        for (int j = 0; j < siz; j = j + 1) { //each piece of info about current listing
                            Log.d("search", "Info[" + j + "]: " + info[j]);
                            if ("Id".equals(info[j])) {  //Id
                                if (result_info.has(info[j])) {
                                    //Translate String to desired datatype
                                    iden = ((String) result_info.getString(info[j]));
                                } else {
                                    iden = ("No " + info[j] + " available");
                                }
                            } else if ("Title".equals(info[j])) { //Title
                                if (result_info.has(info[j])) {
                                    tit = (String) result_info.getString(info[j]);
                                    //Log.d("search", tit);

                                } else {
                                    tit = ("No " + info[j] + " available");
                                }
                            } else if ("IvaRating".equals(info[j])) { //Rating
                                if (result_info.has(info[j])) {
                                    rat = ((double) Integer.parseInt(result_info.getString(info[j])));
                                } else {
                                    rat = (0.0);
                                }
                            } else if ("Description".equals(info[j])) { // Description
                                //  Log.d("search", "Has Descriptions: " + result_info.getJSONArray("Descriptions"));
                                if (result_info.has("Descriptions") && result_info.getJSONArray("Descriptions").length() > 0) {
                                    desc = ((String) result_info.getJSONArray("Descriptions").getJSONObject(0).getString(info[j]));
                                } else {
                                    desc = ("No " + info[j] + " available");
                                }
                            } else if ("Cast".equals(info[j]) || "Director".equals(info[j])) { //Director, Cast
                                if (result_info.has("Contributors") && result_info.getJSONArray("Contributors").length() > 0) {
                                    JSONArray temp = result_info.getJSONArray("Contributors");
                                    for (int k = 0; k < temp.length(); k++) { // for each contributor listed, add its PersonName if it satosfies condition
                                        if (temp.getJSONObject(k).getString("Job").equals("Director")) {
                                            director.concat(temp.getJSONObject(k).getString("PersonName") + ",");
                                        } else if (temp.getJSONObject(k).getString("Job").equals("Actor")) {
                                            cas.add(temp.getJSONObject(k).getString("PersonName"));
                                        }
                                    }
                                } else {
                                    director = "no Director available";
                                }
                            }
                            if (result_info.has(info[j]))
                                Log.d("search", info[j] + ": " + result_info.getString(info[j]));
                        }

                        String img = null;
                        if (result_info.has("Images") && result_info.getJSONArray("Images").length() > 0) {
                            JSONArray temp = result_info.getJSONArray("Images");
                            if (temp.getJSONObject(0) != null)
                                img = temp.getJSONObject(0).getString("FilePath");

                            if (img != null)
                                Log.d("search", "poster found");
                                //m.setPoster(img);
                            else
                                Log.d("search", "no poster");

                            MainActivity.apiCallImage(img, new ApiCallback() {
                                @Override
                                public void onCallback(String res) throws JSONException, MalformedURLException {
                                    Log.d("img", res);
                                    //res gets the image URL
                                    JSONObject obj = new JSONObject(res); //make it a JSON object
                                    String addr = obj.getString("Url");
                                    Log.d("img", "addr: " + addr);
                                    im.add(new URL(addr));

                                    Log.d("img", "im[0]: " + im.get(im.size()-1));
                                }
                            });
                        }

                        Log.d("img", "im[0] after downloading: ");
                        if(im != null && im.size() > 0) {
                            Media m = new Media(iden, tit, gens, cas, dur, director, writer, desc, im.get(im.size() - 1), rat);
                            Log.d("search", "m.Title = " + m.getTitle());
                            MediaRanking.mediaList.add(m);

                            Log.d("search", " returned Medias: " + MediaRanking.mediaList.size());
                            mcb.onCallback(MediaRanking.mediaList);
                        }else{
                            Media m = new Media(iden, tit, gens, cas, dur, director, writer, desc, null, rat);
                            Log.d("search", "m.Title = " + m.getTitle());
                            MediaRanking.mediaList.add(m);

                            Log.d("search", " returned Medias: " + MediaRanking.mediaList.size());
                            mcb.onCallback(MediaRanking.mediaList);
                        }

                    }
                }
            });
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}


class MediaRankingAdapter extends RecyclerView.Adapter<MediaRankingAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private static final String TAG = "RankingAdapter";

    private ArrayList<Media> mediaList;
    private Context context;
    private ItemTouchHelper itemTouchHelper;


    public MediaRankingAdapter(ArrayList<Media> list, Context con) {
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

        holder.text.setText(mediaList.get(position).getTitle());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + mediaList.get(position));
            }
        });

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MediaDetails.class);
                Gson gson = new Gson();
                String json = gson.toJson(mediaList.get(position));
                intent.putExtra("data", json);
                context.startActivity(intent);

                Toast.makeText(context, mediaList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<Media> getMediaList() {
        return mediaList;
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Media media = mediaList.get(fromPosition);
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
