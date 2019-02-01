package com.allandroidprojects.ecomsample.startup;

import android.content.BroadcastReceiver;
import android.media.CamcorderProfile;
import android.util.Log;
import java.util.*;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.allandroidprojects.ecomsample.R;
import com.google.firebase.messaging.RemoteMessage;
import com.allandroidprojects.ecomsample.startup.ActionReceiver;

public class NotificationHandler extends com.google.firebase.messaging.FirebaseMessagingService{
    int color=0xff123456;
    public NotificationHandler() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        Log.d("Notification","Notification Received: \n"
                +"Title:"+title
                +"Message:"+message);
        ActionReceiver send=new ActionReceiver();
        send.getnotifyinfo(title,message);
        Log.d("called","notify");
        sendNotification(title,message);

    }

    @Override
    public void onDeletedMessages() {

    }

    @Override
    public void onNewToken(String token) {
        Log.d("NotificationHandler", "Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

    }

    private void sendNotification(String title,String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Intent intentaction=new Intent(this,ActionReceiver.class);
        Log.d("Intentaction","done");
        intentaction.setAction("Interested");
        intentaction.setAction("Not Interested");
        //intentaction.putExtra("Yes","Yes");
        //intentaction.putExtra("No","No");

        String channelId = ""; // we need to get channel id here = getString(R.string.default_notification_channel_id);
       PendingIntent pIntent=PendingIntent.getBroadcast(this,1,intentaction,PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .addAction(R.mipmap.tick,"Interested",pIntent)
                        .addAction(R.drawable.ic_clear_black_18dp,"Not Interested",pIntent)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}


