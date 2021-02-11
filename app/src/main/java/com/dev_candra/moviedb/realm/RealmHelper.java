package com.dev_candra.moviedb.realm;

import android.content.Context;

import com.dev_candra.moviedb.data.ModelMovie;
import com.dev_candra.moviedb.data.ModelTV;
import com.dev_candra.moviedb.utils.Method;

import java.util.ArrayList;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    private Context mContext;
    private Realm realm;
    private RealmResults<ModelMovie> modelMovies;
    private RealmResults<ModelTV> modelTv;


    public RealmHelper(Context context){
        this.mContext = context;
        Realm.init(mContext);
        realm = Realm.getDefaultInstance();
    }

    public ArrayList<ModelMovie>showFavoriteMovie(){
        ArrayList<ModelMovie> data = new ArrayList<>();
        modelMovies = realm.where(ModelMovie.class).findAll();

        if (modelMovies.size() > 0){
            for (int i = 0; i < modelMovies.size(); i++){
                ModelMovie dataMovie = new ModelMovie();
                dataMovie.setId(Objects.requireNonNull(modelMovies.get(i)).getId());
                dataMovie.setTittle(Objects.requireNonNull(modelMovies.get(i)).getTittle());
                dataMovie.setVoteAverage(Objects.requireNonNull(modelMovies.get(i)).getVoteAverage());
                dataMovie.setOverview(Objects.requireNonNull(modelMovies.get(i)).getOverview());
                dataMovie.setReleaseDate(Objects.requireNonNull(modelMovies.get(i)).getReleaseDate());
                dataMovie.setPosterPath(Objects.requireNonNull(modelMovies.get(i)).getPosterPath());
                dataMovie.setBackdropPath(Objects.requireNonNull(modelMovies.get(i)).getBackdropPath());
                dataMovie.setPopularity(Objects.requireNonNull(modelMovies.get(i)).getPopularity());
                data.add(dataMovie);
            }
        }
        return data;
    }

    public ArrayList<ModelTV> showFavoriteTV(){
        ArrayList<ModelTV> modelTVArrayList = new ArrayList<>();
        modelTv = realm.where(ModelTV.class).findAll();

            if (modelTv.size() > 0) {
                for (int i = 0; i < modelTv.size(); i++) {
                    ModelTV dataTv = new ModelTV();
                    dataTv.setId(Objects.requireNonNull(modelTv.get(i)).getId());
                    dataTv.setOverview(Objects.requireNonNull(modelTv.get(i)).getOverview());
                    dataTv.setName(Objects.requireNonNull(modelTv.get(i)).getName());
                    dataTv.setVoteAverage(Objects.requireNonNull(modelTv.get(i)).getVoteAverage());
                    dataTv.setReleaseDate(Objects.requireNonNull(modelTv.get(i)).getReleaseDate());
                    dataTv.setPosterPath(Objects.requireNonNull(modelTv.get(i)).getPosterPath());
                    dataTv.setBackdropPath(Objects.requireNonNull(modelTv.get(i)).getBackdropPath());
                    dataTv.setPopularity(Objects.requireNonNull(modelTv.get(i)).getPopularity());
                    modelTVArrayList.add(dataTv);
                }
            }
            return modelTVArrayList;
    }

    public void addFavoriteMovie(
        int Id, String title, double VoteAverage,String Overview,
        String ReleaseDate, String PosterPath, String BackdropPath,
        String Popularity
    ){
        ModelMovie movie = new ModelMovie();
        movie.setId(Id);
        movie.setTittle(title);
        movie.setVoteAverage(VoteAverage);
        movie.setOverview(Overview);
        movie.setReleaseDate(ReleaseDate);
        movie.setPosterPath(PosterPath);
        movie.setBackdropPath(BackdropPath);
        movie.setPopularity(Popularity);

        realm.beginTransaction();
        realm.copyToRealm(movie);
        realm.commitTransaction();

    }

    public void addFavoriteTV(
        int Id, String Title, double VoteAverage, String Overview,
        String ReleaseDate, String PostePath, String BackdropPath,
        String Popularity
    ){
        ModelTV tv = new ModelTV();
        tv.setId(Id);
        tv.setName(Title);
        tv.setVoteAverage(VoteAverage);
        tv.setOverview(Overview);
        tv.setReleaseDate(ReleaseDate);
        tv.setPosterPath(PostePath);
        tv.setBackdropPath(BackdropPath);
        tv.setPopularity(Popularity);

        realm.beginTransaction();
        realm.copyToRealm(tv);
        realm.commitTransaction();

    }

    public void deleteFavoriteMovie(int Id){
        realm.beginTransaction();
        RealmResults<ModelMovie> modelMovies = realm.where(ModelMovie.class).findAll();
        modelMovies.deleteAllFromRealm();
        realm.commitTransaction();

    }

    public void deleteFavoriteTV(int Id){
        realm.beginTransaction();
        RealmResults<ModelTV> modelTVS = realm.where(ModelTV.class).findAll();
        modelTVS.deleteAllFromRealm();
        realm.commitTransaction();

    }
}
