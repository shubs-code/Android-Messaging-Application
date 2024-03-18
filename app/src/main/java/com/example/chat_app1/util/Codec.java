package com.example.chat_app1.util;

import android.util.Base64;

public class Codec {

    public static String Base64Encode(String data){
        return android.util.Base64.encodeToString( data.getBytes(),(android.util.Base64.URL_SAFE| Base64.NO_WRAP ));
    }
    public static String Base64Decode(String data){
        return new String( Base64.decode(data, (Base64.URL_SAFE|Base64.NO_WRAP )));
    }
}
