package com.journaldev.recyclerviewmultipleviewtype.AlarmClock;

import io.realm.RealmObject;

/**
 * Created by Farshad on 8/22/2016.
 */
public class BedTimeHours extends RealmObject {

    public int SleepHours;
    public int SleepMinutes;

    public int getSleepHours() {
        return SleepHours;
    }

    public void setSleepHours(int sleepHours) {
        SleepHours = sleepHours;
    }

    public int getSleepMinutes() {
        return SleepMinutes;
    }

    public void setSleepMinutes(int sleepMinutes) {
        SleepMinutes = sleepMinutes;
    }

    @Override
    public String toString() {
        return "BedTimeHours{" +
                "SleepHours=" + SleepHours +
                ", SleepMinutes=" + SleepMinutes +
                '}';
    }
}
