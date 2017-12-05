package com.example.order1.Customer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.order1.R;

import java.util.ArrayList;
import java.util.List;

//食物的适配器
public class FoodAdapter extends ArrayAdapter<Food> {
    private int resourceId;
     String food_count="1";
    static Food f=null;
    private Food food=null;
    List<Food> foods=new ArrayList<>();

    public FoodAdapter(Context context, int textViewResourceId, List<Food> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        foods=objects;
    };
    public View getView(final int position, View convertView, ViewGroup parent){
         food=getItem(position);
        food_count="1";
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        final TextView foodname=(TextView)view.findViewById(R.id.food_name);
        final TextView foodprice=(TextView)view.findViewById(R.id.food_price);
        foodname.setText(food.getFood_name());
        String strprice="￥"+food.getFood_price()+"元";
        foodprice.setText(strprice);
        final TextView foodcount=(TextView)view.findViewById(R.id.food_count);
        final ImageButton    delete=(ImageButton)view.findViewById(R.id.delete_pic);
        final ImageButton    add=(ImageButton)view.findViewById(R.id.add_first);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delete.getVisibility()==View.INVISIBLE){
                    delete.setVisibility(View.VISIBLE);
                    foodcount.setVisibility(View.VISIBLE);
                    foodcount.setText("1");
                    f=new Food();
                    f.setFood_count(1);
                    f.setFood_name(foods.get(position).getFood_name());
                    //f.setFood_name(food.getFood_name());
                    f.setFood_price(foods.get(position).getFood_price());
                    FoodContentActivity.orderedfood.add(f);
                   // orderfood.add(f);
                    Log.e("添加",f.getFood_name());
                    f=null;
                }
                else{
                    food_count=foodcount.getText().toString();
                    foodcount.setText(""+(Integer.parseInt(food_count)+1));
                    for(int i=0;i<FoodContentActivity.orderedfood.size();i++){
                        if(foods.get(position).getFood_name().equals(FoodContentActivity.orderedfood.get(i).getFood_name())){
                            FoodContentActivity.orderedfood.get(i).setFood_count(Integer.parseInt(food_count)+1);
                            Log.e("添加",""+foods.get(position).getFood_name());
                            break;
                        }
                    }
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_count=foodcount.getText().toString();
                Log.e("delete",""+foods.get(position).getFood_name());
                if(!food_count.equals("1")){
                    foodcount.setText(""+(Integer.parseInt(food_count)-1));
                    for(int i=0;i<FoodContentActivity.orderedfood.size();i++){
                        if(foods.get(position).getFood_name().equals(FoodContentActivity.orderedfood.get(i).getFood_name())){
                            FoodContentActivity.orderedfood.get(i).setFood_count(Integer.parseInt(food_count)-1);
                            Log.e("删除",""+foods.get(position).getFood_name());
                            break;
                        }
                    }
                  //  f.setFood_count((Integer.parseInt(food_count)-1));
                }
                else{
                    delete.setVisibility(View.INVISIBLE);
                    foodcount.setVisibility(View.INVISIBLE);
                    food_count="1";
                    //orderfood.remove(f);
                    for(int i=0;i<FoodContentActivity.orderedfood.size();i++){
                        if(foods.get(position).getFood_name().equals(FoodContentActivity.orderedfood.get(i).getFood_name())){
                            FoodContentActivity.orderedfood.remove(i);
                            Log.e("删除l",""+foods.get(position).getFood_name());
                           break;

                        }
                    }
                }
            }
        });

        return  view;
    };
}
