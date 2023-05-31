package com.moutamid.airbnb.notifications;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fxn.stash.Stash;
import com.moutamid.airbnb.constant.Constants;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class FcmNotificationsSender {
    private static final String TAG = "FcmNotificationsSender";
    String body;
    private String fcmServerKey = "";
    Activity mActivity;
    Context mContext;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private RequestQueue requestQueue;
    String title;
    String userFcmToken;

    public FcmNotificationsSender(String userFcmToken2, String title2, String body2, Context mContext2, Activity mActivity2) {
        fcmServerKey = Stash.getString(Constants.KEY);
        this.userFcmToken = userFcmToken2;
        this.title = title2;
        this.body = body2;
        this.mContext = mContext2;
        this.mActivity = mActivity2;
    }

    public void SendNotifications() {
//        if (Stash.getBoolean(Constants.PAUSE_STATUS, false))
//            return;

        this.requestQueue = Volley.newRequestQueue(this.mActivity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", this.userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", this.title);
            notiObject.put("body", this.body);
            notiObject.put("icon", "icon");
            mainObj.put("notification", notiObject);
            this.requestQueue.add(new JsonObjectRequest(1, "https://fcm.googleapis.com/fcm/send", mainObj, new Response.Listener<JSONObject>() {
                public void onResponse(JSONObject response) {
                    Log.e(TAG, "onResponse: response: " + response.toString());
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "onErrorResponse: ERROR: " + error.getMessage());
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("Content-Type", "application/json");
                    header.put("Authorization", "key=" + fcmServerKey);
//                    header.put("content-type", "application/json");
//                    header.put("authorization", "key=" + fcmServerKey);
//                    header.put("authorization", "key=AAAApf24zxI:APA91bGR1OY2AYOcCJ9Nt156xLrOXrkzJKbwM6hj4d03d4YenZWBxgFTI4fnQnOMmZzFlXOlvr_VsGo39waxcVH4oyJYLZWK-YeMQgP5KDiOkdimuTFa93PoJY-1fRh5NeOhP0IMlGeZ");
                    return header;
                }
            });
        } catch (JSONException e) {
            Log.e(TAG, "SendNotifications: ERROR:" + e.getMessage());
            e.printStackTrace();
        }
    }
}

