package com.example.receive;

import cn.bmob.v3.BmobObject;

/**
 * Created by 张宇 on 2017/7/10.
 */
//同顾客端，收到顾客的评估
public class Evaluate extends BmobObject {
    private String table;
    private String store;
    private String store_con;
    private String store_spic;
    private String dish;
    private String dish_con;
    private String dish_spic;
    private  String complian_time;

    public Evaluate(String table, String store, String store_con, String store_spic, String dish, String dish_con, String dish_spic,String complian_time) {
        this.table=table;
        this.store = store;
        this.store_con = store_con;
        this.store_spic=store_spic;
        this.dish = dish;
        this.dish_con = dish_con;
        this.dish_spic=dish_spic;
        this.complian_time=complian_time;
    }

    public String getComplian_time() {
        return complian_time;
    }

    public void setComplian_time(String complian_time) {
        this.complian_time = complian_time;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getStore_spic() {
        return store_spic;
    }

    public void setStore_spic(String store_spic) {
        this.store_spic = store_spic;
    }

    public String getDish_spic() {
        return dish_spic;
    }

    public void setDish_spic(String dish_spic) {
        this.dish_spic = dish_spic;
    }

    public String getStore() {

        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getStore_con() {
        return store_con;
    }

    public void setStore_con(String store_con) {
        this.store_con = store_con;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public String getDish_con() {
        return dish_con;
    }

    public void setDish_con(String dish_con) {
        this.dish_con = dish_con;
    }
}
