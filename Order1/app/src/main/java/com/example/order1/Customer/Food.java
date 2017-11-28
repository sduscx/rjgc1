package com.example.order1.Customer;

/**
 * Created by 张宇 on 2017/7/5.
 */

//顾客点的餐的详细信息
public class Food {
    private String food_name;
    private int food_price;
    private int food_count;

    public Food(){

    }
    public Food(int food_price, String food_name, int food_count) {
        this.food_price = food_price;
        this.food_name = food_name;
        this.food_count=food_count;

    }

    public int getFood_count() {
        return food_count;
    }

    public void setFood_count(int food_count) {
        this.food_count = food_count;
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
