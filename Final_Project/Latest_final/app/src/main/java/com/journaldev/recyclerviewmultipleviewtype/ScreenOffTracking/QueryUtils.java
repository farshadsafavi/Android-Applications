package com.journaldev.recyclerviewmultipleviewtype.ScreenOffTracking;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public final class QueryUtils {

    // Realm Object
    private Realm realm;
    ArrayList<Screenoff> screenoffs;
    private QueryUtils() {
    }

    public static ArrayList<Screenoff> extractScreenoffs() {
        long max_duration = 0;
        int max_year = 0;
        int max_mounth = 0;
        int max_day = 0;
        int max_hour = 0;
        int max_minute = 0;
        int max_second = 0;
        int max_target = 0;
        long currentTime;

        ArrayList<Screenoff> screenoffs = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ScreenOffDataMax> ScreenOffDataRealmResults = realm.where(ScreenOffDataMax.class).findAll();
        for (ScreenOffDataMax screenOffDataMax : ScreenOffDataRealmResults) {
            max_target = screenOffDataMax.getTargetSleep();
            currentTime = screenOffDataMax.getCurrentTime();
            max_duration = screenOffDataMax.getScreenOffDurationMax();
            max_year = screenOffDataMax.getScreenOffYearMax();
            max_mounth = screenOffDataMax.getScreenOffMonthMax();
            max_day = screenOffDataMax.getScreenOffDayMax();
            max_hour = screenOffDataMax.getScreenOffhourMax();
            max_minute = screenOffDataMax.getScreenOffMinuteMax();
            max_second = screenOffDataMax.getScreenOffSecondMax();
            Screenoff screenoff = new Screenoff(max_target,
                    currentTime,
                    max_duration,
                    max_year,
                    max_mounth,
                    max_day,
                    max_hour,
                    max_minute,
                    max_second);
            screenoffs.add(screenoff);
        }

        return screenoffs;
    }


}