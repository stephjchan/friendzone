package com.example.stephaniechan.friendzone;

import android.graphics.Bitmap;

/**
 * Created by Stephanie Chan on 3/14/2018.
 */

public class MicroReportItem {

    private Bitmap LocationPhoto;
    private String EventLocationAddress;
    private String EventLocationName;
    private String EventName;
    private String EventStartTime;
    private String EventUsername;
    private String ID;

    public MicroReportItem() {
    }

    public MicroReportItem(Bitmap locationPhoto, String eventLocationAddress, String eventLocationName,
                           String eventName, String eventUsername, String eventStartTime, String id) {
        LocationPhoto = locationPhoto;
        EventLocationAddress = eventLocationAddress;
        EventLocationName = eventLocationName;
        EventName = eventName;
        EventUsername = eventUsername;
        EventStartTime = eventStartTime;
        ID = id;
    }

    //Getter
    public Bitmap getLocationPhoto() {
        return LocationPhoto;
    }

    public String getEventLocationAddress() {
        return EventLocationAddress;
    }

    public String getEventLocationName() {
        return EventLocationName;
    }

    public String getEventName() {
        return EventName;
    }

    public String getEventUsername() {
        return EventUsername;
    }

    public String getEventStartTime() {
        return EventStartTime;
    }

    public String getID() {
        return ID;
    }

    //Setter
    public void setLocationPhoto(Bitmap locationPhoto) {
        LocationPhoto = locationPhoto;
    }

    public void setEventLocationAddress(String eventLocationAddress) {
        EventLocationAddress = eventLocationAddress;
    }

    public void setEventLocationName(String eventLocationName) {
        EventLocationName = eventLocationName;
    }

    public void setEventName(String event_name) {
        EventName = event_name;
    }

    public void setEventUsername(String event_user) {
        EventUsername = event_user;
    }

    public void setEventStartTime(String eventStartTime) {
        EventStartTime = eventStartTime;
    }

    public void setID(String id) {
        ID = id;
    }
}