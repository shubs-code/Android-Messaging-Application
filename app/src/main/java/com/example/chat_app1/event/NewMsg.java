package com.example.chat_app1.event;

import com.example.chat_app1.model.CommunicationList;

public interface NewMsg {
    void Message(String Email, CommunicationList msg);
}
