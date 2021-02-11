package com.dev_candra.moviedb.actvitites;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dev_candra.moviedb.R;

public class WebView extends AppCompatActivity {

   private android.webkit.WebView web1;
   private String webUrl;
   private Toolbar toolbar;
   private TextView textNamaWeb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        web1 = findViewById(R.id.web);
        textNamaWeb = findViewById(R.id.tvWeb);
        textNamaWeb.setSelected(true);
        webUrl = getIntent().getStringExtra("url");
        textNamaWeb.setText(webUrl);
        handleWebView(webUrl);
        toolbar = findViewById(R.id.toolbarWebView);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void handleWebView(String url) {
        web1.setWebViewClient(new WebViewClient());
        WebSettings settings = web1.getSettings();
        settings.setJavaScriptEnabled(true);
        web1.loadUrl(url);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            this.finish();
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        if (web1.canGoBack()){
            web1.goBack();
        }else{
            super.onBackPressed();
        }
    }

}
