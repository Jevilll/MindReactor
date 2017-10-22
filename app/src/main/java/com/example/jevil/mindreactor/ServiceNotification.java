package com.example.jevil.mindreactor;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ServiceNotification extends Service {
    public final static String BROADCAST_ACTION = "com.example.jevil.mindreactor";
    NotificationManager nm;

    public ServiceNotification() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        sendNotif();
        regReceiver();
        return super.onStartCommand(intent, flags, startId);
    }

    void sendNotif() {
        Context context = getApplicationContext();
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_timer);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.ic_play);
        builder.setContent(remoteViews);
        builder.setOngoing(true);


//        Intent intent1 = new Intent(BROADCAST_ACTION);
//        intent1.putExtra("action2", "second2");
//        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.btn2, pendingIntent1);

//        Intent intent = new Intent(BROADCAST_ACTION);
//        intent.putExtra("action2", "first2");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, getIntent(0), PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ibSetTime, pendingIntent);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 1, getIntent(1), PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ibPause, pendingIntent2);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE); // Создаем экземпляр менеджера уведомлений
        notificationManager.notify(1, builder.build());
    }

    Intent getIntent(int i) {
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra("action", i);
        Log.d("myLogs", "id: " + i);
        return intent;
    }

    void regReceiver() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int extra = intent.getIntExtra("action", -1);
                Log.d("myLogs", "action: " + extra);
                switch (extra) {
                    case 0:
                        Toast.makeText(context, "first2", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(context, "second2", Toast.LENGTH_SHORT).show();
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
