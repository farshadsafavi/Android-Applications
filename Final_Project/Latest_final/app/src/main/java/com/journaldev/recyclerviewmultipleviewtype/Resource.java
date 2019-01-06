package com.journaldev.recyclerviewmultipleviewtype;

import android.location.Location;

import io.realm.RealmObject;

/**
 * Created by Farshad on 11/6/2016.
 */
public class Resource extends RealmObject {
    String component;
    String title;
    String content;
    String keywords;
    String criteria;
    String url;
    String goal;

    Double locationLat;
    Double locationLog;

    String locationName;
    String calendarDate;

    public String getCriteria() {
        return criteria;
    }
    public void setCriteria(String activity_type) {
        this.criteria = activity_type;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getComponent() {
        return component;
    }
    public void setComponent(String url) {
        this.component = url;
    }

    public String getKeywords() {return keywords;}
    public void setKeywords(String keywords){this.keywords=keywords;}

    public String getUrl() {return url;}
    public void setUrl(String url){this.url=url;}

    public String getGoal() {return goal;}
    public void setGoal(String goal){this.goal=goal;}

    public Double getLocationLat() {return locationLat;}
    public void setLocationLat(Double locationLat){this.locationLat=locationLat;}

    public Double getLocationLog() {return locationLog;}
    public void setLocationLog(Double locationLog){this.locationLog=locationLog;}

    public String getLocationName() {return locationName;}
    public void setLocationName(String locationName){this.locationName=locationName;}

    public String getCalendarDate() {return calendarDate;}
    public void setCalendarDate(String calendarDate){this.calendarDate=calendarDate;}

    @Override
    public String toString() {
        return "Resource{" +
                "Component='" + component + '\'' +
                ", Title='" + title + '\'' +
                ", Content='" + content + '\'' +
                ", Keywords='" + keywords + '\'' +
                ", Criteria='" + criteria + '\'' +
                ", URL='" + url + '\'' +
                ", Goal='" + goal + '\'' +
                '}';
    }
}
