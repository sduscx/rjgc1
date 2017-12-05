package com.example.receive;

import cn.bmob.v3.BmobObject;

/**
 * Created by 张宇 on 2017/7/12.
 */

//显示顾客已经加入购物车但是还没有下单的菜
public class GoingOrder extends BmobObject {

    private String food_name;
    private int food_price;
    private int food_count;
    public GoingOrder(){

    }
    public int getFood_count() {
        return food_count;
    }

    public String getFood_name() {
        return food_name;
    }



    public int getFood_price() {
        return food_price;
    }


}
