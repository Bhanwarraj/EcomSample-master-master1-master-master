package com.allandroidprojects.ecomsample.startup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.util.Log;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.allandroidprojects.ecomsample.startup.NotificationHandler;
import com.allandroidprojects.ecomsample.utility.SaveInformation;


public class ActionReceiver extends BroadcastReceiver {
    private static final int REQUEST_PERMISSION_WRITE = 1001;
    private static final int REQUEST_WRITE_STORAGE = 1;
    private int STORAGE_PERMISSION_CODE = 23;
   static String title1;
    static String message1;
   static String id1, personname1;
    String str;
    private boolean permissionGranted;
    static String personemail1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Received1", "ReceiverYes1");
        String action = intent.getAction();
        if ("Yes".equals(action)) {
            str = "Yes";
            Log.d("Received", "ReceiverYes");
            MainActivity i= new MainActivity();
            try {
                 Calendar cal=Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyy", Locale.US);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                  Long time= cal.getTimeInMillis();
                i.writeToAnyFile(str,title1,message1,personname1,"info.csv",personemail1,time, context);
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if ("No".equals(action)) {
            Log.d("NO","run");
           String str1 = "No";
            MainActivity i= new MainActivity();
            try {
                Calendar cal=Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyy", Locale.US);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                 Long time1= cal.getTimeInMillis();
                i.writeToAnyFile(str1,title1,message1,personname1,"info.csv",personemail1,time1, context);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

    public void getnotifyinfo(String title, String message) {
        title1 = title;
        message1 = message;

    }

    public void getuserinfo(String id, String personname,String personemail) {
        id1 = id;
        personname1 = personname;
        personemail1=personemail;
        Log.d("person","y"+personname);
    }


}
