package com.example.stephaniechan.friendzone;

/**
 * Created by Stephanie Chan on 3/14/2018.
 */

public class MicroReportItem {

    private int LocationPhoto;
    private String EventName;
    private String EventUser;
    private String Place;
    private String EventTime;

    public MicroReportItem() {
    }

    public MicroReportItem(int locationPhoto, String eventName, String eventUser, String place, String eventTime) {
        LocationPhoto = locationPhoto;
        EventName = eventName;
        EventUser = eventUser;
        Place = place;
        EventTime = eventTime;
    }

    //Getter
    public int getLocationPhoto() {
        return LocationPhoto;
    }

    public String getEventName() {
        return EventName;
    }

    public String getEventUser() {
        return EventUser;
    }

    public String getPlace() {
        return Place;
    }

    public String getEventTime() {
        return EventTime;
    }

    //Setter
    public void setLocationPhoto(int locationPhoto) {
        LocationPhoto = locationPhoto;
    }

    public void setEventName(String event_name) {
        EventName = event_name;
    }

    public void setEventUser(String event_user) {
        EventUser = event_user;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public void setEventTime(String eventTime) {
        EventTime = eventTime;
    }
}
