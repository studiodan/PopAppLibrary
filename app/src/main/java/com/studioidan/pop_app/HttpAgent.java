package com.studioidan.pop_app;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PopApp_laptop on 14/06/2015.
 */
public class HttpAgent extends AsyncTask<String, String, String> {
    private static final int TIME_OUT = 10000;
    private String tag = "HttpAgent";

    public enum HTTP_METHOD {GET, POST}

    public Dialog mDialog = null;
    public String mUrl;
    public String mMethodName="";
    public boolean isJson = true;
    public Context context;
    public Map<String, String> headers;
    public Map<String, String> params;
    private String mError;
    private HTTP_METHOD method = HTTP_METHOD.GET;
    private HttpCallBack mCallback;

    // contractors
    public HttpAgent(String url) {
        headers = new HashMap<String, String>();
        params = new HashMap<String, String>();
        this.mUrl = url;
    }

    public HttpAgent methodName(String methodName) {
        this.mMethodName = methodName;
        return this;
    }

    public HttpAgent method(HTTP_METHOD method) {
        this.method = method;
        return this;
    }

    public HttpAgent addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public HttpAgent addParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    public HttpAgent setJson(boolean isJson) {
        this.isJson = isJson;
        return this;
    }

    public HttpAgent withCallback(HttpCallBack callback) {
        this.mCallback = callback;
        return this;
    }

    public HttpAgent withContext(Context con) {
        this.context = con;
        return this;
    }

    public void setDialog(Dialog dialog) {
        this.mDialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        mError = "";
        if (mCallback != null)
            mCallback.onRequestStart(mMethodName);
        if (mDialog != null)
            mDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String responseStr = "";
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, TIME_OUT);
        HttpClient httpClient = new DefaultHttpClient(httpParameters);        String paramsString = "";

        if (method == HTTP_METHOD.GET) {
            //set params
            paramsString = URLEncodedUtils.format(mapToList(params), "UTF-8");
            // add it to our request
            HttpGet httpGet = new HttpGet(mUrl + "?" + paramsString);
            try {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
                //execute and get response
                HttpResponse response = httpClient.execute(httpGet);
                responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                // check response status code
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK)
                    mError = "statusCode: " + response.getStatusLine().getStatusCode() + "\n" + responseStr;

            } catch (IOException e) {
                e.printStackTrace();
                mError = e.getMessage();
            }
        } else {
            HttpPost httpPost = new HttpPost(mUrl);
            if (httpPost == null || mUrl.trim().equals("")) {
                mError = "url is not correct";
                return null;
            }
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(mapToList(params), "UTF-8"));
                HttpResponse response = httpClient.execute(httpPost);
                responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");

                // check response status code
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
                    mError = "statusCode: " + response.getStatusLine().getStatusCode() + "\n" + responseStr;

            } catch (IOException e) {
                e.printStackTrace();
                mError = e.getMessage();
            }
        }
        return responseStr;
    }

    @Override
    protected void onPostExecute(String res) {
        //super.onPostExecute(s);
        if (mDialog != null)
            mDialog.dismiss();

        if (mCallback != null) {
            if (!mError.equals("")) {
                mCallback.onError(mMethodName, mError);
                return;
            }

            //there was no error
            if (isJson) {
                try {
                    JSONObject responseJson = new JSONObject(res);
                    mCallback.onJsonRequestEnd(mMethodName, responseJson);
                } catch (JSONException e) {
                    Log.e(tag, e.getMessage());
                    mCallback.onError(mMethodName, e.getMessage());
                }
            } else
                mCallback.onRequestEnd(mMethodName, res);
            //minor change

        }
    }

    private List<NameValuePair> mapToList(Map<String, String> map) {
        List<NameValuePair> answer = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> param : params.entrySet())
            answer.add(new BasicNameValuePair(param.getKey(), param.getValue()));

        return answer;
    }

}
