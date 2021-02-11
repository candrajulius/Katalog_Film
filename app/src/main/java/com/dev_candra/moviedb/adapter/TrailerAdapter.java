package com.dev_candra.moviedb.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev_candra.moviedb.R;
import com.dev_candra.moviedb.data.ModelTrailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private List<ModelTrailer> modelTrailers;
    private Context context;


    public TrailerAdapter(List<ModelTrailer> trailers,Context context){
        this.context = context;
        this.modelTrailers = trailers;
    }

    @NonNull
    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trailer,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.ViewHolder holder, int position) {
        final ModelTrailer modelTrailer = modelTrailers.get(position);
        holder.bind(modelTrailer);
    }

    @Override
    public int getItemCount() {
        return modelTrailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Button btnTrailer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnTrailer = itemView.findViewById(R.id.btnTrailer);
        }

        public void bind(final ModelTrailer modelTrailer){
            btnTrailer.setText(modelTrailer.getType());
            btnTrailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + modelTrailer.getKey()));
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
