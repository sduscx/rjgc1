package com.example.order1.Customer.receive;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 张宇 on 2017/8/17.
 */

//同顾客端，显示消息的内容
public class Message {

    private String request;
   private  String type;
    private Date date;
   private String dateStr;


    public Message(String request, String type, Date data ) {
        this.request = request;
        this.type=type;
        dateStr= formatDate(data);
    }
    public Message(String request, String type, String dateStr ) {
        this.request = request;
        this.type=type;
        this.dateStr=dateStr;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }


    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String  formatDate(Date date1){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String t1=format.format(date1);
        return t1;
    }
}
