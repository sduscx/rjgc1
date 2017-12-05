package com.example.receive;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by 张宇 on 2017/7/14.
 */

public class TableAdapter extends ArrayAdapter<Table> {
    private int resourceId;
    public TableAdapter(Context context, int textViewResourceId, List<Table> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    };
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         Table t=getItem(position);
        View v= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView grid_tablename=(TextView)v.findViewById(R.id.grid_tablename);
        grid_tablename.setText(t.getTable_num());
        return v;
    }
}
