package com.yfas.aliu4830.brodcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver{
    private static final int NOTIFICATION_ID = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("NOTIFICATION")) {
            NotificationManager manager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent2 = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);
            Notification notify = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.trim)
                    .setWhen(System.currentTimeMillis())
                    .setTicker("您的***项目即将到期，请及时处理！")
                    .setContentTitle("项目到期提醒")
                    .setStyle(new Notification.BigTextStyle().bigText("此处注明的是有关需要提醒项目的某些重要内容"))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setNumber(1).build();
            manager.notify(NOTIFICATION_ID, notify);
        }

    }
}
