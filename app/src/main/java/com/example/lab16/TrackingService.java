package com.example.lab16;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrackingService extends Service {

    private final IBinder serviceBinder = new ServiceControl();
    
    private int elapsedSeconds = 0;
    private boolean active = false;
    private ScheduledExecutorService timeExecutor;
    private static final int MSG_ID = 2024;
    private NotificationManager nm;
    private static final String CHANNEL_ID = "tracker_channel_01";

    public class ServiceControl extends Binder {
        public TrackingService getBoundService() {
            return TrackingService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        initChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String actionRequested = (intent != null) ? intent.getAction() : null;

        if ("ACTION_TERMINATE".equals(actionRequested)) {
            stopSelf();
            return START_NOT_STICKY;
        }

        if (!active) {
            active = true;
            startForeground(MSG_ID, buildStatusNotification());
            launchTimer();
        }
        return START_STICKY;
    }

    private void launchTimer() {
        timeExecutor = Executors.newSingleThreadScheduledExecutor();
        timeExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                elapsedSeconds++;
                refreshNotification();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void initChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel(
                    CHANNEL_ID,
                    "Suivi en arrière-plan",
                    NotificationManager.IMPORTANCE_LOW
            );
            nm.createNotificationChannel(chan);
        }
    }

    private Notification buildStatusNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Compteur Actif")
                .setContentText("Durée : " + getTimeLabel(elapsedSeconds))
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }

    private void refreshNotification() {
        nm.notify(MSG_ID, buildStatusNotification());
    }

    private String getTimeLabel(int totalSecs) {
        int m = totalSecs / 60;
        int s = totalSecs % 60;
        return String.format("%02d:%02d", m, s);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    @Override
    public void onDestroy() {
        active = false;
        if (timeExecutor != null) {
            timeExecutor.shutdown();
        }
        stopForeground(true);
        super.onDestroy();
    }
}
