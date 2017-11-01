package com.example.jevil.mindreactor.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.widget.Chronometer;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.jevil.mindreactor.Events.TabEvents.AddCompleteEvent;
import com.example.jevil.mindreactor.R;

import static android.os.SystemClock.elapsedRealtime;

public class ServiceNotification extends Service {
    public final static String BROADCAST_ACTION = "com.example.jevil.mindreactor.Services";
    NotificationManager nm;
    RemoteViews remoteViews;
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    Chronometer chronometer;
    long timeWhenStopped = 0;
    boolean isPlay = false;
    public static long timeStop;
    PendingIntent pi;
    int itemId;


    public ServiceNotification() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        itemId = intent.getIntExtra("itemId", -1);
        sendNotif();
        regReceiver();
        return START_STICKY; // система перезапускает службу при возможности
    }

    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(1);
    }

    void sendNotif() {
        Context context = getApplicationContext();
        remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_timer2);

        builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.drawable.ic_action_back);
        builder.setOngoing(true);
        builder.setTicker("Внимание!!!");
        builder.setUsesChronometer(true);
//        builder.setContent(remoteViews);

        remoteViews.setChronometer(R.id.chronometer3, elapsedRealtime(), null, true);
        PendingIntent piSetTime = PendingIntent.getBroadcast(this, 0, getIntent(1), PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ivSetTime, piSetTime);
        PendingIntent piPause = PendingIntent.getBroadcast(this, 1, getIntent(2), PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ivPause, piPause);
        notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE); // Создаем экземпляр менеджера уведомлений

        notificationManager.notify(1, builder.build());

        chronometer = new Chronometer(context);
        chronometer.start();

    }

    Intent getIntent(int i) {
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra("action", i);
        return intent;
    }

    void regReceiver() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int extra = intent.getIntExtra("action", -1);
                switch (extra) {
                    case 1: // закрываем уведомление, отправляем время в AddCompleteEvent
                        notificationManager.cancel(1);
                        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                        context.sendBroadcast(it);
                        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                                new Intent(context, AddCompleteEvent.class).putExtra("type", "addFromNotification").putExtra("itemId", itemId), PendingIntent.FLAG_UPDATE_CURRENT);

                        try {
                            timeStop = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000 / 60; // конвертируем миллисекунды в минуты
                            pi.send((int) timeStop);
                            contentIntent.send();
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                        stopSelf();
                        break;
                    case 2:
                        if (isPlay) { // нажато воспроизведение
                            remoteViews.setImageViewResource(R.id.ivPause, R.mipmap.ic_pause);
                            chronometer.setBase(SystemClock.elapsedRealtime()+timeWhenStopped);
                            chronometer.start();
                            remoteViews.setChronometer(R.id.chronometer3, chronometer.getBase(), null, true);
                            notificationManager.notify(1, builder.build());
                            isPlay = false;
                        } else { // нажата пауза
                            remoteViews.setImageViewResource(R.id.ivPause, R.mipmap.ic_play);
                            timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                            chronometer.stop();
                            remoteViews.setChronometer(R.id.chronometer3, chronometer.getBase(), null, false);
                            notificationManager.notify(1, builder.build());
                            isPlay = true;
                        }
                        break;
                    case -1:
                        Toast.makeText(context, "Ошибка, плохой intent", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(receiver, filter);
    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
