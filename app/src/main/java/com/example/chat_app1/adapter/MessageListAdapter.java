package com.example.chat_app1.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.chat_app1.R;
import com.example.chat_app1.model.MessageList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessageListAdapter extends ArrayAdapter<MessageList> {
    Context context;
    public MessageListAdapter(@NonNull Context context, ArrayList<MessageList> arrayList) {
        super(context, 0,arrayList);
        this.context = context;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // convertView which is recyclable view
        View currentItemView = convertView;
        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.message_list, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        MessageList messageItem = getItem(position);

        ImageView userImage = currentItemView.findViewById(R.id.profile_picture);
        //userImage.setImageResource(R.drawable.user_image);//logic to retrieve user image
        Picasso.with(context).load(messageItem.getProfileImagePath()).centerCrop().fit().into(userImage);
        TextView textView1 = currentItemView.findViewById(R.id.textView);
        textView1.setText(messageItem.getName());

        TextView textView2 = currentItemView.findViewById(R.id.textView3);
        textView2.setText(messageItem.getMessage());

        TextView textView3 = currentItemView.findViewById(R.id.textView2);
        textView3.setText(messageItem.getTimeString());

        // then return the recyclable view
        return currentItemView;
    }
}
