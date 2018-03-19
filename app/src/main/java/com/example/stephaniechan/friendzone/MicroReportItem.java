package com.example.stephaniechan.friendzone;

/**
 * Created by Stephanie Chan on 3/14/2018.
 */

public class MicroReportItem {

    private int LocationPhoto;
    private String EventLocationAddress;
    private String EventLocationName;
    private String EventName;
    private String EventStartTime;
    private String EventUsername;

    public MicroReportItem() {
    }

    public MicroReportItem(int locationPhoto, String eventLocationAddress, String eventLocationName,
                           String eventName, String eventUsername, String eventStartTime) {
        LocationPhoto = locationPhoto;
        EventLocationAddress = eventLocationAddress;
        EventLocationName = eventLocationName;
        EventName = eventName;
        EventUsername = eventUsername;
        EventStartTime = eventStartTime;
    }

    //Getter
    public int getLocationPhoto() {
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

    //Setter
    public void setLocationPhoto(int locationPhoto) {
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
}
