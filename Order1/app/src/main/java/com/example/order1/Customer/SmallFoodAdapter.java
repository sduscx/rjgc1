package com.example.order1.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.order1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张宇 on 2017/7/13.
 */

//账单的适配器
public class SmallFoodAdapter extends ArrayAdapter<GoingOrder> {

    private int resourceId;
    private List<GoingOrder> food=new ArrayList<>();
     int count=0;
     int all;
    GoingOrder f;
    TextView order_foodname;
    int price;
    TextView order_count;
    TextView food_all;
    int size;
    public SmallFoodAdapter(Context context, int textViewResourceId, List<GoingOrder> objects,int listsize){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        food=objects;
         size=listsize;
    };
    public View getView(final int position, View convertView, ViewGroup parent){
       View view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

         f =food.get(position);
        OrderCarActivity.updateFood.add(f);
        if(position==(size-1)){
            OrderCarActivity.con_money();
        }
         order_foodname=(TextView)view.findViewById(R.id.order_foodname);
        order_foodname.setText(f.getFood_name());
        price=f.getFood_price();
         order_count=(TextView)view.findViewById(R.id.order_count);
        count=f.getFood_count();
        order_count.setText("x"+count);
        all=price*count;
        food_all=(TextView)view.findViewById(R.id.food_all);
        food_all.setText("￥"+all);
        ImageButton order_delete=(ImageButton)view.findViewById(R.id.order_delete);
        order_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>=2){
                    count=count-1;
                    order_count.setText("x"+count);
                    f.setFood_count(count);
                    all=count*price;
                    food_all.setText("￥"+all);
                    OrderCarActivity.updateFood.get(position).setFood_count(count);
                    OrderCarActivity.money-=price;
                    OrderCarActivity.table_price.setText("￥"+ OrderCarActivity.money+"元");
                }else{
                    food.remove(position);
                    notifyDataSetChanged();
                    OrderCarActivity.updateFood.remove(f);
                    OrderCarActivity.money-=price;
                    OrderCarActivity.table_price.setText("￥"+ OrderCarActivity.money+"元");
                }
            }
        });
        ImageButton order_add=(ImageButton)view.findViewById(R.id.order_add);
        order_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count+=1;
                order_count.setText("x"+count);
                f.setFood_count(count);
                all=count*price;
                food_all.setText("￥"+all);
                OrderCarActivity.updateFood.get(position).setFood_count(count);
                OrderCarActivity.money+=price;
                OrderCarActivity.table_price.setText("￥"+ OrderCarActivity.money+"元");

            }
        });


        return  view;
    };
}

