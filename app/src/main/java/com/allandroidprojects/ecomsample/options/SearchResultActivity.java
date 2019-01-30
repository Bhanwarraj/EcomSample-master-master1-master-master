package com.allandroidprojects.ecomsample.options;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity  {
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
                Log.d("Whatsearches222",query);
               String result = solveQuery(query);
                return true;
            }

            private String solveQuery(String query) {
                String result = null;
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

}


