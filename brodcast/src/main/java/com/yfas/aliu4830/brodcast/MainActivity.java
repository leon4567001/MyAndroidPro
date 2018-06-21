package com.yfas.aliu4830.brodcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    final int NOTIFYID = 0x66;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setAction("NOTIFICATION");
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int type = AlarmManager.RTC_WAKEUP;
        //new Date()：表示当前日期，可以根据项目需求替换成所求日期
        //getTime()：日期的该方法同样可以表示从1970年1月1日0点至今所经历的毫秒数
        //long triggerAtMillis = new Date().getTime();
        long triggerAtMillis = System.currentTimeMillis();
        Log.d("date", "onCreate: "+triggerAtMillis);
        long intervalMillis = 1000 * 60;
        manager.setInexactRepeating(type, triggerAtMillis, intervalMillis, pi);


       /* //获取通知管理器,用于发送通知
        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //创建notification对象
        Notification.Builder notification = new Notification.Builder(this);
        //打开通知自动消失
        notification.setAutoCancel(true);
        //设置通知图标
        notification.setSmallIcon(R.drawable.trim);
        //设置通知标题内容
        notification.setContentTitle("有新的点检任务!");
        notification.setContentText("点击开始!");
        //使用系统默认的声音震动 需要注册声明
        notification.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
        //设置发送时间
        notification.setWhen(System.currentTimeMillis());
        //创建跳转的intent
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
        //点击跳转
        notification.setContentIntent(pendingIntent);
        //发送通知
        notificationManager.notify(NOTIFYID, notification.build());*/
    }
}
