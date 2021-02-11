package com.dev_candra.moviedb.actvitites;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dev_candra.moviedb.R;
import com.dev_candra.moviedb.api.Service;
import com.dev_candra.moviedb.data.ModelCastinng;
import com.dev_candra.moviedb.data.ModelCrew;
import com.dev_candra.moviedb.utils.Method;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class DetailOrang extends AppCompatActivity {

    Method method;
    ModelCastinng modelCastinng;
    ModelCrew modelCrew;
    private ImageView gambarOrang;
    private Toolbar toolbar;
    private int id;
    String cover;
    private TextView textNama,textkelamin,textkelahiran,textLokasiKelahiran,textJulmlahReputasi,textBiograpi,textJudul;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_orang);
        method = new Method(this);
        textNama = findViewById(R.id.nama_orang);
        textkelahiran = findViewById(R.id.tanggal_kelahiran);
        textLokasiKelahiran = findViewById(R.id.lokasi_kelahiran);
        textJulmlahReputasi = findViewById(R.id.reputasi);
        textBiograpi = findViewById(R.id.biografi_orang);
        gambarOrang = findViewById(R.id.gambarOrangnya);
        textkelamin = findViewById(R.id.gender);
        textJudul = findViewById(R.id.name_person);
        toolbar = findViewById(R.id.toolbar_detal);
        modelCrew = (ModelCrew) getIntent().getSerializableExtra("model_crew");
        modelCastinng = (ModelCastinng) getIntent().getSerializableExtra("model_casting");
        method.makeDialog("Mohon Tunggu","Sedang menampilkan data");

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (modelCastinng != null ){
            method.dialogShow();
            textJudul.setSelected(true);

            cover = modelCastinng.getProfil_path();
            textNama.setText(modelCastinng.getName());
            textJudul.setText(modelCastinng.getName());
            id = modelCastinng.getId();


            AndroidNetworking.get(Service.BASEURL + Service.PERSON + Service.API_KEY + Service.LANGUAGE)
                    .addPathParameter("id",String.valueOf(id))
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response != null) {
                                    method.dialogDismis();
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMMM yyyy");
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                                    if (response.getString("gender").equalsIgnoreCase("2")){
                                        textkelamin.setText("Laki-Laki");
                                    }else{
                                        textkelamin.setText("Perempuan");
                                    }
                                    textkelahiran.setText(simpleDateFormat.format(Objects.requireNonNull(format.parse(response.getString("birthday")))));
                                    textLokasiKelahiran.setText(response.getString("place_of_birth"));
                                    textJulmlahReputasi.setText(response.getString("popularity"));
                                    textBiograpi.setText(response.getString("biography"));
                                    String image = response.getString("profile_path");
                                    Glide.with(DetailOrang.this)
                                            .load(Service.URL_IMAGE + image)
                                            .apply(new RequestOptions()
                                                    .placeholder(R.drawable.ic_image)
                                                    .transform(new RoundedCorners(16)))
                                            .into(gambarOrang);
                                }else{
                                    textkelamin.setText("Kosong");
                                    textkelahiran.setText("Kosong");
                                    textLokasiKelahiran.setText("Kosong");
                                    textJulmlahReputasi.setText("Kosong");
                                    textBiograpi.setText("Kosong");
                                    Glide.with(DetailOrang.this)
                                            .load(R.drawable.ic_image)
                                            .apply(new RequestOptions()
                                                .placeholder(R.drawable.ic_image)
                                                .transform(new RoundedCorners(16)))
                                            .into(gambarOrang);


                                }
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                                method.dialogDismis();
                                Log.d("DetailOrang", "onResponse: Gagal " + e.getMessage());
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            method.dialogDismis();
                            Log.d("DetailOrang", "onError: Gagal " + anError.getMessage());
                        }
                    });
        }else if  (modelCrew != null){
            method.dialogShow();
            id =  modelCrew.getId();
            textNama.setText(modelCrew.getName());
            textJudul.setText(modelCrew.getName());
            textJudul.setSelected(true);


            AndroidNetworking.get(Service.BASEURL + Service.PERSON + Service.API_KEY + Service.LANGUAGE)
                    .addPathParameter("id",String.valueOf(id))
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response != null) {
                                    method.dialogDismis();
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMMM yyyy");
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                                    if (response.getString("gender") == "2"){
                                        textkelamin.setText("Laki-Laki");
                                    }else{
                                        textkelamin.setText("Perempuan");
                                    }
                                    textkelahiran.setText(simpleDateFormat.format(Objects.requireNonNull(format.parse(response.getString("birthday")))));
                                    textLokasiKelahiran.setText(response.getString("place_of_birth"));
                                    textJulmlahReputasi.setText(response.getString("popularity"));
                                    textBiograpi.setText(response.getString("biography"));
                                    String image = response.getString("profile_path");
                                    Glide.with(DetailOrang.this)
                                            .load(Service.URL_IMAGE + image)
                                            .apply(new RequestOptions()
                                                    .placeholder(R.drawable.ic_image)
                                                    .transform(new RoundedCorners(16)))
                                            .into(gambarOrang);
                                }else{
                                    method.dialogDismis();
                                    textkelamin.setText("Kosong");
                                    textLokasiKelahiran.setText("Kosong");
                                    textJulmlahReputasi.setText("Kosong");
                                    textkelahiran.setText("Kosong");
                                    textBiograpi.setText("Kosong");
                                    Glide.with(DetailOrang.this)
                                            .load(R.drawable.ic_image)
                                            .apply(new RequestOptions()
                                                .placeholder(R.drawable.ic_image)
                                                .transform(new RoundedCorners(16)))
                                            .into(gambarOrang);
                                }
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                                method.dialogDismis();
                                Log.d("DetailOrang", "onResponse: Gagal " + e.getMessage());
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            method.dialogDismis();
                            Log.d("DetailOrang", "onError: Gagal " + anError.getMessage());
                        }
                    });

        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
