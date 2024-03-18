package com.example.chat_app1.firebase;

import static com.example.chat_app1.util.Constants.ApplicationForeground;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;


import com.example.chat_app1.MainActivity;
import com.example.chat_app1.util.ONotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseFCM";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("message"));
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if(!ApplicationForeground) {
            //RealtimeDatabase.getFirebaseInstance(this).readMsgList(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sendONotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
            } else {
                //sendNotification(remoteMessage);
            }
        }

    }
    private void sendONotification(String title,String body)
    {
        String icon="";

        int j=111;
        Intent intent=new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        ONotification oNotification=new ONotification(this);
        Notification.Builder builder=oNotification.getOreoNotication(title,body,pendingIntent,defaultSound,icon);
        int i=0;
        if(j>0){
            i=j;
        }
        oNotification.getManager().notify(i,builder.build());
    }
}
