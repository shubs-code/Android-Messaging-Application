package com.example.chat_app1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.chat_app1.model.CommunicationList;
import com.example.chat_app1.model.MessageList;
import com.example.chat_app1.util.Constants;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    private static final String DB_NAME = "wired";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "users";

    private static final String ProfileImagePath_COL="profileImagePath";
    private static final String Name_COL = "name" ;
    private static final String Message_COL = "message";
    private static final String Email_COL = "email";
    private static final String Fcm_token_COL = "token";
    private static final String Time_Millis_COL = "time_millis";
    private static final String Time_Millis_Updated_COL = "time_millis_updated";
    private static final String User_ID_COL = "userid";
    private static final String User_About_COL = "userAbout";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + Email_COL + " TEXT PRIMARY KEY,"
                + Message_COL + " TEXT,"
                + Name_COL + " TEXT,"
                + Time_Millis_COL + " INTEGER, "
                + Time_Millis_Updated_COL + " INTEGER, "
                + ProfileImagePath_COL + " TEXT, "
                + User_ID_COL + " INTEGER, "
                + Fcm_token_COL + " TEXT ,"
                + User_About_COL + " TEXT )";

        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addNewFriend(String profileImagePath, String name, String message, String email, long time_millis, long time_millis_updated,int userID ,String fcm_token,String About) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Email_COL,email);
        values.put(Message_COL,message);
        values.put(Name_COL,name);
        values.put(Time_Millis_COL,time_millis);
        values.put(Time_Millis_Updated_COL,time_millis_updated);
        values.put(ProfileImagePath_COL,profileImagePath);
        values.put(User_ID_COL,userID);
        values.put(Fcm_token_COL,fcm_token);
        values.put(User_About_COL,About);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<MessageList> getUserChats() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<MessageList> messageList = new ArrayList<MessageList>();
        String sql = "SELECT * FROM " + DBHandler.TABLE_NAME+" ORDER BY "+DBHandler.Time_Millis_COL+" DESC";
        Cursor cursorChatUsers = db.rawQuery(sql, new String[] {});

        if (cursorChatUsers.moveToFirst()) {
            do {
                messageList.add(new MessageList(
                        cursorChatUsers.getString(5),
                        cursorChatUsers.getString(2),
                        cursorChatUsers.getString(1),
                        cursorChatUsers.getString(0),
                        cursorChatUsers.getLong(4),
                        cursorChatUsers.getLong(3),
                        cursorChatUsers.getString(7),
                        cursorChatUsers.getString(8)
                        ));

                Log.d("Database","A user"+cursorChatUsers.getString(0));

            } while (cursorChatUsers.moveToNext());
        }
        Log.d("Database","closing cursor");
        cursorChatUsers.close();
        return messageList;
    }

    public void updateUserChat(String email, String message , Long time_millis, String name, String profileImagePath,  Long time_millis_updated ) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if(message!=null){values.put(Message_COL,message);}
        if(name!=null){values.put(Name_COL,name);}
        if(time_millis!=null){values.put(Time_Millis_COL,time_millis);}
        if(time_millis_updated!=null){values.put(Time_Millis_Updated_COL,time_millis_updated);}
        if(profileImagePath!=null){values.put(ProfileImagePath_COL,profileImagePath);}

        db.update(TABLE_NAME, values, Email_COL+"=?", new String[]{email});
        db.close();
    }//TODO TEST

    public void CreateUserCommunicationTable(String userID){
        userID = userID.substring(0,userID.lastIndexOf('@'));
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "CREATE TABLE " + Constants.UserTableName+userID + " ("
                + Constants.Message_COL + " TEXT,"
                + Constants.IsRecieved_COL + " BOOLEAN,"
                + Constants.time_COL + " INTEGER PRIMARY KEY)";
        db.execSQL(query);
    }
    public void addMessage(String userID, CommunicationList newMsg){
        userID = userID.substring(0,userID.lastIndexOf('@'));
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constants.Message_COL,newMsg.getMessage());
        values.put(Constants.IsRecieved_COL,newMsg.isRecieved());
        values.put(Constants.time_COL,newMsg.getTimeMillis());

        db.insert(Constants.UserTableName+userID, null, values);
        db.close();
    }

    public ArrayList<CommunicationList> getMessages(String userID) {
        userID = userID.substring(0,userID.lastIndexOf('@'));
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CommunicationList> communicationList = new ArrayList<CommunicationList>();
        String sql = "SELECT * FROM " + Constants.UserTableName+userID;// +" ORDER BY "+Constants.time_COL+" DESC"
        Cursor cursorChatUsers = db.rawQuery(sql, new String[] {});

        if (cursorChatUsers.moveToFirst()) {
            do {
                communicationList.add(       new CommunicationList(
                        cursorChatUsers.getString(0),
                        cursorChatUsers.getInt(1)!=0,
                        cursorChatUsers.getLong(2)
                ));
            } while (cursorChatUsers.moveToNext());
        }
        cursorChatUsers.close();
        return communicationList;
    }

}
