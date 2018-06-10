package com.eaapps.thebesacademy.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Utils.Constants;
import com.eaapps.thebesacademy.Utils.StoreKey;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;


public class MyFirebaseMessaging extends FirebaseMessagingService {
    public String name;
    public String message;
    StoreKey storeKey;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.out.println("eslamgamal2");
        storeKey = new StoreKey(this);
        for (String key : remoteMessage.getData().keySet()) {
            System.out.println("key: "+key+" value: " + remoteMessage.getData().get(key));
        }
        name = remoteMessage.getData().get("name");
        message = remoteMessage.getData().get("message");

        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, Constants.homeClasses(storeKey.getUser())), 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(name)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setSmallIcon(R.drawable.icon_apps)
                .setShowWhen(true)
                .setColor(Color.RED)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setLocalOnly(true)
                .build();
        NotificationManagerCompat.from(this)
                .notify(new Random().nextInt(), notification);


    }
}
