package com.example.receive;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 */

//该碎片显示顾客的反馈信息
public class ComplianFragment extends Fragment {

List<Evaluate> evaluates=new ArrayList<>();
    public ComplianFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_complian, container, false);
        final ListView complain_list=(ListView)v.findViewById(R.id.complain_list);
        BmobQuery<Evaluate> query=new BmobQuery<Evaluate>();
        query.findObjects(new FindListener<Evaluate>() {
            @Override
            public void done(List<Evaluate> list, BmobException e) {
                if(e==null){
                    for(int i=0;i<list.size();i++){
                        Evaluate evaluate=new Evaluate(list.get(i).getTable(),list.get(i).getStore(),list.get(i).getStore_con(),list.get(i).getStore_spic()
                        ,list.get(i).getDish(),list.get(i).getDish_con(),list.get(i).getDish_spic(),list.get(i).getCreatedAt());
                        evaluates.add(evaluate);
                    }
                    Collections.reverse(evaluates);
                    ComplainAdapter adapter=new ComplainAdapter(getActivity(),R.layout.complian_item,evaluates);
                    complain_list.setAdapter(adapter);
                }
            }
        });

        return v;
    }

}
