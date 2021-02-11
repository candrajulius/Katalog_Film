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

import com.dev_candra.moviedb.R;
import com.dev_candra.moviedb.actvitites.MainActivity;

import java.util.Calendar;

public class NotificationDailyReceiver extends BroadcastReceiver {


    private static final String NOTIFACATION_CHANNEL_ID = "channel_01";
    private static final int NOTIFICATION_ID = 100;

    public NotificationDailyReceiver(){}

    @Override
    public void onReceive(Context context, Intent intent) {
        sendNotification(context,context.getString(R.string.app_name),"Ada kejutan hari ini");
    }

    public void setDailyAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,NotificationDailyReceiver.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        PendingIntent intent1 = PendingIntent.getBroadcast(context,NOTIFICATION_ID,intent,0);
        if (alarmManager != null){
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,intent1);
        }
    }

    public void sendNotification(Context context, String title, String des){
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(
                Context.NOTIFICATION_SERVICE
        );
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,NotificationDailyReceiver.NOTIFICATION_ID,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri uriTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_baseline_movie_24)
                .setContentTitle(title)
                .setContentText(des)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context,android.R.color.transparent))
                .setVibrate(new long[]{1000,1000,1000,1000})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(uriTone);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
            NOTIFACATION_CHANNEL_ID,"Notification_Channel_Name",NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(Color.YELLOW);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});

            builder.setChannelId(NOTIFACATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(NotificationDailyReceiver.NOTIFICATION_ID,builder.build());
    }

    public void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendignIntent(context));
    }

    private PendingIntent getPendignIntent(Context context) {
        Intent intent = new Intent(context,NotificationDailyReceiver.class);
        return PendingIntent.getBroadcast(context,NOTIFICATION_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
