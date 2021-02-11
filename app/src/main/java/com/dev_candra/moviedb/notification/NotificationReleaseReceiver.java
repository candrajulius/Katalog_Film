package com.dev_candra.moviedb.notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.dev_candra.moviedb.R;
import com.dev_candra.moviedb.actvitites.MainActivity;
import com.dev_candra.moviedb.api.Service;
import com.dev_candra.moviedb.utils.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotificationReleaseReceiver extends BroadcastReceiver {

    private static final String NOTIFICATION_CHANNEL_ID = "channel_02";
    private static final int NOTIFICATION_ID = 101;
    private Context mContext;
    private Method method;

    public NotificationReleaseReceiver(){}

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        getReleaseMovie();
        method = new Method(mContext);
    }

    private void getReleaseMovie() {
        final String dateToday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        AndroidNetworking.get(Service.BASEURL + Service.MOVIE_POPULAR
        + Service.API_KEY + Service.LANGUAGE + Service.NOTIF_DATE + dateToday
        + Service.REALESE_DATE + dateToday)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                sendNotification(mContext,jsonObject.getString("title"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            method.makeToast("Gagal menampilkan data");
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        method.makeToast("Gagal menampilkan data");
                    }
                });
    }

    private void sendNotification(Context mContext, String title) {
        NotificationManager manager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(mContext, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,NotificationReleaseReceiver.NOTIFICATION_ID,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri uriTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_baseline_movie_24)
                .setContentTitle(title)
                .setContentText("Ada film baru hari ini")
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(mContext,android.R.color.transparent))
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(uriTone);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,"Notification_channel_name",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.enableLights(true);
            channel.setLightColor(Color.YELLOW);
            channel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            manager.createNotificationChannel(channel);
        }

        manager.notify(NotificationReleaseReceiver.NOTIFICATION_ID,builder.build());

    }

    public void setReleaseAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,NotificationReleaseReceiver.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,NOTIFICATION_ID,intent,0);
        if (alarmManager != null){
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,pendingIntent
            );
        }
    }

    public void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context));
    }

    private PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context,NotificationReleaseReceiver.class);
        return PendingIntent.getBroadcast(context,NOTIFICATION_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
