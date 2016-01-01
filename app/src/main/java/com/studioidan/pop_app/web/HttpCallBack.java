package com.studioidan.pop_app.web;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by PopApp_laptop on 04/08/2015.
 */
public class HttpCallBack implements IRequestCallback {
    private final String tag = "HttpCallBack";

    public void onRequestStart(String mMethodName) {
        Log.d(tag, "Method: " + mMethodName + "started");
    }

    @Override
    public void onRequestEnd(String mMethodName, String response) {
        Log.d(tag, "Method: " + mMethodName + "\nresponse: " + response);
    }
    public void onRequestJsonEnd(String methodName,JSONObject responseObj){
        Log.d(tag, "Method: " + methodName + "\nresponse: " + responseObj.toString());
    }

    @Override
    public void onError(String mMethodName, String error) {
        Log.d(tag, "Error in Method: " + mMethodName + "\nmessage: " + error);
    }
}
