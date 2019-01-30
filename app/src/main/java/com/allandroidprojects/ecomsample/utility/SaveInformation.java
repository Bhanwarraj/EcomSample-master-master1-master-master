package com.allandroidprojects.ecomsample.utility;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.allandroidprojects.ecomsample.startup.ActionReceiver;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveInformation {

    private  Context context;
    public String PersonName;
    public String Emailid;
    public  String Person_familymember;
    public String personid;

    public SaveInformation(String name, String emailid, String person_familymember, String personid, Context context) throws IOException {
        this.PersonName = name;
        this.Emailid = emailid;
       this. Person_familymember = person_familymember;
       this.personid=personid;

    //public void writeuserdata(String , String title, String message, String personname, String localFileName, String personemail, Long time, Context context) throws IOException {
        Log.d("Received", "ReceiverYes10");
        //  localFileName is with the file with extension which needs the content to be written on it.
        File file = new File(context.getFilesDir(), "userdata.csv");
        BufferedWriter bufferedWriter = null;
        bufferedWriter = new BufferedWriter(new FileWriter(file, true));
        bufferedWriter.write(PersonName);
        bufferedWriter.write(",");
        bufferedWriter.write(Emailid);
        bufferedWriter.write(",");
        bufferedWriter.write(Person_familymember);
        bufferedWriter.write(",");
        bufferedWriter.write(personid);
        bufferedWriter.write("\n");

        bufferedWriter.close();
        Log.d("Received", "ReceiverYes9");
        uploadAnyFile("users", Emailid, "userdata.csv", context);

    }

    public void uploadAnyFile(String firebaseFolder, String personemail, String sourceFileName, Context context1) {
        Log.d("Uploading", "Storagebucket");
        File dir = context1.getFilesDir();
        File file = new File(dir, sourceFileName);
        /*if(file.exists())
            Log.d("status", "exists");
        else
            Log.d("status", "No!");*/
        Uri file2 = Uri.fromFile(file);
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://ecommercenotify.appspot.com");
        StorageReference storageRef = storage.getReference();

        StorageReference riv = storageRef.child(firebaseFolder + "/" + personemail + "/" + sourceFileName);
        UploadTask uploadTask = riv.putFile(file2);
        Log.d("Uploading1", "Storagebucket1");

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Succes", "Uploaded");
            }
        });
    }


}


