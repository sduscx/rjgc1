package com.example.order1.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.order1.R;
;

import java.io.Serializable;
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

/**
 * Created by 张宇 on 2017/7/5.
 */

public class FoodContentActivity extends AppCompatActivity {
    String type;
    ListView listView;
    List<Food> food=new ArrayList<>();
    static List<Food> orderedfood=new ArrayList<>();
    String table_num;
    FoodAdapter adapter=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_foodcontent);
        type=getIntent().getStringExtra("type");
        table_num=getIntent().getStringExtra("table_num");
        //Toast.makeText(this, "type"+type, Toast.LENGTH_SHORT).show();
        listView=(ListView)findViewById(R.id.food_list);
        initFood();
        //对每个item进行监听，如果被按了，表示该菜被选中，则保存起来
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }
    //根据传来的菜的种类不同，加载不同的菜单
    private void initFood(){
        BmobQuery<Food_Res> query=new BmobQuery<Food_Res>();
        query.addWhereEqualTo("food_type",type);
        query.addWhereEqualTo("ifhave",true);
        query.findObjects(new FindListener<Food_Res>() {
            @Override
            public void done(List<Food_Res> list, BmobException e) {
                if(e==null){
                    for(int i=0;i<list.size();i++){
                        Food f=new Food(list.get(i).getFood_price(),list.get(i).getFood_name(),0);
                        food.add(f);
                    }

                     adapter=new FoodAdapter(FoodContentActivity.this,R.layout.custom_food_content,food);
                    listView.setAdapter(adapter);
                }
                else{
                    Log.e("error",""+e);
                }
            }
        });

    }
    //按返回的时候，将已经点的数据传到云端
    @Override
    public void onBackPressed() {
        List<BmobObject> going_order = new ArrayList<BmobObject>();
        for (int i = 0; i < orderedfood.size(); i++) {
            GoingOrder g=new GoingOrder(orderedfood.get(i).getFood_price(),orderedfood.get(i).getFood_name(),orderedfood.get(i).getFood_count(),type,table_num,false);
           going_order.add(g);
        }
        new BmobBatch().insertBatch(going_order).doBatch(new QueryListListener<BatchResult>() {

            @Override
            public void done(List<BatchResult> o, BmobException e) {
                if(e==null){
                    for(int i=0;i<o.size();i++){
                        BatchResult result = o.get(i);
                        BmobException ex =result.getError();
                        if(ex==null){
                           Log.e("add_success","第"+i+"个数据批量添加成功："+result.getCreatedAt()+","+result.getObjectId()+","+result.getUpdatedAt());
                        }else{
                            Log.e("add_success","第"+i+"个数据批量添加失败："+ex.getMessage()+","+ex.getErrorCode());
                        }
                    }
                    Toast.makeText(FoodContentActivity.this, "已加入添加成功，在购物车等亲~", Toast.LENGTH_SHORT).show();

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
        orderedfood.clear();
        adapter=null;
        finish();
    }

}
