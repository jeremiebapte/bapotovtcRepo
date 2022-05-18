package com.bapoto.vtc.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bapoto.bapoto.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotifService extends FirebaseMessagingService {

    private static final String CANAL = "MyCanal";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String myMesage = remoteMessage.getNotification().getBody();
        Log.d("FireBase", "message ous venez de recevoir une notif" + myMesage);

    // Create notification
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, CANAL);
        notifBuilder.setContentTitle("ma notification");
        notifBuilder.setContentText(myMesage);

        //image
        notifBuilder.setSmallIcon(R.drawable.ic_image_notification);

        //envoi notif
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.R) {

            String channelId = getString(R.string.default_notification_channel_id);
            String channelTitle = getString(R.string.notification_title);
            String channelDescription = getString(R.string.default_notification_channel_desc);
            NotificationChannel channel = new NotificationChannel(channelId,channelTitle,NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDescription);
            notificationManager.createNotificationChannel(channel);
            notifBuilder.setChannelId(channelId);


        }

        notificationManager.notify(1,notifBuilder.build());







    }
}
