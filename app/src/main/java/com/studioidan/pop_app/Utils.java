package com.studioidan.pop_app;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PopApp_laptop on 22/07/2015.
 */
public class Utils {
    public static List<NameValuePair> getParams(String... params)
    {
        if(params.length%2 !=0)
            return null;
        List<NameValuePair> answer = new ArrayList<>();
        for (int i = 0; i <= params.length-2 ; i+=2) {
            answer.add(new BasicNameValuePair(params[i],params[i+1]));
        }
        return  answer;
    }
}
