package com.example.gourav.pdf;

import android.content.Context;
import android.os.PowerManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.gourav.pdf.MainActivity.mProgressDialog;

public class FileDownloader {
    private static final int  MEGABYTE = 1024 * 1024;
    private Context context;
    private PowerManager.WakeLock mWakeLock;


    public static void downloadFile(String fileUrl, File directory){
        try {

            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            //urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            int total=0;

            while((bufferLength = inputStream.read(buffer))>0 ){
                total+=bufferLength;
                int a=((int) (total * 100 / totalSize));
                mProgressDialog.setProgress(a);
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();
            mProgressDialog.dismiss();
            MainActivity a=null;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void update(Integer progress) {

        // Update the progress dialog
        mProgressDialog.setProgress(progress);
        // Dismiss the progress dialog
        //mProgressDialog.dismiss();
    }
    public static void set()

    {
        MainActivity a = null;
        a.view();
    }







}