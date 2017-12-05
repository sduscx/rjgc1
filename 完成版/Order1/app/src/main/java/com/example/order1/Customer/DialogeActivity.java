package com.example.order1.Customer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
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
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;

/**
 * Created by 张宇 on 2017/8/19.
 */

//顾客商家对话的界面
public class DialogeActivity extends AppCompatActivity {
    private String table_num;
    private String monitorStr=null;

    //显示的对话界面
    private ListView msgList;
    private List<Message> dialoges=new ArrayList<>();
    MessageAdapter adapter=null;
    Button  input_btn;
    EditText input_edit;

    //即将发送的消息
    private String str=null;
    Message newMsg=null;
    boolean ifDia=false;
    BmobRealTimeData rtd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagloge_activity);
        table_num=getIntent().getStringExtra("table_num");

        //查询是否有数据
        msgList=(ListView)findViewById(R.id.msg_list);
        queryMessage();

        //初始化输入框和输入按钮
        input_btn=(Button)findViewById(R.id.msg_input_btn);
        input_edit=(EditText)findViewById(R.id.msg_input_edit);
        input_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str=input_edit.getText().toString();
                //发送的消息不能为
                if(str.length()==0){
                    Toast.makeText(DialogeActivity.this, "您输入的内容为空！", Toast.LENGTH_SHORT).show();
                }else{
                    //修改数据
                    newMsg=new Message(str,"custom",new Date());
                    //判断是不是新数据
                    //是新数据
                    if(monitorStr==null){
                        final Dialoge newDia=new Dialoge();
                        newDia.setTable_num(table_num);
                        newDia.add("message",newMsg);
                        newDia.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e==null){
                                    monitorStr=newDia.getObjectId();
                                    //重新刷新listview
                                    input_edit.getText().clear();
                                    dialoges.add(newMsg);

                                    newMsg=null;
                                    if(adapter!=null){
                                        adapter.notifyDataSetChanged();

                                    }else{
                                        adapter=new MessageAdapter(DialogeActivity.this,R.layout.msg_custom,dialoges);
                                        msgList.setAdapter(adapter);
                                    }
                                    //开始监听
                                     startListen();

                                }else{
                                    Toast.makeText(DialogeActivity.this, "发送失败请稍后重试", Toast.LENGTH_SHORT).show();
                                    Log.e("对话wu","上传失败");
                                }
                            }
                        });
                    }
                //不是新数据
                    else{
                        Dialoge newDia=new Dialoge();
                        newDia.add("message",newMsg);
                        newDia.update(monitorStr, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    input_edit.getText().clear();
                                    dialoges.add(newMsg);
                                    Log.e("添加","新的"+newMsg.getRequest());
                                    adapter.notifyDataSetChanged();
                                    msgList.setAdapter(adapter);
                                    Log.e("添加",""+newMsg.getRequest());
                                    newMsg=null;
                                }else{
                                    Toast.makeText(DialogeActivity.this, "发送失败请稍后重试", Toast.LENGTH_SHORT).show();
                                    Log.e("对话you","上传失败");
                                }
                            }
                        });
                    }
                }
            }
        });

    }
    public void queryMessage(){
        BmobQuery<Dialoge> message=new BmobQuery<>();
        message.addWhereEqualTo("table_num",table_num);
        message.findObjects(new FindListener<Dialoge>() {
            @Override
            public void done(List<Dialoge> list, BmobException e) {
                if(e==null){
                    if(list.size()==1){
                        //区分第一条数据和别的数据，在别的地方监听这一行数据。向Main传递一个参数，参数正确重新监听Message表
                        monitorStr=list.get(0).getObjectId();
                        dialoges=list.get(0).getMessage();
                        adapter=new MessageAdapter(DialogeActivity.this,R.layout.msg_custom,dialoges);
                        msgList.setAdapter(adapter);
                        //开始监听
                       startListen();
                    }
                }else{
                    Log.e("对话",""+e);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //告诉Main，开始监听对话表
        MainActivity.ifDia=true;
        MainActivity.monitorStr=monitorStr;
        rtd.unsubRowUpdate("Dialoge",monitorStr);
        finish();
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
                    Log.e("bmob", "数据"+js.getString("message"));
                    //最后一条数据肯定是新的
                    JSONArray newjs= js.getJSONArray("message");
                    JSONObject lastJs=newjs.getJSONObject(newjs.length()-1);
                  //  Log.e("聊天",""+lastJs.getString("request"));
                    //发送通知
                  //添加到adapter中
                    if(lastJs.getString("type").equals("shop")){
                        Message newMsgs=new Message(lastJs.getString("request"),"shop",lastJs.getString("dateStr"));
                        dialoges.add(newMsgs);
                        //msgList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(msgList);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
