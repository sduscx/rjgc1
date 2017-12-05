package com.example.receive;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张宇 on 2017/7/13.
 */

public class SmallFoodAdapter extends ArrayAdapter<GoingOrder> {

    private int resourceId;
    private List<GoingOrder> order_food=new ArrayList<>();
    static int count;
    static int all;
    GoingOrder f;
    TextView order_foodname;
    int price;
    TextView order_count;
    TextView food_all;
    public SmallFoodAdapter(Context context, int textViewResourceId, List<GoingOrder> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        order_food=objects;
    };
    public View getView(int position, View convertView, ViewGroup parent){
        f =order_food.get(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        order_foodname=(TextView)view.findViewById(R.id.order_foodname);
        order_foodname.setText(f.getFood_name());
        price=f.getFood_price();
         order_count=(TextView)view.findViewById(R.id.order_count);
        count=f.getFood_count();
        order_count.setText("x"+count);
        all=price*count;
        food_all=(TextView)view.findViewById(R.id.food_all);
        food_all.setText("￥"+all);
        return  view;
    };
}

