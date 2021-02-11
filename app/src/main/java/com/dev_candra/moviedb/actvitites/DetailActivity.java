package com.dev_candra.moviedb.actvitites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dev_candra.moviedb.R;
import com.dev_candra.moviedb.adapter.CastingAdapter;
import com.dev_candra.moviedb.adapter.CrewAdapter;
import com.dev_candra.moviedb.adapter.TrailerAdapter;
import com.dev_candra.moviedb.api.Service;
import com.dev_candra.moviedb.data.ModelCastinng;
import com.dev_candra.moviedb.data.ModelCrew;
import com.dev_candra.moviedb.data.ModelMovie;
import com.dev_candra.moviedb.data.ModelTrailer;
import com.dev_candra.moviedb.realm.RealmHelper;
import com.dev_candra.moviedb.utils.Method;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements CastingAdapter.onSelect,CrewAdapter.onSelect {

    Toolbar toolbar;
    TextView tvTitle,tvName,tvRating,tvRelease,tvPopularity,tvOverview;
    ImageView imgCover,imgPhoto;
    RecyclerView rvTrailer,rvCrew,rvAktorUtama;
    MaterialFavoriteButton imgFavorite;
    FloatingActionButton fabShare;
    RatingBar ratingBar;
    String nameFilm, ReleaseDate,Popularity,Overview,Cover,Thumbnail,movieUrl;
    int Id;
    double Rating;
    ModelMovie modelMovie;
    Method method;
    List<ModelTrailer>modelTrailers = new ArrayList<>();
    List<ModelCrew> modelCrewList = new ArrayList<>();
    List<ModelCastinng> modelCastinngs = new ArrayList<>();
    TrailerAdapter trailerAdapter;
    CrewAdapter crewAdapter;
    CastingAdapter castingAdapter;
    RealmHelper helper;
    Button link;
    private static String TAG = "Detail Activity";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        method = new Method(this);
        if (Build.VERSION.SDK_INT >= 21){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        method.makeDialog("Mohon Tunggu","Sedang menampilkan data");

        initView();

    }

    @SuppressLint("SetTextI18n")
    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.nama_developer));
        link = findViewById(R.id.buttonLink);
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ratingBar = findViewById(R.id.ratingBar);
        imgCover = findViewById(R.id.imgCover);
        imgPhoto = findViewById(R.id.imgPhoto);
        imgFavorite = findViewById(R.id.imgFavorite);
        tvTitle = findViewById(R.id.tvTitle);
        tvName = findViewById(R.id.tvName);
        tvRating = findViewById(R.id.tvRating);
        tvRelease = findViewById(R.id.tvRelease);
        tvPopularity = findViewById(R.id.tvPopularity);
        tvOverview = findViewById(R.id.tvOverview);
        rvTrailer = findViewById(R.id.rvTrailer);
        rvCrew = findViewById(R.id.rvCrew);
        rvAktorUtama = findViewById(R.id.rvCasting);
        fabShare = findViewById(R.id.fabShare);
        helper = new RealmHelper(this);

        modelMovie = (ModelMovie) getIntent().getSerializableExtra("detailMovie");

        if (modelMovie != null){
            Id = modelMovie.getId();
            nameFilm = modelMovie.getTittle();
            Rating = modelMovie.getVoteAverage();
            ReleaseDate = modelMovie.getReleaseDate();
            Popularity = modelMovie.getPopularity();
            Overview = modelMovie.getOverview();
            Cover = modelMovie.getBackdropPath();
            Thumbnail = modelMovie.getPosterPath();
            movieUrl = Service.URLFILM + "" + Id;

            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent webIntent = new Intent(DetailActivity.this,WebView.class);
                    webIntent.putExtra("url",movieUrl);
                    startActivity(webIntent);
                }
            });

            link.setText("Link Web " + nameFilm);

            tvTitle.setText(nameFilm);
            tvName.setText(nameFilm);
            tvRating.setText(Rating + "/10");
            tvPopularity.setText(Popularity);
            tvOverview.setText(Overview);
            tvRelease.setText(ReleaseDate);
            tvTitle.setSelected(true);
            tvName.setSelected(true);
            trailerAdapter = new TrailerAdapter(modelTrailers,this);
            castingAdapter = new CastingAdapter(this,modelCastinngs,this);
            crewAdapter = new CrewAdapter(this,modelCrewList,this);

            float newValue = (float)Rating;
            ratingBar.setNumStars(5);
            ratingBar.setStepSize((float)0.5);
            ratingBar.setRating(newValue / 2);

            Glide.with(this)
                    .load(Service.URLIMAGE + Cover)
                    .into(imgCover);

            Glide.with(this)
                    .load(Service.URLIMAGE + Thumbnail)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgPhoto);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            rvAktorUtama.setLayoutManager(layoutManager);
            rvAktorUtama.setHasFixedSize(true);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            rvCrew.setLayoutManager(linearLayoutManager);
            rvCrew.setHasFixedSize(true);

            rvTrailer.setHasFixedSize(true);
            rvTrailer.setLayoutManager(new LinearLayoutManager(this));

            getTrailer();
            getCrew();
            getAktorUtama();
        }



        imgFavorite.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                if (favorite){
                    Id = modelMovie.getId();
                    nameFilm = modelMovie.getTittle();
                    Rating = modelMovie.getVoteAverage();
                    Overview = modelMovie.getOverview();
                    ReleaseDate = modelMovie.getReleaseDate();
                    Thumbnail = modelMovie.getPosterPath();
                    Cover = modelMovie.getBackdropPath();
                    Popularity = modelMovie.getPopularity();
                    imgFavorite.setFavorite(true);

                    helper.addFavoriteMovie(Id,nameFilm,Rating,Overview,ReleaseDate,Thumbnail,Cover,Popularity);
                    method.makeSnackbar(buttonView,modelMovie.getTittle() + "Ditambahkan ke favorite");
                }else{
                    helper.deleteFavoriteMovie(modelMovie.getId());
                    method.makeSnackbar(buttonView,modelMovie.getTittle() + "Dihapus dari favorite");
                }
            }
        });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String subject = modelMovie.getTittle();
                String description = modelMovie.getOverview();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
                shareIntent.putExtra(Intent.EXTRA_TEXT,subject + "\n\n" + description + "\n\n" + movieUrl);
                startActivity(Intent.createChooser(shareIntent,"Bagikan dengan: "));

            }
        });
    }

    private void getTrailer() {
        method.dialogShow();
        AndroidNetworking.get(Service.BASEURL + Service.MOVIE_VIDEO + Service.API_KEY + Service.LANGUAGE)
                .addPathParameter("id",String.valueOf(Id))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        method.dialogDismis();
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelTrailer dataTrailer = new ModelTrailer();
                                dataTrailer.setKey(jsonObject.getString("key"));
                                dataTrailer.setType(jsonObject.getString("type"));
                                modelTrailers.add(dataTrailer);
                                rvTrailer.setAdapter(trailerAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            method.makeToast("Gagal menampilkan trailer");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        method.makeToast("Tidak ada jaringan internet");
                    }
                });
    }

    private void setWindowFlag(DetailActivity detailActivity, final int flagTranslucentStatus, boolean b) {
        Window window = detailActivity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        if (b){
            winParams.flags |= flagTranslucentStatus;
        }else{
            winParams.flags &= ~flagTranslucentStatus;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getCrew(){
        AndroidNetworking.get(Service.BASEURL + Service.MOVIE_CREW + Service.API_KEY + Service.LANGUAGE)
                .addPathParameter("id",String.valueOf(Id))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            method.dialogDismis();
                            JSONArray jsonArray = response.getJSONArray("crew");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelCrew modelCrew = new ModelCrew();
                                modelCrew.setId(jsonObject.getInt("id"));
                                modelCrew.setName(jsonObject.getString("name"));
                                modelCrew.setJob(jsonObject.getString("job"));
                                modelCrew.setProfil_path(jsonObject.getString("profile_path"));
                                modelCrewList.add(modelCrew);
                                rvCrew.setAdapter(crewAdapter);
                            }
                        } catch (JSONException e) {
                            method.dialogDismis();
                            e.printStackTrace();
                            Log.d("DetailActivity", "onResponse: Gagal " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        method.dialogDismis();
                        Log.d("DetailActivity", "onError: Gagal " + anError.getMessage());
                    }
                });
    }

