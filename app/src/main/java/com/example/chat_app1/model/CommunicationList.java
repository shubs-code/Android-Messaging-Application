package com.example.chat_app1.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommunicationList {
    private String message;
    private boolean isRecieved;
    private long timeMillis;
    public CommunicationList(String message, boolean isRecieved, long timeMillis) {
        this.message = message;
        this.isRecieved = isRecieved;
        this.timeMillis = timeMillis;
    }
    public CommunicationList(){}

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public boolean isRecieved() {
        return isRecieved;
    }
    public void setRecieved(boolean recieved) {
        isRecieved = recieved;
    }
    public long getTimeMillis() {
        return timeMillis;
    }
    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public String timetoString(){
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(this.timeMillis);
        return new SimpleDateFormat("dd/M/yy HH:mm").format(cal.getTime());
    }

}
