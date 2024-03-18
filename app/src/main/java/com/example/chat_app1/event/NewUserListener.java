package com.example.chat_app1.event;

import com.example.chat_app1.model.UserProfile;

import java.util.ArrayList;
import java.util.List;

public class NewUserListener {

    private List<NewUser> listeners = new ArrayList<NewUser>();

    public void addListener(NewUser toAdd) {
        listeners.add(toAdd);
    }
    public void removeListener(NewUser toRemove) {
        listeners.remove(toRemove);
    }
    public void newUser(UserProfile user){
        for(NewUser u : listeners){
            u.user(user);
        }
    }
}
