package com.example.chat_app1.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MessageList {
    private String profileImagePath;
    private String name ;
    private String message;
    private String email;
    private long userID;
    private long time_millis ;
    private String fcm_token;
    private String userAbout;

    public MessageList(String profileImagePath, String name, String message, String email,long userID, long time_millis,String fcm_token,String about) {
        this.profileImagePath = profileImagePath;
        this.name = name;
        this.message = message;
        this.email = email;
        this.userID = userID;
        this.time_millis = time_millis;
        this.fcm_token = fcm_token;
        this.userAbout = about;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime_millis() {
        return time_millis;
    }

    public void setTime_millis(long time_millis) {
        this.time_millis = time_millis;
    }

    public String getTimeString(){
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(this.time_millis);//get recieved time
        final String timeString1 =
                new SimpleDateFormat("dd-M-yy HH:mm").format(cal.getTime());
        final String timeString2 =
                new SimpleDateFormat("dd-M-yy HH:mm").format(new Date().getTime());
        int diff = Integer.parseInt(timeString2.substring(0, 2)) - Integer.parseInt(timeString1.substring(0, 2));

        if (diff < 1) {
            return timeString1.trim().substring(8);
        } else if (diff >= 1) {
            return timeString1.trim().substring(0, 8);
        }
        return "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserID() {
        return 99;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public long getLastUpdaetd() {
        return userID;
    }

    public void setLastUpdaetd(long userID) {
        this.userID = userID;
    }

    public String getUserAbout() {
        return userAbout;
    }

    public void setUserAbout(String userAbout) {
        this.userAbout = userAbout;
    }
}