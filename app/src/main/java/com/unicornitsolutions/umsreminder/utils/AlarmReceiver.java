package com.unicornitsolutions.umsreminder.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.unicornitsolutions.umsreminder.R;
import com.unicornitsolutions.umsreminder.ui.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm went off!", Toast.LENGTH_SHORT).show();
        SharedPreferencesHelper helper = new SharedPreferencesHelper(context);
        String companyName = intent.getStringExtra("companyName");
        String machineName = intent.getStringExtra("machineName");
        int i = 0;
        while (helper.getData("alarm_"+i)!=null){
            i++;
        }
        helper.saveData("alarm_"+i,companyName+"_"+machineName);



        createNotificationChannel(context);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmSound == null) {
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher) // Set your own icon
                .setContentTitle("Alarm")
                .setContentText(companyName+" company needs your attention. "+machineName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(alarmSound)
                .setAutoCancel(true);

        // Create an Intent to open the app when the notification is clicked
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        builder.setContentIntent(pendingIntent);

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0, builder.build());
        }
    }

    private void createNotificationChannel(Context context) {
        CharSequence name = "Alarm Channel";
        String description = "Channel for alarm notifications";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
        channel.setDescription(description);
        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), null);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }
}