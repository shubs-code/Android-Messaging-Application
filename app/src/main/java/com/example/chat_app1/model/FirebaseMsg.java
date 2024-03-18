package com.example.chat_app1.model;

public class FirebaseMsg {
    private String Message;
    private long time_millis;

    public FirebaseMsg() {
    }

    public FirebaseMsg(String message, long time_millis) {
        Message = message;
        this.time_millis = time_millis;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public long getTime_millis() {
        return time_millis;
    }

    public void setTime_millis(long time_millis) {
        this.time_millis = time_millis;
    }
}
