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

public class TvHorizontalAdapter extends RecyclerView.Adapter<TvHorizontalAdapter.ViewHolder> {

    private List<ModelTV> modelTv;
    private Context mConttext;
    private TvHorizontalAdapter.onSelectData onSelectData;


    public TvHorizontalAdapter(Context mConttext,List<ModelTV> modelTVS,TvHorizontalAdapter.onSelectData xSelectdata){
        this.mConttext = mConttext;
        this.modelTv = modelTVS;
        this.onSelectData = xSelectdata;
    }


    public interface onSelectData {
        void onSelect(ModelTV modelTV);
    }

    @NonNull
    @Override
    public TvHorizontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_film_horizontal,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TvHorizontalAdapter.ViewHolder holder, int position) {
        final ModelTV dataTv = modelTv.get(position);
        holder.bind(dataTv);
    }

    @Override
    public int getItemCount() {
        return modelTv.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
        }

        private void bind(final ModelTV modelTV){
            Glide.with(mConttext)
                    .load(Service.URLIMAGE + modelTV.getPosterPath())
                    .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image)
                        .transform(new RoundedCorners(16)))
                    .into(imgPhoto);

            imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelectData.onSelect(modelTV);
                }
            });
        }

    }
}
