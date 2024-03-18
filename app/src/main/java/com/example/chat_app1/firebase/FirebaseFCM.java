package com.example.chat_app1.firebase;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class FirebaseFCM {
    private static final String TAG = "FirebaseFCM";

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = "https://www.google.com";
    private Context context;
    public FirebaseFCM(Context context){
        this.context=context;
    }

    public void sendAndRequestResponse() {

        mRequestQueue = Volley.newRequestQueue(context);

        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(context,"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG,"Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }
}
