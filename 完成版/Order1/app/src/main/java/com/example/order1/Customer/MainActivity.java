package com.example.order1.Customer;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.order1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.ValueEventListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //是否进行数据监听，第一次打开Main不需要进行监听
    static boolean ifDia=false;
    //需要监听时监听哪一行数据
    static String monitorStr=null;
    //判断是否进行评价，一个顾客一次就餐只能评价一次
    static  boolean ifConclude=false;
    //主界面上的8个点餐按钮
    Button set_meal;
    Button special;
    Button west;
    Button east;
    Button today_good;
    Button drink;
    Button main_food;
    Button health_dish;
    String table_num;
    String phone_num;
    //主界面上的时间日期显示
    TextView time_day;
    TextView time_week;
    //侧栏滑动上的联取wifi操作
    private WifiManager wifiManager;
    List<ScanResult> wifi_list;
    WifiConnector wac;
    //左侧的桌号
    TextView left_name;
    int scoreInTable=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_main);
        //接受注册登陆界面传来的桌号和手机号
        table_num = getIntent().getStringExtra("tableNumber");
        phone_num=getIntent().getStringExtra("phoneNumber");
        Log.e("table_num",table_num+phone_num);
        //设置主界面上的时间
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
         String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){
            mWay ="Sunday";
        }else if("2".equals(mWay)){
            mWay ="Monday";
        }else if("3".equals(mWay)){
            mWay ="Tuesday";
        }else if("4".equals(mWay)){
            mWay ="Wednesday";
        }else if("5".equals(mWay)){
            mWay ="Thursday ";
        }else if("6".equals(mWay)){
            mWay ="Friday";
        }else if("7".equals(mWay)){
            mWay ="Saturday";
        }
        time_day=(TextView)findViewById(R.id.time_day);
        time_day.setText(mDay);
        time_week=(TextView)findViewById(R.id.time_week) ;
        time_week.setText(mWay);
        //初始化主界面上的菜名按钮
        set_meal = (Button) findViewById(R.id.set_meal);
        special = (Button) findViewById(R.id.special);
        west = (Button) findViewById(R.id.west);
        east = (Button) findViewById(R.id.east);
        today_good = (Button) findViewById(R.id.today_good);
        drink = (Button) findViewById(R.id.drink);
        main_food = (Button) findViewById(R.id.main_food);
        health_dish=(Button)findViewById(R.id.health_dish);
        //进行侧栏滑动的相关设置
        //工具栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //悬浮窗
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OrderCarActivity.class);
                intent.putExtra("table_num", table_num);
                intent.putExtra("phone_num",phone_num);
                startActivity(intent);
             /*   //显示已经要点，还没点的
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

            }
        });
       //侧栏滑动
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //左侧的自动联取wifi操作
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wac = new WifiConnector(wifiManager);
        wac.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 操作界面
                super.handleMessage(msg);
            }
        };
        //设置左侧的桌号
        left_name=(TextView)findViewById(R.id.number_main);
       // left_name.setText(table_num);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //测试重复调用此方法会不会造成影响
        //如果已经打开过和前台对话界面才进行监听
        if(ifDia){
            //监听表
            final BmobRealTimeData rtd=new BmobRealTimeData();
            rtd.start(new ValueEventListener() {
                @Override
                public void onConnectCompleted(Exception e) {
                    if(e==null){
                        if(rtd.isConnected()){
                            //rtd.subTableUpdate("Message");
                            rtd.subRowUpdate("Dialoge",monitorStr);
                        }
                    }else{
                        Log.e("监听表","失败"+e);
                    }
                }
                @Override
                public void onDataChange(JSONObject jsonObject) {
                    try {
                        Log.e("+",""+jsonObject.toString());
                        JSONObject js=new JSONObject(jsonObject.getString("data"));
                        Log.e("bmob", "数据"+js.getString("message"));
                        //最后一条数据肯定是新的
                        JSONArray newjs= js.getJSONArray("message");
                        JSONObject lastJs=newjs.getJSONObject(newjs.length()-1);
                        Log.e("聊天",""+lastJs.getString("request"));
                        sendNotifycation(js.getString("request"));
                        //发送通知

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //侧栏滑动的几个menu的监听响应问题
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.left_order){
            Intent intent=new Intent(MainActivity.this,OrderActivity.class);
            intent.putExtra("table_num",table_num);
            startActivity(intent);

        } else if (id == R.id.left_wifi) {
            //final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            //progressDialog.setTitle("wifi连接中，请稍后...");
            //progressDialog.setCancelable(true);
            wifi_connect();
            //查询到，并链接到
            //progressDialog.show();
        }
        else if(id==R.id.left_point1) {
           getScoreInStore();


        }else if (id == R.id.left_dialog) {
            Intent intent=new Intent(MainActivity.this,DialogeActivity.class);
            intent.putExtra("table_num",table_num);
            startActivity(intent);

        } else if (id == R.id.left_complain) {
            if(!ifConclude){
                Intent intent = new Intent(MainActivity.this, ComplianActivity.class);
                intent.putExtra("table_num",table_num);
                intent.putExtra("phoneNumber",phone_num);
                startActivity(intent);
            }else{
                Toast.makeText(this, "此次就餐，您已进行评价,期待您的下次光临.", Toast.LENGTH_SHORT).show();
            }

        }  else if(id==R.id.ask_quike){
            Toast.makeText(this, "催单了", Toast.LENGTH_SHORT).show();
            askQuick();
        } else if(id==R.id.left_about){
            //查询关于本店
            aboutStore();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //几个点餐按钮的监听
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_meal:
                Intent intent1 = new Intent(MainActivity.this, FoodContentActivity.class);
                intent1.putExtra("type", set_meal.getText().toString());
                intent1.putExtra("table_num", table_num);
                startActivity(intent1);
                break;
            case R.id.special:
                Intent intent2 = new Intent(MainActivity.this, FoodContentActivity.class);
                intent2.putExtra("type", special.getText().toString());
                intent2.putExtra("table_num", table_num);
                startActivity(intent2);
                break;
            case R.id.west:
                Intent intent3 = new Intent(MainActivity.this, FoodContentActivity.class);
                intent3.putExtra("type", west.getText().toString());
                intent3.putExtra("table_num", table_num);
                startActivity(intent3);
                break;
            case R.id.east:
                Intent intent4 = new Intent(MainActivity.this, FoodContentActivity.class);
                intent4.putExtra("type", east.getText().toString());
                intent4.putExtra("table_num", table_num);
                startActivity(intent4);
                break;
            case R.id.drink:
                Intent intent5 = new Intent(MainActivity.this, FoodContentActivity.class);
                intent5.putExtra("type", drink.getText().toString());
                intent5.putExtra("table_num", table_num);
                startActivity(intent5);
                break;
            case R.id.main_food:
                Intent intent6 = new Intent(MainActivity.this, FoodContentActivity.class);
                intent6.putExtra("type", main_food.getText().toString());
                intent6.putExtra("table_num", table_num);
                startActivity(intent6);
                break;
            case R.id.today_good:
                Intent intent7 = new Intent(MainActivity.this, FoodContentActivity.class);
                intent7.putExtra("type", today_good.getText().toString());
                intent7.putExtra("table_num", table_num);
                startActivity(intent7);
                break;
            case R.id.health_dish:
                Intent intent8 = new Intent(MainActivity.this, FoodContentActivity.class);
                intent8.putExtra("type", health_dish.getText().toString());
                intent8.putExtra("table_num", table_num);
                startActivity(intent8);
                break;
        }

    }

    //如果收到商家发来的消息，则发送一个通知
    public void sendNotifycation(String str){
        Intent intent=new Intent(this,DialogeActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification=new NotificationCompat.Builder(this)
                .setContentTitle("商家及时回复了您的消息.")
                .setContentText(str)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();
        manager.notify(1,notification);

    }

    //顾客催单的相应操作
    public void askQuick(){
        Hurry hurry=new Hurry(table_num,new Date());
        hurry.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(MainActivity.this, "商家已收到您的催单，请耐心等待...", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "催单失败，请稍后重试...", Toast.LENGTH_SHORT).show();
                    Log.e("催单失败",""+e);
                }
            }
        });

    }

    //自动联取wifi的操作
    public void wifi_connect(){
        Toast.makeText(this, "wifi连接中，请稍后...", Toast.LENGTH_LONG).show();
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        wifi_list = wifiManager.getScanResults();
        if (wifi_list == null) {
            Toast.makeText(MainActivity.this, "此处信号太差，未检索到相关wifi信号...", Toast.LENGTH_LONG).show();
        } else {
            BmobQuery<Food_Res> query = new BmobQuery<Food_Res>();
            query.addWhereEqualTo("ifwifi", true);
            query.findObjects(new FindListener<Food_Res>() {
                @Override
                public void done(List<Food_Res> list, BmobException e) {
                    if(list.size()!=0){
                        try{
                        for (int i = 0; i < wifi_list.size(); i++) {
                            if (list.get(0).getWifiname().equals(wifi_list.get(i).SSID)) {

                                    wac.connect(list.get(0).getWifiname(), list.get(0).getWifipass(), WifiConnector.WifiCipherType.WIFICIPHER_WPA);
                                    // progressDialog.cancel();
                                    Toast.makeText(MainActivity.this, "wifi连接成功", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }}catch(Exception e2){
                            Toast.makeText(MainActivity.this, "商家还未设置wifi", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        }
    }

    //关于本店
    public void aboutStore(){
        BmobQuery<Food_Res> query = new BmobQuery<Food_Res>();
        query.addWhereEqualTo("ifabout",true);
        query.findObjects(new FindListener<Food_Res>() {
            @Override
            public void done(List<Food_Res> list, BmobException e) {
              //显示对话框
                Log.e("about",list.get(0).getAbout());
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final TextView newtext=new TextView(MainActivity.this);
                newtext.setText(list.get(0).getAbout());
                builder.setView(newtext);
               builder.setPositiveButton("确定",null);
                builder.show();
            }
        });
    }

    //查询在本店的积分
    public void getScoreInStore(){
        BmobQuery<Custom> query=new BmobQuery();
       query.addWhereEqualTo("phoneNumber",phone_num);
        query.findObjects(new FindListener<Custom>() {
            @Override
            public void done(List<Custom> list, BmobException e) {
                if(e==null){
                    scoreInTable= list.get(0).getScore();
                   /* Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/
                    Toast.makeText(MainActivity.this, "您在本店的积分为"+scoreInTable+"分.", Toast.LENGTH_SHORT).show();
                }else{
                    Log.e("查询积分",""+e);
                }
            }
        });

    }

}
