package com.dev_candra.moviedb.actvitites;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dev_candra.moviedb.R;
import com.dev_candra.moviedb.fragment.FragmentFavorite;
import com.dev_candra.moviedb.fragment.FragmentMovie;
import com.dev_candra.moviedb.fragment.FragmentTv;
import com.dev_candra.moviedb.utils.BottomBarBehaviour;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

public class MainActivity extends AppCompatActivity {

    Fragment fragment = null;
    private Toolbar toolbar;
    private long backPresTime;
    private Toast backToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);

        initToolbar();
        backToast = Toast.makeText(this,"Tekan sekali lagi untuk menutup aplikasi",Toast.LENGTH_SHORT);
        initView();
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!= null){
            getSupportActionBar().setTitle(getResources().getString(R.string.nama_developer));
            getSupportActionBar().setSubtitle(getResources().getString(R.string.app_name));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.setting){
            startActivity(new Intent(MainActivity.this,SettingAcitivty.class));
        }

        return true;
    }

    private void initView(){
        BubbleNavigationLinearView navigationView = findViewById(R.id.navigationBar);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)navigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomBarBehaviour());

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FragmentMovie()).commit();

        navigationView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                switch (position){
                    case 0:
                        fragment = new FragmentMovie();
                        break;
                    case 1:
                        fragment = new FragmentTv();
                        break;
                    case 2:
                        fragment = new FragmentFavorite();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (backPresTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
        }else {
            backToast.show();
        }

        backPresTime = System.currentTimeMillis();

    }
}

