package com.example.order1.Customer;

import cn.bmob.v3.BmobObject;

/**
 * Created by 张宇 on 2017/7/12.
 */

//用于已经加入购物车，但是还没结账
public class GoingOrder extends BmobObject {
    private String food_type;
    private String food_name;
    private int food_price;
    private int food_count;
    private String table_num;
    private boolean if_order;
    private int price_all;
    private  String ifok;

    public int getPrice_all() {
        return price_all;
    }

    public void setPrice_all(int price_all) {
        this.price_all = price_all;
    }
    public GoingOrder(int price_all){
        this.price_all=price_all;
    }

    public boolean isIf_order() {
        return if_order;
    }

    public void setIf_order(boolean if_order) {
        this.if_order = if_order;
    }

    public String getTable_num() {
        return table_num;
    }

    public void setTable_num(String table_num) {
        this.table_num = table_num;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public int getFood_count() {
        return food_count;
    }

    public void setFood_count(int food_count) {
        this.food_count = food_count;
    }

    public GoingOrder(){

    }
    public GoingOrder(int food_price, String food_name, int food_count){
        this.food_price = food_price;
        this.food_name = food_name;
        this.food_count=food_count;

    }
    public GoingOrder(int food_price, String food_name, int food_count,String food_type,String table_num,boolean if_order) {
        this.food_price = food_price;
        this.food_name = food_name;
        this.food_count=food_count;
        this.food_type=food_type;
        this.table_num=table_num;
        this.if_order=if_order;
        ifok="no";
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public int getFood_price() {
        return food_price;
    }

    public void setFood_price(int food_price) {
        this.food_price = food_price;
    }

}
