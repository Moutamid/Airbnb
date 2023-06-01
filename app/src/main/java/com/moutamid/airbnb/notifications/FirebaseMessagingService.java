package com.moutamid.airbnb.notifications;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.fxn.stash.Stash;
import com.google.firebase.messaging.RemoteMessage;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.SplashScreenActivity;
import com.moutamid.airbnb.constant.Constants;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    NotificationManager mNotificationManager;

    public void onNewToken(String s) {
        super.onNewToken(s);
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, 0).edit();
        editor.putString("name", s);
        editor.apply();
        Log.d("ContentValues", "onNewToken: " + s);
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        if (Stash.getBoolean(Constants.PAUSE_STATUS, false))
//            return;

        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(2));
        r.play();
        if (Build.VERSION.SDK_INT >= 28) {
            r.setLooping(false);
        }

        ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(new long[]{100, 300, 300, 300}, -1);

//        int resourceImage = getResources().getIdentifier(remoteMessage.getNotification().getIcon(), "drawable", getPackageName());
        NotificationCompat.Builder builder = new NotificationCompat.Builder((Context) this, "Your_channel_id");
//        if (Build.VERSION.SDK_INT >= 21) {
//            builder.setSmallIcon(resourceImage);
//        } else {
//            builder.setSmallIcon(resourceImage);
//        }

        builder.setSmallIcon(R.drawable.airbnb);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(this, SplashScreenActivity.class),
                PendingIntent.FLAG_IMMUTABLE);//134217728
        builder.setContentTitle(remoteMessage.getNotification().getTitle());
        builder.setContentText(remoteMessage.getNotification().getBody());
        builder.setContentIntent(pendingIntent);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()));
        builder.setAutoCancel(true);
        builder.setPriority(PRIORITY_HIGH);
        this.mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);//"notification"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.mNotificationManager.createNotificationChannel(new NotificationChannel("Your_channel_id", "Message Notification", NotificationManager.IMPORTANCE_HIGH));//4
        }
        builder.setChannelId("Your_channel_id");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.mNotificationManager.notify(100, builder.build());
//        NotificationManagerCompat.from(this).notify(100, builder.build());
    }
}

