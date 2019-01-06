package com.journaldev.recyclerviewmultipleviewtype.ScreenOffTracking;

import io.realm.RealmObject;

/**
 * Created by Farshad on 8/22/2016.
 */
public class TargetSleepHours extends RealmObject {

    private int TargetSleepHours;

    public int getTargetSleepHours() {
        return TargetSleepHours;
    }

    public void setTargetSleepHours(int targetSleepHours) {
        TargetSleepHours = targetSleepHours;
    }

    @Override
    public String toString() {
        return "TargetSleepHours{" +
                "TargetSleepHours=" + TargetSleepHours +
                '}';
    }
}
