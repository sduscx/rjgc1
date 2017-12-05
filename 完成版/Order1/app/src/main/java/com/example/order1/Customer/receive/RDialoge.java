package com.example.order1.Customer.receive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.order1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;

public class RDialoge extends AppCompatActivity {

    private String str=null;
    private String monitorStr=null;
    static String table_num=null;
    BmobRealTimeData rtd=null;
    List<Message> Msg_list=new ArrayList<>();
   MessageAdapter messageAdapter;
    Button input_btn;
    EditText input_edit;
    Message newMsg=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rdialoge);
        final ListView list_msg=(ListView)findViewById(R.id.message_info);
        table_num=getIntent().getStringExtra("table_num");
        BmobQuery<Dialoge> query=new BmobQuery<>();
        query.addWhereEqualTo("table_num",table_num);
        query.findObjects(new FindListener<Dialoge>() {
            @Override
            public void done(List<Dialoge> list, BmobException e) {
                if(e==null){
                    if(list.size()==1){
                        Msg_list=list.get(0).getMessage();
                        monitorStr=list.get(0).getObjectId();
                    }
                    messageAdapter=new MessageAdapter(RDialoge.this,R.layout.message_shop,Msg_list);
                    list_msg.setAdapter(messageAdapter);
                }
            }
        });


        //开始监听
        if(rtd==null){
            startListen();
        }else{
            //先取消监听这个
            rtd.unsubRowUpdate("Dialoge",monitorStr);
            startListen();
        }
        input_btn=(Button)findViewById(R.id.msg_input_btn);
        input_edit=(EditText)findViewById(R.id.msg_input_edit);
        input_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str=input_edit.getText().toString();
                if(str.length()==0){
                    Toast.makeText(RDialoge.this, "您输入的内容为空！", Toast.LENGTH_SHORT).show();
                }else{
                    //修改数据
                    newMsg=new Message(str,"shop",new Date());
                    //不是新数据
                    Dialoge newDia=new Dialoge();
                    newDia.add("message",newMsg);
                    newDia.update(monitorStr, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                input_edit.getText().clear();
                                Msg_list.add(newMsg);
                                newMsg=null;
                                messageAdapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(RDialoge.this, "发送失败请稍后重试", Toast.LENGTH_SHORT).show();
                                Log.e("对话you","上传失败"+e);
                            }
                        }
                    });

                }
            }
        });
    }

    public void startListen(){
        rtd=new BmobRealTimeData();
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
                    // Log.e("+",""+jsonObject.toString());
                    JSONObject js=new JSONObject(jsonObject.getString("data"));
                    // Log.e("bmob", "数据"+js.getString("message"));
                    //最后一条数据肯定是新的
                    JSONArray newjs= js.getJSONArray("message");
                    JSONObject lastJs=newjs.getJSONObject(newjs.length()-1);
                    //  Log.e("聊天",""+lastJs.getString("request"));
                    //发送通知
                    //添加到adapter中
                    if(lastJs.getString("type").equals("custom")){
                        Message newMsgs=new Message(lastJs.getString("request"),lastJs.getString("type"),lastJs.getString("dateStr"));
                        Msg_list.add(newMsgs);
                        messageAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消监听
        rtd.unsubRowUpdate("Dialoge",monitorStr);
    }
}
