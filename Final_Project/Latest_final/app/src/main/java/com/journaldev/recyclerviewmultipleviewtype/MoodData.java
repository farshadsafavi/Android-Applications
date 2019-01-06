package com.journaldev.recyclerviewmultipleviewtype;

import io.realm.RealmObject;

/**
 * Created by Owlia on 11/15/2016.
 */

public class MoodData extends RealmObject {
    private int satisfaction;
    private int year;
    private int month;
    private int day;

    public int getSatisfaction(){return satisfaction;}
    public void setSatisfaction(int satisfaction){this.satisfaction=satisfaction;}

    public int getYear(){return year;}
    public void setYear(int year){this.year=year;}

    public int getMonth(){return month;}
    public void setMonth(int month){this.month=month;}

    public int getDay(){return day;}
    public void setDay(int day){this.day=day;}

    @Override
    public String toString() {
        return "MoodData{" +
                "Satisfaction: '" + satisfaction +
                ", Date: " + year+"/"+month+"/"+day +
                '}';
    }
}
