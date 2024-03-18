package com.example.chat_app1.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private SharedPreferences sharedPreferences;
    private Context context;

    public Preferences(Context context) {
        this.context= context;
        sharedPreferences=  context.getSharedPreferences("MySharedPref",MODE_PRIVATE);
    }

    public void writeDataString(String key, String value){
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(key, value);
        myEdit.commit();
    }
    public void writeDataInt(String key, int value){
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt(key, value);
        myEdit.commit();
    }
    public void writeDataBool(String key, boolean value){
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putBoolean(key, value);
        myEdit.commit();
    }
    public Integer getInt(String key){
        Integer data = sharedPreferences.getInt(key, 0);
        return data;
    }
    public Boolean getBool(String key){
        Boolean data = sharedPreferences.getBoolean(key, false);
        return data;
    }
    public String getString(String key){
        String data = sharedPreferences.getString(key, null);
        return data;
    }
}
