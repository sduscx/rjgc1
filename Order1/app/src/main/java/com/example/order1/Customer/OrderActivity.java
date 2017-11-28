package com.example.order1.Customer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.order1.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class OrderActivity extends AppCompatActivity {

    int priceOfAll=0;
    String table_num=null;
    List<GoingOrder> goingOrders=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        final ListView  listView=(ListView)findViewById(R.id.order_list);
        final TextView orderPrice=(TextView)findViewById(R.id.ordertable_money);

       table_num=getIntent().getStringExtra("table_num");
        BmobQuery<OrderFood> query=new BmobQuery<OrderFood>();
        query.addWhereEqualTo("table_num",table_num);
        query.findObjects(new FindListener<OrderFood>() {
            @Override
            public void done(List<OrderFood> list, BmobException e) {
                if(e==null){
                    for(int i=0;i<list.size();i++){
                        goingOrders.addAll(list.get(i).getGoingOrder());
                        for(int j=0;j<list.get(i).getGoingOrder().size();j++){
                            priceOfAll+=list.get(i).getGoingOrder().get(j).getFood_price()*list.get(i).getGoingOrder().get(j).getFood_count();
                        }
                    }
                    OrderFoodAdapter adapter=new OrderFoodAdapter(OrderActivity.this,R.layout.order_list_item,goingOrders);
                    listView.setAdapter(adapter);
                    orderPrice.setText("本次用餐您已在本店消费"+priceOfAll+"元");
                }else{
                    Log.e("查询",""+e);
                }
            }
        });



    }
}
