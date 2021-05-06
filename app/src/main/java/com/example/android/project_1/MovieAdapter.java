package com.example.android.project_1;

/**
 * Created by amr5aled on 3/5/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.List;

/**
 * Created by amr5aled on 3/4/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context context;
    List<movies> movs;
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tile,rate;
        ImageView photo;

        public ViewHolder(View itemView) {
            super(itemView);
            tile=(TextView)itemView.findViewById(R.id.title);
            rate=(TextView)itemView.findViewById(R.id.userrating);
            photo=( ImageView)itemView.findViewById(R.id.thumbnail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pos=getAdapterPosition();
                  movies clickedDataItem = movs.get(pos);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("movies", clickedDataItem );
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);


                }
            });
        }
    }




    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.mov_card,parent,false);
        return new ViewHolder(view);
    }

    public MovieAdapter(Context context, List<movies> movs) {
        this.context = context;
        this.movs = movs;
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        movies p=movs.get(position);
        String poster = "https://image.tmdb.org/t/p/w500" +p.getPosterPath();

        Glide.with(context)
                .load(poster).into( holder.photo);
        holder.tile.setText(p.getOriginalTitle());
        holder.rate.setText(""+p.getVoteAverage());

    }

    @Override
    public int getItemCount() {
        return movs.size();
    }
}
