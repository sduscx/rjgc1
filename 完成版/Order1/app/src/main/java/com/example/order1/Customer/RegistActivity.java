package com.example.order1.Customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.order1.R;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

//注册界面
public class RegistActivity extends AppCompatActivity {
    Button registe;
    EditText phone_number_registe;
    EditText password_registe;
    EditText password_registe_again;
    static boolean ifRegister=false;
    static boolean ifRight=true;
    private  String table;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "d0cd97fda96f5f03ab1fa9f28d690ae9");
        setContentView(R.layout.custom_registe);
        registe=(Button)findViewById(R.id.registe);
        phone_number_registe=(EditText)findViewById(R.id.registe_phonenumber);
        password_registe=(EditText)findViewById(R.id.registe_password);
        password_registe_again=(EditText)findViewById(R.id.registe_password_again);
        addListener();
    }
    //监听器
    public void addListener(){
       registe.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(RegistActivity.this, "注册中，请稍后", Toast.LENGTH_SHORT).show();
               final  String password_str=password_registe.getText().toString();
               final  String password_again=password_registe_again.getText().toString();
               final String number=phone_number_registe.getText().toString();
               if(password_str.isEmpty()||number.isEmpty()||number.length()!=11){
                   Toast.makeText(RegistActivity.this, "请填入正确信息", Toast.LENGTH_SHORT).show();
                   ifRight=false;
               }
               if(!password_str.equals(password_again)){
                   Toast.makeText(RegistActivity.this, "您两次输入的密码不一致，请重新输入...", Toast.LENGTH_SHORT).show();
                   ifRight=false;
               }
               // Toast.makeText(CustomerLoadActivity.this, "1"+ifRight, Toast.LENGTH_SHORT).show();
               //判断输入的手机号码是否合法
               if(ifRight){
                   for(int i=0;i<number.length();i++){
                       if(number.charAt(i)<'0'||number.charAt(i)>'9'){
                           ifRight=false;
                           Toast.makeText(RegistActivity.this, "您的手机号输入不合法，请重新输入。", Toast.LENGTH_SHORT).show();
                       }
                   }
               }
               if(ifRight){
                   //检验输入的号码是否已经在本店进行注册，如果未曾注册就先注册将数据传到数据库
                   BmobQuery<Custom> query=new BmobQuery<Custom>();
                   query.addWhereEqualTo("phoneNumber",number);
                   query.findObjects(new FindListener<Custom>() {
                       @Override
                       public void done(List<Custom> list, BmobException e) {
                           if(list!=null){
                               for(Custom c:list){
                                   if(c.getPhoneNumber().equals(number)){
                                       //不能重复注册
                                       ifRegister=true;
                                       Toast.makeText(RegistActivity.this, "您已在本店进行注册，请直接登录", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }
                           if(!ifRegister){
                               Custom c=new Custom(number,password_str,10,false);
                               c.save(new SaveListener<String>() {
                                   @Override
                                   public void done(String s, BmobException e) {
                                       Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                      storeInformation(number,password_str);
                                       buildDialog();
                                       //此处应给前台发送桌号信息，前台通过，进入主界面
                                   }
                               });
                           }
                       }
                   });

               }
               ifRegister=false;
               ifRight=true;
           }
       });
    }

    public void storeInformation(String numbers,String pass){
        SharedPreferences setinfo= PreferenceManager.getDefaultSharedPreferences(RegistActivity.this);
        SharedPreferences.Editor editor=setinfo.edit();
        editor.putString("number",numbers);
        editor.putString("password",pass);
        editor.apply();
    }
    //核对信息
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
                password=input_table_pass.getText().toString();
                if(table==null||password==null){
                    Toast.makeText(RegistActivity.this, "请填入完整信息，否则，您无法进入主界面...", Toast.LENGTH_SHORT).show();
                }
                else{
                    BmobQuery<Table> query=new BmobQuery<Table>();
                    query.addWhereEqualTo("table_num",table);
                    query.addWhereEqualTo("password",password);
                    query.findObjects(new FindListener<Table>() {
                        @Override
                        public void done(List<Table> list, BmobException e) {
                            if(e==null){
                                if(list.size()!=0){
                                    Intent intent =new Intent(RegistActivity.this,MainActivity.class);
                                    intent.putExtra("tableNumber",table);
                                    intent.putExtra("phoneNumber",phone_number_registe.getText().toString());
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(RegistActivity.this, "您输入的房间号有误，请重新输入...", Toast.LENGTH_SHORT).show();
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
