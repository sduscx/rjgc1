package com.example.order1.Customer.receive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.order1.Customer.Evaluate;
import com.example.order1.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ComplainActivity extends AppCompatActivity {
    List<Evaluate> evaluates=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_fragment_complian);
        final ListView complain_list=(ListView)findViewById(R.id.complain_list);
        BmobQuery<Evaluate> query=new BmobQuery<Evaluate>();
        query.findObjects(new FindListener<Evaluate>() {
            @Override
            public void done(List<Evaluate> list, BmobException e) {
                if(e==null){
                    for(int i=0;i<list.size();i++){
                        Evaluate evaluate=new Evaluate(list.get(i).getTable(),list.get(i).getStore(),list.get(i).getStore_con(),list.get(i).getStore_spic()
                                ,list.get(i).getDish(),list.get(i).getDish_con(),list.get(i).getDish_spic(),list.get(i).getCreatedAt());
                        evaluates.add(evaluate);
                    }
                    Collections.reverse(evaluates);
                    ComplainAdapter adapter=new ComplainAdapter(ComplainActivity.this,R.layout.r_complian_item,evaluates);
                    complain_list.setAdapter(adapter);
                }
            }
        });
    }
}
