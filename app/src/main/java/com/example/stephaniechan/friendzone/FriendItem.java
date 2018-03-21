package com.example.stephaniechan.friendzone;

/**
 * Created by Janice on 3/9/2018.
 */

public class FriendItem {
    // THESE NAMES NEED TO MATCH DB
    private String Email;
    private String Username;
    private String ID;
    private Boolean IsFriend;

    public FriendItem(){}

    public FriendItem(String email, String username, String id, boolean isFriend){
        Email = email;
        Username = username;
        ID = id;
        IsFriend = isFriend;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUsername(){
        return Username;
    }

    public void setUsername(String username){
        Username = username;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Boolean getIsFriend() {
        return IsFriend;
    }

    public void setIsFriend(Boolean isFriend) {
        IsFriend = isFriend;
    }

}
