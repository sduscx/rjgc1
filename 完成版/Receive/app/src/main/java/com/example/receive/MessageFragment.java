package com.example.receive;


import android.os.*;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
//消息碎片分为左右两个部分，左侧为 联系人，右侧为联系人与商家的对话界面
public class MessageFragment extends Fragment {
    //联系人以及联系人发来的信息
    List<Dialoge> Peo_list=new ArrayList<>();

    //联系人名列表
    List<String> PeoName_list=new ArrayList<>();

    //联系人信息列表
    List<Message> Msg_list=new ArrayList<>();
    MessageAdapter messageAdapter;
    Message newMsg=null;
    private String str=null;
    private String monitorStr=null;
    static String table_num=null;
    BmobRealTimeData rtd=null;

    Button input_btn;
    EditText input_edit;
    RelativeLayout relativeLayout;
    public MessageFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_message, container, false);
        relativeLayout=(RelativeLayout) v.findViewById(R.id.relative);
        final ListView list_peo=(ListView)v.findViewById(R.id.message_peo);
        final ListView list_msg=(ListView)v.findViewById(R.id.message_info);

        //查询表中数据
        BmobQuery<Dialoge> query=new BmobQuery<Dialoge>();
        query.findObjects(new FindListener<Dialoge>() {
            @Override
            public void done(List<Dialoge> list, BmobException e) {
                if (e == null) {
                   Peo_list=list;
                    for(int i=0;i<list.size();i++){
                        PeoName_list.add(list.get(i).getTable_num());
                    }
                    ArrayAdapter<String> StringAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,PeoName_list);
                    list_peo.setAdapter(StringAdapter);
                }else{
                    Log.e("对话",""+e);
                }
            }
        });

        list_peo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                messageAdapter=null;
                if(relativeLayout.getVisibility()==View.GONE){
                    relativeLayout.setVisibility(View.VISIBLE);
                }
                Msg_list=Peo_list.get(position).getMessage();
                monitorStr=Peo_list.get(position).getObjectId();
                messageAdapter=new MessageAdapter(getActivity(),R.layout.message_shop,Msg_list);
                list_msg.setAdapter(messageAdapter);
                table_num=PeoName_list.get(position);
                //开始监听
                if(rtd==null){
                    startListen();
                }else{
                    //先取消监听这个
                    rtd.unsubRowUpdate("Dialoge",monitorStr);
                    startListen();
                }


            }
        });

        input_btn=(Button)v.findViewById(R.id.msg_input_btn);
        input_edit=(EditText)v.findViewById(R.id.msg_input_edit);
        input_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str=input_edit.getText().toString();
                if(str.length()==0){
                    Toast.makeText(getActivity(), "您输入的内容为空！", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getActivity(), "发送失败请稍后重试", Toast.LENGTH_SHORT).show();
                                    Log.e("对话you","上传失败"+e);
                                }
                            }
                        });

                }
            }
        });
        return v;

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
