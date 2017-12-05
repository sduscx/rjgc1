package com.example.receive;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    //左边
    Button table;
    Button add_dish;
    Button find_dish;
    Button about;
    Button create_room;
    Button set_wifi;

    Button complain_receive;
    Button custom_msg;

    private String type_name=null;
    private String table_name="1";
    private String party_type=null;
    private String bef_etChange=null;

    //设置本店wifi
    private WifiManager wifiManager;
    List<ScanResult> list;
    List<String> wifi_name=new ArrayList<>();
    ListView wifi_list;

    //用来设置按钮的颜色
    String before_button="已点餐桌";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "d0cd97fda96f5f03ab1fa9f28d690ae9");
        setContentView(R.layout.main);

        //先将界面设置成已经点餐的顾客界面
        replaceFragment(new TableFragment());

        //初始化走左侧按钮
        table=(Button)findViewById(R.id.table);
        add_dish=(Button)findViewById(R.id.add_dish);
        find_dish=(Button)findViewById(R.id.find_dish);
        about=(Button)findViewById(R.id.about);
        create_room=(Button)findViewById(R.id.create_room);
        set_wifi=(Button)findViewById(R.id.set_wifi);

        complain_receive=(Button)findViewById(R.id.complian_receive);
        custom_msg=(Button)findViewById(R.id.custom_message);

        //监听是不是有人进行催单
        final BmobRealTimeData rtd=new BmobRealTimeData();
        rtd.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                if(e==null){
                    if(rtd.isConnected()){
                        rtd.subTableUpdate("Hurry");
                    }
                }else{
                    Log.e("监听表","失败");
                }
            }
            @Override
            public void onDataChange(JSONObject jsonObject) {
             //   Gson gson=new Gson();
                try {
                    JSONObject js=new JSONObject(jsonObject.getString("data"));
                    sendNotifycation(js.getString("table_num")+"在"+js.getString("dateStr")+"进行催单");
                    //进行通知，催单
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        //监听是不是有人发来了信息
        final BmobRealTimeData rtd1=new BmobRealTimeData();
        rtd1.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                if(e==null){
                    if(rtd1.isConnected()){
                        rtd1.subTableUpdate("Dialoge");
                    }
                }else{
                    Log.e("监听表","失败");
                }
            }
            @Override
            public void onDataChange(JSONObject jsonObject) {
                JSONObject js= null;
                try {
                    js = new JSONObject(jsonObject.getString("data"));
                    JSONArray newjs= js.getJSONArray("message");
                    JSONObject lastJs=newjs.getJSONObject(newjs.length()-1);
                    if(lastJs.getString("type").equals("custom")){
                        Toast.makeText(MainActivity.this, "有顾客发来信息，请注意查收", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });

}

    //左侧按钮的监听
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.table:
                setColorOfBut("已点餐桌");
                replaceFragment(new TableFragment());
                break;
            case R.id.add_dish:
                setColorOfBut("添加菜品");
                add_dish();
                break;
            case R.id.find_dish:
                setColorOfBut("查看菜品");
                replaceFragment(new DishFragment());
                break;
            case R.id.about:
                setColorOfBut("关于本店");
                add_about_store();
                break;
            case R.id.create_room:
                setColorOfBut("创建房间");
                createRoom();
                break;
            case  R.id.set_wifi:
                setColorOfBut("设置");
                //先进行扫描，然后显示在对话框
                wifi_set();
                break;
            case R.id.complian_receive:
                setColorOfBut("评价");
                replaceFragment(new ComplianFragment());
                break;
            case R.id.custom_message:
                setColorOfBut("消息");
                //显示发来的内容
                replaceFragment(new MessageFragment());

        }
    }

    //顾客端催单，在商家端显示通知
    public void sendNotifycation(String str){
        Intent intent=new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification=new NotificationCompat.Builder(this)
                .setContentTitle("有顾客进行催单")
                .setContentText(str)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();
        manager.notify(1,notification);

    }

    //设置按钮的颜色
    public void setColorOfBut(String button_name){
        switch (before_button){
            case "已点餐桌":
                table.setTextColor(getResources().getColor(R.color.table_unchoose));
                break;
            case "添加菜品":
                add_dish.setTextColor(getResources().getColor(R.color.table_unchoose));
                break;
            case "查看菜品":
                find_dish.setTextColor(getResources().getColor(R.color.table_unchoose));
                break;
            case "关于本店":
                about.setTextColor(getResources().getColor(R.color.table_unchoose));
                break;
            case "创建房间":
                create_room.setTextColor(getResources().getColor(R.color.table_unchoose));
                break;
            case "设置":
                set_wifi.setTextColor(getResources().getColor(R.color.table_unchoose));
                break;
            case "评价":
                complain_receive.setTextColor(getResources().getColor(R.color.table_unchoose));
                break;
            case "消息":
                custom_msg.setTextColor(getResources().getColor(R.color.table_unchoose));
                break;
        }
        before_button=button_name;
        switch (before_button){
            case "已点餐桌":
                table.setTextColor(getResources().getColor(R.color.table_choose));
                break;
            case "添加菜品":
                add_dish.setTextColor(getResources().getColor(R.color.table_choose));
                break;
            case "查看菜品":
                find_dish.setTextColor(getResources().getColor(R.color.table_choose));
                break;
            case "关于本店":
                about.setTextColor(getResources().getColor(R.color.table_choose));
                break;
            case "创建房间":
                create_room.setTextColor(getResources().getColor(R.color.table_choose));
                break;
            case "设置":
                set_wifi.setTextColor(getResources().getColor(R.color.table_choose));
                break;
            case "评价":
                complain_receive.setTextColor(getResources().getColor(R.color.table_choose));
                break;
            case "消息":
                custom_msg.setTextColor(getResources().getColor(R.color.table_choose));
                break;
        }
    }

    //根据按钮替换右侧不同的碎片
    public void replaceFragment(Fragment fragment){
          FragmentManager fragmentManager=getSupportFragmentManager();
          FragmentTransaction transaction=fragmentManager.beginTransaction();
          transaction.replace(R.id.right_layout,fragment);
          transaction.commit();
    }

    //设置本店wifi的方法
    public void wifi_set(){
        final LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.set_wifi_dialoge, null);
        final EditText pass_first=(EditText)textEntryView.findViewById(R.id.wifi_pass_first);
        final EditText pass_second=(EditText)textEntryView.findViewById(R.id.wifi_pass_second);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        list = wifiManager.getScanResults();
        if (list == null) {
            Toast.makeText(this, "wifi未打开！", Toast.LENGTH_LONG).show();
        }else {
            wifi_list=(ListView)textEntryView.findViewById(R.id.wifi_list);
            for(int i=0;i<list.size();i++){
                if(i<=7){
                    wifi_name.add( list.get(i).SSID);
                }else{
                    break;
                }

            }
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_single_choice,wifi_name);
            wifi_list.setAdapter(adapter);
            wifi_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("请选择本店的公共wifi");
            builder.setView(textEntryView);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(!pass_first.getText().toString().isEmpty()&&pass_first.getText().toString().equals(pass_second.getText().toString())){
                        if(wifi_list.getCheckedItemCount()==1){
                            final String wifiname=wifi_name.get(wifi_list.getCheckedItemPosition());
                            final String wifipass=pass_first.getText().toString();
                            BmobQuery<Food_Res> query=new BmobQuery<Food_Res>();
                            query.addWhereEqualTo("if_wifi",true);
                            query.findObjects(new FindListener<Food_Res>() {
                                @Override
                                public void done(List<Food_Res> list, BmobException e) {
                                    if(e==null){
                                        if(list.size()!=0){
                                            Food_Res f1=new Food_Res();
                                            f1.setWifiname(wifiname);
                                            f1.setWifipass(wifipass);
                                            f1.update(list.get(0).getObjectId(), new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                        Log.e("wifi","修改成功");
                                                        Toast.makeText(MainActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Log.e("wifi","修改失败"+e);
                                                    }
                                                }
                                            });
                                        }
                                        else{
                                            Food_Res newWifi=new Food_Res(wifipass,wifiname,true,false,false);
                                            newWifi.save(new SaveListener<String>() {
                                                @Override
                                                public void done(String s, BmobException e) {
                                                    if(e==null){
                                                        Log.e("wifi","成功");
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "您两次输入的密码不一致，请重新输入...", Toast.LENGTH_SHORT).show();
                        wifi_set();
                    }
                    wifi_name.clear();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    wifi_name.clear();
                }
            });
            builder.show();
        }

    }

    //为顾客创建一个房间
    public void createRoom(){
        final LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.createroom_dialoge, null);
        final Spinner floor=(Spinner)textEntryView.findViewById(R.id.floor_spin);
        final Spinner floor_hall=(Spinner)textEntryView.findViewById(R.id.floorcon_hall);
        final Spinner floor_second=(Spinner)textEntryView.findViewById(R.id.floorcon_second);
        final Spinner floor_third=(Spinner)textEntryView.findViewById(R.id.floorcon_third);
        final EditText input_peo=(EditText)textEntryView.findViewById(R.id.input_people);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(textEntryView);
        floor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch ((String)floor.getSelectedItem()){
                    case "大厅":
                        floor_hall.setVisibility(View.VISIBLE);
                        floor_hall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                table_name=(String)floor_hall.getSelectedItem();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        floor_second.setVisibility(View.GONE);
                        floor_third.setVisibility(View.GONE);
                        break;
                    case "二楼":
                        floor_hall.setVisibility(View.GONE);
                        floor_second.setVisibility(View.VISIBLE);
                        floor_third.setVisibility(View.GONE);
                        table_name="201";
                        floor_second.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                table_name=(String)floor_second.getSelectedItem();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case "三楼":
                        floor_hall.setVisibility(View.GONE);
                        floor_second.setVisibility(View.GONE);
                        floor_third.setVisibility(View.VISIBLE);
                        table_name="301";
                        floor_third.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                table_name=(String)floor_third.getSelectedItem();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final Spinner table_type=(Spinner)textEntryView.findViewById(R.id.party_spin);
        table_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                party_type=(String)table_type.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //先查询这个桌是不是有人了
                BmobQuery<Table> query=new BmobQuery<Table>();
                query.addWhereEqualTo("table_num",table_name);
                query.findObjects(new FindListener<Table>() {
                    @Override
                    public void done(List<Table> list, BmobException e) {
                        if(e==null){
                            if(list.size()==0){
                                //随机生成一个数字
                                final int password=(int)(Math.random()*10000);
                              final   Table table=new Table(table_name,false,0,party_type,(String)floor.getSelectedItem(),Integer.parseInt(input_peo.getText().toString()),""+password);
                                table.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if(e==null){
                                            TableFragment.addTableByMain(table);
                                            Toast.makeText(MainActivity.this, "创建成功,您的密码是"+password, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(MainActivity.this, "该房间已被创建，请重新创建房间...", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                           Log.e("shiwu",""+e);
                        }
                    }
                });


            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                table_name="1号桌";
            }
        });
        builder.show();
    }

    //公布关于本店的一些信息
    public void add_about_store(){
        final LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.adout_store, null);
        final EditText about_sth = (EditText)textEntryView.findViewById(R.id.about_store_et);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(textEntryView);
        final BmobQuery<Food_Res> query=new BmobQuery<Food_Res>();
        query.addWhereEqualTo("ifabout",true);
        query.findObjects(new FindListener<Food_Res>() {
            @Override
            public void done(List<Food_Res> list, BmobException e) {
                if(e==null){
                    if(list.size()!=0){
                        bef_etChange=list.get(0).getAbout();
                        about_sth.setText(bef_etChange);

                    }
                }
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(bef_etChange==null){
                    Food_Res newAbout=new Food_Res();
                    newAbout.setAbout(about_sth.getText().toString());
                    newAbout.setIfhave(false);
                    newAbout.setIfabout(true);
                    newAbout.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if(bef_etChange!=null&&!bef_etChange.equals(about_sth.getText().toString())){
                    final BmobQuery<Food_Res> query=new BmobQuery<Food_Res>();
                    query.addWhereEqualTo("ifabout",true);
                    query.findObjects(new FindListener<Food_Res>() {
                        @Override
                        public void done(List<Food_Res> list, BmobException e) {
                            if(e==null){
                                if(list.size()!=0){

                                    Food_Res f=new Food_Res();
                                    f.setAbout(about_sth.getText().toString());
                                    f.setIfhave(false);
                                    f.setIfabout(true);
                                    f.update(list.get(0).getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            Toast.makeText(MainActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    ;
                                }
                            }
                        }
                    });

                }

            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    //添加本店的菜品
    public void add_dish(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.add_food_dialoge, null);
        final EditText price = (EditText) textEntryView.findViewById(R.id.input_price);
        final EditText name = (EditText)textEntryView.findViewById(R.id.input_name);
        final Spinner type=(Spinner)textEntryView.findViewById(R.id.spinner_food);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type_name=(String)type.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(textEntryView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Food_Res newfood=new Food_Res(name.getText().toString(),Integer.parseInt(price.getText().toString()),type_name,true,null,false);
                newfood.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null){
                            Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "上传失败，请稍后操作", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.setNeutralButton("取消",null);
        builder.show();
    }




}
