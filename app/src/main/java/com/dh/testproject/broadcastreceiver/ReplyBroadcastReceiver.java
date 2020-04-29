package com.dh.testproject.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import com.dh.testproject.R;

public class ReplyBroadcastReceiver extends BroadcastReceiver {
    public static final String REPLY_CUSTOM_ACTION = "com.dh.reply.broadcast";
    private static final String KEY_TEXT_REPLY = "key_text_reply";
    private static final String TAG = "ReplyBroadcastReceiver";
    private static final String BASIC_NOTIFICATION_CHANNEL_ID = "basic_notification";
    private static final int REPLY_NOTIFICATION_NOTIFY_ID = 101;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "action: " + intent.getAction());
        Log.e(TAG, "获取回复结果：" + getMessageText(intent));
        Toast.makeText(context,getMessageText(intent),Toast.LENGTH_SHORT).show();



        createNotification(context);
    }

    private void createNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, BASIC_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.app6)
                .setContentTitle(context.getString(R.string.basic_notification))
                .setContentText(context.getString(R.string.basic_notification))
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(REPLY_NOTIFICATION_NOTIFY_ID, builder.build());
    }


    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(KEY_TEXT_REPLY);
        }
        return "";
    }

}
