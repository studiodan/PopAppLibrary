package com.studioidan.pop_app;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by PopApp_laptop on 04/08/2015.
 */
public class HttpCallBack {
    private final String tag = "HttpCallBack";

    public void onRequestStart(String mMethodName) {
        Log.d(tag, "Method: " + mMethodName + "started");
    }

    public void onRequestEnd(String mMethodName, String response) {
        Log.d(tag, "Method: " + mMethodName + "\nresponse: " + response);
    }

    public void onJsonRequestEnd(String methodName, JSONObject responseObj){
        Log.d(tag, "Method: " + methodName + "\nresponse: " + responseObj.toString());
    }

    public void onError(String mMethodName, String error) {
        Log.d(tag, "Error in Method: " + mMethodName + "\nmessage: " + error);
    }
}
