package com.dev_candra.moviedb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dev_candra.moviedb.R;
import com.dev_candra.moviedb.api.Service;
import com.dev_candra.moviedb.data.ModelMovie;

import java.util.List;

public class MovieTrendingAdapter extends RecyclerView.Adapter<MovieTrendingAdapter.ViewHolder> {

    private List<ModelMovie> modelMovieList;
    private MovieTrendingAdapter.onSelected onSelected;
    private Context context;

    public MovieTrendingAdapter(List<ModelMovie> modelMovie,Context context,MovieTrendingAdapter.onSelected xSelected){
        this.context = context;
        this.modelMovieList = modelMovie;
        this.onSelected = xSelected;
    }

    public interface onSelected{
        void onSelected(ModelMovie modelMovie);
    }

    @NonNull
    @Override
    public MovieTrendingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.list_item_film_horizontal,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrendingAdapter.ViewHolder holder, int position) {
        final ModelMovie modelMovie = modelMovieList.get(position);
        holder.bind(modelMovie);
    }

    @Override
    public int getItemCount() {
        return modelMovieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView gambarTrendingMovie;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gambarTrendingMovie = itemView.findViewById(R.id.imgPhoto);
        }

        private void bind(final ModelMovie modelMovie){

            Glide.with(itemView)
                    .load(Service.URLIMAGE + modelMovie.getPosterPath())
                    .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image)
                        .transform(new RoundedCorners(16)))
                    .into(gambarTrendingMovie);

            gambarTrendingMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelected.onSelected(modelMovie);
                }
            });
        }
    }

}
