package com.studioidan.pop_app.web;

/**
 * Created by PopApp_laptop on 04/08/2015.
 */
public interface IRequestCallback {
    public void onRequestEnd(String methodName, String response);
    public void onError(String methodName, String error);

}
