package com.journaldev.recyclerviewmultipleviewtype.ScreenOffTracking;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class ServiceScreen extends Service {

	 @Override
	 public IBinder onBind(Intent intent) {
	  return null;
	 }
 
	@Override
	 public void onStart(Intent intent, int startId) {
	     try {
	          IntentFilter filter = new IntentFilter();
	          filter.addAction(Intent.ACTION_SCREEN_ON);
	          filter.addAction(Intent.ACTION_SCREEN_OFF);
	          BroadcastReceiver mReceiver = new ReceiverScreen();
	          registerReceiver(mReceiver, filter);
	     } catch (Exception e) {
	          Log.d("ServiceScreen",e.toString());
	     }
	 }
}


