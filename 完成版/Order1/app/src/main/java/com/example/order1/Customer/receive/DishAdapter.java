package com.example.order1.Customer.receive;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.order1.R;

import java.util.List;

/**
 * Created by 张宇 on 2017/8/8.
 */

//显示网格布局的内容

public class DishAdapter extends ArrayAdapter<Food_Res> {
    int resourceId;
    Context context;
    public DishAdapter(Context context, int resource, List<Food_Res> objects) {
        super(context, resource,objects);
        this.context=context;
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Food_Res f=getItem(position);
        View v= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView grid_dishname=(TextView)v.findViewById(R.id.grid_dishname);
        grid_dishname.setText(f.getFood_name());
        if(!f.isIfhave()){
           grid_dishname.setTextColor(context.getResources().getColor(R.color.dish_donthave));
        }else{
            grid_dishname.setTextColor(context.getResources().getColor(R.color.dish_have));
        }

        return v;
    }
}
