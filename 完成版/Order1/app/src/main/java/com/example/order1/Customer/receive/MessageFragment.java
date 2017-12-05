package com.example.order1.Customer.receive;


import android.content.Intent;
import android.os.Bundle;
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


    Button input_btn;
    EditText input_edit;
    public MessageFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_message, container, false);
        final ListView list_peo=(ListView)v.findViewById(R.id.message_peo);


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
                Intent intent =new Intent(getActivity(),RDialoge.class);
                intent.putExtra("table_num",Peo_list.get(position).getTable_num());
                startActivity(intent);




            }
        });


        return v;

    }



}
