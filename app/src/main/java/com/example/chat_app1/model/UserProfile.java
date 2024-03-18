package com.example.chat_app1.model;

import java.util.Date;

public class UserProfile {
    String FCMToken;
    String ImageURL;
    String About;
    String Name;
    String Email;
    long LastSeen;
    long LastUpdated;

    public UserProfile(String Email) {
        this.Email = Email;
        this.Name = Email.substring(0,Email.lastIndexOf('@'));
        this.About = "I am using Wired.";
        this.LastUpdated = new Date().getTime();
        this.LastSeen = new Date().getTime();
        this.ImageURL = "profile/default_user.png";
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public UserProfile() {
    }

    public String getFCMToken() {
        return FCMToken;
    }

    public void setFCMToken(String FCMToken) {
        this.FCMToken = FCMToken;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getLastSeen() {
        return LastSeen;
    }

    public void setLastSeen(long lastSeen) {
        LastSeen = lastSeen;
    }

    public long getLastUpdated() {
        return LastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        LastUpdated = lastUpdated;
    }
}
