package com.example.chat_app1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.chat_app1.adapter.MediaListAdapter;
import com.example.chat_app1.adapter.MessageListAdapter;
import com.example.chat_app1.model.MediaList;
import com.example.chat_app1.model.MessageList;

import java.util.ArrayList;
import java.util.Date;

public class MediaActivity extends AppCompatActivity {

    final ArrayList<MediaList> mediaListArray = new ArrayList<MediaList>();
    MediaListAdapter mediaListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        for(int i =0 ;i<5;i++){//for testing
            mediaListArray.add(new MediaList("shubham.png","/","2MB",false,0));
        }
        mediaListAdapter = new MediaListAdapter(this,mediaListArray);
        ListView messageListView = findViewById(R.id.media_list_view);
        messageListView.setAdapter(mediaListAdapter);
    }
}