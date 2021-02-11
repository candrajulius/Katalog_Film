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

public class MovieHorizontalAdapter extends RecyclerView.Adapter<MovieHorizontalAdapter.ViewHolder> {

    private List<ModelMovie> items;
    private MovieHorizontalAdapter.onSelectData onSelectData;
    private Context mContext;

    public MovieHorizontalAdapter(List<ModelMovie> items,Context mContext,MovieHorizontalAdapter.onSelectData xSelectData){
        this.mContext = mContext;
        this.items = items;
        this.onSelectData = xSelectData;
    }

    public interface onSelectData{
        void onSelected(ModelMovie modelMovie);
    }

    @NonNull
    @Override
    public MovieHorizontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_film_horizontal,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHorizontalAdapter.ViewHolder holder, int position) {
        final  ModelMovie modelMovie = items.get(position);

        Glide.with(mContext)
                .load(Service.URLIMAGE + modelMovie.getPosterPath())
                .apply(new RequestOptions()
                    .placeholder(R.drawable.ic_image)
                    .transform(new RoundedCorners(16)))
                .into(holder.imgPhoto);

        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectData.onSelected(modelMovie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgPhoto;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPhoto = itemView.findViewById(R.id.imgPhoto);
        }
    }
}
