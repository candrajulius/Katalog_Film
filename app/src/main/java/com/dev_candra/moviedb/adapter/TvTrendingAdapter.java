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
import com.dev_candra.moviedb.data.ModelTV;

import java.util.List;

public class TvTrendingAdapter extends RecyclerView.Adapter<TvTrendingAdapter.ViewHolder> {

   private Context context;
   private List<ModelTV>modelTVS;
   private TvTrendingAdapter.onSelected onSelected;


   public TvTrendingAdapter(Context context, List<ModelTV> modelTVList,TvTrendingAdapter.onSelected xSelected){
       this.context = context;
       this.modelTVS = modelTVList;
       this.onSelected = xSelected;
   }

   public interface onSelected {
       void onSelect(ModelTV modelTV);
   }


    @NonNull
    @Override
    public TvTrendingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.list_item_film_horizontal,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TvTrendingAdapter.ViewHolder holder, int position) {
       ModelTV modelTV = modelTVS.get(position);
       holder.bind(modelTV);
    }

    @Override
    public int getItemCount() {
        return modelTVS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgPhoto);
        }

        private void bind(final ModelTV modelTV){

            Glide.with(itemView)
                    .load(Service.URLIMAGE + modelTV.getPosterPath())
                    .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image)
                        .transform(new RoundedCorners(16)))
                    .into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelected.onSelect(modelTV);
                }
            });
        }
    }
}
