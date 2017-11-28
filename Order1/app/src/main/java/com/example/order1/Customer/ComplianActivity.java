package com.example.order1.Customer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.order1.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by 张宇 on 2017/7/10.
 */


//顾客进行评价的界面
public class ComplianActivity extends AppCompatActivity {


    private String table_num;
    //一个按钮，并且判断它们是否被点击了
    Button unhappy_store;private boolean if_unhappys=false;
    Button happy_store;private boolean if_happys=false;
    Button veryhappy_store;private boolean if_veryhappys=false;
    Button unhappy_dish;private boolean if_unhappyd=false;
    Button happy_dish;private boolean if_happyd=false;
    Button veryhappy_dish;private boolean if_veryhappyd=false;

    LinearLayout storebad;
    LinearLayout storegood;
    LinearLayout dishbad;
    LinearLayout dishgood;

    EditText edit_bad_store;
    EditText edit_good_store;
    EditText edit_bad_dish;
    EditText edit_good_dish;

    Button submit;

    private String store_tem=null;
    private  String dish_tem=null;
    //private String objectId=null;
    private int score=10;
    private String phonenum;

   private int cb_array[][]=
    {
        {
            R.id.store_bad1, R.id.store_bad2, R.id.store_bad3, R.id.store_bad4
        },
        {
            R.id.store_good1, R.id.store_good2, R.id.store_good3, R.id.store_good4
        },
        {
            R.id.dish_bad1, R.id.dish_bad2, R.id.dish_bad3,R.id.dish_bad4,R.id.dish_bad5
        },
        {
                R.id.dish_good1,R.id.dish_good2,R.id.dish_good3,R.id.dish_good4,R.id.dish_good5
        }
    };
    private List<String> content_store=new ArrayList<>();
    private List<String> content_dish=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complain_activity);
        table_num=getIntent().getStringExtra("table_num");
        //对进行评价的顾客的手机号进行积分
       // objectId=getIntent().getStringExtra("phone_objectId");
        phonenum=getIntent().getStringExtra("phoneNumber");

        //实例化控件
        unhappy_store=(Button)findViewById(R.id.unhappy_store);
        happy_store=(Button)findViewById(R.id.happy_store);
        veryhappy_store=(Button)findViewById(R.id.veryhappy_store);
        unhappy_dish=(Button)findViewById(R.id.unhappy_dish);
        happy_dish=(Button)findViewById(R.id.happy_dish);
        veryhappy_dish=(Button)findViewById(R.id.veryhappy_dish);

        storebad=(LinearLayout)findViewById(R.id.store_bad_linear);
        storegood=(LinearLayout)findViewById(R.id.store_good_linear);
        dishbad=(LinearLayout)findViewById(R.id.diah_bad_linear);
        dishgood=(LinearLayout)findViewById(R.id.diah_good_linear);

        edit_bad_store=(EditText)findViewById(R.id.ev_bad_store);
        edit_good_store=(EditText)findViewById(R.id.ev_good_store);
        edit_bad_dish=(EditText)findViewById(R.id.ev_bad_dish);
        edit_good_dish=(EditText)findViewById(R.id.ev_good_dish);

        submit=(Button)findViewById(R.id.submit_evaluation_button);


        for(int i=0;i<cb_array.length;i++){

                for(int j=0;j<cb_array[i].length;j++){

                          ChexkBox_sth(i,j,cb_array[i][j]);
                }


        }
    }
    //实例化所有的多选框
    public void ChexkBox_sth(int i,int j,int checkID){
        final int first=i;
        final CheckBox c=(CheckBox)findViewById(checkID);
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(first==0||first==1){
                        content_store.add(""+c.getText());
                    }
                    else {
                        content_dish.add(""+c.getText());
                    }

                }
                else{
                    if(first==0||first==1){
                        content_store.remove(""+c.getText());
                    }
                    else {
                        content_dish.remove(""+c.getText());
                    }
                }
            }
        });
    }
    //对按钮进行监听
    public void onClick(View v){
        switch (v.getId()){
            case R.id.unhappy_store:
                content_store.clear();
                if(!if_unhappys){
                        if_unhappys=true;
                        if_happys=false;
                        if_veryhappys=false;
                        unhappy_store.setBackgroundResource(R.drawable.unhappy2);
                        happy_store.setBackgroundResource(R.drawable.happy1);
                        veryhappy_store.setBackgroundResource(R.drawable.veryhappy1);
                        storebad.setVisibility(View.VISIBLE);
                        storegood.setVisibility(View.GONE);
                }
                if(!edit_good_store.getText().toString().isEmpty()){
                    edit_bad_store.setText(edit_good_store.getText().toString());
                }
                break;
            case R.id.happy_store:
                content_store.clear();
                if(!if_happys){
                    if_happys=true;
                    if_unhappys=false;
                    if_veryhappys=false;
                    happy_store.setBackgroundResource(R.drawable.happy2);
                    unhappy_store.setBackgroundResource(R.drawable.unhappy1);
                    veryhappy_store.setBackgroundResource(R.drawable.veryhappy1);
                    storebad.setVisibility(View.GONE);
                    storegood.setVisibility(View.VISIBLE);
                }
                if(!edit_bad_store.getText().toString().isEmpty()){
                    edit_good_store.setText(edit_bad_store.getText().toString());
                }


                break;
            case R.id.veryhappy_store:
                content_store.clear();
                if(!if_veryhappys){
                    if_happys=false;
                    if_unhappys=false;
                    if_veryhappys=true;
                    happy_store.setBackgroundResource(R.drawable.happy1);
                    unhappy_store.setBackgroundResource(R.drawable.unhappy1);
                    veryhappy_store.setBackgroundResource(R.drawable.veryhappy2);
                    storebad.setVisibility(View.GONE);
                    storegood.setVisibility(View.VISIBLE);
                }
                if(!edit_bad_store.getText().toString().isEmpty()){
                    edit_good_store.setText(edit_bad_store.getText().toString());
                }

                break;
            case R.id.unhappy_dish:
                content_dish.clear();
                if(!if_unhappyd){
                    if_happyd=false;
                    if_unhappyd=true;
                    if_veryhappyd=false;
                    happy_dish.setBackgroundResource(R.drawable.happy1);
                    unhappy_dish.setBackgroundResource(R.drawable.unhappy2);
                    veryhappy_dish.setBackgroundResource(R.drawable.veryhappy1);
                    dishbad.setVisibility(View.VISIBLE);
                    dishgood.setVisibility(View.GONE);
                }
                if(!edit_good_dish.getText().toString().isEmpty()){
                    edit_bad_dish.setText(edit_good_dish.getText().toString());
                }

                break;
            case R.id.happy_dish:
                content_dish.clear();
                if(!if_happyd){
                    if_happyd=true;
                    if_unhappyd=false;
                    if_veryhappyd=false;
                    happy_dish.setBackgroundResource(R.drawable.happy2);
                    unhappy_dish.setBackgroundResource(R.drawable.unhappy1);
                    veryhappy_dish.setBackgroundResource(R.drawable.veryhappy1);
                    dishbad.setVisibility(View.GONE);
                    dishgood.setVisibility(View.VISIBLE);
                }
                if(!edit_bad_dish.getText().toString().isEmpty()){
                    edit_good_dish.setText(edit_bad_dish.getText().toString());
                }

                break;
            case R.id.veryhappy_dish:
                content_dish.clear();
                if(!if_veryhappyd){
                    if_happyd=false;
                    if_unhappyd=false;
                    if_veryhappyd=true;
                    happy_dish.setBackgroundResource(R.drawable.happy1);
                    unhappy_dish.setBackgroundResource(R.drawable.unhappy1);
                    veryhappy_dish.setBackgroundResource(R.drawable.veryhappy2);
                    dishbad.setVisibility(View.GONE);
                    dishgood.setVisibility(View.VISIBLE);
                }
                if(!edit_bad_dish.getText().toString().isEmpty()){
                    edit_good_dish.setText(edit_bad_dish.getText().toString());
                }

                break;
            case R.id.submit_evaluation_button:
                //判断所有的多选框和按钮的状态，提交到云服务器，并对顾客进行积分
                String store_specific="";
                String dish_specific="";
                String store_taste="中评";
                if(if_unhappys){
                    store_taste="差评";
                }else if(if_happys){
                    store_taste="中评";
                }else if(if_veryhappys){
                    store_taste="好评";
                }else {
                        store_tem=edit_good_store.getText().toString();
                }
                String dish_taste="中评";
                if(if_unhappyd){
                    dish_taste="差评";
                }else if(if_happyd){
                    dish_taste="中评";
                }else if(if_veryhappyd){
                    dish_taste="好评";
                }else {
                    dish_tem=edit_good_dish.getText().toString();
                }

                for(int j=0;j<content_store.size();j++){
                    if(j==0){
                        store_specific+="商店："+content_store.get(0);
                    }else {
                        store_specific+=","+content_store.get(j);
                    }

                }

                for(int j=0;j<content_dish.size();j++){
                    if(j==0){
                        dish_specific+="菜品："+content_dish.get(0);
                    }else {
                        dish_specific+=","+content_dish.get(j);
                    }

                }


                Evaluate e=new Evaluate(table_num,store_taste,store_tem,store_specific,dish_taste,dish_tem,dish_specific);
                e.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null){
                            Toast.makeText(ComplianActivity.this, "上传成功...", Toast.LENGTH_SHORT).show();
                         //   Log.e("id",objectId);


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
                                                        finish();
                                                        MainActivity.ifConclude=true;
                                                        Toast.makeText(ComplianActivity.this, "评价成功...", Toast.LENGTH_SHORT).show();                                                    }else{
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



                            //为顾客添加积分
           /*                 Custom custom=new Custom();
                            custom.increment("score",10);
                            custom.update(objectId, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        finish();
                                        MainActivity.ifConclude=true;
                                        Toast.makeText(ComplianActivity.this, "评价成功...", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(ComplianActivity.this, "未评价成功，请稍后重试...", Toast.LENGTH_SHORT).show();
                                        Log.e("评价出错",""+e);
                                    }
                                }
                            });*/
                        }
                    }
                });
                break;
    }
}}
