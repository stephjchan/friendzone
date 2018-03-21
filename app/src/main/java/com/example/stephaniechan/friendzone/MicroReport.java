package com.example.stephaniechan.friendzone;

/**
 * Created by Jansen on 3/18/2018.
 */

public class MicroReport {
    public String EventName;
    public String TimeStamp;
    public String EventStartTime;
    public String EventLocationName;
    public String EventLocationAddress;
    public String EventUsername;
    // either a google Place ID or an image URL
    public String EventPhotoRetriever;
    public MicroReport(){

    }
    public MicroReport(String eventName, String eventStartTime, String eventLocation,
                       String eventLocationAddress, String userName, String timeStamp, String EventPhotoRetriever){
        this.EventName = eventName;
        this.EventStartTime = eventStartTime;
        this.EventLocationName = eventLocation;
        this.EventLocationAddress = eventLocationAddress;
        this.EventUsername = userName;
        this.TimeStamp = timeStamp;
        this.EventPhotoRetriever = EventPhotoRetriever;


    }
}
