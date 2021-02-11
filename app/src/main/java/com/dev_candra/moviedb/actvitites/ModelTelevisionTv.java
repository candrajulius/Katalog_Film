package com.dev_candra.moviedb.actvitites;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.dev_candra.moviedb.data.ModelTV;
import com.dev_candra.moviedb.data.ModelTrailer;
import com.dev_candra.moviedb.realm.RealmHelper;
import com.dev_candra.moviedb.utils.Method;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ModelTelevisionTv extends AppCompatActivity implements CastingAdapter.onSelect,CrewAdapter.onSelect{

    Toolbar toolbar;
    TextView tvTitle,tvName,tvRating,tvRelease,tvPopularity,tvOverview;
    ImageView imgCover,imgPhoto;
    RecyclerView rvTrailer,rvCrew,rvAktorUtama;
    MaterialFavoriteButton imgFavorite;
    FloatingActionButton fabShare;
    RatingBar ratingBar;
    String NameFilm,ReleaseDate,Popularity,OverView,Cover,Thumbnail,movieUrl;
    int Id;
    double Rating;
    ModelTV modelTV;
    Method method;
    List<ModelTrailer> modelTrailers = new ArrayList<>();
    TrailerAdapter trailerAdapter;
    CrewAdapter crewAdapter;
    CastingAdapter castingAdapter;
    RealmHelper helper;
    Button buttonLink;
    List<ModelCrew>modelCrewList = new ArrayList<>();
    List<ModelCastinng> modelCastinngs = new ArrayList<>();
    private String TAG = "ModelTelevisionTV";




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= 21){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        method = new Method(this);
        buttonLink = findViewById(R.id.buttonLink);
        rvCrew = findViewById(R.id.rvCrew);
        rvAktorUtama = findViewById(R.id.rvCasting);
       initView();

        method.makeDialog("Mohon Tunggu","Sedang menampilkan trailer");

    }

    private void setWindowFlag(Activity activityv, final int bits, boolean b) {
        Window window = activityv.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        if (b){
            winParams.flags |= bits;
        }else{
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }

    @SuppressLint("SetTextI18n")
    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
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
        fabShare = findViewById(R.id.fabShare);

        helper = new RealmHelper(this);

        modelTV = (ModelTV) getIntent().getSerializableExtra("detailTv");

        if (modelTV != null){
            Id = modelTV.getId();
            NameFilm = modelTV.getName();
            Rating = modelTV.getVoteAverage();
            ReleaseDate = modelTV.getReleaseDate();
            Popularity = modelTV.getPopularity();
            OverView = modelTV.getOverview();
            Cover = modelTV.getBackdropPath();
            Thumbnail = modelTV.getPosterPath();
            movieUrl = Service.URLTV + "" + Id;

            float newValue = (float)Rating;
            ratingBar.setNumStars(5);
            ratingBar.setStepSize((float)0.5);
            ratingBar.setRating(newValue / 2);

            tvTitle.setText(NameFilm);
            tvName.setText(NameFilm);
            tvRating.setText(Rating + "/10");
            tvRelease.setText(ReleaseDate);
            tvPopularity.setText(Popularity);
            tvOverview.setText(OverView);
            tvName.setSelected(true);
            trailerAdapter = new TrailerAdapter(modelTrailers,this);
            castingAdapter = new CastingAdapter(this,modelCastinngs,this);
            crewAdapter = new CrewAdapter(this,modelCrewList,this);

            buttonLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ModelTelevisionTv.this,WebView.class);
                    intent.putExtra("url",movieUrl);
                    startActivity(intent);
                }
            });

            buttonLink.setText("Link Web " + NameFilm);

            Glide.with(this)
                    .load(Service.URLIMAGE + Cover)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCover);

            Glide.with(this)
                    .load(Service.URLIMAGE + Thumbnail)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgPhoto);



            rvTrailer.setHasFixedSize(true);
            rvTrailer.setLayoutManager(new LinearLayoutManager(this));

            rvCrew.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            rvCrew.setLayoutManager(linearLayoutManager);

            rvAktorUtama.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
            linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            rvAktorUtama.setLayoutManager(linearLayoutManager1);


            getTrailer();

            getCastingTv();

            getCrewTv();

        }


        imgFavorite.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                if (favorite){
                    Id = modelTV.getId();
                    NameFilm = modelTV.getName();
                    Rating = modelTV.getVoteAverage();
                    OverView = modelTV.getOverview();
                    ReleaseDate = modelTV.getReleaseDate();
                    Thumbnail = modelTV.getPosterPath();
                    Cover = modelTV.getBackdropPath();
                    Popularity = modelTV.getBackdropPath();
                    imgFavorite.setFavorite(true);

                    helper.addFavoriteTV(Id,NameFilm,Rating,OverView,ReleaseDate,Thumbnail,Cover,Popularity);
                    method.makeSnackbar(buttonView,modelTV.getName() + " Ditambahkan ke favorite");
                }else{
                    imgFavorite.setFavorite(false);
                    helper.deleteFavoriteTV(modelTV.getId());
                    method.makeSnackbar(buttonView,modelTV.getName() + " Dihapus dari favorite");
                }
            }
        });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String subject = modelTV.getName();
                String description = modelTV.getOverview();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
                shareIntent.putExtra(Intent.EXTRA_TEXT,subject + "\n\n" + description + "\n\n" + movieUrl);
                startActivity(Intent.createChooser(shareIntent,"Bagikan dengan: "));
            }
        });

    }

    private void getTrailer(){
        method.dialogShow();
        AndroidNetworking.get(Service.BASEURL + Service.TV_VIDEO + Service.API_KEY + Service.LANGUAGE)
                .addPathParameter("id",String.valueOf(Id))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            method.dialogDismis();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelTrailer modelTrailer = new ModelTrailer();
                                modelTrailer.setKey(jsonObject.getString("key"));
                                modelTrailer.setType(jsonObject.getString("type"));
                                modelTrailers.add(modelTrailer);
                                showTrailer();
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

    private void showTrailer(){
        trailerAdapter = new TrailerAdapter(modelTrailers,ModelTelevisionTv.this);
        rvTrailer.setAdapter(trailerAdapter);
        trailerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCrewTv(){
        AndroidNetworking.get(Service.BASEURL + Service.TV_CREW + Service.API_KEY + Service.LANGUAGE)
                .addPathParameter("id",String.valueOf(Id))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        method.dialogDismis();
                        try {
                            JSONArray jsonArray = response.getJSONArray("crew");
                            for (int o = 0; o < jsonArray.length(); o++){
                                JSONObject jsonObject = jsonArray.getJSONObject(o);
                                ModelCrew dataCrewTv = new ModelCrew();
                                dataCrewTv.setId(jsonObject.getInt("id"));
                                dataCrewTv.setName(jsonObject.getString("name"));
                                dataCrewTv.setJob(jsonObject.getString("job"));
                                dataCrewTv.setProfil_path(jsonObject.getString("profile_path"));
                                modelCrewList.add(dataCrewTv);
                                rvCrew.setAdapter(crewAdapter);
                            }
                        } catch (JSONException e) {
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


    private void getCastingTv(){
        AndroidNetworking.get(Service.BASEURL + Service.TV_CREW + Service.API_KEY + Service.LANGUAGE)
                .addPathParameter("id",String.valueOf(Id))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        method.dialogDismis();
                        try {
                            JSONArray jsonArray = response.getJSONArray("cast");
                            for (int o = 0; o < jsonArray.length(); o++){
                                JSONObject jsonObject = jsonArray.getJSONObject(o);
                                ModelCastinng modelCastinng = new ModelCastinng();
                                modelCastinng.setId(jsonObject.getInt("id"));
                                modelCastinng.setName(jsonObject.getString("name"));
                                modelCastinng.setCharacter(jsonObject.getString("character"));
                                modelCastinng.setProfil_path(jsonObject.getString("profile_path"));
                                modelCastinngs.add(modelCastinng);
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
        Intent intent = new Intent(ModelTelevisionTv.this,DetailOrang.class);
        intent.putExtra("model_casting",modelCastinng);
        startActivity(intent);
    }

    @Override
    public void onSelected(ModelCrew modelCrew) {
        Intent intent = new Intent(ModelTelevisionTv.this,DetailOrang.class);
        intent.putExtra("model_crew",modelCrew);
        startActivity(intent);
    }
}
