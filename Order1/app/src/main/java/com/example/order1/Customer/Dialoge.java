package com.example.order1.Customer;

import android.app.Dialog;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by 张宇 on 2017/8/19.
 */

//顾客和商家对话的类，顾客的桌号，顾客对商家发送的消息
public class Dialoge extends BmobObject {

    private String table_num;
    private List<Message> message;

    public Dialoge(){

    }




    public String getTable_num() {
        return table_num;
    }

    public void setTable_num(String table_num) {
        this.table_num = table_num;
    }

    public List<Message> getMessage() {
        return message;
    }

    public void setMessage(List<Message> message) {
        this.message = message;
    }
}
