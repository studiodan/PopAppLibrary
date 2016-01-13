package com.studioidan.pop_app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PopApp_laptop on 13/01/2016.
 */
public class DateUtils {

    /**
     * example for input: "yyyy-MM-dd'T'HH:mm:ss"
     * example for output: yyyy-MM-dd HH:mm:ss
     */
    public String convertDateFormat(String date, String inputFormatStr, String outputFormatStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = inputFormat.parse(date);
            String answer = outputFormat.format(d);
            return answer;
        } catch (Exception ex) {
            return "";
        }
    }
}
