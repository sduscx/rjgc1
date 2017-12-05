package com.example.order1.Customer.receive;

import cn.bmob.v3.BmobObject;

/**
 * Created by 张宇 on 2017/7/8.
 */
//每个table上的详细信息
public class Table extends BmobObject {
    private String  table_num;
    private int table_price;
    private boolean if_finish;
    private String table_type;
    private String floor;
    private int people;
    private String table_info;
    private String password;

    public Table(){

    }
    public Table(String  table_num, boolean if_finish, int table_price, String table_type, String floor, int people, String password) {
        this.table_num = table_num;
        this.if_finish = if_finish;
        this.table_price = table_price;
        this.table_type=table_type;
        this.floor=floor;
        this.people=people;
        this.password=password;
    }

    public Table(String table_num, boolean if_finish, int table_price ) {
        this.table_num = table_num;
        this.if_finish = if_finish;
        this.table_price = table_price;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTable_info() {
        return table_info;
    }

    public void setTable_info(String table_info) {
        this.table_info = table_info;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getTable_type() {

        return table_type;
    }

    public void setTable_type(String table_type) {
        this.table_type = table_type;
    }

    public String getTable_num() {

        return table_num;
    }

    public void setTable_num(String table_num) {
        this.table_num = table_num;
    }

    public int getTable_price() {
        return table_price;
    }

    public void setTable_price(int table_price) {
        this.table_price = table_price;
    }

    public boolean isIf_finish() {
        return if_finish;
    }

    public void setIf_finish(boolean if_finish) {
        this.if_finish = if_finish;
    }
}
