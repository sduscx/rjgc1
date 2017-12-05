package com.example.order1.Customer;

import cn.bmob.v3.BmobObject;

/**
 * Created by 张宇 on 2017/7/5.
 */

//记载顾客信息的类
public class Custom extends BmobObject {
    private String phoneNumber;
    private String password;
    private int score;
    //private boolean ifLoad;


    public Custom(){

    }
    public Custom(String phoneNumber, String password, int score, boolean ifLoad) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.score = score;
       // this.ifLoad=ifLoad;
    }

    /*public boolean isIfLoad() {
        return ifLoad;
    }

    public void setIfLoad(boolean ifLoad) {
        this.ifLoad = ifLoad;
    }*/

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
