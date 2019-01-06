package com.journaldev.recyclerviewmultipleviewtype.ScreenOffTracking;

import io.realm.RealmObject;

public class ScreenOffDataMax extends RealmObject {
    int  targetSleep;
    long currentTime;
    long screenOffDurationMax;
    int screenOffYearMax;
    int screenOffMonthMax;
    int screenOffDayMax;
    int screenOffhourMax;
    int screenOffMinuteMax;
    int screenOffSecondMax;

    @Override
    public String toString() {
        return "ScreenOffDataMax{" +
                "targetSleep=" + targetSleep +
                ", currentTime=" + currentTime +
                ", screenOffDurationMax=" + screenOffDurationMax +
                ", screenOffYearMax=" + screenOffYearMax +
                ", screenOffMonthMax=" + screenOffMonthMax +
                ", screenOffDayMax=" + screenOffDayMax +
                ", screenOffhourMax=" + screenOffhourMax +
                ", screenOffMinuteMax=" + screenOffMinuteMax +
                ", screenOffSecondMax=" + screenOffSecondMax +
                '}';
    }

    public int getTargetSleep() {
        return targetSleep;
    }

    public void setTargetSleep(int targetSleep) {
        this.targetSleep = targetSleep;
    }


    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public long getScreenOffDurationMax() {
        return screenOffDurationMax;
    }

    public void setScreenOffDurationMax(long screenOffDurationMax) {
        this.screenOffDurationMax = screenOffDurationMax;
    }

    public int getScreenOffYearMax() {
        return screenOffYearMax;
    }

    public void setScreenOffYearMax(int screenOffYearMax) {
        this.screenOffYearMax = screenOffYearMax;
    }

    public int getScreenOffMonthMax() {
        return screenOffMonthMax;
    }

    public void setScreenOffMonthMax(int screenOffMonthMax) {
        this.screenOffMonthMax = screenOffMonthMax;
    }

    public int getScreenOffDayMax() {
        return screenOffDayMax;
    }

    public void setScreenOffDayMax(int screenOffDayMax) {
        this.screenOffDayMax = screenOffDayMax;
    }

    public int getScreenOffhourMax() {
        return screenOffhourMax;
    }

    public void setScreenOffhourMax(int screenOffhourMax) {
        this.screenOffhourMax = screenOffhourMax;
    }

    public int getScreenOffMinuteMax() {
        return screenOffMinuteMax;
    }

    public void setScreenOffMinuteMax(int screenOffMinuteMax) {
        this.screenOffMinuteMax = screenOffMinuteMax;
    }

    public int getScreenOffSecondMax() {
        return screenOffSecondMax;
    }

    public void setScreenOffSecondMax(int screenOffSecondMax) {
        this.screenOffSecondMax = screenOffSecondMax;
    }
}
