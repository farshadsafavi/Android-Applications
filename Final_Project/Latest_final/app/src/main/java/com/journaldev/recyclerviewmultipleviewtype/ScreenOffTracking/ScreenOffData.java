package com.journaldev.recyclerviewmultipleviewtype.ScreenOffTracking;

import io.realm.RealmObject;

public class ScreenOffData extends RealmObject {
    long currentTime;
    long screenOffDuration;
    int screenOffYear;
    int screenOffMonth;
    int screenOffDay;
    int screenOffhour;
    int screenOffMinute;
    int screenOffSecond;

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public long getScreenOffDuration() {
        return screenOffDuration;
    }

    public void setScreenOffDuration(long screenOffDuration) {
        this.screenOffDuration = screenOffDuration;
    }

    public int getScreenOffYear() {
        return screenOffYear;
    }

    public void setScreenOffYear(int screenOffYear) {
        this.screenOffYear = screenOffYear;
    }

    public int getScreenOffMonth() {
        return screenOffMonth;
    }

    public void setScreenOffMonth(int screenOffMonth) {
        this.screenOffMonth = screenOffMonth;
    }

    public int getScreenOffDay() {
        return screenOffDay;
    }

    public void setScreenOffDay(int screenOffDay) {
        this.screenOffDay = screenOffDay;
    }

    public int getScreenOffhour() {
        return screenOffhour;
    }

    public void setScreenOffhour(int screenOffhour) {
        this.screenOffhour = screenOffhour;
    }

    public int getScreenOffMinute() {
        return screenOffMinute;
    }

    public void setScreenOffMinute(int screenOffMinute) {
        this.screenOffMinute = screenOffMinute;
    }

    public int getScreenOffSecond() {
        return screenOffSecond;
    }

    public void setScreenOffSecond(int screenOffSecond) {
        this.screenOffSecond = screenOffSecond;
    }

    @Override
    public String toString() {
        return "ScreenOffData{" +
                "currentTime=" + currentTime +
                ", screenOffDuration=" + screenOffDuration +
                ", screenOffYear=" + screenOffYear +
                ", screenOffMonth=" + screenOffMonth +
                ", screenOffDay=" + screenOffDay +
                ", screenOffhour=" + screenOffhour +
                ", screenOffMinute=" + screenOffMinute +
                ", screenOffSecond=" + screenOffSecond +
                '}';
    }
}
