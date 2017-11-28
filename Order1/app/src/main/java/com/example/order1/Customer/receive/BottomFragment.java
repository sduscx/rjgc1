package com.example.order1.Customer.receive;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.order1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomFragment extends Fragment {

   String before_button="桌号";
    Button table_bottom;
    Button friend_bottom;
    Button hurry_bottom;
    public BottomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.r_fragment_bottom, container, false);
        table_bottom=(Button)v.findViewById(R.id.table_bottom);
        friend_bottom=(Button)v.findViewById(R.id.friend_bottom);
        hurry_bottom=(Button)v.findViewById(R.id.query_bottom);
        table_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorOfBut("桌号");
                replaceFragment(new TableFragment());
            }
        });
        friend_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorOfBut("顾客");
                replaceFragment(new MessageFragment());
            }
        });
        hurry_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorOfBut("催单");
                replaceFragment(new HurryFragment());
            }
        });
        replaceFragment(new TableFragment());
        return v;
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.main_layout,fragment);
        transaction.commit();
    }
    public void setColorOfBut(String button_name){
        switch (before_button){
            case "桌号":
                table_bottom.setTextColor(getResources().getColor(R.color.table_unchoose));
                break;
            case "顾客":
                friend_bottom.setTextColor(getResources().getColor(R.color.table_unchoose));
                break;
            case "催单":
                hurry_bottom.setTextColor(getResources().getColor(R.color.table_unchoose));
                break;
        }
        before_button=button_name;
        switch (before_button){
            case "桌号":
                table_bottom.setTextColor(getResources().getColor(R.color.table_choose));
                break;
            case "顾客":
                friend_bottom.setTextColor(getResources().getColor(R.color.table_choose));
                break;
            case "催单":
                hurry_bottom.setTextColor(getResources().getColor(R.color.table_choose));
                break;
        }
    }

}
