package com.journaldev.recyclerviewmultipleviewtype.ScreenOffTracking;

import io.realm.RealmObject;

/**
 * Created by Farshad on 11/29/2016.
 */

public class ScreenOnData extends RealmObject {
    long currentTime;
    long screenOnDuration;
    int screenOnYear;
    int screenOnMonth;
    int screenOnDay;
    int screenOnhour;
    int screenOnMinute;
    int screenOnSecond;

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public long getScreenOnDuration() {
        return screenOnDuration;
    }

    public void setScreenOnDuration(long screenOnDuration) {
        this.screenOnDuration = screenOnDuration;
    }

    public int getScreenOnYear() {
        return screenOnYear;
    }

    public void setScreenOnYear(int screenOnYear) {
        this.screenOnYear = screenOnYear;
    }

    public int getScreenOnMonth() {
        return screenOnMonth;
    }

    public void setScreenOnMonth(int screenOnMonth) {
        this.screenOnMonth = screenOnMonth;
    }

    public int getScreenOnDay() {
        return screenOnDay;
    }

    public void setScreenOnDay(int screenOnDay) {
        this.screenOnDay = screenOnDay;
    }

    public int getScreenOnhour() {
        return screenOnhour;
    }

    public void setScreenOnhour(int screenOnhour) {
        this.screenOnhour = screenOnhour;
    }

    public int getScreenOnMinute() {
        return screenOnMinute;
    }

    public void setScreenOnMinute(int screenOnMinute) {
        this.screenOnMinute = screenOnMinute;
    }

    public int getScreenOnSecond() {
        return screenOnSecond;
    }

    public void setScreenOnSecond(int screenOnSecond) {
        this.screenOnSecond = screenOnSecond;
    }

    @Override
    public String toString() {
        return "ScreenOnData{" +
                "currentTime=" + currentTime +
                ", screenOnDuration=" + screenOnDuration +
                ", screenOnYear=" + screenOnYear +
                ", screenOnMonth=" + screenOnMonth +
                ", screenOnDay=" + screenOnDay +
                ", screenOnhour=" + screenOnhour +
                ", screenOnMinute=" + screenOnMinute +
                ", screenOnSecond=" + screenOnSecond +
                '}';
    }
}
