package com.example.receive;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 张宇 on 2017/8/2.
 */

//收到顾客对本店的评价，以表格的形式进行显示
public class ComplainAdapter extends ArrayAdapter <Evaluate>{
    private int resourceId;
    public ComplainAdapter(Context context, int textViewResourceId, List<Evaluate> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    };

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Evaluate e=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView table=(TextView)view.findViewById(R.id.table);
        TextView store=(TextView)view.findViewById(R.id.stroe);
        TextView store_con=(TextView)view.findViewById(R.id.stroe_con);
        TextView store_spic=(TextView)view.findViewById(R.id.stroe_spic);
        TextView dish=(TextView)view.findViewById(R.id.dish);
        TextView dish_con=(TextView)view.findViewById(R.id.dish_con);
        TextView dish_spic=(TextView)view.findViewById(R.id.dish_spic);
        TextView complian_time=(TextView)view.findViewById(R.id.complain_time);
        table.setText(e.getTable());
        store.setText(e.getStore());
        store_con.setText(e.getStore_con());
        store_spic.setText(e.getDish_spic());
        dish.setText(e.getDish());
        dish_con.setText(e.getDish_con());
        dish_spic.setText(e.getDish_spic());
        complian_time.setText(e.getComplian_time());
        return view;
    }
}
