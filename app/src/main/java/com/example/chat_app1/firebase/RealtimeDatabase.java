package com.example.chat_app1.firebase;

import com.example.chat_app1.database.DBHandler;
import com.example.chat_app1.event.NewMsgListener;
import com.example.chat_app1.event.NewUserListener;
import com.example.chat_app1.model.CommunicationList;
import com.example.chat_app1.model.FirebaseMsg;
import com.example.chat_app1.model.MessageList;
import com.example.chat_app1.model.UserProfile;
import com.example.chat_app1.util.Codec;
import com.example.chat_app1.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class RealtimeDatabase {
    FirebaseDatabase database ;
    DBHandler db;
    private HashMap<String, Integer> activeListenerPaths = new HashMap<String, Integer>();
    public NewMsgListener msgListeners=new NewMsgListener();
    public NewMsgListener msgListListeners=new NewMsgListener();
    public RealtimeDatabase(Context context) {
        this.database =  FirebaseDatabase.getInstance("https://messaging-application-f71c9-default-rtdb.asia-southeast1.firebasedatabase.app/");
        db = new DBHandler(context);
    }
    public void test(String msg){
//        String email = Codec.Base64Encode("shubhamsingh21-23@bhavans.ac.in");
//        DatabaseReference myRef = database.getReference(email);
//        myRef.setValue(msg);
    }

    public void getUserByEmail(String email,NewUserListener userListener){
        String emailBase64 = Codec.Base64Encode(email);
        String path = "users/"+emailBase64+"/profile";
        DatabaseReference myRef = database.getReference(path);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    UserProfile user = snapshot.getValue(UserProfile.class);
                    Log.d("FireBase",user.getName()+" fromDataChange "+user.getAbout());
                    userListener.newUser(user);
                }else{
                    Log.d("FireBase","No user found fromDataChange");
                    userListener.newUser(null);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setUserProfile(UserProfile user) {
        String emailBase64 =  Codec.Base64Encode(user.getEmail());
        String path = "users/" + emailBase64 + "/profile";
        DatabaseReference myRef = database.getReference(path);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FireBase", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        user.setFCMToken(token);
                        myRef.setValue(user);
                    }
                });
    }

    public void addNewMesg(String to , String from, FirebaseMsg message){
        String userTo =  Codec.Base64Encode(to);
        String userFrom =  Codec.Base64Encode(from);
        String path = "users/" + userTo + "/MessageList/"+userFrom;
        DatabaseReference msgNode = database.getReference(path).push();
        msgNode.setValue(message);
        String notifyPath = "users/" + userTo + "/MessageUserList/"+userFrom;
        DatabaseReference notifyNode = database.getReference(notifyPath);
        notifyNode.setValue(message);
    }

    public void getNewMesg( String to , String from){
        ArrayList<CommunicationList> messages = new ArrayList<CommunicationList>();
        String userTo =  Codec.Base64Encode(to);
        String userFrom =  Codec.Base64Encode(from);
        String path = "users/" + userTo + "/MessageList/"+userFrom;
        DatabaseReference msgNode = database.getReference(path);

        msgNode.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FirebaseMsg MSG = snapshot.getValue(FirebaseMsg.class);
                CommunicationList msg = new CommunicationList(MSG.getMessage(),true,MSG.getTime_millis());
                db.addMessage(from,msg);
                db.updateUserChat(from,MSG.getMessage(),MSG.getTime_millis(),null,null,null);
                msgListeners.newMsg(from,msg);
                database.getReference(path+"/"+snapshot.getKey()).removeValue();
                Log.d("FirebaseRealtime","From firebase-> msg key "+snapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readMsgList(String to ){
        String userTo =  Codec.Base64Encode(to);
        String path = "users/" + userTo + "/MessageUserList";
        DatabaseReference msgListNode = database.getReference(path);
        msgListNode.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FirebaseMsg msg = snapshot.getValue(FirebaseMsg.class);
                String from = Codec.Base64Decode(snapshot.getKey());
                database.getReference(path+"/"+snapshot.getKey()).removeValue();
                Log.d("FirebaseRealtime",msg.getMessage()+" "+from);
                msgListListeners.newMsg(from,new CommunicationList(msg.getMessage(),true,msg.getTime_millis()));
                if(!activeListenerPaths.containsKey(from)) {
                    getNewMesg(to, from);
                    activeListenerPaths.put(from,1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        }

    public void updateLastSeen(String to ){
        String toBase64 = Codec.Base64Encode(to);
        String path = "users/" + toBase64 + "/profile/lastSeen";
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try{
                    DatabaseReference profileNode = database.getReference(path);
                    profileNode.setValue(new Date().getTime());
                }
                catch (Exception e) {
                    Log.d("FirebaseRealtime","Error in upadteLastseen() ");
                }
                finally{
                    if(Constants.ApplicationForeground){handler.postDelayed(this, 10000);}
                }
            }
        };
        handler.post(runnable);
    }
    public void addString(String path , String data ){
        DatabaseReference dataNode = database.getReference(path);
        dataNode.setValue(data);
    }
    public void addLong(String path , Long data ){
        DatabaseReference dataNode = database.getReference(path);
        dataNode.setValue(data);
    }
    public DatabaseReference getReference(String path){
        return database.getReference(path);
    }

    public void addLastSeenListener(){} //TODO
    public void removeLastSeenListener(){} //TODO
    public void getLastSeenListener(){} //TODO

    private static DatabaseReference LastSeenNode;
    // TODO private static lastseenListener listener;
    private static RealtimeDatabase firebaseInstanceObject;
    synchronized public static RealtimeDatabase getFirebaseInstance(Context context){
        if(RealtimeDatabase.firebaseInstanceObject!=null){
            return RealtimeDatabase.firebaseInstanceObject;
        }else{
            RealtimeDatabase.firebaseInstanceObject = new RealtimeDatabase(context);
            return RealtimeDatabase.firebaseInstanceObject;
        }
    }
}