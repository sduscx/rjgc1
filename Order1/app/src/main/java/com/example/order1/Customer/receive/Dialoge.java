package com.example.order1.Customer.receive;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by 张宇 on 2017/8/19.
 */

//同于顾客端
public class Dialoge extends BmobObject {
    private String table_num;
    private List<Message> message;

    public Dialoge(){

    }
    public Dialoge(String table_num, List<Message> message) {
        this.table_num = table_num;
        this.message = message;
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
