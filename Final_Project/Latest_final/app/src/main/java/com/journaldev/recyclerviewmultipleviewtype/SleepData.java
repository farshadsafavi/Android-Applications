package com.journaldev.recyclerviewmultipleviewtype;

import io.realm.RealmObject;

/**
 * Created by Owlia on 11/3/2016.
 */

public class SleepData extends RealmObject {
    private String timeOfSleep;
    private String sleepiness;
    private int hoursOfSleep;
    private boolean needSuggestions;
    private int year;
    private int month;
    private int day;
    //private boolean haveFace;

    public SleepData() {}

    public String getTimeOfSleep() {
        return timeOfSleep;
    }
    public void setTimeOfSleep(String timeOfSleep) {
        this.timeOfSleep = timeOfSleep;
    }

    public String getSleepiness() {
        return sleepiness;
    }
    public void setSleepiness(String sleepiness) {
        this.sleepiness = sleepiness;
    }

    public int getHoursOfSleep(){return hoursOfSleep;}
    public void setHoursOfSleep(int hoursOfSleep){this.hoursOfSleep=hoursOfSleep;}

    public boolean isNeedSuggestions(){return needSuggestions;}
    public void setNeedSuggestions(boolean needSuggestions){this.needSuggestions=needSuggestions;}


    public int getYear(){return year;}
    public void setYear(int year){this.year=year;}

    public int getMonth(){return month;}
    public void setMonth(int month){this.month=month;}

    public int getDay(){return day;}
    public void setDay(int day){this.day=day;}


    @Override
    public String toString() {
        return "SleepData{" +
                "Time of Sleep: '" + timeOfSleep + '\'' +
                ", Sleepiness: " + sleepiness +
                ", Hours of Sleep: " + hoursOfSleep +
                ", Need Suggestions? " + needSuggestions +
                ", Date: " + year+"/"+month+"/"+day +
                '}';
    }
}
