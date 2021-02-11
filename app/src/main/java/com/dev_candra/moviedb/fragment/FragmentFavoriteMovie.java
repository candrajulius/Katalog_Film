package com.dev_candra.moviedb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev_candra.moviedb.R;
import com.dev_candra.moviedb.actvitites.DetailActivity;
import com.dev_candra.moviedb.adapter.MovieAdapter;
import com.dev_candra.moviedb.data.ModelMovie;
import com.dev_candra.moviedb.realm.RealmHelper;

import java.util.ArrayList;
import java.util.List;

public class FragmentFavoriteMovie extends Fragment implements MovieAdapter.onSelectData {

    private RecyclerView rvMovieFav;
    private List<ModelMovie> modelMovies = new ArrayList<>();
    private RealmHelper helper;
    private TextView txtNoData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite_film,container,false);

        initView(view);

        return view;
    }

    private void initView(View view){
        helper = new RealmHelper(getActivity());

        txtNoData = view.findViewById(R.id.tvNotFound);
        rvMovieFav = view.findViewById(R.id.rvMovieFav);
        rvMovieFav.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovieFav.setAdapter(new MovieAdapter(modelMovies,getActivity(),this));
        rvMovieFav.setHasFixedSize(true);
        setData();
    }

    @Override
    public void onSelected(ModelMovie modelMovie) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("detailMovie",modelMovie);
        startActivity(intent);
    }

    private void setData(){
        modelMovies = helper.showFavoriteMovie();
        if (modelMovies.size() == 0){
            txtNoData.setVisibility(View.VISIBLE);
            rvMovieFav.setVisibility(View.GONE);
        }else{
            txtNoData.setVisibility(View.GONE);
            rvMovieFav.setVisibility(View.VISIBLE);
            rvMovieFav.setAdapter(new MovieAdapter(modelMovies,getActivity(),this));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }
}
