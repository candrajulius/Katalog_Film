package com.dev_candra.moviedb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev_candra.moviedb.R;
import com.dev_candra.moviedb.actvitites.ModelTelevisionTv;
import com.dev_candra.moviedb.adapter.TvAdapter;
import com.dev_candra.moviedb.data.ModelTV;
import com.dev_candra.moviedb.realm.RealmHelper;

import java.util.ArrayList;
import java.util.List;

public class FragmentFavoriteTv extends Fragment implements TvAdapter.onSelectData {

    private RecyclerView rvMovieFav;
    private List<ModelTV> modelTVS = new ArrayList<>();
    private RealmHelper helper;
    private TvAdapter tvAdapter;
    private TextView textNotData;



    public FragmentFavoriteTv(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite_film,container,false);

        helper = new RealmHelper(getActivity());
        textNotData = view.findViewById(R.id.tvNotFound);
        tvAdapter = new TvAdapter(getActivity(),modelTVS,this);

        setRecyclerView(view);
        return view;
    }

    @Override
    public void onSelect(ModelTV modelTV) {
        Intent intent = new Intent(getActivity(), ModelTelevisionTv.class);
        intent.putExtra("detailTv",modelTV);
        startActivity(intent);
    }

    private void setRecyclerView(View view){
        rvMovieFav =  view.findViewById(R.id.rvMovieFav);
        rvMovieFav.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovieFav.setAdapter(tvAdapter);
        rvMovieFav.setHasFixedSize(true);
        setData();
    }

    private void setData(){
        modelTVS = helper.showFavoriteTV();
        if (modelTVS.size() == 0) {
            textNotData.setVisibility(View.VISIBLE);
            rvMovieFav.setVisibility(View.GONE);
        }else{
            textNotData.setVisibility(View.GONE);
            rvMovieFav.setVisibility(View.VISIBLE);
            rvMovieFav.setAdapter(new TvAdapter(getActivity(),modelTVS,this));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }
}
