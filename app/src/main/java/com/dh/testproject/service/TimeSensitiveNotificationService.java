package com.dh.testproject.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.PermissionChecker;

import com.dh.testproject.Main3Activity;
import com.dh.testproject.R;

public class TimeSensitiveNotificationService extends Service {
    private static final String BASIC_NOTIFICATION_CHANNEL_ID = "basic_notification";
    private static final int URGENT_NOTIFICATION_NOTIFY_ID = 103;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent fullScreenIntent = new Intent(this, Main3Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                fullScreenIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, BASIC_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.app6)
                .setContentTitle("敏感通知")
                .setContentText("我是敏感通知，我跟Notification绑定")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setFullScreenIntent(pendingIntent, true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        startForeground(URGENT_NOTIFICATION_NOTIFY_ID, builder.build());

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
