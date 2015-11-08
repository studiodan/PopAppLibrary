package com.studioidan.popapplibrary;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by PopApp_laptop on 26/08/2015.
 */
public class DownloadAgent extends AsyncTask<Void, String, String> {

    private String mUrl = "";
    private String mSavePath = "";
    private DownloadCallback mCallback;

    public DownloadAgent(String mUrl, String mSavePath, DownloadCallback callback) {
        this.mSavePath = mSavePath;
        this.mUrl = mUrl;
        this.mCallback = callback;
    }

    @Override
    protected String doInBackground(Void... params) {
        int count;
        try {
            URL url = new URL(mUrl);
            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            OutputStream output = new FileOutputStream(mSavePath);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
            return e.getMessage();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        int prog = Integer.parseInt(progress[0]);
        Log.d(getClass().getName(), "" + prog);
        if (mCallback != null)
            mCallback.onProgress(prog);
    }

    @Override
    protected void onPostExecute(String error) {
        if (mCallback != null)
            mCallback.onDone(error,mSavePath);

        //String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";

        //my_image.setImageDrawable(Drawable.createFromPath(imagePath));
    }

    public interface DownloadCallback {
        void onStart();
        void onProgress(int progress);
        void onDone(String error, String filePath);
        //void onProgress(int progress);
    }
}
