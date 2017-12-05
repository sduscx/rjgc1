package com.example.order1.Customer.receive;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by 张宇 on 2017/8/24.
 */

//顾客进行催单得到类，催单的桌号，催单的时间
public class Hurry extends BmobObject {
    private String table_num;
    private String dateStr;

    public Hurry() {

    }

    public Hurry(String table_num, Date date) {
        this.table_num = table_num;
        dateStr=formatDate(date);
    }


    public String getTable_num() {
        return table_num;
    }

    public void setTable_num(String table_num) {
        this.table_num = table_num;
    }
    public String  formatDate(Date date1){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String t1=format.format(date1);
        return t1;
    }

    public String getDateStr() {
        return dateStr;
    }
}
