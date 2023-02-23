package com.bapoto.vtc.firebase;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bapoto.bapoto.R;
import com.bapoto.vtc.ui.user.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
        public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
            super.onMessageReceived(remoteMessage);

            // Check if message contains a data payload
            if (remoteMessage.getData().size() > 0) {
                // Handle data payload of FCM message
                handleDataMessage(remoteMessage);
            }

            // Check if message contains a notification payload
            if (remoteMessage.getNotification() != null) {
                // Handle notification payload of FCM message
                handleNotification(remoteMessage.getNotification().getBody());
            }
        }

        private void handleDataMessage(RemoteMessage remoteMessage) {
            // Extract data from the message
            Map<String, String> data = remoteMessage.getData();

            // Perform actions based on the data received
            String title = data.get("title");
            String message = data.get("message");

            // Show the notification
            showNotification(title, message);
        }

        private void handleNotification(String messageBody) {
            // Perform actions based on the notification received
            showNotification("FCM Message", messageBody);
        }

        private void showNotification(String title, String message) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            @SuppressLint("UnspecifiedImmutableFlag")
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.bapologo_white)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }

    }

