package com.example.order1.Customer.receive;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.order1.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class ReActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private WifiManager wifiManager;
    List<ScanResult> list;
    List<String> wifi_name=new ArrayList<>();
    ListView wifi_list;

    private String table_name="1";
    private String party_type=null;
    private String bef_etChange=null;
    private String type_name=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.re_activity_re);









        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.re, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.add_dish) {
           add_dish();
        } else if (id == R.id.search_dish) {

           Intent intent=new Intent(ReActivity.this,RDish.class);
            startActivity(intent);
        } else if (id == R.id.about_stores) {
            add_about_store();
        } else if (id == R.id.create_table) {
            createRoom();
        } else if (id == R.id.setwifi) {
           wifi_set();
        }else if (id == R.id.comment) {
          Intent intent =new Intent(ReActivity.this,ComplainActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.main_layout,fragment);
        transaction.commit();
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
                            Toast.makeText(ReActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(ReActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ReActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ReActivity.this, "上传失败，请稍后操作", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.setNeutralButton("取消",null);
        builder.show();
    }
    public void createRoom(){
        final LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.r_createroom_dialoge, null);
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
                                            Toast.makeText(ReActivity.this, "创建成功,您的密码是"+password, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(ReActivity.this, "该房间已被创建，请重新创建房间...", Toast.LENGTH_SHORT).show();
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
    //设置本店wifi的方法
    public void wifi_set(){
        final LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.r_set_wifi_dialoge, null);
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
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(ReActivity.this,android.R.layout.simple_list_item_single_choice,wifi_name);
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
                                                        Toast.makeText(ReActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ReActivity.this, "您两次输入的密码不一致，请重新输入...", Toast.LENGTH_SHORT).show();
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

}
