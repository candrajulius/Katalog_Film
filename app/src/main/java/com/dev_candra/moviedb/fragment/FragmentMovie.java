package com.dev_candra.moviedb.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.dev_candra.moviedb.actvitites.DetailActivity;
import com.dev_candra.moviedb.adapter.MovieAdapter;
import com.dev_candra.moviedb.adapter.MovieHorizontalAdapter;
import com.dev_candra.moviedb.adapter.MovieTrendingAdapter;
import com.dev_candra.moviedb.api.Service;
import com.dev_candra.moviedb.data.ModelMovie;
import com.dev_candra.moviedb.utils.Method;
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

public class FragmentMovie extends Fragment implements MovieHorizontalAdapter.onSelectData,MovieAdapter.onSelectData,MovieTrendingAdapter.onSelected {

    private RecyclerView rvNowPlaying,rvFilmRecomend,rvTrendingHariIni,rvTrendingMingguIni;
    private MovieHorizontalAdapter movieHorizontalAdapter;
    private MovieTrendingAdapter trendingAdapter;
    private List<ModelMovie>modelTrending;
    private MovieAdapter movieAdapter;
    private ProgressDialog progressDialog;
    private SearchView searchView;
    private List<ModelMovie> moviePlayNow;
    private List<ModelMovie> moviewPopular;
    private Method method;
    private TextView text1;
    private String TAG = "FragmentMovie";


    public FragmentMovie(){

    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_film,container,false);

        moviePlayNow = new ArrayList<>();
        moviewPopular = new ArrayList<>();
        modelTrending = new ArrayList<>();
        text1 = view.findViewById(R.id.rekomendasiFilm);

        text1.setText("Rekomendasi Film Movie");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data");
        method = new Method(getActivity());



        searchView = view.findViewById(R.id.searchFilm);

        setRecyclerView(view);

        setSearchView();

