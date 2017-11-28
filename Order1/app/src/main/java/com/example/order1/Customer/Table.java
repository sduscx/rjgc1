package com.example.order1.Customer;

import cn.bmob.v3.BmobObject;

/**
 * Created by 张宇 on 2017/7/8.
 */

public class Table extends BmobObject {
    private String  table_num;
    public Table() {
    }

    public String getTable_num() {

        return table_num;
    }

    public void setTable_num(String table_num) {
        this.table_num = table_num;
    }

}
