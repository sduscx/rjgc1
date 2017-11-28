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

/**
 * Created by 张宇 on 2017/8/19.
 */

public class MessageAdapter extends ArrayAdapter<Message> {
    int resourceId;
    List<Message> messages=new ArrayList<>();
    TextView mDate;
    TextView mMsg;
    public MessageAdapter(Context context, int textViewResourceId, List<Message> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        messages=objects;
    };
    public View getView(int position, View convertView, ViewGroup parent){
        Message m=messages.get(position);

        //判断是谁发来的消息
        if(convertView==null){
            //引入左边布局
            if(m.getType().equals("shop")){
                convertView=LayoutInflater.from(getContext()).inflate(R.layout.msg_shop,parent,false);
                mDate=(TextView) convertView.findViewById(R.id.shop_msg_date);
                mMsg=(TextView)convertView.findViewById(R.id.shop_msg);
            }else {
                //引入右边布局
                convertView=LayoutInflater.from(getContext()).inflate(R.layout.msg_custom,parent,false);
                mDate=(TextView) convertView.findViewById(R.id.custom_msg_date);
                mMsg=(TextView)convertView.findViewById(R.id.custom_msg);
            }
            mDate.setText(m.getDateStr());
            mMsg.setText(m.getRequest());
        }
        return  convertView;
    };
}
