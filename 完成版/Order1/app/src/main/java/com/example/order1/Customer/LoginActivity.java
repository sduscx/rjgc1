package com.example.order1.Customer;

import android.content.DialogInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.order1.Customer.receive.LoadActivity;
import com.example.order1.R;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

//登陆界面
public class LoginActivity extends AppCompatActivity {
    Button login_in;
    Button sign_in;
    EditText phone_number;
    EditText password;
    static boolean ifLoad=false;
    private String table;
    private String password_str;
    String phoneNumber;
    Button go_receive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "d0cd97fda96f5f03ab1fa9f28d690ae9");
        setContentView(R.layout.custom_login);
        go_receive=(Button)findViewById(R.id.go_receive);
        go_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginActivity.this, LoadActivity.class);
                startActivity(intent);
            }
        });

        login_in=(Button)findViewById(R.id.login_in);
        sign_in=(Button)findViewById(R.id.sign_in);
        phone_number=(EditText)findViewById(R.id.login_phonenumber);
        password=(EditText)findViewById(R.id.login_password);
        addListener();

        SharedPreferences setinfo1= PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        phone_number.setText(setinfo1.getString("number",""));
        password.setText(setinfo1.getString("password",""));
    }

    //设置监听
    public void addListener(){
       login_in.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(LoginActivity.this,RegistActivity.class);
               startActivity(intent);
               finish();
           }
       });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifcan_load();
                //buildDialog();
                //这里要改回来
            }
        });
    }

    //判断输入的账号和密码是否正确
    public void ifcan_load(){
        //保证信息完整
        final String password_str=password.getText().toString();
        phoneNumber=phone_number.getText().toString();
        if(password_str.isEmpty()||phoneNumber.isEmpty()){
            Toast.makeText(LoginActivity.this, "请填入完整信息", Toast.LENGTH_SHORT).show();
        }
        else{
            //根据输入的数据到数据库进行查询，判断输入的手机号和密码是否正确
            BmobQuery<Custom> query=new BmobQuery<Custom>();
            query.addWhereEqualTo("phoneNumber",phoneNumber);
            query.findObjects(new FindListener<Custom>() {
                @Override
                public void done(List<Custom> list, BmobException e) {
                    if(list!=null){
                        if(list.size()!=0){
                            for(Custom c:list){//数组里的数据进行遍历
                                if(c.getPhoneNumber().equals(phoneNumber)){
                                    //查到电话号码，进行判断，密码对不对
                                    ifLoad=true;//说明该用户进行了注册
                                    if(c.getPassword().equals(password_str)){
                                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                        buildDialog();
                                        //此处应给前台发送桌号信息，前台通过，进入主界面

                                    }//没找到，即输入密码错误
                                    else {
                                        Toast.makeText(LoginActivity.this, "密码有误，请重新输入", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                }
            });

        }
        ifLoad=false;
    }

    //显示桌号和密码
    public void buildDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View textEntryView = LayoutInflater.from(this).inflate(R.layout.dialoge_into_room, null);
        final EditText input_table_num=(EditText)textEntryView.findViewById(R.id.input_table_num);
        final EditText input_table_pass=(EditText)textEntryView.findViewById(R.id.input_table_pass);
        builder.setView(textEntryView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                table=input_table_num.getText().toString();
                password_str=input_table_pass.getText().toString();
                if(table==null||password_str==null){
                    Toast.makeText(LoginActivity.this, "请填入完整信息，否则，您无法进入主界面...", Toast.LENGTH_SHORT).show();
                }
                else{
                    BmobQuery<Table> query=new BmobQuery<Table>();
                    query.addWhereEqualTo("table_num",table);
                    query.addWhereEqualTo("password",password_str);
                    query.findObjects(new FindListener<Table>() {
                        @Override
                        public void done(List<Table> list, BmobException e) {
                            if(e==null){
                                if(list.size()!=0){
                                    Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                                    intent.putExtra("tableNumber",table);
                                    intent.putExtra("phoneNumber",phoneNumber);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "您输入的房间号有误，请重新输入...", Toast.LENGTH_SHORT).show();
                                    buildDialog();
                                }
                            }
                        }
                    });
                }

            }
        });
        builder.show();

    }
}
