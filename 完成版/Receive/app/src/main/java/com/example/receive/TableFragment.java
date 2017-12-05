package com.example.receive;




import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class TableFragment extends Fragment implements View.OnClickListener{

    Button table_all;
    Button table_hall;
    Button table_second;
    Button table_third;
  static   List<Table> tables_hall=new ArrayList<>();
 static    List<Table> tables_third=new ArrayList<>();
   static List<Table> tables_second=new ArrayList<>();
  static   List<Table> tables_all=new ArrayList<>();
    List<String> table_all_ID=new ArrayList<>();
    List<String> table_hall_ID=new ArrayList<>();
    List<String> table_second_ID=new ArrayList<>();
    List<String> table_third_ID=new ArrayList<>();

    GridView grid;
   static TableAdapter adapter=null;
    static String before_button="全部";
    static Table t_detail=null;
    static Table t_delete=null;
    String str_delete_id=null;
    List<String> strs_delete_id=new ArrayList<>();
    LinearLayout table_linear;
    TextView tv_number;
    TextView tv_peonumber;
    TextView tv_info;
    TextView tv_password;

    LinearLayout table_linear_change;
    TextView table_change_number;
    EditText et_peonumber;
    EditText et_info;
    Spinner floor;
    Spinner floor_hall;
    Spinner floor_second;
    Spinner floor_third;
    String table_name="1号桌";
    Button table_change_OK ;
    Button table_change_not_OK;
    Button table_delete;
    int t_deleteposition=0;
    View gridseleteView;
    List<GoingOrder> goingOrders=new ArrayList<>();
    private int priceOfAll=0;
    private List<OrderFood> hasOrder=new ArrayList<>();
    public TableFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_table, container, false);
        table_all=(Button)v.findViewById(R.id.table_all);
        table_hall=(Button)v.findViewById(R.id.table_hall);
        table_second=(Button)v.findViewById(R.id.table_second);
        table_third=(Button)v.findViewById(R.id.table_third);
        grid=(GridView)v.findViewById(R.id.table_grid);
        table_all.setOnClickListener(this);
        table_hall.setOnClickListener(this);
        table_second.setOnClickListener(this);
        table_third.setOnClickListener(this);
        initTableLinear(v);
        changeTableInfo(v);
        query_all();

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (before_button){
                    case "全部":
                        t_detail=tables_all.get(position);

                        break;
                    case "大厅":
                        t_detail=tables_hall.get(position);

                        break;
                    case "二楼":
                        t_detail=tables_second.get(position);

                        break;
                    case "三楼":
                        t_detail=tables_third.get(position);
                        break;

                }
                Log.e("短暂点击","执行"+t_detail.getTable_num());
                BmobQuery<OrderFood> query=new BmobQuery<OrderFood>();
                query.addWhereEqualTo("table_num",t_detail.getTable_num());
                query.findObjects(new FindListener<OrderFood>() {
                    @Override
                    public void done(List<OrderFood> list, BmobException e) {
                        if(e==null){
                            hasOrder=list;
                                for(int i=0;i<list.size();i++){
                                    goingOrders.addAll(list.get(i).getGoingOrder());
                                    for(int j=0;j<list.get(i).getGoingOrder().size();j++){
                                        priceOfAll+=list.get(i).getGoingOrder().get(j).getFood_price()*list.get(i).getGoingOrder().get(j).getFood_count();
                                    }
                                }

                            room_dish_info();

                        }else{
                            Log.e("查询",""+e);
                        }
                    }
                });

            }
        });
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundResource(R.color.grid_choose);
                gridseleteView=view;
                table_linear.setVisibility(View.VISIBLE);
                switch (before_button){
                    case "全部":
                        t_delete=tables_all.get(position);
                        break;
                    case "大厅":
                        t_delete=tables_hall.get(position);
                        break;
                    case "二楼":
                        t_delete=tables_second.get(position);
                        break;
                    case "三楼":
                        t_delete=tables_third.get(position);
                        break;
                }
                t_deleteposition=position;
                initTableDate();
                return true;
            }
        });
        return v;
    }

    public void initTableLinear(View v){
        table_linear =(LinearLayout)v.findViewById(R.id.table_linear_content);
        tv_password=(TextView)v.findViewById(R.id.table_password);
        tv_number=(TextView)v.findViewById(R.id.table_linear_number);
        tv_peonumber=(TextView)v.findViewById(R.id.table_peo_number);
        tv_info=(TextView)v.findViewById(R.id.table_info);
        Button cancel_btn=(Button)v.findViewById(R.id.table_not_OK);
       // Button ok_btn=(Button)v.findViewById(R.id.table_OK);
        Button modify_btn=(Button)v.findViewById(R.id.table_change);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table_linear.setVisibility(View.GONE);
                gridseleteView.setBackgroundResource(R.color.grid_nochoose);
               //grid.getSelectedView().setBackgroundResource(R.color.grid_nochoose);
            }
        });
        modify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table_linear.setVisibility(View.GONE);
                table_linear_change.setVisibility(View.VISIBLE);
                initTableChangeDate();
            }
        });
    }
    public void initTableDate(){
        tv_password.setText("密码为："+t_delete.getPassword());
        tv_number.setText("桌号"+t_delete.getTable_num());
        table_name=t_delete.getTable_num();
        tv_peonumber.setText("人数        "+t_delete.getPeople());
        tv_info.setText("还没设置");
    }
    public void changeTableInfo(View v){
        table_linear_change =(LinearLayout)v.findViewById(R.id.table_linear_change);
       // et_number=(EditText)v.findViewById(R.id.table_linear_number_et);
        table_change_number=(TextView)v.findViewById(R.id.table_change_number);
        et_peonumber=(EditText)v.findViewById(R.id.table_peo_number_et);
        et_info=(EditText)v.findViewById(R.id.table_info_et);
         floor=(Spinner)v.findViewById(R.id.floor_spin_change);
        floor_hall=(Spinner)v.findViewById(R.id.floorcon_hall_change);
          floor_second=(Spinner)v.findViewById(R.id.floorcon_second_change);
         floor_third=(Spinner)v.findViewById(R.id.floorcon_third_change);
         table_change_OK=(Button)v.findViewById(R.id.table_change_OK);
        table_change_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Table change_table=new Table();
                change_table.setTable_info(et_info.getText().toString());
                change_table.setPeople(Integer.parseInt(et_peonumber.getText().toString()));
                change_table.setTable_num(table_name);
                String strObjectID="";
                    switch (before_button){
                        case "全部":
                            strObjectID=table_all_ID.get(t_deleteposition);
                            break;
                        case "大厅":
                            strObjectID=table_hall_ID.get(t_deleteposition);
                            break;
                        case "二楼":
                            strObjectID=table_second_ID.get(t_deleteposition);
                            break;
                        case "三楼":
                            strObjectID=table_third_ID.get(t_deleteposition);
                            break;

                }
                change_table.update(strObjectID, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.e("更新","成功");
                            table_linear_change.setVisibility(View.GONE);
                            gridseleteView.setBackgroundResource(R.color.grid_nochoose);
                            table_name="1号桌";
                            t_deleteposition=0;
                            //这里需要对GoingOrder进行修改
                        }
                        else{
                            Log.e("更新失败",""+e);
                        }
                    }
                });
            }
        });
        table_change_not_OK=(Button)v.findViewById(R.id.table_change_not_OK);
        table_change_not_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 table_linear_change.setVisibility(View.GONE);
                t_delete=null;
            }
        });
        table_delete=(Button)v.findViewById(R.id.table_delete);
        table_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除三个表中数据，更新adapter
                deleteTable();
                switch (before_button){
                    case "全部":
                      tables_all.remove(t_delete);
                        break;
                    case "大厅":
                   tables_hall.remove(t_delete);
                        break;
                    case "二楼":
                     tables_second.remove(t_delete);
                        break;
                    case "三楼":
                    tables_third.remove(t_delete);
                        break;

                }
                adapter.notifyDataSetChanged();
                table_linear.setVisibility(View.GONE);
            }
        });
    }
    public void deleteTable(){
        //删除table表
        //先查询，在删除
        Table table_d=new Table();
        table_d.delete(t_delete.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.e("Table",",success");
                }else{
                    Log.e("Table",",defeat");

                }
            }
        });
       //删除Hurry表
        BmobQuery<Hurry> hurry_query=new BmobQuery<>();
        hurry_query.addWhereEqualTo("table_num",table_name);
        hurry_query.findObjects(new FindListener<Hurry>() {
            @Override
            public void done(List<Hurry> list, BmobException e) {
                if(e==null){
                    if(list.size()==0){
                        Log.e("Hurry","没催单");
                    }else{
                        //在这删除
                        BmobBatch batch =new BmobBatch();
                        List<BmobObject>  hurrys= new ArrayList<BmobObject>();
                        hurrys.addAll(list);
                        batch.deleteBatch(hurrys);
                        batch.doBatch(new QueryListListener<BatchResult>(){

                            @Override
                            public void done(List<BatchResult> results, BmobException ex) {
                                if(ex==null){
                                   Log.e("删除Hurry","成功");
                                }else{
                                    Log.i("Hurry","失败："+ex.getMessage()+","+ex.getErrorCode());
                                }
                            }
                        });
                    }
                }else{
                    Log.e("hurry",""+e);
                }
            }
        });
        //删除Dialoge
        BmobQuery<Dialoge> dialoge_query=new BmobQuery<>();
        dialoge_query.addWhereEqualTo("table_num",table_name);
        dialoge_query.findObjects(new FindListener<Dialoge>() {
            @Override
            public void done(List<Dialoge> list, BmobException e) {
                if(e==null){
                    if(list.size()==0){
                        Log.e("Dialoge","没对话");
                    }else{
                        //在这删除
                        BmobBatch batch =new BmobBatch();
                        List<BmobObject>  dialoges= new ArrayList<BmobObject>();
                        dialoges.addAll(list);
                        batch.deleteBatch(dialoges);
                        batch.doBatch(new QueryListListener<BatchResult>(){

                            @Override
                            public void done(List<BatchResult> results, BmobException ex) {
                                if(ex==null){
                                    Log.e("删除Dialoge","成功");
                                }else{
                                    Log.i("Dialpge","失败："+ex.getMessage()+","+ex.getErrorCode());
                                }
                            }
                        });
                    }
                }
            }
        });
        //删除orderedFood表
                        //在这删除
        BmobQuery<OrderFood> orderFoodBmobQuery=new BmobQuery<>();
        orderFoodBmobQuery.addWhereEqualTo("table_num",table_name);
        orderFoodBmobQuery.findObjects(new FindListener<OrderFood>() {
            @Override
            public void done(List<OrderFood> list, BmobException e) {
                if(e==null){
                        //在这删除
                        BmobBatch batch =new BmobBatch();
                        List<BmobObject>  orders= new ArrayList<BmobObject>();
                        orders.addAll(list);
                        batch.deleteBatch(orders);
                        batch.doBatch(new QueryListListener<BatchResult>(){

                            @Override
                            public void done(List<BatchResult> results, BmobException ex) {
                                if(ex==null){
                                    Log.e("删除OrderFood","成功");
                                }else{
                                    Log.i("OrderFood","失败："+ex.getMessage()+","+ex.getErrorCode());
                                }
                            }
                        });

                }
            }
        });

    }

    public void initTableChangeDate(){
       // et_number.setText(""+t_delete.getTable_num());
        table_change_number.setText(t_delete.getTable_num());
        et_peonumber.setText(""+t_delete.getPeople());
        et_info.setText("还没设置");
        floor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch ((String)floor.getSelectedItem()){
                    case "大厅":
                        floor_hall.setVisibility(View.VISIBLE);
                        floor_hall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                table_name=(String)floor_hall.getSelectedItem();
                                table_change_number.setText("桌号"+table_name);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        floor_second.setVisibility(View.GONE);
                        floor_third.setVisibility(View.GONE);
                        break;
                    case "二楼":
                        floor_hall.setVisibility(View.GONE);
                        floor_second.setVisibility(View.VISIBLE);
                        floor_third.setVisibility(View.GONE);
                        table_name="沧浪201";
                        floor_second.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                table_name=(String)floor_second.getSelectedItem();
                                table_change_number.setText("桌号"+table_name);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case "三楼":
                        floor_hall.setVisibility(View.GONE);
                        floor_second.setVisibility(View.GONE);
                        floor_third.setVisibility(View.VISIBLE);
                        table_name="维也纳301";
                        floor_third.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                table_name=(String)floor_third.getSelectedItem();
                                table_change_number.setText("桌号"+table_name);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

  public void query_all(){
      Log.e("query","kaishi");
        if(tables_all.size()!=0){
            tables_all.clear();
        }
        BmobQuery<Table> query=new BmobQuery<>();

        query.findObjects(new FindListener<Table>() {
            @Override
            public void done(List<Table> list, BmobException e) {
                if(e==null){
                    Log.e("daxiao",""+list.size());
                    for(int i=0;i<list.size();i++){
                      //  Table t_temp=new Table(list.get(i).getTable_num(),false,list.get(i).getTable_price());
                        Table t_temp=list.get(i);
                        if(t_temp.getTable_num()!=null){
                            tables_all.add(t_temp);
                            table_all_ID.add(list.get(i).getObjectId());
                        }

                    }
                        adapter=new TableAdapter(getActivity(),R.layout.grid_item,tables_all);
                        grid.setAdapter(adapter);

                }
                else{
                    Log.e("error",""+e);
                }
            }
        });
    }
    public  void query_table(String floor_name){
        final String floor_n=floor_name;
        BmobQuery<Table> query=new BmobQuery<>();
        query.addWhereEqualTo("floor",floor_n);
        query.findObjects(new FindListener<Table>() {
            @Override
            public void done(List<Table> list, BmobException e) {
                if(e==null){
                    switch (floor_n){
                        case "大厅":
                            if(list.size()!=0){
                                tables_hall.clear();
                                for (int i=0;i<list.size();i++){
                                    Table t_temp=list.get(i);
                                    tables_hall.add(t_temp);
                                    table_hall_ID.add(list.get(i).getObjectId());
                                }
                                adapter=new TableAdapter(getActivity(),R.layout.grid_item,tables_hall);
                                grid.setAdapter(adapter);
                            }
                            else{
                                grid.setAdapter(adapter);
                            }
                            break;
                        case "二楼":
                            if(list.size()!=0){
                                tables_second.clear();
                                for (int i=0;i<list.size();i++){
                                    Table t_temp=list.get(i);
                                    tables_second.add(t_temp);
                                    table_second_ID.add(list.get(i).getObjectId());

                                }
                                adapter=new TableAdapter(getActivity(),R.layout.grid_item,tables_second);
                                grid.setAdapter(adapter);
                            }
                            else{
                                grid.setAdapter(adapter);
                            }
                            break;
                        case "三楼":
                            if(list.size()!=0){
                                tables_third.clear();
                                for (int i=0;i<list.size();i++){
                                    Table t_temp=list.get(i);
                                    tables_third.add(t_temp);
                                    table_third_ID.add(list.get(i).getObjectId());
                                }
                                adapter=new TableAdapter(getActivity(),R.layout.grid_item,tables_third);
                                grid.setAdapter(adapter);
                            }
                            else{
                                grid.setAdapter(adapter);
                            }

                            break;
                    }


                }
            }
        });
    }
    public void room_dish_info(){
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View textEntryView = factory.inflate(R.layout.room_to_dish, null);
        final ListView room_dish_list=(ListView)textEntryView.findViewById(R.id.room_dish_list);
        final TextView room_dish_text=(TextView)textEntryView.findViewById(R.id.dig_text);
        room_dish_text.setText("￥"+priceOfAll+"元");
        SmallFoodAdapter small_adapter=new SmallFoodAdapter(getActivity(),R.layout.dig_room_dish_show,goingOrders);
        room_dish_list.setAdapter(small_adapter);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(textEntryView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goingOrders.clear();
                priceOfAll=0;
            }
        });
        builder.show();



    }
    public void setColorOfBut(String table){
        switch (before_button){
            case "全部":
                table_all.setTextColor(getResources().getColor(R.color.table_unchoose));
                break;
            case "大厅":
                table_hall.setTextColor(getResources().getColor(R.color.table_unchoose));
                break;
            case "二楼":
                table_second.setTextColor(getResources().getColor(R.color.table_unchoose));
                break;
            case "三楼":
                table_third.setTextColor(getResources().getColor(R.color.table_unchoose));
                break;
        }
        before_button=table;
        switch (table){
            case "全部":
                table_all.setTextColor(getResources().getColor(R.color.table_choose));
                break;
            case "大厅":
                table_hall.setTextColor(getResources().getColor(R.color.table_choose));
                break;
            case "二楼":
                table_second.setTextColor(getResources().getColor(R.color.table_choose));
                break;
            case "三楼":
                table_third.setTextColor(getResources().getColor(R.color.table_choose));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.table_all:
                if(!"全部".equals(before_button)){
                    setColorOfBut("全部");
                    adapter=null;
                    query_all();
                }
                break;
            case R.id.table_hall:
                if(!"大厅".equals(before_button)){
                    setColorOfBut("大厅");
                    adapter=null;
                    query_table("大厅");
                }
                break;
            case R.id.table_second:
                if(!"二楼".equals(before_button)){
                    setColorOfBut("二楼");
                    adapter=null;
                    query_table("二楼");
                }
                break;
            case R.id.table_third:
                if(!"三楼".equals(before_button)){
                    setColorOfBut("三楼");
                    adapter=null;
                    query_table("三楼");
                }
                break;
        }
    }

    public static void addTableByMain(Table newTable){
        switch (before_button){
            case "全部":
                tables_all.add(newTable);
                break;
            case "大厅":
                tables_hall.add(newTable);
                break;
            case "二楼":
                tables_second.add(newTable);
                break;
            case "三楼":
                tables_third.add(newTable);
                break;

        }
        adapter.notifyDataSetChanged();

    }
}
