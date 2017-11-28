package com.example.order1.Customer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.order1.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 张宇 on 2017/7/9.
 */

public class OrderCarActivity extends AppCompatActivity {
    private  String tablenum=null;
    private String phonenum=null;
    ListView foodlist;
    SmallFoodAdapter adapter;
   static   TextView table_price;
    Button submit_but;
    private  int score;
    static List<GoingOrder> updateFood=new ArrayList<>();
     List<GoingOrder> forIDFood=new ArrayList<>();
    static int money=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_foodlist);
        tablenum=getIntent().getStringExtra("table_num");
        phonenum=getIntent().getStringExtra("phone_num");
        foodlist=(ListView)findViewById(R.id.big_list);
        //initTableware();
        //初始化已经加入购物车的东西
        initFood();
        table_price=(TextView)findViewById(R.id.table_money);
        submit_but=(Button)findViewById(R.id.submit_order_button);
        submit_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(money==0){
                    Toast.makeText(OrderCarActivity.this, "请您先前往点餐", Toast.LENGTH_SHORT).show();
                    finish();
                }else {

                    score = (int) (money / 100) * 10;
                    if (score == 0) {
                        Toast.makeText(OrderCarActivity.this, R.string.no_score, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OrderCarActivity.this, "订单提交中，请耐心等待...", Toast.LENGTH_SHORT).show();
                        BmobQuery<Custom> query = new BmobQuery<Custom>();
                        query.addWhereEqualTo("phoneNumber", phonenum);
                        query.findObjects(new FindListener<Custom>() {
                            @Override
                            public void done(List<Custom> list, BmobException e) {
                                if (e == null) {
                                    if (list.size() == 1) {
                                        score += list.get(0).getScore();
                                        String changId=list.get(0).getObjectId();
                                        Custom changeCustom=new Custom();
                                        changeCustom.setScore(score);
                                        changeCustom.update(changId, new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if(e==null){
                                                    Toast.makeText(OrderCarActivity.this, "恭喜您在本店已有" + score + "积分!", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Log.e("修改积分失败",""+e);
                                                }
                                            }
                                        });
                                        //list.get(0).setScore(score);

                                    }
                                } else {
                                    Log.e("提交订单", "" + e);
                                }
                            }
                        });
                    }
                    //创建一个已点额表
                    //上传的数据应该简单些
                    OrderFood food = new OrderFood(tablenum, phonenum);
                    food.addAll("goingOrder", updateFood);
                    food.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(OrderCarActivity.this, "点餐成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }
                    });

                    //删除将要点的
                    List<BmobObject> persons = new ArrayList<BmobObject>();
                    persons.addAll(forIDFood);
                    new BmobBatch().deleteBatch(persons).doBatch(new QueryListListener<BatchResult>() {

                        @Override
                        public void done(List<BatchResult> o, BmobException e) {
                            if (e == null) {
                                for (int i = 0; i < o.size(); i++) {
                                    BatchResult result = o.get(i);
                                    BmobException ex = result.getError();
                                    if (ex == null) {
                                        Log.e("数据", "删除成功");
                                    } else {
                                    }
                                }
                            } else {
                                Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });

                }
            }
        });

    }


    //计算总价钱
    public static void con_money(){
        for(int i=0;i<updateFood.size();i++){
            int price=updateFood.get(i).getFood_price();
            int count=updateFood.get(i).getFood_count();
            money+=price*count;
        }
        table_price.setText("￥"+money+"元");
    }

    //初始化购物车物品
    public  synchronized void initFood(){
        BmobQuery<GoingOrder> query=new BmobQuery<>();
        query.addWhereEqualTo("table_num",tablenum);
        query.findObjects(new FindListener<GoingOrder>() {
            @Override
            public void done(List<GoingOrder> list, BmobException e) {
                if(e==null){
                        forIDFood=list;
                        adapter=new SmallFoodAdapter(OrderCarActivity.this,R.layout.order_foodcon,list,list.size());
                        foodlist.setAdapter(adapter);
                }else{
                    Log.e("初始化账单",""+e);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateFood.clear();
        money=0;
    }
}
