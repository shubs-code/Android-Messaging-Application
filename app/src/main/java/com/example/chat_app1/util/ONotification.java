package com.example.chat_app1.util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import com.example.chat_app1.R;

public class ONotification extends ContextWrapper
{

    private static final String CHANNEL_ID="com.wired";
    private static final String CHANNEL_NAME="New Message";

    private NotificationManager notificationManager;

    public ONotification(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel()
    {
        NotificationChannel channel= new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager()
    {
        if (notificationManager==null){
            notificationManager= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        }
        return notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getOreoNotication(String title, String body, PendingIntent pendingIntent, Uri sounduri,String icon)
    {
        return new Notification.Builder(getApplicationContext(),CHANNEL_ID).setContentIntent(pendingIntent).setContentTitle(title).setContentText(body).setSmallIcon(R.mipmap.ic_launcher_foreground).setSound(sounduri).setAutoCancel(true);
    }
}