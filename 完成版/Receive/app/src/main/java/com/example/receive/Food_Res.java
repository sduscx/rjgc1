package com.example.receive;


import cn.bmob.v3.BmobObject;

/**
 * Created by 张宇 on 2017/7/5.
 */

//商家进行添加本店菜品资源时候用到的类
public class Food_Res extends BmobObject{
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
    public String getWifiname() {
        return wifiname;
    }

    public void setWifiname(String wifiname) {
        this.wifiname = wifiname;
    }

    public String getWifipass() {
        return wifipass;
    }

    public void setWifipass(String wifipass) {
        this.wifipass = wifipass;
    }

    public Boolean getIfwifi() {
        return ifwifi;
    }

    public void setIfwifi(Boolean ifwifi) {
        this.ifwifi = ifwifi;
    }

    public boolean isIfabout() {
        return ifabout;
    }

    public void setIfabout(boolean ifabout) {
        this.ifabout = ifabout;
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

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }


    public boolean isIfhave() {
        return ifhave;
    }

    public void setIfhave(boolean ifhave) {
        this.ifhave = ifhave;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
