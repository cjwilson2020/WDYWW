package com.example.whatdoyouwannawatch;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MediaRankingAdapter extends RecyclerView.Adapter<MediaRankingAdapter.ViewHolder>{

    private static final String TAG = "RankingAdapter";

    private ArrayList<String> mediaList;
    private Context context;


    public MediaRankingAdapter(ArrayList<String> list, Context con){
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
                Toast.makeText(context, mediaList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView text;
        Button btn;
        RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.text_candidate_media);
            btn = itemView.findViewById(R.id.btn_media_details);
            layout = itemView.findViewById(R.id.media_ranking_layout);
        }
    }
}
