package com.example.order1.Customer.receive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.order1.R;

import java.sql.BatchUpdateException;

public class LoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
       final  EditText password=(EditText)findViewById(R.id.re_password);
        Button load_btn=(Button)findViewById(R.id.re_sign_in);
        load_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals("123456")){
                    //注册成功进入主界面
                    Intent intent=new Intent(LoadActivity.this,ReActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
