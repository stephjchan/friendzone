package com.example.stephaniechan.friendzone;

/**
 * Created by Janice on 3/9/2018.
 */

public class FriendItem {
    // THESE NAMES NEED TO MATCH DB
    public String Email;
    public String Username;

    public FriendItem(){}

    public FriendItem(String email, String username){
        Email = email;
        Username = username;
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

}