//    private void setRecyclerViewCrew(){
//        CrewAdapter crewAdapter = new CrewAdapter(this,modelCrewList);
//        rvCrew.setAdapter(new CrewAdapter(this,modelCrewList));
//        crewAdapter.notifyDataSetChanged();
//    }
//
//    private void setRecyclerViewCast(){
//        CrewAdapter crewAdapter1 = new CrewAdapter(this,modelCrewList);
//        rvAktorUtama.setAdapter(crewAdapter1);
//        crewAdapter1.notifyDataSetChanged();
//    }

    private void getAktorUtama(){
        AndroidNetworking.get(Service.BASEURL + Service.MOVIE_CREW + Service.API_KEY + Service.LANGUAGE)
                .addPathParameter("id",String.valueOf(Id))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        method.dialogDismis();
                        try {
                            JSONArray jsonArray = response.getJSONArray("cast");
                            for (int o = 0; o < jsonArray.length(); o++){
                                JSONObject jsonObject = jsonArray.getJSONObject(o);
                                ModelCastinng apiCast = new ModelCastinng();
                                apiCast.setId(jsonObject.getInt("id"));
                                apiCast.setName(jsonObject.getString("name"));
                                apiCast.setProfil_path(jsonObject.getString("profile_path"));
                                apiCast.setCharacter(jsonObject.getString("character"));
                                modelCastinngs.add(apiCast);
                                rvAktorUtama.setAdapter(castingAdapter);
                            }
                        } catch (JSONException e) {
                            method.dialogDismis();
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: Gagal " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        method.dialogDismis();
                        Log.d(TAG, "onError: Gagal " + anError.getMessage());
                    }
                });
    }

    @Override
    public void onSelected(ModelCastinng modelCastinng) {
        Intent intent = new Intent(DetailActivity.this,DetailOrang.class);
        intent.putExtra("model_casting",modelCastinng);
        startActivity(intent);
    }

    @Override
    public void onSelected(ModelCrew modelCrew) {
        Intent intent = new Intent(DetailActivity.this,DetailOrang.class);
        intent.putExtra("model_crew",modelCrew);
        startActivity(intent);
    }
}
