package com.journaldev.recyclerviewmultipleviewtype.ScreenOffTracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class ReceiverScreen extends BroadcastReceiver {
    public FileOutputStreamScreenData mTestFileOutputStream;
    public FileOutpuScreenOn mTestFileOutputScreeOn;


    // Golobal Variables
    long    screenOffDuration;
    int     screenOffYear;
    int     screenOffMonth;
    int     screenOffDay;
    int     screenOffhour;
    int     screenOffMinute;
    int     screenOffSecond;
    long    timediff;
    long    screenOffTime;
    int     screenOnYear;
    int     screenOnMonth;
    int     screenOnDay;
    int     screenOnHour;
    int     screenOnMinute;
    int     screenOnSecond;
    long    screenOnDuration;

    @Override
    public void onReceive(Context context, Intent intent) {
        long currentTimeLong = System.currentTimeMillis();
        if (currentTimeLong != 0) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                currentTimeLong = System.currentTimeMillis();
                screenOnDuration = currentTimeLong;
                Calendar c = Calendar.getInstance();
                screenOnYear = c.get(Calendar.YEAR);
                screenOnMonth = c.get(Calendar.MONTH);
                screenOnDay = c.get(Calendar.DAY_OF_MONTH);
                screenOnHour = c.get(Calendar.HOUR_OF_DAY);
                screenOnMinute = c.get(Calendar.MINUTE);
                screenOnSecond = c.get(Calendar.SECOND);
                //store record data in the root derectory
                timediff = currentTimeLong - timediff;
                mTestFileOutputStream.TestWrite(screenOffTime + "-" +
                        timediff + "-" +
                        screenOffYear + "-" +
                        screenOffMonth + "-" +
                        screenOffDay + "-" +
                        screenOffhour + "-" +
                        screenOffMinute + "-" +
                        screenOffSecond +
                        "\n");
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                currentTimeLong = System.currentTimeMillis();
                Calendar c = Calendar.getInstance();
                screenOffYear = c.get(Calendar.YEAR);
                screenOffMonth = c.get(Calendar.MONTH);
                screenOffDay = c.get(Calendar.DAY_OF_MONTH);
                screenOffhour = c.get(Calendar.HOUR_OF_DAY);
                screenOffMinute = c.get(Calendar.MINUTE);
                screenOffSecond = c.get(Calendar.SECOND);
                screenOffTime = currentTimeLong;
                timediff = currentTimeLong;
                screenOnDuration = currentTimeLong - screenOnDuration;
                mTestFileOutputScreeOn.TestWrite(currentTimeLong + "-" +
                        screenOnDuration + "-" +
                        screenOnYear + "-" +
                        screenOnMonth + "-" +
                        screenOnDay + "-" +
                        screenOnHour + "-" +
                        screenOnMinute + "-" +
                        screenOnSecond +
                        "\n");
            }
        }
    }
}
