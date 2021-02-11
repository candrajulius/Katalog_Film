package com.dev_candra.moviedb.actvitites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dev_candra.moviedb.R;
import com.dev_candra.moviedb.api.Service;
import com.dev_candra.moviedb.notification.NotificationDailyReceiver;
import com.dev_candra.moviedb.notification.NotificationReleaseReceiver;
import com.dev_candra.moviedb.prefrence.SettingPreference;
import com.dev_candra.moviedb.utils.Method;

public class SettingAcitivty extends AppCompatActivity implements View.OnClickListener {

    private Button whatsDeveloper,detailAplikasi;
    private Switch switchReminder,switchRelease;
    private NotificationReleaseReceiver releaseReceiver;
    private NotificationDailyReceiver dailyReceiver;
    private SettingPreference settingPreference;
    private Toolbar toolbar;
    private Method method;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        whatsDeveloper = findViewById(R.id.whatssappDeveloper);
        detailAplikasi = findViewById(R.id.detail);
        toolbar = findViewById(R.id.toolbarSetting);
        switchRelease = findViewById(R.id.swRealeseToday);
        switchReminder = findViewById(R.id.swDailyReminder);
        releaseReceiver = new NotificationReleaseReceiver();
        dailyReceiver = new NotificationDailyReceiver();
        method = new Method(this);

        whatsDeveloper.setOnClickListener(this);
        detailAplikasi.setOnClickListener(this);
        switchReminder.setOnClickListener(this);
        switchRelease.setOnClickListener(this);

        settingPreference = new SettingPreference(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setSwitchReminder();
        setSwitchReleaseReminder();

        switchReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchReminder.isChecked()){
                    dailyReceiver.setDailyAlarm(getApplicationContext());
                    settingPreference.setDailyReminder(true);
                    method.makeToast("Pengingat harian diaktifkan");
                }else{
                    dailyReceiver.cancelAlarm(getApplicationContext());
                    settingPreference.setDailyReminder(false);
                    method.makeToast("Pengingat harian dinonaktifkan");
                }
            }
        });
    }

    private void setSwitchReminder() {
        if (settingPreference.getDailyNumber()){
            switchReminder.setChecked(true);
        }else{
            switchReminder.setChecked(false);
        }
    }

    private void setSwitchReleaseReminder(){
        if (settingPreference.getReleaseReminder()){
            switchRelease.setChecked(true);
        }else{
            switchRelease.setChecked(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            this.finish();
        }

        return true;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.whatssappDeveloper){
            handleWhatssDeveloper();
        }else if (view.getId() == R.id.detail){
            handleDetailAplikasi();
        }else if (view.getId() == R.id.swDailyReminder){
            handleSwitchReminder();
        }else if (view.getId() == R.id.swRealeseToday){
            handleSwitchReleaseToday();
        }
    }

    private void handleSwitchReleaseToday() {
        if (switchRelease.isChecked()){
            releaseReceiver.setReleaseAlarm(getApplicationContext());
            settingPreference.setReleaseReminder(true);
            method.makeToast("Pengingat rilis diaktifkan ");
        }else{
            releaseReceiver.cancelAlarm(getApplicationContext());
            settingPreference.setReleaseReminder(false);
            method.makeToast("Pengingat rilis dinonaktifkan");
        }
    }

    private void handleWhatssDeveloper(){
        final String helloUser = "Hallo developer saya ingin bertanya tentang aplikasi anda ? ";
        final String url = "https://wa.me/6282311558341/?text=" + helloUser;
        method.dialogShow();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean AppInstalled = checkInstalled("com.whatsapp");
                if (AppInstalled){
                    method.dialogDismis();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }else{
                    method.dialogDismis();
                    method.makeToast("Anda belum menginstall aplikasi atau aplikasi belum di Update");
                }
            }
        },5000);
    }
    @SuppressLint("SetTextI18n")
    private void handleDetailAplikasi(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingAcitivty.this,R.style.AlertDialogTheme);
        View view = LayoutInflater.from(SettingAcitivty.this).inflate(R.layout.dialog_layout
        ,(RelativeLayout)findViewById(R.id.relative));
        builder.setView(view);
        ((TextView)view.findViewById(R.id.kejelasan)).setText("Menampilkan Film Movie Sedang Tayang \n " +
                "Menampilkan Film TV Sedang Tayang \n Menampilkan Film Movie Yang Populer \n Menampilkan Film TV Yang Populer \n " +
                "Mencari Film Movie Dan Film TV \n Menampilkan Detail Movie Dan TV \n Menampilkan Trailer Movie Dan TV \n " +
                "Menampilkan Web Movie Dan TV \n Menggunakan Movie Dan TV Favorite \n Memberi Tahu Pengguna Untuk Melihat Movie Dan TV Release \n" +
                "Menampilkan Movie Dan TV Trending");

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.OK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.close1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(SettingAcitivty.this,WebView.class);
              intent.putExtra("url",Service.URL);
              startActivity(intent);
            }
        });

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        }
        alertDialog.show();

    }

    private void handleSwitchReminder(){
        if (switchReminder.isChecked()){
            dailyReceiver.setDailyAlarm(getApplicationContext());
            settingPreference.setDailyReminder(true);
            method.makeToast("Pengingat harian diaktifkan");
        }else{
            dailyReceiver.cancelAlarm(getApplicationContext());
            settingPreference.setDailyReminder(false);
            method.makeToast("Pengingat harian di nonaktifkan");
        }
    }

    private Boolean checkInstalled(String url ){
        boolean installed;
        PackageManager packageManager = getPackageManager();
        try{
            packageManager.getPackageInfo(url,PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            installed = false;
        }
        return installed;
    }


}
