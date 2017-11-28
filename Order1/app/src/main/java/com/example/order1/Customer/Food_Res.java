package com.example.order1.Customer;


import cn.bmob.v3.BmobObject;

/**
 * Created by 张宇 on 2017/7/5.
 */

public class Food_Res extends BmobObject {
    private String food_name;
    private int food_price;
    private String food_type;
    private boolean ifhave;
    private String iffood;
    private String about;
    private boolean ifabout;
    private String wifiname;
    private String wifipass;
    private Boolean ifwifi;


    public Food_Res() {
    }

    public Food_Res(String wifipass, String wifiname, boolean ifwifi,boolean ifhave, boolean ifabout) {
        this.ifwifi = ifwifi;
        this.wifipass = wifipass;
        this.wifiname = wifiname;
        this.ifhave = ifhave;
        this.ifabout = ifabout;
        iffood="false";
    }

    public Food_Res(String food_name, int food_price, String food_type, boolean ifhave, String about, boolean ifabout) {
        this.food_name = food_name;
        this.food_price = food_price;
        this.food_type = food_type;
        this.ifhave = ifhave;
        this.about=about;
        this.ifabout=ifabout;
        iffood="true";

    }

   public String getAbout() {
       return about;
    }

    public String getWifiname() {
        return wifiname;
    }

    public String getWifipass() {
        return wifipass;
    }

    public String getFood_name() {
        return food_name;
    }

    public int getFood_price() {
        return food_price;
    }
    public void setWifiname(String wifiname) {
        this.wifiname = wifiname;
    }
    public void setWifipass(String wifipass) {
        this.wifipass = wifipass;
    }


}
