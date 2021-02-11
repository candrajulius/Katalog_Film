package com.dev_candra.moviedb.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.dev_candra.moviedb.R;
import com.dev_candra.moviedb.actvitites.ModelTelevisionTv;
import com.dev_candra.moviedb.adapter.TvAdapter;
import com.dev_candra.moviedb.adapter.TvHorizontalAdapter;
import com.dev_candra.moviedb.adapter.TvTrendingAdapter;
import com.dev_candra.moviedb.api.Service;
import com.dev_candra.moviedb.data.ModelTV;
import com.dev_candra.moviedb.utils.Method;
import com.google.gson.JsonArray;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentTv extends Fragment implements TvHorizontalAdapter.onSelectData, TvAdapter.onSelectData,TvTrendingAdapter.onSelected {

    private RecyclerView rvNowPlaying,rvFilmRecomended,rvTrendingHariIni,rvTrendingMingguIni;
    private TvHorizontalAdapter tvHorizontalAdapter;
    private List<ModelTV> tvPlayNow;
    private List<ModelTV> tvPopuler;
    private TvAdapter tvAdapter;
    private Method method;
    private SearchView searchFilm;
    private TextView text1;
    private TvTrendingAdapter trendingAdapter;
    private List<ModelTV>modelTvTrending;



    public FragmentTv(){}



    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_film,container,false);

        method = new Method(getActivity());
        tvPlayNow = new ArrayList<>();
        tvPopuler = new ArrayList<>();
        modelTvTrending = new ArrayList<>();
        text1 = view.findViewById(R.id.rekomendasiFilm);

        text1.setText("Rekomendasi Film TV");

        method.makeDialog("Mohon Tunggu","Sedang menampilkan data");
        handleSearch(view);


        return view;
    }

    private void handleSearch(View view){
        searchFilm = view.findViewById(R.id.searchFilm);
        searchFilm.setQueryHint(getString(R.string.search_film));
        searchFilm.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.equals("")){
                    getFilmTV();
                }else {
                    setSearchTV(s);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")){
                    getFilmTV();
                }else{
                    setSearchTV(s);
                }
                return true;
            }
        });
        int searchPlateId = searchFilm.getContext().getResources()
                .getIdentifier("android:id/search_plate",null,null);
        View searchPlate = searchFilm.findViewById(searchPlateId);

        if (searchPlate != null){
            searchPlate.setBackgroundColor(Color.TRANSPARENT);
        }

        setRecyclerView(view);

    }

    private void setRecyclerView(View view){
        rvNowPlaying = view.findViewById(R.id.rvNowPlaying);
        rvNowPlaying.setHasFixedSize(true);
        rvNowPlaying.setLayoutManager(new CardSliderLayoutManager(getActivity()));

        rvFilmRecomended = view.findViewById(R.id.rvFilmRecommend);
        rvFilmRecomended.setHasFixedSize(true);
        rvFilmRecomended.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvTrendingHariIni = view.findViewById(R.id.rvTrendingHariIni);
        rvTrendingHariIni.setHasFixedSize(true);
        rvTrendingHariIni.setLayoutManager(new CardSliderLayoutManager(getActivity()));
        new CardSnapHelper().attachToRecyclerView(rvTrendingHariIni);

        rvTrendingMingguIni = view.findViewById(R.id.rvTrendingMingguIni);
        rvTrendingMingguIni.setLayoutManager(new CardSliderLayoutManager(getActivity()));
        new CardSnapHelper().attachToRecyclerView(rvTrendingMingguIni);

        getTvHorizontal();
        getFilmTV();
        getTrendingTvForToday();
        getTrendingForWeek();
    }


    private void getTrendingTvForToday(){
        method.dialogShow();
        AndroidNetworking.get(Service.BASEURL + Service.TV_TRENDING + Service.API_KEY)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            method.dialogDismis();
                            modelTvTrending = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("EEE, d MMMM yyyy");
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("first_air_date");
                                ModelTV modelTV = new ModelTV();
                                modelTV.setId(jsonObject.getInt("id"));
                                modelTV.setName(jsonObject.getString("name"));
                                modelTV.setOverview(jsonObject.getString("overview"));
                                modelTV.setBackdropPath(jsonObject.getString("backdrop_path"));
                                modelTV.setPopularity(jsonObject.getString("popularity"));
                                modelTV.setReleaseDate(format.format(Objects.requireNonNull(simpleDateFormat.parse(datePost))));
                                modelTV.setVoteAverage(jsonObject.getDouble("vote_average"));
                                modelTV.setPosterPath(jsonObject.getString("poster_path"));
                                modelTvTrending.add(modelTV);
                                showTVTrendingForDay();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            method.dialogDismis();
                            method.makeToast("Gagal mengambil data!!");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        method.dialogDismis();
                        method.makeToast("Tidak ada jaringan internet");
                    }
                });
    }

    private void getFilmTV(){
        method.dialogShow();
        AndroidNetworking.get(Service.BASEURL + Service.TV_POPULAR + Service.API_KEY + Service.LANGUAGE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            method.dialogDismis();
                            tvPopuler = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelTV dataApi = new ModelTV();
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMMM yyyy");
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("first_air_date");

                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setName(jsonObject.getString("name"));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setReleaseDate(simpleDateFormat.format(Objects.requireNonNull((dateFormat).parse(datePost))));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                tvPopuler.add(dataApi);
                                showFilmTv();
                            }

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            method.makeToast("Gagal menampilkan data");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        method.dialogDismis();
                        method.makeToast("Tidak ada jaringan internet");
                    }
                });

    }

    private void setSearchTV(String s){
        method.dialogDismis();
        AndroidNetworking.get(Service.BASEURL + Service.SEARCH_TV + Service.API_KEY + Service.LANGUAGE + Service.QUERY + s)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tvPopuler = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelTV dataTv = new ModelTV();
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMMM yyyy");
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("first_air_date");

                                dataTv.setId(jsonObject.getInt("id"));
                                dataTv.setName(jsonObject.getString("name"));
                                dataTv.setOverview(jsonObject.getString("overview"));
                                dataTv.setPopularity(jsonObject.getString("popularity"));
                                dataTv.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataTv.setPosterPath(jsonObject.getString("poster_path"));
                                dataTv.setReleaseDate(dateFormat.format(Objects.requireNonNull(formatter.parse(datePost))));
                                tvPopuler.add(dataTv);
                                showFilmTv();
                            }
                        } catch (JSONException | ParseException e) {
                            method.dialogDismis();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        method.makeToast("Tidak ada jaringan internet");
                    }
                });
    }

    @Override
    public void onSelect(ModelTV modelTV) {
        Intent intent = new Intent(getActivity(), ModelTelevisionTv.class);
        intent.putExtra("detailTv",modelTV);
        startActivity(intent);
    }

    private void getTvHorizontal(){
        method.dialogDismis();
        AndroidNetworking.get(Service.BASEURL + Service.TV_PLAYNOW + Service.API_KEY + Service.LANGUAGE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        method.dialogDismis();
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelTV modelTV = new ModelTV();
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("EEE, d MMMM yyyy");
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("first_air_date");

                                modelTV.setId(jsonObject.getInt("id"));
                                modelTV.setName(jsonObject.getString("name"));
                                modelTV.setOverview(jsonObject.getString("overview"));
                                modelTV.setVoteAverage(jsonObject.getDouble("vote_average"));
                                modelTV.setReleaseDate(format.format(Objects.requireNonNull(simpleDateFormat.parse(datePost))));
                                modelTV.setPosterPath(jsonObject.getString("poster_path"));
                                modelTV.setBackdropPath(jsonObject.getString("backdrop_path"));
                                modelTV.setPopularity(jsonObject.getString("popularity"));
                                tvPlayNow.add(modelTV);
                                showFilmTVHorizontal();

                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        method.dialogDismis();
                    }
                });
    }

    private void showFilmTVHorizontal(){
        tvHorizontalAdapter = new TvHorizontalAdapter(getActivity(),tvPlayNow,this);
        rvNowPlaying.setAdapter(tvHorizontalAdapter);
        rvNowPlaying.setHasFixedSize(true);
        tvHorizontalAdapter.notifyDataSetChanged();
    }

    private void showFilmTv(){
        tvAdapter = new TvAdapter(getActivity(),tvPopuler,this);
        rvFilmRecomended.setAdapter(tvAdapter);
        rvFilmRecomended.setHasFixedSize(true);
        tvAdapter.notifyDataSetChanged();
    }

    private void showTVTrendingForDay(){
        trendingAdapter = new TvTrendingAdapter(getActivity(),modelTvTrending,this);
        rvTrendingHariIni.setAdapter(trendingAdapter);
        rvTrendingHariIni.setHasFixedSize(true);
        trendingAdapter.notifyDataSetChanged();
    }

    private void getTrendingForWeek() {
        method.dialogDismis();
        AndroidNetworking.get(Service.BASEURL + Service.TV_TRENDING_WEEK + Service.API_KEY)
            .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelTV tvApi = new ModelTV();
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("EEE, d MMMM yyyy");
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("first_air_date");
                                tvApi.setId(jsonObject.getInt("id"));
                                tvApi.setOverview(jsonObject.getString("overview"));
                                tvApi.setPopularity(jsonObject.getString("popularity"));
                                tvApi.setName(jsonObject.getString("name"));
                                tvApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                tvApi.setReleaseDate(format.format(Objects.requireNonNull(simpleDateFormat.parse(datePost))));
                                tvApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                tvApi.setPosterPath(jsonObject.getString("poster_path"));
                                modelTvTrending.add(tvApi);
                                showTrendingForWeek();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            method.dialogDismis();
                            method.makeToast("Gagal mengambil data");
                            Log.d("FragmentTV", "onResponse: Gagal " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        method.dialogDismis();
                        method.makeToast("Tidak ada jaringan internet");
                    }
                });
    }

    private void showTrendingForWeek(){
        trendingAdapter = new TvTrendingAdapter(getActivity(),modelTvTrending,this);
        rvTrendingMingguIni.setAdapter(trendingAdapter);
        trendingAdapter.notifyDataSetChanged();
    }

    private void getRekomendasi(){
        final ModelTV modelTV = new ModelTV();
        AndroidNetworking.get(Service.BASEURL + Service.TV_REKOMENDASI + Service.API_KEY + Service.LANGUAGE)
                .addPathParameter("id",String.valueOf(modelTV.getId()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        method.dialogDismis();
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int o = 0; o < jsonArray.length(); o++){
                                ModelTV modelTV1 = new ModelTV();
                                JSONObject jsonObject = jsonArray.getJSONObject(o);
                                SimpleDateFormat format = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("first_air_date");
                                modelTV1.setId(jsonObject.getInt("id"));
                                modelTV1.setName(jsonObject.getString("name"));
                                modelTV1.setOverview(jsonObject.getString("overview"));
                                modelTV1.setPopularity(jsonObject.getString("popularity"));
                                modelTV1.setPosterPath(jsonObject.getString("poster_path"));
                                modelTV1.setBackdropPath(jsonObject.getString("backdrop_path"));
                                modelTV1.setReleaseDate(format.format(Objects.requireNonNull(dateFormat.parse(datePost))));
                                modelTV1.setVoteAverage(jsonObject.getDouble("vote_average"));
                                showFilmTv();
                            }

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Log.d("fragmentTV", "onResponse: Gagal " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("fragmentTV", "onError: Gagal " + anError.getMessage());
                    }
                });
    }

}
