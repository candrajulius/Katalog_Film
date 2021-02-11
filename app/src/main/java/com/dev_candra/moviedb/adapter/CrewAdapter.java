package com.dev_candra.moviedb.adapter;

import android.content.Context;
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
import com.dev_candra.moviedb.api.Service;
import com.dev_candra.moviedb.data.ModelCrew;


import java.util.List;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.ViewHolder> {

    private Context context;
    private List<ModelCrew>modelCrewList;
    private CrewAdapter.onSelect onSelect;

    public CrewAdapter(Context context,List<ModelCrew>modelCrews,CrewAdapter.onSelect xSelect){
        this.context = context;
        this.modelCrewList = modelCrews;
        this.onSelect = xSelect;
    }

    public interface onSelect {
        void onSelected(ModelCrew modelCrew);
    }


    @NonNull
    @Override
    public CrewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_slider_crew,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CrewAdapter.ViewHolder holder, int position) {
        ModelCrew modelCrew = modelCrewList.get(position);
        holder.bind(modelCrew);
    }

    @Override
    public int getItemCount() {
        return modelCrewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textName;
        TextView textJob;
        ImageView gambarOrang;
        CardView cardView1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.namaCrew);
            textJob = itemView.findViewById(R.id.characterCrew);
            gambarOrang = itemView.findViewById(R.id.gambar);
            cardView1 = itemView.findViewById(R.id.card);
        }
        public void bind(final ModelCrew modelCrew){
            textName.setText(modelCrew.getName());
            textJob.setText(modelCrew.getJob());
            Glide.with(itemView)
                    .load(Service.URLIMAGE + modelCrew.getProfil_path())
                    .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image)
                        .transform(new RoundedCorners(16)))
                    .into(gambarOrang);

            cardView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelect.onSelected(modelCrew);
                }
            });

        }
    }
}
