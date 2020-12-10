package com.example.whatdoyouwannawatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MediaRanking extends AppCompatActivity implements AsyncResponse {

    public static ArrayList<Media> mediaList = new ArrayList<Media>(5);
    private String genreList = null;
    private String streamingServiceList = null;
    private String theatreID;
    public static ArrayList<URL> im = new ArrayList<>();
    ProgressDialog p;
    MediaApiAsyncTask aTask;
    FirebaseUser fbUser;
    private static String r;
    private static String i;
    AsyncResponse asyncResponse1;
    mCallBack mcb;


    private static final String TAG = "MediaRanking";
    String progTypes;

    class mCallBack implements MediaCallback {

        @Override
        public void onCallback(ArrayList<Media> m) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initRecyclerView();
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_ranking);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        Bundle extras = getIntent().getExtras();
        mcb = new mCallBack();
        if (extras != null) { //extra passed into this
            genreList = extras.getString("genreList");
            streamingServiceList = extras.getString("streamingServiceList");
            theatreID = extras.getString("theatreID");
            progTypes = extras.getString("progType");
        }
        extras.clear();

        asyncResponse1 = new AsyncResponse() {


            @Override
            public void processFinish(String result) throws InterruptedException {
                p.cancel();
                if (result == null){
                    Toast.makeText(MediaRanking.this, "No results found from this query", Toast.LENGTH_SHORT).show();
                }else {
                    //Here you will receive the result fired from async class
                    //of onPostExecute(result) method.
                    Toast.makeText(MediaRanking.this, "Your titles finished downloading.", Toast.LENGTH_LONG).show();
                    Log.d("search", "Your titles finished downloading.");
                    try {

                        JSONObject obj = new JSONObject(result); //make it a JSON object
                        JSONArray hits = obj.getJSONArray("Hits"); //The hits are the actual result listings
                        //Log.d("search", hits.toString(2));

                        //public Media(String id, String title, List<String> genres, List<String> cast, LocalTime length, String director, String writer, String description, Image poster, Double rating) {
                        final int len = hits.length();
                        Log.d("search", "number of results: " + len);

                        for (int i = 0; i < Math.min(5, hits.length()); i = i + 1) {
                            Log.d("search", "Result #" + (i + 1));

                            String imgPath = "";
                            String identity = "";
                            String title = "";
                            String year = "";
                            ArrayList<String> genres = new ArrayList<String>();
                            ArrayList<String> cast = new ArrayList<String>();
                            int duration = 0;
                            String language = "";
                            String director = "";
                            String writer = "";
                            String desc = "";
                            Double rating = 0.0;
                            Media m = new Media(identity, title, genres, cast, duration, director, writer, desc, imgPath, rating);

                            final JSONObject result_info = hits.getJSONObject(i).getJSONObject("Source"); //all the info for this listing

                            String[] info = {"Id", "Title", "Year", "IvaRating", "Runtime", "OriginalLanguage", "Description", "Director", "Cast", "Genres", "Poster"};
                            final int siz = info.length;

                            for (int j = 0; j < siz; j = j + 1) { //each piece of info we want about the current listing
                                if ("Id".equals(info[j])) {  //Id
                                    if (result_info.has(info[j])) {
                                        identity = ((String) result_info.getString(info[j]));
                                    } else {
                                        identity = ("No " + info[j] + " available");
                                    }
                                    String type;
                                    if (identity.contains("Movie"))
                                        type = "Movie";
                                    else if (identity.contains("Show"))
                                        type = "Show";
                                    else
                                        type = "Type unavailable";
                                    m.setType(type);
                                    m.setId(identity);
                                    Log.d("search", info[j] + ": " + identity);
                                } else if ("Title".equals(info[j])) { //Title
                                    if (result_info.has(info[j])) {
                                        title = (String) result_info.getString(info[j]);
                                    } else {
                                        title = ("No " + info[j] + " available");
                                    }
                                    m.setTitle(title);
                                    Log.d("search", info[j] + ": " + title);

                                } else if ("Year".equals(info[j])) {
                                    if (result_info.has(info[j])) {
                                        year = ((Integer) result_info.getInt(info[j])).toString();
                                    } else {
                                        year = ("No " + info[j] + " available");
                                    }
                                    m.setYear(year);
                                    Log.d("search", info[j] + ": " + year);

                                } else if ("IvaRating".equals(info[j])) { //Rating
                                    if (result_info.has(info[j])) {
                                        rating = (double) result_info.getInt(info[j]);
                                        Log.d("search", info[j] + ": " + rating);
                                    } else {
                                        rating = (0.0);

                                        Log.d("search", info[j] + ": No Rating information available");
                                    }
                                    m.setRating(rating);

                                } else if ("Runtime".equals(info[j])) { //Runtime
                                    if (result_info.has(info[j])) {
                                        duration = result_info.getInt(info[j]);
                                        Log.d("search", info[j] + ": " + duration);
                                    } else {
                                        duration = 0;

                                        Log.d("search", info[j] + ": No Runtime information available");
                                    }
                                    m.setLength(duration);

                                } else if ("Language".equals(info[j])) { //Language
                                    if (result_info.has(info[j])) {
                                        language = result_info.getString("OriginalLanguage");
                                    } else {
                                        language = "No " + info[j] + " available";
                                    }
                                    Log.d("search", info[j] + ": " + language);
                                    m.setLanguage(language);

                                } else if ("Description".equals(info[j])) { // Description
                                    if (result_info.has("Descriptions")) {
                                        JSONArray temp = result_info.getJSONArray("Descriptions");
                                        if (temp.length() > 0) {
                                            desc = ((String) result_info.getJSONArray("Descriptions").getJSONObject(0).getString(info[j]));
                                        } else {
                                            desc = ("No " + info[j] + " available");
                                        }

                                    } else {
                                        desc = ("No " + info[j] + " available");
                                    }
                                    m.setDescription(desc);
                                    Log.d("search", info[j] + ": " + desc);
                                } else if ("Cast".equals(info[j]) || "Director".equals(info[j])) { //Director, Cast
                                    if (result_info.has("Contributors")) {
                                        JSONArray temp = result_info.getJSONArray("Contributors");
                                        if (temp.length() > 0) {
                                            for (int k = 0; k < temp.length(); k++) { // for each contributor listed, add its PersonName if it satisfies condition
                                                if (temp.getJSONObject(k).getString("Job").equals("Director")) {
                                                    director.concat(temp.getJSONObject(k).getString("PersonName") + ",");
                                                    Log.d("search", info[j] + ": " + director);
                                                } else if (temp.getJSONObject(k).getString("Job").equals("Actor")) {
                                                    cast.add(temp.getJSONObject(k).getString("PersonName"));
                                                    Log.d("search", info[j] + ": " + cast);
                                                }
                                            }
                                        }
                                    } else {
                                        director = "No director information available";
                                        cast.add("No Cast information available");
                                    }
                                    m.setDirector(director);
                                    m.setCast(cast);
                                } else if ("Genres".equals(info[j])) { // Description
                                    if (result_info.has(info[j])) {
                                        JSONArray temp = result_info.getJSONArray(info[j]);
                                        if (temp.length() > 0) {
                                            for (int k = 0; k < temp.length(); k++) {
                                                genres.add(temp.getString(k));
                                            }
                                        } else {
                                            genres.add("No " + info[j] + " information available");
                                        }
                                    } else {
                                        genres.add("No " + info[j] + " information available");
                                    }
                                    m.setGenres(genres);
                                    Log.d("search", info[j] + ": " + genres.toString());
                                } else if ("Poster".equals(info[j])) {
                                    if (result_info.has("Images") && result_info.getJSONArray("Images").length() > 0) {
                                        JSONArray temp = result_info.getJSONArray("Images");
                                        imgPath = temp.getJSONObject(0).getString("FilePath");

                                        m.setPoster(imgPath);

                                    } else {
                                        imgPath = "No poster found";
                                    }
                                    Log.d("search", info[j] + ": " + imgPath);
                                }
                            }


                            mediaList.add(m);

                        }
                        Log.d("search", "List of media retrieved: " + mediaList.toString());
                        mcb.onCallback(mediaList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

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
        getMediaList(progTypes, genreList, streamingServiceList, mcb);
    }

    public void onClickRanking(View v) {
        // TODO for testing purposes
        ArrayList<String> toasts = new ArrayList<String>();
        for (Media e : mediaList) {
            toasts.add(e.getTitle());
        }
        // Toast.makeText(MediaRanking.this, "" + toasts.toString(), Toast.LENGTH_SHORT).show();

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

    @Override
    public void processFinish(String result) throws InterruptedException {

    }

    private class RequestTokenInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            Log.d("search", "Request info before refresh: " + request.headers());

            String newA = "https://ivaee-internet-video-archive-entertainment-v1.p.rapidapi.com/entertainment/search/?Includes=Runtime%2CImages%2CContributors%2CYear%2CGenres%2CDescriptions%2CIvaRating%2COriginalLanguage&OfferTypes=Subscription";
            if (progTypes != null && progTypes.length() > 2){
                String pt = "&ProgramTypes=" + progTypes.replaceAll(",", "%2C");
                newA = newA.concat(pt.replaceAll("\\s+",""));
            }
            if(genreList.length()> 0 ){
                String g = "&Genres=" + genreList.replaceAll(",", "%2C");
                newA = newA.concat(g.replaceAll("\\s+",""));
            }
            if(streamingServiceList != null && streamingServiceList.length() > 0){
                String pr = "&Providers=" + streamingServiceList.replaceAll(",", "%2C");
                newA = newA.concat(pr.replaceAll("\\s+",""));
            }

            Request newRequest = request.newBuilder()
                    .url( newA)
                    .header("content-type", "application/json")
                    .header("x-rapidapi-key", "4a8ffa13admsh40c5848568afe5ap104e50jsne0c10b5828d5")
                    .header("x-rapidapi-host", "ivaee-internet-video-archive-entertainment-v1.p.rapidapi.com")
                    .header("Genres", request.headers().get("Genres"))
                    .header("ProgramTypes", request.headers().get("ProgramTypes"))
                    .header("Providers", request.headers().get("Providers"))
                    .get()
                    .build();

            Log.d("search", "Request info after refresh: " + newRequest.headers());
            long t1 = System.nanoTime();
            Log.d("search", String.format("Sending request %s on %s%n%s",
                    newRequest.url(), chain.connection(), newRequest.headers()));

            Response response = chain.proceed(newRequest);

            long t2 = System.nanoTime();
            Log.d("search", String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    }


    private class MediaApiAsyncTask extends AsyncTask<String, Void, String> {
        String r;
        public AsyncResponse delegate = null;
        // you may separate this or combined to caller class.

        public MediaApiAsyncTask(AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(MediaRanking.this);
            p.setMessage("Please Wait, fetching titles...");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0].replaceAll("\\s+","");
            String progTypes = strings[1].replaceAll("\\s+","");
            String genres = strings[2].replaceAll("\\s+","");
            String providers = strings[3].replaceAll("\\s+","");

            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new RequestTokenInterceptor())
                    .build();
            //A client for networking with the Api online

            Request.Builder builder = new Request.Builder()
                    .url(address)
                    .get();
            builder
                    .header("content-type", "application/json")
                    .header("x-rapidapi-key", "4a8ffa13admsh40c5848568afe5ap104e50jsne0c10b5828d5")
                    .header("x-rapidapi-host", "ivaee-internet-video-archive-entertainment-v1.p.rapidapi.com")
                    .header("Genres", genres)
                    .header("ProgramTypes", progTypes)
                    .header("Providers", providers)
                    .header("SortBy", "Relevance")
                    .header("Includes", "Runtime,Images,Contributors,Year,Genres,Descriptions,IvaRating,OriginalLanguage");
            Request request = builder.build();

            Response response = null;
            try {
                Call call = client.newCall(request);
                response = call.execute();

                if (response.isSuccessful()) {
                    //Log.d("search", response.body().toString());
                    try (ResponseBody responseBody = response.body()) {

                        String results = responseBody.string();
                        Log.d("search", String.format("Received response for %s%n%s",
                                response.request().url(), results));
                        r = results;
                        if (r != null && r.length() > 0) {
                            return r;
                        }

                    } catch (IOException i) {
                        i.printStackTrace();
                    }

                } else {
                    Log.d("search", "Response not successful");
                    ResponseBody bod = response.body();
                    Log.d("search", bod.string());
                    throw new IOException("Unexpected code " + response);
                }
                Log.d("search", request.toString());
                String r = response.body().string();
                response.body().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("search", "Post execute String: " + s);
            
            try {
                p.dismiss();
                delegate.processFinish(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            MediaRanking.r = r;
            Log.d("search", "Canceled, returned: " + r);
            p.cancel();
            //Toast.makeText(MediaRanking.this, "AsyncTask is stopped", Toast.LENGTH_LONG).show();
            this.cancel(true);

        }
    }

    public interface AsyncResponse {
        void processFinish(String output) throws InterruptedException;
    }

    public void getMediaList(String progTypes, String genreList, String streamingServiceList, final MediaCallback mcb) {
        ArrayList<Media> mediaList = new ArrayList<Media>();
        if (genreList != null)
            genreList = genreList.replaceAll("\\s+", "");
        if (streamingServiceList != null)
            streamingServiceList = streamingServiceList.replaceAll("\\s+", "");


        Log.d("search", "genres: " + genreList);
        Log.d("search", "providers: " + streamingServiceList);
        Log.d("search", "progTypes: " + progTypes);

        String address = "https://ivaee-internet-video-archive-entertainment-v1.p.rapidapi.com/entertainment/search/?SortBy=Relevance&Includes=Runtime%2CImages%2CContributors%2CYear%2CGenres%2CDescriptions%2CIvaRating%2COriginalLanguage&ProgramTypes=Movie%2CShow";

        aTask = new MediaApiAsyncTask(MediaRanking.this.asyncResponse1);
        aTask.execute(address, progTypes, genreList, streamingServiceList);

        Log.d("search", "Api Search Results: " + r);

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

        holder.media = mediaList.get(position);

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

        String list = "";
        for (Media m : mediaList) {
            list += m.getTitle();
        }
        //Toast.makeText(context, list, Toast.LENGTH_SHORT).show();
    }

    public void setTouchHelper(ItemTouchHelper helper) {
        this.itemTouchHelper = helper;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener {
        TextView text;
        Button btn;
        RelativeLayout layout;
        GestureDetector gestureDetector;
        Media media;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.text_candidate_media);
            layout = itemView.findViewById(R.id.media_ranking_layout);


            btn = itemView.findViewById(R.id.btn_media_details);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MediaDetails.class);
                    Gson gson = new Gson();
                    String json = gson.toJson(media);
                    intent.putExtra("data", json);
                    context.startActivity(intent);
                }
            });

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
