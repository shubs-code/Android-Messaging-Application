package com.example.chat_app1.event;

import com.example.chat_app1.model.CommunicationList;
import com.example.chat_app1.model.UserProfile;

import java.util.ArrayList;
import java.util.List;

public class NewMsgListener {
    private List<NewMsg> listeners = new ArrayList<NewMsg>();

    public void addListener(NewMsg toAdd) {
        listeners.add(toAdd);
    }

    public void removeListener(NewMsg toRemove) {
        listeners.remove(toRemove);
    }

    public void removeListener(){listeners.clear();}

    public void newMsg(String Email, CommunicationList msg){
        for(NewMsg u : listeners){
            u.Message(Email, msg);
        }
    }
}
