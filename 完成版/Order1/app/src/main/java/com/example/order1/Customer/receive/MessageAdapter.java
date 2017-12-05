package com.example.order1.Customer.receive;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.order1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张宇 on 2017/8/19.
 */

//每一位顾客所有消息的适配器
public class MessageAdapter extends ArrayAdapter<Message> {
    private int resourceId;
    List<Message> messages=new ArrayList<>();
    TextView mDate;
    TextView mMsg;
    TextView table_num;
    public MessageAdapter(Context context, int textViewResourceId, List<Message> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        messages=objects;
    };
    public View getView(int position, View convertView, ViewGroup parent){
        Message m=getItem(position);
        if(convertView==null){
            //引入左边布局
            if(m.getType().equals("custom")){
                convertView=LayoutInflater.from(getContext()).inflate(R.layout.message_custom,parent,false);
                mDate=(TextView) convertView.findViewById(R.id.custom_msg_date);
                mMsg=(TextView)convertView.findViewById(R.id.custom_msg);
                table_num=(TextView)convertView.findViewById(R.id.msg_custom_name) ;
                table_num.setText(RDialoge.table_num);
            }else {
                //引入右边布局


                convertView=LayoutInflater.from(getContext()).inflate(R.layout.message_shop,parent,false);
                mDate=(TextView) convertView.findViewById(R.id.shop_msg_date);
                mMsg=(TextView)convertView.findViewById(R.id.shop_msg);
            }
            mDate.setText(m.getDateStr());
            mMsg.setText(m.getRequest());
        }
        return  convertView;
    };
}
