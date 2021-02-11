package com.dev_candra.moviedb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dev_candra.moviedb.R;
import com.dev_candra.moviedb.api.Service;
import com.dev_candra.moviedb.data.ModelMovie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<ModelMovie> movieList;
    private static MovieAdapter.onSelectData onSelectData;
    private Context context;
    private double Rating;

    public MovieAdapter(List<ModelMovie> modelMovieList,Context context,MovieAdapter.onSelectData xSelectData){
        this.context = context;
        this.movieList = modelMovieList;
        this.onSelectData = xSelectData;
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_film,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        final ModelMovie data = movieList.get(position);

        Rating = data.getVoteAverage();
        holder.tvTitle.setText(data.getTittle());
        holder.tvDesc.setText(data.getOverview());
        holder.tvRealeseDate.setText(data.getReleaseDate());

        float newValue = (float)Rating;
        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setStepSize((float)0.5);
        holder.ratingBar.setRating(newValue / 2);

        Glide.with(context)
                .load(Service.URLIMAGE + data.getPosterPath())
                .apply(new RequestOptions()
                    .placeholder(R.drawable.ic_image)
                    .transform(new RoundedCorners(16)))
                .into(holder.imgPhoto);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectData.onSelected(data);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public interface onSelectData {
        void onSelected(ModelMovie modelMovie);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView imgPhoto;
        private TextView tvTitle;
        private TextView tvRealeseDate;
        private TextView tvDesc;
        private RatingBar ratingBar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cvFilm);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvRealeseDate = itemView.findViewById(R.id.tvReleaseDate);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
