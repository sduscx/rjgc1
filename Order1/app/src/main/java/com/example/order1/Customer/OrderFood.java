package com.example.order1.Customer;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by 张宇 on 2017/8/26.
 */

public class OrderFood extends BmobObject {
   private  String table_num;
   private String phone_num;
    private List<GoingOrder> goingOrder;
   private String iffinish;
    private String ifokstr;

    public OrderFood() {
    }

    public OrderFood(String table_num, String phone_num) {
        this.table_num = table_num;
        this.phone_num = phone_num;
        this.goingOrder=goingOrder;
        iffinish="no";


    }
    public String setIfOk(int size){
        String temp=null;
        for(int i=0;i<size;i++){
            temp+="0";
        }
        return temp;
    }

    public List<GoingOrder> getGoingOrder() {
        return goingOrder;
    }

    public void setGoingOrder(List<GoingOrder> goingOrder) {
        this.goingOrder = goingOrder;
    }
    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }



    public String getTable_num() {
        return table_num;
    }

    public void setTable_num(String table_num) {
        this.table_num = table_num;
    }
}
