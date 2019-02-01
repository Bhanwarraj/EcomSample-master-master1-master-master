package com.allandroidprojects.ecomsample;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class Default_Notification extends AppCompatActivity {

    String personemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default__notification);
        activeHour("No",this);

    }
    public void activeHour(String check,Context ct)
    {TimeZone tz = TimeZone.getTimeZone("GMT+5:30");
        Calendar c = Calendar.getInstance(tz);
        int time = Integer.parseInt(String.format("%02d", c.get(Calendar.HOUR_OF_DAY)));

        int p = Calendar.DATE;
            Log.d("Received", "default");
            //localFileName is with the file with extension which needs the content to be written on it.
            try {
                URL website = new URL("https://storage.googleapis.com/ecommercenotify.appspot.com/users/" + personemail + "/" + "active_hours.csv");
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream("active_hours.csv");
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                File inputFile = new File("active_hours.csv");
               // Read existing file
                CSVReader reader = new CSVReader(new FileReader(inputFile),',');
                List<String[]> csvBody = reader.readAll();
               // get CSV row column  and replace with by using row and column
                String []line;
                int count=0;
                int x;
                while ((line=reader.readNext()) != null) {
                    if(line[24].equals(Integer.toString(p)))
                    {
                         x= Integer.parseInt(line[24]);
                    }
                    else {
                    }
                    count++;
                }
             //   if(count<7 && x==p)
               // {

                //}
               // csvBody.get(p)[time] = ;
                reader.close();
               // Write to CSV file which is open
                CSVWriter writer = new CSVWriter(new FileWriter(inputFile), ',');
                writer.writeAll(csvBody);
                writer.flush();
                writer.close();
            } catch (Exception e) {
                File dir = ct.getFilesDir();
                File file = new File(dir,"active_hours.csv");
                FileWriter output = null;
                try {
                    output = new FileWriter(file);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                CSVWriter writer1 = new CSVWriter(output);
                String[] header = {"00:00-01:00", "01:00-02:00", "02:00-03:00", "03:00-04:00", "04:00-05:00", "05:00-06:00", "06:00-07:00", "07:00-08:00", "08:00-09:00", "09:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00", "18:00-19:00", "19:00-20:00", "20:00-21:00", "21:00-22:00", "22:00-23:00", "23:00-00:00", "date"};
                writer1.writeNext(header);
                String[] data = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", Integer.toString(p)};
                if (check.equals("No")) {
                    data[time] = Integer.toString(Integer.parseInt(data[time]) + 1);
                    writer1.writeNext(data);
                    upload_file("users", personemail, "active_hours.csv", Default_Notification.this);
                } else if (check.equals("Yes")) {
                    data[time] = Integer.toString(Integer.parseInt(data[time]) + 3);
                    writer1.writeNext(data);
                    upload_file("users", personemail, "active_hours.csv", Default_Notification.this);


                }
            }


            Log.d("Received", "Default2");
            //readFromAnyFile(localFileName, context);
            //uploadAnyFile("users", personemail, localFileName, context);

        }


    public  void personinfo(String email)
    {
        personemail=email;
    }


    public void upload_file(String firebaseFolder, String personemail, String sourceFileName, Context context1) {
        Log.d("Uploading", "Storagebucket_activehours");
        File dir = getFilesDir();
        File file = new File(dir, sourceFileName);

        Uri file2 = Uri.fromFile(file);
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://ecommercenotify.appspot.com");
        StorageReference storageRef = storage.getReference();

        StorageReference riv = storageRef.child(firebaseFolder + "/" + personemail + "/" + sourceFileName);
        UploadTask uploadTask = riv.putFile(file2);
        Log.d("Uploading1", "Storagebuckket upload");

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Succes", "Uploaded active_hours");
            }
        });
    }


}


