package com.example.order1.Customer.receive;


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

import com.example.order1.Customer.Dialoge;
import com.example.order1.Customer.GoingOrder;
import com.example.order1.Customer.Hurry;
import com.example.order1.Customer.OrderFood;
import com.example.order1.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class TableFragment extends Fragment {
  static   List<Table> tables_all=new ArrayList<>();
    List<String> table_all_ID=new ArrayList<>();


    GridView grid;
   static TableAdapter adapter=null;
    static String before_button="全部";
    static Table t_detail=null;
    static Table t_delete=null;
    String str_delete_id=null;
    List<String> strs_delete_id=new ArrayList<>();

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
        View v=inflater.inflate(R.layout.r_fragment_table, container, false);


        grid=(GridView)v.findViewById(R.id.table_grid);
        //changeTableInfo(v);
        query_all();

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (before_button){
                    case "全部":
                        t_detail=tables_all.get(position);
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
                switch (before_button){
                    case "全部":
                        t_delete=tables_all.get(position);
                        break;

                }
                view.setBackgroundResource(R.color.grid_choose);
                gridseleteView=view;
                initTableLinear();

                t_deleteposition=position;
              //  initTableDate();
                return true;
            }
        });
        return v;
    }

    public void initTableLinear(){
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View v = factory.inflate(R.layout.r_room_to_dish_long, null);
        TextView   tv_password=(TextView)v.findViewById(R.id.table_password);
        TextView tv_number=(TextView)v.findViewById(R.id.table_linear_number);
        TextView tv_peonumber=(TextView)v.findViewById(R.id.table_peo_number);
        TextView tv_info=(TextView)v.findViewById(R.id.table_info);
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        tv_password.setText("密码为："+t_delete.getPassword());
        tv_number.setText("桌号"+t_delete.getTable_num());
        table_name=t_delete.getTable_num();
        tv_peonumber.setText("人数        "+t_delete.getPeople());
        tv_info.setText("还没设置");

        builder.setView(v);


        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gridseleteView.setBackgroundResource(R.color.grid_nochoose);
            }
        });

        builder.setPositiveButton("删除本桌", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTable();
                tables_all.remove(t_delete);
                adapter.notifyDataSetChanged();

            }
        });
        builder.show();
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
                        adapter=new TableAdapter(getActivity(),R.layout.r_grid_item,tables_all);
                        grid.setAdapter(adapter);

                }
                else{
                    Log.e("error",""+e);
                }
            }
        });
    }

    public void room_dish_info(){
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View textEntryView = factory.inflate(R.layout.r_room_to_dish, null);
        final ListView room_dish_list=(ListView)textEntryView.findViewById(R.id.room_dish_list);
         TextView room_dish_text=(TextView)textEntryView.findViewById(R.id.dig_text);
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


    public static void addTableByMain(Table newTable){
        switch (before_button){
            case "全部":
                tables_all.add(newTable);
                break;

        }
        adapter.notifyDataSetChanged();

    }
}
