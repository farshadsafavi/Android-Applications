package com.journaldev.recyclerviewmultipleviewtype.AlarmClock;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.journaldev.recyclerviewmultipleviewtype.R;
import com.journaldev.recyclerviewmultipleviewtype.Strategy.StrategyFragment;

public class RingtonePlayingService extends Service {

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.e("MyActivity", "In the service");
        return null;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {


        final NotificationManager mNM = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(this.getApplicationContext(), StrategyFragment.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Notification mNotify  = new Notification.Builder(this)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle("It is time to go to bed.")
                .setContentText("Click me!")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();

        String state = intent.getExtras().getString("extra");

        // get richard's thing
        String richard_id = intent.getExtras().getString("quote id");
        Log.e("Service: richard id is " , richard_id);

        mNM.notify(0, mNotify);

        Log.e("MyActivity", "In the service");

        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}