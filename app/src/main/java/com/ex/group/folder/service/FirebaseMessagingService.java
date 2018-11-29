package com.ex.group.folder.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ex.group.folder.IntroActivity;
import com.google.firebase.messaging.RemoteMessage;

import com.ex.group.folder.R;

/**
 * Created by heejung on 2017-05-04.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
    final static String TAG = "FirebaseMessage";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        Map<String, String> data = remoteMessage.getData();
//        String title = data.get("title");
//        String message = data.get("message");

//        Log.i(TAG, "title==="+title);
//        Log.i(TAG, "message==="+message);

        Log.i(TAG, "msg get from===>>"+remoteMessage.getFrom());
        Log.i(TAG, "msg.getdata size====>>"+remoteMessage.getData().size());

        if(remoteMessage.getData().size()>0){
            Log.i(TAG, "msg.getdata====>>"+remoteMessage.getData());
        }
        if(remoteMessage.getNotification() != null){
            Log.i(TAG, "msg body===>"+remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, IntroActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_04)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
