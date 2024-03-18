package com.example.chat_app1.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chat_app1.firebase.FirebaseFCM;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Backgroundworker extends Service {

    private RequestQueue mRequestQueue;

    public Backgroundworker() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String FcmToken = intent.getStringExtra("fcm_token");
        String url = "https://fcm.googleapis.com/fcm/send";

        String TOPIC = FcmToken;
        String NOTIFICATION_TITLE = intent.getStringExtra("msg_from");
        String NOTIFICATION_MESSAGE = intent.getStringExtra("msg_content");
        Log.d("Message data payload: ",NOTIFICATION_TITLE+" "+NOTIFICATION_MESSAGE);
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e("FirebaseFCMService", "onCreate Exception: " + e.getMessage() );
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("FirebaseFCMService", "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FirebaseFCMService", "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "key=AAAAXqWns5Y:APA91bEPErnKPs6vdtPmF0RGz0OJloU24QzCIByMCrIHyH7jYmteL19ae-jasP7_9A1DGzW2zOz-gavk4ICj_R2Ffaa13hLTuTq10hQKVzlFYtwit5ok8aPgw5Z7ySkxoMaecAXzv6Qj");
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        mRequestQueue.add(jsonObjectRequest);
        
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRequestQueue = Volley.newRequestQueue(this);
        Log.d("FirebaseFCMService", "started");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}