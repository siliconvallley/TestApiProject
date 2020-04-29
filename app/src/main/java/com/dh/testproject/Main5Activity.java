package com.dh.testproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.app.RemoteInput;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.IconCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.dh.testproject.broadcastreceiver.ReplyBroadcastReceiver;
import com.dh.testproject.service.TimeSensitiveNotificationService;

import java.util.ArrayList;
import java.util.List;

/**
 * 各种通知测试
 */
public class Main5Activity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Main5Activity";
    private static final String BASIC_NOTIFICATION_CHANNEL_ID = "basic_notification";
    private static final int BASIC_NOTIFICATION_NOTIFY_ID = 100;
    private static final int REPLY_NOTIFICATION_NOTIFY_ID = 101;
    private static final String KEY_TEXT_REPLY = "key_text_reply";
    private static final int REPLY_REQUEST_CODE = 100;
    private static final String PROGRESS_NOTIFICATION_CHANNEL_ID = "progress_notification";
    private static final int PROGRESS_NOTIFICATION_NOTIFY_ID = 102;
    private static final int PROGRESS_MAX = 100;
    private static final int URGENT_NOTIFICATION_NOTIFY_ID = 103;
    private static final String EXPANDABLE_NOTIFICATION_CHANNEL_ID = "expandable_notification";
    private static final int EXPANDABLE_NOTIFICATION_NOTIFY_ID = 104;
    private static final String GROUP_KEY_TEST = "group_key_test";

    private Button btBasicNotification;
    private Button btReplyNotification;
    private ReplyBroadcastReceiver replyBroadcastReceiver;
    private Button btProgressNotification;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        int curProgress = 0;

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                Log.e(TAG, "收到消息了");
                curProgress += 5;
            }
            progressBuilder.setProgress(PROGRESS_MAX, curProgress, false);
            progressManagerCompat.notify(PROGRESS_NOTIFICATION_NOTIFY_ID, progressBuilder.build());
            if (curProgress <= PROGRESS_MAX) {
                handler.postDelayed(runnable, 500);
            } else {
                curProgress = 0;
                progressBuilder.setProgress(PROGRESS_MAX, curProgress, false);
                progressManagerCompat.notify(PROGRESS_NOTIFICATION_NOTIFY_ID, progressBuilder.build());
            }
        }
    };
    private NotificationCompat.Builder progressBuilder;
    private NotificationManagerCompat progressManagerCompat;
    private Button btUrgentNotification;
    private Button btExpandableNotification;
    private Button btStartActivityNotification;
    private Button btGroupNotification;
    private Button btOpenNotificationSettings;
    private Button btCustomNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        initViews();
        initListeners();
        createNotificationChannel();
        createBroadcast();
    }

    private void createBroadcast() {
        replyBroadcastReceiver = new ReplyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ReplyBroadcastReceiver.REPLY_CUSTOM_ACTION);
        registerReceiver(replyBroadcastReceiver, intentFilter);
    }

    private void createNotificationChannel() {
        createNotificationChannel(
                BASIC_NOTIFICATION_CHANNEL_ID,
                getString(R.string.basic_channel_name),
                getString(R.string.basic_channel_des));
        createNotificationChannel(
                PROGRESS_NOTIFICATION_CHANNEL_ID,
                getString(R.string.progress_channel_name),
                getString(R.string.progress_channel_des));
        createNotificationChannel(
                EXPANDABLE_NOTIFICATION_CHANNEL_ID,
                getString(R.string.expandable_channel_name),
                getString(R.string.expandable_channel_des));
    }

    private void initListeners() {
        btBasicNotification.setOnClickListener(this);
        btReplyNotification.setOnClickListener(this);
        btProgressNotification.setOnClickListener(this);
        btUrgentNotification.setOnClickListener(this);
        btExpandableNotification.setOnClickListener(this);
        btStartActivityNotification.setOnClickListener(this);
        btGroupNotification.setOnClickListener(this);
        btOpenNotificationSettings.setOnClickListener(this);
        btCustomNotification.setOnClickListener(this);
    }

    private void initViews() {
        btBasicNotification = findViewById(R.id.btBasicNotification);
        btReplyNotification = findViewById(R.id.btReplyNotification);
        btProgressNotification = findViewById(R.id.btProgressNotification);
        btUrgentNotification = findViewById(R.id.btUrgentNotification);
        btExpandableNotification = findViewById(R.id.btExpandableNotification);
        btStartActivityNotification = findViewById(R.id.btStartActivityNotification);
        btGroupNotification = findViewById(R.id.btGroupNotification);
        btOpenNotificationSettings = findViewById(R.id.btOpenNotificationSettings);
        btCustomNotification = findViewById(R.id.btCustomNotification);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btBasicNotification:
                basicNotification();
                break;
            case R.id.btReplyNotification:
                replyNotification();
                break;
            case R.id.btProgressNotification:
                progressNotification();
                break;
            case R.id.btUrgentNotification:
                urgentNotification();
                break;
            case R.id.btExpandableNotification:
                //expandableNotificationImg();
                //expandableNotificationText();
                //expandableNotificationText1();
                expandableNotificationText2();
                //expandableNotificationMedia();
                break;
            case R.id.btStartActivityNotification:
                startActivityNotification();
                //startActivityNotification1();
                break;
            case R.id.btGroupNotification:
                groupNotification();
                break;
            case R.id.btOpenNotificationSettings:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    openNotificationSettings();
                }
                break;
            case R.id.btCustomNotification:
                customNotification();
                break;
        }
    }

    /**
     * 使用自定义Notification布局
     */
    private void customNotification() {
        RemoteViews smallView = new RemoteViews(getPackageName(), R.layout.small_notification_layout);
        RemoteViews largeView = new RemoteViews(getPackageName(), R.layout.small_notification_layout);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, BASIC_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.head_icon)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(smallView)
                .setCustomBigContentView(largeView);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(BASIC_NOTIFICATION_NOTIFY_ID, builder.build());
    }

    /**
     * 打开通知设置
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openNotificationSettings() {
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        //intent.putExtra(Settings.EXTRA_CHANNEL_ID, BASIC_NOTIFICATION_CHANNEL_ID);
        //intent.putExtra(Settings.EXTRA_CHANNEL_ID, EXPANDABLE_NOTIFICATION_CHANNEL_ID);
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, PROGRESS_NOTIFICATION_CHANNEL_ID);
        startActivity(intent);
    }

    /**
     * 获取Notification信息
     */
    private void getNotificationInfo() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = manager.getNotificationChannel(BASIC_NOTIFICATION_CHANNEL_ID);
            List<NotificationChannel> notificationChannels = manager.getNotificationChannels();

            channel.getSound();
            channel.getVibrationPattern();

            // 根据通道ID删除通道
            manager.deleteNotificationChannel(BASIC_NOTIFICATION_CHANNEL_ID);
        }
    }

    /**
     * 将通知分组
     */
    private void groupNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.head_icon);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, BASIC_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.app6)
                .setContentTitle("分组通知")
                .setContentText("有两条新消息")
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("Alex Faarborg  Check this out")
                        .addLine("Jeff Chang    Launch Party")
                        .setBigContentTitle("2 new messages")
                        .setSummaryText("聊天消息"))
                .setGroup(GROUP_KEY_TEST)
                .setGroupSummary(true);

        NotificationCompat.Builder builder1 = new NotificationCompat.Builder(this, BASIC_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.app6)
                .setContentTitle("张三")
                .setContentText("出来吃饭了")
                .setGroup(GROUP_KEY_TEST)
                .setLargeIcon(bitmap);

        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this, BASIC_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.app6)
                .setContentTitle("李四")
                .setContentText("出来看电影")
                .setGroup(GROUP_KEY_TEST)
                .setLargeIcon(bitmap);

        NotificationCompat.Builder builder3 = new NotificationCompat.Builder(this, BASIC_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.app6)
                .setContentTitle("王五")
                .setContentText("出来喝酒")
                .setGroup(GROUP_KEY_TEST)
                .setLargeIcon(bitmap);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(10, builder1.build());
        managerCompat.notify(20, builder2.build());
        managerCompat.notify(30, builder3.build());
        managerCompat.notify(BASIC_NOTIFICATION_NOTIFY_ID, builder.build());
    }

    /**
     * 通过Notification去启动Activity，不会加入回退栈
     */
    private void startActivityNotification1() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, BASIC_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.head_icon)
                .setContentTitle("启动Activity")
                .setContentText("真意外，我竟然可以启动Activity")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(BASIC_NOTIFICATION_NOTIFY_ID, builder.build());
    }

    /**
     * 通过Notification去启动Activity
     */
    private void startActivityNotification() {
        Intent intent = new Intent(this, ResultActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, BASIC_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.head_icon)
                .setContentTitle("启动Activity")
                .setContentText("真意外，我竟然可以启动Activity")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(BASIC_NOTIFICATION_NOTIFY_ID, builder.build());
    }

    /**
     * 音视频类型的通知
     */
    private void expandableNotificationMedia() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test_icon, options);

        Bitmap head = BitmapFactory.decodeResource(getResources(), R.mipmap.head_icon);

        PendingIntent previousPendingIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
        PendingIntent pausePendingIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
        PendingIntent nextPendingIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);

        MediaSessionCompat mediaSession = new MediaSessionCompat(this, TAG);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, EXPANDABLE_NOTIFICATION_CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.head_icon)
                .setContentTitle("林俊杰")
                .setContentText("裂缝中的阳光")
                .addAction(R.mipmap.previous, "Previous", previousPendingIntent)
                .addAction(R.mipmap.pause, "Pause", pausePendingIntent)
                .addAction(R.mipmap.next, "Next", nextPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(mediaSession.getSessionToken()))
                .setLargeIcon(bitmap);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(EXPANDABLE_NOTIFICATION_NOTIFY_ID, builder.build());
    }

    /**
     * 可扩展通知 -- 添加消息类型的文本
     */
    private void expandableNotificationText2() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.head_icon);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.app6);

        IconCompat iconCompat1 = IconCompat.createWithBitmap(bitmap);
        Person person1 = new Person.Builder()
                .setName("张三")
                .setBot(true)
                .setImportant(true)
                .setIcon(iconCompat1)
                .build();
        NotificationCompat.MessagingStyle.Message message1 = new NotificationCompat.MessagingStyle.Message(
                "你好，今天天气怎么样？",
                System.currentTimeMillis(),
                person1);

        IconCompat iconCompat2 = IconCompat.createWithBitmap(bitmap);
        Person person2 = new Person.Builder()
                .setName("李四")
                .setBot(true)
                .setImportant(true)
                .setIcon(iconCompat2)
                .build();
        NotificationCompat.MessagingStyle.Message message2 = new NotificationCompat.MessagingStyle.Message(
                "你好，今天天气很好，出来玩！",
                System.currentTimeMillis(),
                person2);

        //IconCompat iconCompat3 = IconCompat.createWithBitmap(bitmap1);
        IconCompat iconCompat3 = IconCompat.createWithResource(this,R.mipmap.app6);
        Person person3 = new Person.Builder()
                .setName("王五")
                .setBot(true)
                .setImportant(true)
                .setIcon(iconCompat3)
                .build();
        NotificationCompat.MessagingStyle.Message message3 = new NotificationCompat.MessagingStyle.Message(
                "赶紧出来喝酒",
                System.currentTimeMillis(),
                person3)
                .setData("image/png", resourceToUri(this,R.mipmap.test_icon));

        NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle(person1)
                .setConversationTitle("侃大山联盟")
                .addMessage(message3)
                .addMessage(message2)
                .addMessage(message1);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, EXPANDABLE_NOTIFICATION_CHANNEL_ID)
                .setStyle(messagingStyle)
                .setSmallIcon(R.mipmap.app6)
                .setContentTitle("李白")
                .setContentText("晚上一起吃饭吟诗？")
                .setLargeIcon(bitmap)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setSubText("111")
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(EXPANDABLE_NOTIFICATION_NOTIFY_ID, builder.build());
    }

    /**
     * 可扩展通知 -- 文本，单独添加一行  最多添加6行
     * 如果超过6行，只会显示6行
     */
    private void expandableNotificationText1() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.head_icon);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                .setBigContentTitle("杜甫发来的消息")
                .setSummaryText("我是一个Summary内容")
                .addLine("君不见黄河之水天上来")
                .addLine("奔流到海不复回")
                .addLine("君不见高堂明镜悲白发")
                .addLine("朝如青丝暮成雪")
                .addLine("人生得意须尽欢")
                .addLine("莫使金樽空对月");



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, EXPANDABLE_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.app6)
                .setContentTitle("杜甫来了六条新消息")
                .setContentText("你想去看他发的什么吗？")
                .setStyle(inboxStyle)
                .setLargeIcon(bitmap)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setCategory(NotificationCompat.CATEGORY_EMAIL)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setSubText("副标题");

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(EXPANDABLE_NOTIFICATION_NOTIFY_ID, builder.build());
    }

    /**
     * 可扩展通知 -- 大文本
     */
    private void expandableNotificationText() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.head_icon);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText(getString(R.string.expandable_text))
                .setBigContentTitle("李白")
                .setSummaryText("备忘录");


        // 一般使用服务
        Intent snoozeIntent = new Intent(this, ResultActivity.class);
        snoozeIntent.setAction("");
        snoozeIntent.putExtra("", "");

        PendingIntent snoozePendingIntent = PendingIntent.getService(this, 0, snoozeIntent, 0);
        NotificationCompat.Action snoozeAction =
                new NotificationCompat.Action.Builder(
                        R.mipmap.app6,
                        "Snooze",
                        snoozePendingIntent)
                        .build();

        Intent dismissIntent = new Intent(this, ResultActivity.class);
        dismissIntent.setAction("");
        dismissIntent.putExtra("", "");

        PendingIntent dismissPendingIntent = PendingIntent.getService(this, 0, dismissIntent, 0);
        NotificationCompat.Action dismissAction =
                new NotificationCompat.Action.Builder(
                        R.mipmap.app6,
                        "Dismiss",
                        dismissPendingIntent)
                        .build();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, EXPANDABLE_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.app6)
                .setContentTitle("李白")
                .setContentText("晚上一起吃饭吟诗？")
                .setStyle(bigTextStyle)
                .setLargeIcon(bitmap)
                .addAction(snoozeAction)
                .addAction(dismissAction)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(EXPANDABLE_NOTIFICATION_NOTIFY_ID, builder.build());
    }

    /**
     * 可扩展的通知 -- image
     */
    private void expandableNotificationImg() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test_icon, options);

        Bitmap headBm = BitmapFactory.decodeResource(getResources(), R.mipmap.head_icon);

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap)
                .bigLargeIcon(null)
                .setBigContentTitle("张三的截图")
                .setSummaryText("这是靓女的截图");

        Intent mainIntent = new Intent(this, ResultActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        //taskStackBuilder.addParentStack(ResultActivity.class);
        //taskStackBuilder.addNextIntent(mainIntent);
        taskStackBuilder.addNextIntentWithParentStack(mainIntent);

        PendingIntent mainPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, EXPANDABLE_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.app6)
                .setContentTitle("张三的截图")
                .setContentText("你想看这是什么截图吗？")
                .setStyle(bigPictureStyle)
                .setLargeIcon(headBm) // 设置图片收缩之后，右侧的显示大图
                .setSubText("111") // 设置Summary
                .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .addPerson("杜甫");

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(EXPANDABLE_NOTIFICATION_NOTIFY_ID, builder.build());
    }

    /**
     * 紧急通知
     */
    private void urgentNotification() {
        Intent fullScreenIntent = new Intent(this, Main3Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                fullScreenIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder replaceBuilder = new NotificationCompat.Builder(this, BASIC_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.app6)
                .setContentTitle("替代消息")
                .setContentText("不好意思，我把你替代了，你打我呀，哈哈哈哈...");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, BASIC_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.app6)
                .setContentTitle("全屏警告")
                .setContentText("我是全屏警告")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setFullScreenIntent(pendingIntent, true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPublicVersion(replaceBuilder.build());

        //builder.setAutoCancel(true); // 设置点击之后自动消失
        //builder.setTimeoutAfter(5000); // 设置5秒后自动消失

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        //managerCompat.notify(URGENT_NOTIFICATION_NOTIFY_ID, builder.build());

        startService(new Intent(this, TimeSensitiveNotificationService.class));


        // 清除通知
        //managerCompat.cancelAll();
        //managerCompat.cancel(0);
    }

    /**
     * 带进度条的Notification
     */
    private void progressNotification() {
        progressBuilder = new NotificationCompat.Builder(this, PROGRESS_NOTIFICATION_CHANNEL_ID);
        progressBuilder.setSmallIcon(R.mipmap.app6)
                .setContentTitle("应用更新")
                .setContentText("正在下载应用最新的版本...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);


        progressBuilder.setProgress(PROGRESS_MAX, 0, false);
        handler.postDelayed(runnable, 500);

        progressManagerCompat = NotificationManagerCompat.from(this);
        progressManagerCompat.notify(PROGRESS_NOTIFICATION_NOTIFY_ID, progressBuilder.build());
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(100);
        }
    };

    /**
     * 带回复框的Notification
     */
    private void replyNotification() {
        String replyLabel = getString(R.string.reply_label);
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .setChoices(new String[]{"你好！", "吃饭吗？"}) // 设置回复选项
                .build();

        Intent intent = new Intent(this, ReplyBroadcastReceiver.class);
        //intent.setAction("");
        //PendingIntent.getService();
        //PendingIntent.getForegroundService();
        PendingIntent replyIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                REPLY_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                R.mipmap.app6,
                getString(R.string.reply_label),
                replyIntent)
                .addRemoteInput(remoteInput)
                .build();


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, BASIC_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.app6)
                .setContentTitle(getString(R.string.basic_notification))
                .setContentText(getString(R.string.basic_notification_content))
                .addAction(action)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(REPLY_NOTIFICATION_NOTIFY_ID, builder.build());
    }

    /**
     * 最基本的一些Notification方法的使用
     */
    private void basicNotification() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText(getString(R.string.basic_notification_content))
                .setBigContentTitle("Big Text Title")
                .setSummaryText("备忘录");


        Intent snoozeIntent = new Intent(this, ResultActivity.class);
        snoozeIntent.setAction("");
        snoozeIntent.putExtra("", "");

        PendingIntent snoozePendingIntent = PendingIntent.getService(this, 0, snoozeIntent, 0);
        NotificationCompat.Action snoozeAction =
                new NotificationCompat.Action.Builder(
                        R.mipmap.app6,
                        "Snooze",
                        snoozePendingIntent)
                        .build();

        Intent dismissIntent = new Intent(this, ResultActivity.class);
        dismissIntent.setAction("");
        dismissIntent.putExtra("", "");

        PendingIntent dismissPendingIntent = PendingIntent.getService(this, 0, dismissIntent, 0);
        NotificationCompat.Action dismissAction =
                new NotificationCompat.Action.Builder(
                        R.mipmap.app6,
                        "Dismiss",
                        snoozePendingIntent)
                        .build();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, BASIC_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.app6)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.test_icon))
                .setContentTitle("大文本")
                .setContentText("我是大文本")
                .setStyle(bigTextStyle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                //.setNumber(10) // 设置长按APP图标，显示消息的条数
                //.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL) //控制长按app图标，弹出的menu中图标的大小
                .setContentIntent(pendingIntent)
                .addAction(snoozeAction)
                .addAction(dismissAction)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(BASIC_NOTIFICATION_NOTIFY_ID, builder.build());
    }

    private void createNotificationChannel(String channelId, String channelName, String channelDes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDes);
            // 设置震动
            channel.enableVibration(true);
            // 呼吸灯提醒
            channel.enableLights(true);
            //channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            // 设置在app显示一个提醒的小圆点（在8.0之后 API26）
            channel.setShowBadge(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager == null) return;
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createNotificationGroup(String groupId, String groupName, String groupDes) {
        NotificationManager notificationManager;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager = getSystemService(NotificationManager.class);
        } else {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        if (notificationManager == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannelGroup channelGroup = new NotificationChannelGroup(groupId, groupName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                channelGroup.setDescription(groupDes);
            }
            notificationManager.createNotificationChannelGroup(channelGroup);
        }
    }

    private static Uri resourceToUri(Context context, int resId) {
        return Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://"
                        + context.getResources().getResourcePackageName(resId)
                        + "/"
                        + context.getResources().getResourceTypeName(resId)
                        + "/"
                        + context.getResources().getResourceEntryName(resId));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (replyBroadcastReceiver != null) {
            unregisterReceiver(replyBroadcastReceiver);
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        stopService(new Intent(this, TimeSensitiveNotificationService.class));
    }
}
