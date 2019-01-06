package com.journaldev.recyclerviewmultipleviewtype.ScreenOffTracking;

/**
 * Created by Farshad on 8/22/2016.
 */
public class Screenoff {

    int  targetSleep;
    long currentTime;
    long screenOffDuration;
    int  screenOffYear;
    int  screenOffMonth;
    int  screenOffDay;
    int  screenOffhour;
    int  screenOffMinute;
    int  screenOffSecond;

    public Screenoff(int targetSleep, long currentTime, long screenOffDuration, int creenOffYear, int screenOffMonth, int screenOffDay, int screenOffhour, int screenOffMinute, int screenOffSecond) {
        this.targetSleep = targetSleep;
        this.currentTime = currentTime;
        this.screenOffDuration = screenOffDuration;
        this.screenOffYear = creenOffYear;
        this.screenOffMonth = screenOffMonth;
        this.screenOffDay = screenOffDay;
        this.screenOffhour = screenOffhour;
        this.screenOffMinute = screenOffMinute;
        this.screenOffSecond = screenOffSecond;
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

    public long getScreenOffDuration() {
        return screenOffDuration;
    }

    public void setScreenOffDuration(long screenOffDuration) {
        this.screenOffDuration = screenOffDuration;
    }

    public int getScreenOffYear() {
        return screenOffYear;
    }

    public void setSreenOffYear(int creenOffYear) {
        this.screenOffYear = creenOffYear;
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
}
