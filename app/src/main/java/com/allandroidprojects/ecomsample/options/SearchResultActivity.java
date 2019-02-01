package com.allandroidprojects.ecomsample.options;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.allandroidprojects.ecomsample.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class SearchResultActivity extends AppCompatActivity  {

   static String personemail1;

    private ListView list;
    EditText search;
    ArrayAdapter adapter;
    ListAdapter adp;
    List<String> mdata;
    List<String> allItems=new ArrayList<String>();

    {
        String str[] = {
                "Ashwin","RedmiNote6 Pro", "Sandisk 64GB", "Sony Headphones", " Boat Bluetooth Speaker", "Dell Inspiron15 laptop ",
                "Jacket", "Reebok Shoes", "Denim shirt", " Peter England Trouser", " Nike Skirt", "Polo Cap",
                "Usha Sewing machine", "Pillow", "Lamp", "Syska Led bulb 40W", "Horse Scenry", "Echoes", "The only End", "DSA by Shankar", "C by harish", "Hate burn", "restart my life"
        };
        Collections.addAll(allItems, str);
    }
    String[] Electronics = {
            "RedmiNote6 Pro", "Sandisk 64GB", "Sony Headphones", " Boat Bluetooth Speaker", "Dell Inspiron15 laptop "
    };
    String[] Lifestyle = { "Ashwin","Jacket", "Reebok Shoes", "Denim shirt", " Peter England Trouser", " Nike Skirt", "Polo Cap"
    };
    String[] HomeAppliances = {"Usha Sewing machine", "Pillow", "Lamp", "Syska Led bulb 40W", "Horse Scenry"
    };
    String[] Books = {"Echoes", "The only End", "DSA by Shankar", "C by harish", "Hate burn", "restart my life"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Log.d("openedsearch", "search0");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("openedsearch", "search1");
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.getItem(0);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        Log.d("openedsearch", "search2");
        //EditText search=(EditText) findViewById(R.id.action_search);
        adp= new com.allandroidprojects.ecomsample.ListAdapter(allItems);
       // ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.activity_list,R.id.information,allItems);
        ListView listView=(ListView) findViewById(R.id.listaction1);
        listView.setAdapter(adp);

        assert searchManager != null;
        searchManager.getSearchableInfo(getComponentName());
        searchView.setSubmitButtonEnabled(true);
        searchView.setFocusable(true);
        searchItem.expandActionView();
     /*  search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                SearchResultActivity.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               String result = solveQuery(query);
                TimeZone tz=TimeZone.getTimeZone("GMT+5:30");
                Calendar c=Calendar.getInstance(tz);
                String time=String.format("%02d",c.get(Calendar.HOUR_OF_DAY))+":"+String.format("%02d",c.get(Calendar.MINUTE));
                Log.d("Time",time);
                try {
                    writeToRecentsearch(result,query,time,personemail1,"recent_search.csv", SearchResultActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

            private String solveQuery(String query) {
                String result = "Not found";
                for (String s : Electronics) {
                   if(s.equals(query))
                       return result = "Electronics";
                }
                for (String s : Lifestyle) {
                   if(s.equals(query))
                       return result = "Lifestyle";
                }
                for (String s : HomeAppliances) {
                   if(s.equals(query))
                       return result = "HomeAppliances";
                }
                for (String s : Books) {
                   if(s.equals(query))
                       return result = "Books";
                }
                return  result;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String value=newText;
                Log.d("Whatsearches",value);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
    }

    public void getuserinfo_search(String personemail) {
        personemail1=personemail;
        Log.d("person","y"+personemail);
    }

    public void writeToRecentsearch(String result,String query,String time,String email,String sourcefilename,Context context) throws IOException {

        File file = new File(context.getFilesDir(), sourcefilename);
        Log.d("Context", "msg" + context.getFilesDir());
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            if(file.exists()){
                bufferedWriter.write("Category");
                bufferedWriter.write(",");
                bufferedWriter.write("Search_keyword");
                bufferedWriter.write(",");
                bufferedWriter.write("Time");
                bufferedWriter.write("\n");
            }

        bufferedWriter.write(result);
        bufferedWriter.write(",");
        bufferedWriter.write(query);
        bufferedWriter.write(",");
        bufferedWriter.write(time);
        bufferedWriter.write("\n");

        bufferedWriter.close();

        uploadFilesearch("users", email, sourcefilename, context);

    }

    public void uploadFilesearch(String firebaseFolder,String personemail,String sourcefilename,Context context)
    {
        File dir = context.getFilesDir();
        File file = new File(dir, sourcefilename);
        /*if(file.exists())
            Log.d("status", "exists");
        else
            Log.d("status", "No!");*/
        Uri file2 = Uri.fromFile(file);
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://ecommercenotify.appspot.com");
        StorageReference storageRef = storage.getReference();

        StorageReference riv = storageRef.child(firebaseFolder + "/" + personemail + "/" + sourcefilename);
        UploadTask uploadTask = riv.putFile(file2);
        Log.d("Uploading1", "Searchuploaded");

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Succes", "Uploaded_Search");
            }
        });

    }
}


