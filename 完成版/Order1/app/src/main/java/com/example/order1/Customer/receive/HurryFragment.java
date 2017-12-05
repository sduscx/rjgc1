package com.example.order1.Customer.receive;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.order1.Customer.Hurry;
import com.example.order1.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class HurryFragment extends Fragment {

List<String> hurrylist=new ArrayList<>();
    String time=null;
    public HurryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_hurry, container, false);
        final ListView listView=(ListView)v.findViewById(R.id.list_hurry);
        BmobQuery<Hurry> query=new BmobQuery<>();

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
       final  String t1=format.format(new Date());
        query.findObjects(new FindListener<Hurry>() {
            @Override
            public void done(List<Hurry> list, BmobException e) {
                if(e==null){
                    if(list.size()==0){
                        Toast.makeText(getActivity(), "还没有顾客进行催单", Toast.LENGTH_SHORT).show();
                    }else{
                        for(int i=0;i<list.size();i++){
                            time=list.get(i).getCreatedAt().substring(0,10);
                            if(time.equals(t1)){
                                String str=list.get(i).getTable_num()+"号顾客"+"在"+list.get(i).getCreatedAt()+"时进行催单";
                                hurrylist.add(str);
                            }
                        }
                    }


                    ArrayAdapter<String> StringAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,hurrylist);
                    listView.setAdapter(StringAdapter);
                }
            }
        });
        return v;
    }

}
