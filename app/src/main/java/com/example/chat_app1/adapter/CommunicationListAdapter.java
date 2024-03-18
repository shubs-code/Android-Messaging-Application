package com.example.chat_app1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chat_app1.R;
import com.example.chat_app1.model.CommunicationList;
import com.example.chat_app1.model.MessageList;

import java.util.ArrayList;

public class CommunicationListAdapter extends ArrayAdapter<CommunicationList> {

    Context context;

    public CommunicationListAdapter(@NonNull Context context, ArrayList<CommunicationList> arrayList) {
        super(context, 0,arrayList);
        this.context = context;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        CommunicationList communicationListItem = getItem(position);

        View currentItemView = convertView;
        if (communicationListItem.isRecieved()) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.message_recv, parent, false);
        }else if(communicationListItem.getTimeMillis()==0){
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.message_notification, parent, false);
        }
        else{
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.message_sent, parent, false);
        }

        TextView textView1 = currentItemView.findViewById(R.id.time);
        textView1.setText(communicationListItem.timetoString());

        TextView textView2 = currentItemView.findViewById(R.id.message);
        textView2.setText(communicationListItem.getMessage());

        return currentItemView;
    }
}
