package com.bapoto.vtc.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bapoto.bapoto.R;
import com.bapoto.vtc.model.Admin;
import com.bapoto.vtc.ui.ChatActivity;
import com.bapoto.vtc.ui.MainActivity;
import com.bapoto.vtc.utilities.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MessagingService extends FirebaseMessagingService {


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Admin admin = new Admin();
        admin.id = message.getData().get(Constants.KEY_USER_ID);
        admin.name = message.getData().get(Constants.KEY_NAME);
        admin.token = message.getData().get(Constants.KEY_FCM_TOKEN);

        int notificationId = new Random().nextInt();
        String channelId = "chat_message";


        Intent intent = new Intent(this, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constants.KEY_USER,admin);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channelId);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentTitle(admin.name);
        builder.setContentText(message.getData().get(Constants.KEY_MESSAGE));
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(
                message.getData().get(Constants.KEY_MESSAGE)
        ));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Chat Message";
            String channelDescription = "ce canal est utilisé pour l'envoi de notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel =  new NotificationChannel(channelId,channelName,importance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(notificationId,builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onReservationReceived(@NonNull RemoteMessage reservation) {

       Admin admin = new Admin();
       admin.id = reservation.getData().get(Constants.KEY_USER_ID);
       admin.name = reservation.getData().get(Constants.KEY_NAME);
       admin.token = reservation.getData().get(Constants.KEY_FCM_TOKEN);

        int notificationId = new Random().nextInt();
        String channelId = "chat_message";


        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constants.KEY_USER,admin);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channelId);
        builder.setSmallIcon(R.drawable.bapologo_white);
        builder.setContentTitle(admin.name);
        builder.setContentText("Nouvelle réservation");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Réservation";
            String channelDescription = "ce canal est utilisé pour l'envoi de notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel =  new NotificationChannel(channelId,channelName,importance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(notificationId,builder.build());


    }


}
