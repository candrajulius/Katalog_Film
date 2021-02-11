package com.dev_candra.moviedb.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dev_candra.moviedb.R;
import com.dev_candra.moviedb.actvitites.DetailOrang;
import com.dev_candra.moviedb.api.Service;
import com.dev_candra.moviedb.data.ModelCastinng;

import java.util.List;

public class CastingAdapter extends RecyclerView.Adapter<CastingAdapter.ViewHolder> {

    List<ModelCastinng> modelCastinngs;
    private Context context;
    private  CastingAdapter.onSelect onSelect;

    public CastingAdapter(Context context,List<ModelCastinng> modelCastinngs,CastingAdapter.onSelect xSelect){
        this.context = context;
        this.modelCastinngs = modelCastinngs;
        this.onSelect = xSelect;
    }



    @NonNull
    @Override
    public CastingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_slider_crew,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CastingAdapter.ViewHolder holder, int position) {
        ModelCastinng modelCastinng = modelCastinngs.get(position);
        holder.bind(modelCastinng);
    }

    @Override
    public int getItemCount() {
        return modelCastinngs.size();
    }

    public interface onSelect {
        void onSelected(ModelCastinng modelCastinng);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textPeran;
        private ImageView gambarCasting;
        private CardView card1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textPeran = itemView.findViewById(R.id.characterCrew);
            textName = itemView.findViewById(R.id.namaCrew);
            gambarCasting = itemView.findViewById(R.id.gambar);
            card1 = itemView.findViewById(R.id.card);
        }

        private void bind(final ModelCastinng modelCastinng){

            Glide.with(itemView.getContext())
                    .load(Service.URLIMAGE + modelCastinng.getProfil_path())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_image)
                            .transform(new RoundedCorners(16)))
                    .into(gambarCasting);

            textName.setText(modelCastinng.getName());
            textPeran.setText(modelCastinng.getCharacter());

            card1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelect.onSelected(modelCastinng);
                }
            });

        }
    }
}