        return view;

    }


    private void setSearchView(){
        searchView.setQueryHint(getString(R.string.search_film));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                setSearchMovie(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")){
                    getMovie();
                }else{
                    setSearchMovie(s);
                }
                return true;
            }
        });

        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate",null,null);
        View searchPlate = searchView.findViewById(searchPlateId);

        if (searchPlate != null){
            searchPlate.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void setSearchMovie(String s) {
        AndroidNetworking.get(Service.BASEURL + Service.SEARCH_MOVIE + Service.API_KEY + Service.LANGUAGE + Service.QUERY + s)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            moviewPopular = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i ++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelMovie dataApi = new ModelMovie();
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMMM yyyy");
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("release_date");

                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setTittle(jsonObject.getString("title"));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setReleaseDate(simpleDateFormat.format(Objects.requireNonNull(dateFormat.parse(datePost))));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                moviewPopular.add(dataApi);
                                showMovie();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            method.dialogDismis();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        method.makeToast("Tidak ada jaringan internet");
                        Log.d("Fragment Movie", "onError: onResponse " + anError.getMessage() );
                    }
                });
    }

    private void showMovie() {
        movieAdapter = new MovieAdapter(moviewPopular,getActivity(),this);
        rvFilmRecomend.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
    }

    private void showMovieTrending(){
        trendingAdapter = new MovieTrendingAdapter(modelTrending,getActivity(),this);
        rvTrendingHariIni.setAdapter(trendingAdapter);
        trendingAdapter.notifyDataSetChanged();
    }

    private void showMovieHorizontal(){
        movieHorizontalAdapter = new MovieHorizontalAdapter(moviePlayNow,getActivity(),this);
        rvNowPlaying.setAdapter(movieHorizontalAdapter);
        movieHorizontalAdapter.notifyDataSetChanged();
    }

    private void setRecyclerView(View view){
        rvNowPlaying = view.findViewById(R.id.rvNowPlaying);
        rvNowPlaying.setHasFixedSize(true);
        rvNowPlaying.setLayoutManager(new CardSliderLayoutManager(getActivity()));
        new CardSnapHelper().attachToRecyclerView(rvNowPlaying);

        rvFilmRecomend = view.findViewById(R.id.rvFilmRecommend);
        rvFilmRecomend.setHasFixedSize(true);
        rvFilmRecomend.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvTrendingHariIni = view.findViewById(R.id.rvTrendingHariIni);
        rvTrendingHariIni.setHasFixedSize(true);
        rvTrendingHariIni.setLayoutManager(new CardSliderLayoutManager(getActivity()));
        new CardSnapHelper().attachToRecyclerView(rvTrendingHariIni);

        rvTrendingMingguIni = view.findViewById(R.id.rvTrendingMingguIni);
        rvTrendingMingguIni.setHasFixedSize(true);
        rvTrendingMingguIni.setLayoutManager(new CardSliderLayoutManager(getActivity()));
        new CardSnapHelper().attachToRecyclerView(rvTrendingMingguIni);

        getMovieHorizontal();
        getMovie();
        getMovieTrendingForDay();
        getMovieTrendingForWeek();
    }

    private void getMovieHorizontal() {
        progressDialog.show();
        AndroidNetworking.get(Service.BASEURL + Service.MOVIE_PLAYNOW + Service.API_KEY + Service.LANGUAGE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelMovie dataApi = new ModelMovie();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("release_date");

                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setTittle(jsonObject.getString("title"));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setReleaseDate(simpleDateFormat.format(Objects.requireNonNull(dateFormat.parse(datePost))));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                moviePlayNow.add(dataApi);
                                showMovieHorizontal();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            method.makeToast("Gagal menampilkan data");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        method.makeToast("Tidak ada jaringan internet");
                    }
                });
    }

    private void getMovie(){
        progressDialog.show();
        AndroidNetworking.get(Service.BASEURL + Service.MOVIE_POPULAR + Service.API_KEY + Service.LANGUAGE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            moviewPopular = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelMovie dataApi = new ModelMovie();
                                SimpleDateFormat format = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("release_date");

                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setTittle(jsonObject.getString("title"));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setReleaseDate(format.format(Objects.requireNonNull(dateFormat.parse(datePost))));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                moviewPopular.add(dataApi);
                                showMovie();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            method.makeToast("Gagal menampilkan data");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        method.makeToast("Tidak ada jaringan internet");
                    }
                });
    }

    private void getMovieTrendingForDay(){
        progressDialog.show();
        AndroidNetworking.get(Service.BASEURL + Service.MOVIE_TRENDING + Service.API_KEY)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            modelTrending = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                ModelMovie modelMovie = new ModelMovie();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE, d MMMM yyyy");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = simpleDateFormat.format(Objects.requireNonNull(format.parse(jsonObject.getString("release_date"))));
                                modelMovie.setId(jsonObject.getInt("id"));
                                modelMovie.setOverview(jsonObject.getString("overview"));
                                modelMovie.setPopularity(jsonObject.getString("popularity"));
                                modelMovie.setTittle(jsonObject.getString("title"));
                                modelMovie.setPosterPath(jsonObject.getString("poster_path"));
                                modelMovie.setBackdropPath(jsonObject.getString("backdrop_path"));
                                modelMovie.setVoteAverage(jsonObject.getDouble("vote_average"));
                                modelMovie.setReleaseDate(datePost);
                                modelTrending.add(modelMovie);
                                showMovieTrending();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            method.makeToast("Gagal mengambil data!!");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        method.makeToast("Tidak ada jaringan internet!!");
                    }
                });
    }

    @Override
    public void onSelected(ModelMovie modelMovie) {
        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
        detailIntent.putExtra("detailMovie",modelMovie);
        startActivity(detailIntent);
    }


    private void getMovieTrendingForWeek() {
        method.dialogShow();
        AndroidNetworking.get(Service.BASEURL + Service.MOVIE_TRENDING_WEEK + Service.API_KEY)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            method.dialogDismis();
                            modelTrending = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                ModelMovie dataApi = new ModelMovie();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("EE, d MMMM yyyy");
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("release_date");
                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                dataApi.setTittle(jsonObject.getString("title"));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setReleaseDate(dateFormat.format(Objects.requireNonNull(format.parse(datePost))));
                                modelTrending.add(dataApi);
                                showTrendingMingguIni();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            method.dialogDismis();
                            method.makeToast("Gagal mengambil data");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        method.dialogDismis();
                        method.makeToast("Tidak ada jaringan internet");
                    }
                });
    }

    private void showTrendingMingguIni(){
        trendingAdapter = new MovieTrendingAdapter(modelTrending,getActivity(),this);
        rvTrendingMingguIni.setAdapter(trendingAdapter);
        trendingAdapter.notifyDataSetChanged();
    }

    private void getRekomendasi(){
        ModelMovie modelMovie = new ModelMovie();
        AndroidNetworking.get(Service.BASEURL + Service.MOVIE_REKOMENDASI + Service.API_KEY + Service.LANGUAGE)
                .addPathParameter("id")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int o = 0; o < jsonArray.length(); o++){
                                JSONObject jsonObject = jsonArray.getJSONObject(o);
                                ModelMovie dataApi = new ModelMovie();
                                SimpleDateFormat format = new SimpleDateFormat("EE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("release_date");
                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setTittle(jsonObject.getString("title"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                dataApi.setReleaseDate(format.format(Objects.requireNonNull(dateFormat.parse(datePost))));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                showMovie();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: Gagal " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: Gagal " + anError.getMessage());
                    }
                });


    }

}
