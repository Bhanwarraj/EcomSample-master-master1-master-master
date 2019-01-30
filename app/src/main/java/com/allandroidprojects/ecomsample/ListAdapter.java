package com.allandroidprojects.ecomsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

   public class ListAdapter extends BaseAdapter implements Filterable {
        List<String> mdata;
        List<String>allItems;
        ValueFilter valueFilter;

        public ListAdapter(List<String> cancel_type) {
            mdata=cancel_type;
            allItems= cancel_type;
            Log.d("openedsearch", "search89");

        }


       @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return null;
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                Log.d("openedsearch", "search78");
                valueFilter = new ValueFilter();
            }
            return valueFilter;
        }

        private class ValueFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                Log.d("openedsearch", "search69");
                if (constraint != null && constraint.length() > 0) {
                    List<String> filterList = new ArrayList<>();
                    for (int i = 0; i < allItems.size(); i++) {
                        if ((allItems.get(i).toUpperCase()).contains(constraint.toString().toUpperCase())) {
                            filterList.add(allItems.get(i));
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = allItems.size();
                    results.values = allItems;
                }
                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                mdata = (List<String>) results.values;
                notifyDataSetChanged();
            }

        }



    }





