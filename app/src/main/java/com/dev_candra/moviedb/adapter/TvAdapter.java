package com.dev_candra.moviedb.adapter;

import android.content.Context;
import android.media.Rating;
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
import com.dev_candra.moviedb.data.ModelTV;


import java.util.List;

public class TvAdapter extends RecyclerView.Adapter<TvAdapter.ViewHolder> {

    private List<ModelTV>modelTVS;
    private Context context;
    private TvAdapter.onSelectData onSelectData;
    private double rating;

    public TvAdapter(Context context,List<ModelTV> modelTVS,TvAdapter.onSelectData xSelectData){
        this.context = context;
        this.modelTVS = modelTVS;
        this.onSelectData = xSelectData;
    }

    public interface onSelectData{
        void onSelect(ModelTV modelTV);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_film,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelTV modelTV = modelTVS.get(position);
        holder.bind(modelTV);
    }

    @Override
    public int getItemCount() {
        return modelTVS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private ImageView imgPhoto;
        private TextView tvTitle;
        private TextView tvReleaseDate;
        private TextView tvDesc;
        private RatingBar ratingBar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cvFilm);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvReleaseDate = itemView.findViewById(R.id.tvReleaseDate);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }

        private void bind(final ModelTV modelTV){
            rating = modelTV.getVoteAverage();
            tvTitle.setText(modelTV.getName());
            tvDesc.setText(modelTV.getOverview());
            tvReleaseDate.setText(modelTV.getReleaseDate());

            float newValue = (float)rating;
            ratingBar.setNumStars(5);
            ratingBar.setStepSize((float)0.5);
            ratingBar.setRating(newValue / 2);

            Glide.with(context)
                    .load(Service.URLIMAGE + modelTV.getPosterPath())
                    .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image)
                        .transform(new RoundedCorners(16)))
                    .into(imgPhoto);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelectData.onSelect(modelTV);
                }
            });

        }
    }
}
