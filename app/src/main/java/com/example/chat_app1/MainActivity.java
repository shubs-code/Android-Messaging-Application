package com.example.chat_app1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.chat_app1.adapter.MessageListAdapter;
import com.example.chat_app1.database.DBHandler;
import com.example.chat_app1.event.NewMsg;
import com.example.chat_app1.firebase.FirebaseFCM;
import com.example.chat_app1.firebase.RealtimeDatabase;
import com.example.chat_app1.model.CommunicationList;
import com.example.chat_app1.model.MessageList;
import com.example.chat_app1.service.Backgroundworker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.example.chat_app1.util.Constants.ApplicationForeground;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab ;
    private static final String TAG = "MainActivity";
    private DBHandler dbHandler ;
    private FirebaseAuth mAuth;
    private RealtimeDatabase realtimeDatabase;
    private HashMap<String,Integer> userPosition = new HashMap<>();
    private ArrayList<MessageList> messageListArray ;
    private ListView messageListView;
    MessageListAdapter messageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DBHandler(this);
        fab = findViewById(R.id.floatingActionButton);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null){
            startActivity(new Intent(this, SignIn.class));
            finish();
        }

        messageListView = findViewById(R.id.message_list_view);




        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MessageList messageListItem = (MessageList) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(),CommunicationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email",messageListItem.getEmail());
                bundle.putString("profileImageURL",messageListItem.getProfileImagePath());
                bundle.putLong("lstUpdated",messageListItem.getLastUpdaetd());
                bundle.putString("userEmail",mAuth.getCurrentUser().getEmail());
                bundle.putString("fcm_token",messageListItem.getFcm_token());
                bundle.putString("user_name",messageListItem.getName());
                bundle.putString("userAbout",messageListItem.getUserAbout());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddFriend.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(realtimeDatabase!=null){realtimeDatabase.msgListListeners.removeListener();}
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationForeground = true;

        messageListArray = dbHandler.getUserChats();
        for(int i=0;i<messageListArray.size();i++){userPosition.put(messageListArray.get(i).getEmail(),i);}
        messageListAdapter = new MessageListAdapter(this,messageListArray);
        messageListView.setAdapter(messageListAdapter);

        realtimeDatabase = RealtimeDatabase.getFirebaseInstance(this);
        realtimeDatabase.updateLastSeen(mAuth.getCurrentUser().getEmail());
        realtimeDatabase.msgListListeners.addListener(new NewMsg() {
            @Override
            public void Message(String Email, CommunicationList msg) {

                int position = userPosition.get(Email);
                MessageList user = messageListAdapter.getItem(position);
                user.setMessage(msg.getMessage());
                user.setTime_millis(msg.getTimeMillis());
                messageListAdapter.remove(user);
                messageListAdapter.insert(user,0);
                messageListAdapter.notifyDataSetChanged();
                userPosition.put(Email,0);
            }
        });
        realtimeDatabase.readMsgList(mAuth.getCurrentUser().getEmail());

    }

    @Override
    protected void onPause() {
        super.onPause();
        ApplicationForeground= false;
        realtimeDatabase.msgListListeners.removeListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(),UserSettings.class));
            return true;
        }else if (id == R.id.action_media) {
            startActivity(new Intent(this, MediaActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}