package com.dev_candra.moviedb.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class Method {

    private Context context;
    private ProgressDialog dialog;
    public Method(Context context){
        this.context = context;
        dialog = new ProgressDialog(context);
    }

    public void makeToast(String message){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }

    public void makeDialog(String title,String message){
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
    }

    public void dialogShow(){
        dialog.show();
    }

    public void dialogDismis(){
        dialog.dismiss();
    }

    public void makeSnackbar(View view,String message){
        Snackbar.make(view,message,Snackbar.LENGTH_SHORT).show();
    }

}
