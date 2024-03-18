package com.example.chat_app1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.chat_app1.adapter.CommunicationListAdapter;
import com.example.chat_app1.adapter.MessageListAdapter;
import com.example.chat_app1.database.DBHandler;
import com.example.chat_app1.event.NewMsg;
import com.example.chat_app1.event.NewMsgListener;
import com.example.chat_app1.firebase.RealtimeDatabase;
import com.example.chat_app1.model.CommunicationList;
import com.example.chat_app1.model.FirebaseMsg;
import com.example.chat_app1.model.MessageList;
import com.example.chat_app1.service.Backgroundworker;
import com.example.chat_app1.util.Codec;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.chat_app1.util.Constants.ApplicationForeground;

public class CommunicationActivity extends AppCompatActivity {

    private String msgFromEmailID;
    private String userEmail;
    private String fcm_token;
    private String msgFromProfileURL;
    private int msgFromUserID ;
    private long lastSeen;
    private long lastUpdated;
    private String userAbout;

    Runnable runnable;
    int delay = 10000;
    Handler handler = new Handler();

    private View actionBar ;
    private FloatingActionButton sendMsg ;
    private EditText msgInputField ;
    private ListView communicationListView ;
    private TextView online;

    private DBHandler dbHandler;
    private RealtimeDatabase realtimeDatabase;
    private CommunicationListAdapter communicationListAdapter ;
    ArrayList<CommunicationList> communicationListArray ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.communication_action_bar);
        actionBar = getSupportActionBar().getCustomView();
        online = actionBar.findViewById(R.id.last_seen);
        online.setText("");
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.mipmap.ic_launcher_foreground);

        sendMsg = findViewById(R.id.send_message_fab);
        msgInputField = findViewById(R.id.message_to_send);

        dbHandler = new DBHandler(this);
        realtimeDatabase = RealtimeDatabase.getFirebaseInstance(this);
        Bundle bundle = getIntent().getExtras();
        msgFromEmailID = bundle.getString("email",null);
        msgFromProfileURL = bundle.getString("profileImageURL",null);
        userEmail = bundle.getString("userEmail",null);
        msgFromUserID = bundle.getInt("userID",-1);
        lastUpdated = bundle.getLong("lstUpdated",0);
        fcm_token = bundle.getString("fcm_token",null);
        userAbout = bundle.getString("userAbout",null);
        Log.d("Application",userAbout);// TODO remove
        Picasso.with(this).load(msgFromProfileURL).into((ImageView) actionBar.findViewById(R.id.profile_picture));
        ((TextView)actionBar.findViewById(R.id.name)).setText(bundle.getString("user_name",""));;
        communicationListArray = dbHandler.getMessages(msgFromEmailID);
        communicationListAdapter= new CommunicationListAdapter(this,communicationListArray);
        communicationListView = findViewById(R.id.communication_view);
        if(communicationListArray.size()>6){communicationListView.setStackFromBottom(true);}
        communicationListView.setAdapter(communicationListAdapter);

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommunicationList newMsg = new CommunicationList(msgInputField.getText().toString(),false,new Date().getTime());
                dbHandler.addMessage(msgFromEmailID,newMsg);
                communicationListAdapter.add(newMsg);
                realtimeDatabase.addNewMesg(msgFromEmailID,userEmail,new FirebaseMsg(newMsg.getMessage(),newMsg.getTimeMillis()));
                communicationListAdapter.notifyDataSetChanged();
                Intent notificationIntent = new Intent(getApplicationContext(), Backgroundworker.class);
                notificationIntent.putExtra("fcm_token",fcm_token);
                notificationIntent.putExtra("msg_from",userEmail);
                notificationIntent.putExtra("msg_content",msgInputField.getText().toString());
                startService(notificationIntent);
                msgInputField.setText("");
                communicationListView.smoothScrollToPosition(communicationListAdapter.getCount() -1);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        ApplicationForeground = true;
        realtimeDatabase.msgListeners.addListener(new NewMsg() {
            @Override
            public void Message(String Email, CommunicationList msg) {
                Log.d("CommunicationActivity",":"+Email+":"+msgFromEmailID+":");
                if(Email.equals(msgFromEmailID)) {
                    communicationListAdapter.add(msg);
                    communicationListAdapter.notifyDataSetChanged();
                    communicationListView.smoothScrollToPosition(communicationListAdapter.getCount() -1);
                }
            }
        });

        String pathLastSeen="users/"+ Codec.Base64Encode(msgFromEmailID)+"/profile/lastSeen";
        realtimeDatabase.getReference(pathLastSeen).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lastSeen = snapshot.getValue(Long.class);
                //Toast.makeText(getApplicationContext(),""+lastSeen,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }) ;

        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                if((new Date().getTime()-lastSeen)<30000){online.setText("Online");}
                else{online.setText("");}
            }
        }, delay);
        String pathLastUpdated="users/"+ Codec.Base64Encode(msgFromEmailID)+"/profile/lastUpdated";
        realtimeDatabase.getReference(pathLastUpdated).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(false){//snapshot.getValue(Long.class).equals(lastUpdated)
                    Toast.makeText(getApplicationContext(),"no recent update",Toast.LENGTH_SHORT).show();
                }
                else{
                    String pathUserProfile="users/"+ Codec.Base64Encode(msgFromEmailID)+"/profile";
                    realtimeDatabase.getReference(pathUserProfile).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            HashMap<String, String> profile = new HashMap<>();
                            for(DataSnapshot snap : snapshot.getChildren()){
                                Log.d("Firebase",snap.getKey()+" "+snap.getValue().toString());
                                profile.put(snap.getKey(),snap.getValue().toString());
                            }
                            //dbHandler.updateUserChat(null,null,null,profile.get("name"),profile.get("about"),Long.getLong(profile.get("lastUpdated")));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Toast.makeText(getApplicationContext(),lastUpdated+" recent update "+snapshot.getValue(Long.class),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        ApplicationForeground = false;
        handler.removeCallbacks(runnable);
        realtimeDatabase.msgListeners.removeListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}