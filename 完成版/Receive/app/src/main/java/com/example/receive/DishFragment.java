package com.example.receive;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * A simple {@link Fragment} subclass.
 */

//显示已经在菜谱上的菜
public class DishFragment extends Fragment implements View.OnClickListener {
    Button seller_dish_all;
    Button seller_set_meal;
    Button seller_special;
    Button seller_west;
    Button seller_east;
    Button seller_today_good;
    Button seller_drink;
    Button seller_main_food;
    Button seller_health_dish;
    Button seller_sweet_dish;
    GridView grid;

    //因为必须根据数据在云端中的id，才能对云端中的数据进行修改和删除等操作，所以在查询的时候记录下他们的id

    List<Food_Res> dish_all=new ArrayList<>();
    List<Food_Res> dish_set_meal=new ArrayList<>();
    List<Food_Res> dish_special=new ArrayList<>();
    List<Food_Res> dish_west=new ArrayList<>();
    List<Food_Res> dish_east=new ArrayList<>();
    List<Food_Res> dish_today_good=new ArrayList<>();
    List<Food_Res> dish_drink=new ArrayList<>();
    List<Food_Res> dish_main_food=new ArrayList<>();
    List<Food_Res> dish_health_dish=new ArrayList<>();
    List<Food_Res> dish_sweet_dish=new ArrayList<>();
    List<Food_Res> dish_new=new ArrayList<>();

    List<String> dish_alls=new ArrayList<>();
    List<String> dish_set_meals=new ArrayList<>();
    List<String> dish_specials=new ArrayList<>();
    List<String> dish_wests=new ArrayList<>();
    List<String> dish_easts=new ArrayList<>();
    List<String> dish_today_goods=new ArrayList<>();
    List<String> dish_drinks=new ArrayList<>();
    List<String> dish_main_foods=new ArrayList<>();
    List<String> dish_health_dishs=new ArrayList<>();
    List<String> dish_sweet_dishs=new ArrayList<>();



    LinearLayout dish_detail;
    TextView grid_dish_name;
    EditText grid_dish_price;
    RadioGroup grid_radio;
    Button cancel_btn;
    Button modify_btn;
    RadioButton radioCheck=null;
    DishAdapter adapter;

   private  String before_button="全部";
    Food_Res f_detail=null;
   private  int f_detail_posi=0;
    public DishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_dish, container, false);
        seller_dish_all=(Button)v.findViewById(R.id.seller_dish_all);
        seller_set_meal=(Button)v.findViewById(R.id.seller_set_meal);
        seller_special=(Button)v.findViewById(R.id.seller_special);
        seller_west=(Button)v.findViewById(R.id.seller_west);
        seller_east=(Button)v.findViewById(R.id.seller_east);
        seller_today_good=(Button)v.findViewById(R.id.seller_today_good);
        seller_drink=(Button)v.findViewById(R.id.seller_drink);
        seller_main_food=(Button)v.findViewById(R.id.seller_main_food);
        seller_health_dish=(Button)v.findViewById(R.id.seller_health_dish);
        seller_sweet_dish=(Button)v.findViewById( R.id.seller_sweet_dish);
        grid=(GridView)v.findViewById(R.id.dish_grid);
        //初始化菜
        query_alldish();
        initDishLinear(v);

        seller_dish_all.setOnClickListener(this);
        seller_set_meal.setOnClickListener(this);
        seller_special.setOnClickListener(this);
        seller_west.setOnClickListener(this);
        seller_east.setOnClickListener(this);
        seller_drink.setOnClickListener(this);
        seller_today_good.setOnClickListener(this);
        seller_main_food.setOnClickListener(this);
        seller_health_dish.setOnClickListener(this);
        seller_sweet_dish.setOnClickListener(this);
       // seller_dish_all.setTextColor(getResources().getColor(R.color.seller_button_yes));
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  gridseleteView=view;
                dish_detail.setVisibility(View.VISIBLE);
                switch (before_button){
                    case "全部":
                    f_detail=dish_all.get(position);
                        break;
                    case "套餐":
                        f_detail=dish_set_meal.get(position);
                        break;
                    case "特色菜":
                        f_detail=dish_special.get(position);
                        break;
                    case "西餐":
                        f_detail=dish_west.get(position);
                        break;
                    case "中餐":
                        f_detail=dish_east.get(position);
                        break;
                    case "饮品":
                        f_detail=dish_drink.get(position);
                        break;
                    case"主食":
                        f_detail=dish_main_food.get(position);
                        break;
                    case"今日特色":
                        f_detail=dish_today_good.get(position);
                        break;
                    case"养生菜":
                        f_detail=dish_health_dish.get(position);
                        break;
                    case"甜点":
                        f_detail=dish_sweet_dish.get(position);
                        break;

                }
                f_detail_posi=position;
                initLinearData();
            }

            });


        return v;
    }

    //设置监听
    public void onClick(View v){
    switch(v.getId()){
        case R.id.seller_dish_all:
            if(!"全部".equals(before_button)){
                setColorOfBut("全部");
                adapter=null;
                query_alldish();
            }
            break;
        case R.id.seller_set_meal:
            if(!"套餐".equals(before_button)){
                setColorOfBut("套餐");
                adapter=null;
                query_dish("套餐");

            }
            break;
        case R.id.seller_special:
            if(!"特色菜".equals(before_button)){
                setColorOfBut("特色菜");
                adapter=null;
                query_dish("特色菜");
            }
            break;
        case R.id.seller_west:
            if(!"西餐".equals(before_button)){
                setColorOfBut("西餐");
                adapter=null;
                query_dish("西餐");
            }
            break;
        case R.id.seller_east:
            if(!"中餐".equals(before_button)){
                setColorOfBut("中餐");
                adapter=null;
                query_dish("中餐");
            }
            break;
        case R.id.seller_today_good:
            if(!"今日特色".equals(before_button)){
                setColorOfBut("今日特色");
                adapter=null;
                query_dish("今日特色");
            }
            break;
        case R.id.seller_drink:
            if(!"饮品".equals(before_button)){
                setColorOfBut("饮品");
                adapter=null;
                query_dish("饮品");
            }
            break;
        case R.id.seller_main_food:
            if(!"主食".equals(before_button)){
                setColorOfBut("主食");
                adapter=null;
                query_dish("主食");
            }
            break;
        case R.id.seller_health_dish:
            if(!"养生菜".equals(before_button)){
                setColorOfBut("养生菜");
                adapter=null;
                query_dish("养生菜");
            }
            break;
        case R.id.seller_sweet_dish:
            if(!"甜点".equals(before_button)){
                setColorOfBut("甜点");
                adapter=null;
                query_dish("甜点");
            }
            break;
    }
    }
    //初始化所有的在云端中的菜

    public void query_alldish(){
        if(dish_all.size()!=0){
            dish_all.clear();
            dish_alls.clear();
        }
        BmobQuery<Food_Res> query=new BmobQuery<>();
        query.addWhereEqualTo("iffood","true");
        query.findObjects(new FindListener<Food_Res>() {
            @Override
            public void done(List<Food_Res> list, BmobException e) {
                if(e==null){
                    for(int i=0;i<list.size();i++){
                        Food_Res f=list.get(i);
                        dish_all.add(f);
                        dish_alls.add(list.get(i).getObjectId());
                    }
                    adapter=new DishAdapter(getActivity(),R.layout.dish_item,dish_all);
                    grid.setAdapter(adapter);
                }
                else{
                    Log.e("error",""+e);
                }
            }
        });
    }

   //查询特定的菜
    public  void query_dish(String dish_name){
        final String dish_n=dish_name;
        judgeFoodList().clear();
        judgeList().clear();
        BmobQuery<Food_Res> query=new BmobQuery<>();
        query.addWhereEqualTo("food_type",dish_n);
        query.findObjects(new FindListener<Food_Res>() {
            @Override
            public void done(List<Food_Res> list, BmobException e) {
                if(e==null){
                    switch (dish_n){
                        case "套餐":
                            if(list.size()!=0){
                                dish_set_meal.clear();
                                for (int i=0;i<list.size();i++){
                                    Food_Res f_temp=list.get(i);
                                    dish_set_meal.add(f_temp);
                                    dish_set_meals.add(list.get(i).getObjectId());
                                }
                                    adapter=new DishAdapter(getActivity(),R.layout.dish_item,dish_set_meal);
                                    grid.setAdapter(adapter);
                            }
                            else{
                                grid.setAdapter(adapter);
                            }
                            break;
                        case "特色菜":
                            if(list.size()!=0){
                                dish_special.clear();
                                dish_specials.clear();
                                for (int i=0;i<list.size();i++){
                                    Food_Res f_temp=list.get(i);
                                    dish_special.add(f_temp);
                                   dish_specials.add(list.get(i).getObjectId());
                                }
                                if(dish_special.size()!=0){
                                    adapter=new DishAdapter(getActivity(),R.layout.dish_item,dish_special);
                                    grid.setAdapter(adapter);
                                }
                            }
                            else{
                                grid.setAdapter(adapter);
                            }
                            break;
                        case "西餐":
                            if(list.size()!=0){
                                dish_west.clear();
                                dish_wests.clear();
                                for (int i=0;i<list.size();i++){
                                    Food_Res f_temp=list.get(i);
                                    dish_west.add(f_temp);
                                    dish_wests.add(list.get(i).getObjectId());
                                }
                                adapter=new DishAdapter(getActivity(),R.layout.dish_item,dish_west);
                                grid.setAdapter(adapter);
                            }
                            else{
                                grid.setAdapter(adapter);
                            }
                            break;
                        case "中餐":
                            if(list.size()!=0){
                                dish_east.clear();
                                dish_easts.clear();
                                for (int i=0;i<list.size();i++){
                                    Food_Res f_temp=list.get(i);
                                    dish_east.add(f_temp);
                                    dish_easts.add(list.get(i).getObjectId());
                                }
                                adapter=new DishAdapter(getActivity(),R.layout.dish_item,dish_east);
                                grid.setAdapter(adapter);
                            }
                            else{
                                grid.setAdapter(adapter);
                            }
                            break;
                        case "饮品":
                            if(list.size()!=0){
                                dish_drink.clear();
                                dish_drinks.clear();
                                for (int i=0;i<list.size();i++){
                                    Food_Res f_temp=list.get(i);
                                    dish_drink.add(f_temp);
                                   dish_drinks.add(list.get(i).getObjectId());
                                }
                                adapter=new DishAdapter(getActivity(),R.layout.dish_item,dish_drink);
                                grid.setAdapter(adapter);
                            }
                            else{
                                grid.setAdapter(adapter);
                            }
                            break;
                        case "主食":
                            if(list.size()!=0){
                                dish_main_food.clear();
                                dish_main_foods.clear();
                                for (int i=0;i<list.size();i++){
                                    Food_Res f_temp=list.get(i);
                                    dish_main_food.add(f_temp);
                                   dish_main_foods.add(list.get(i).getObjectId());
                                }
                                adapter=new DishAdapter(getActivity(),R.layout.dish_item,dish_main_food);
                                grid.setAdapter(adapter);
                            }
                            else{
                                grid.setAdapter(adapter);
                            }
                            break;
                        case "今日特色":
                            if(list.size()!=0){
                                dish_today_good.clear();
                                dish_today_goods.clear();
                                for (int i=0;i<list.size();i++){
                                    Food_Res f_temp=list.get(i);
                                    dish_today_good.add(f_temp);
                                   dish_today_goods.add(list.get(i).getObjectId());
                                }
                                adapter=new DishAdapter(getActivity(),R.layout.dish_item,dish_today_good);
                                grid.setAdapter(adapter);
                            }
                            else{
                                grid.setAdapter(adapter);
                            }
                            break;
                        case "养生菜":
                            if(list.size()!=0){
                                dish_health_dish.clear();
                                dish_health_dishs.clear();
                                for (int i=0;i<list.size();i++){
                                    Food_Res f_temp=list.get(i);
                                    dish_health_dish.add(f_temp);
                                   dish_health_dishs.add(list.get(i).getObjectId());
                                }
                                adapter=new DishAdapter(getActivity(),R.layout.dish_item,dish_health_dish);
                                grid.setAdapter(adapter);
                            }
                            else{
                                grid.setAdapter(adapter);
                            }
                            break;
                        case "甜点":
                            if(list.size()!=0){
                                dish_sweet_dish.clear();
                                dish_sweet_dishs.clear();
                                for (int i=0;i<list.size();i++){
                                    Food_Res f_temp=list.get(i);
                                    dish_sweet_dish.add(f_temp);
                                  dish_sweet_dishs.add(list.get(i).getObjectId());
                                }
                                adapter=new DishAdapter(getActivity(),R.layout.dish_item,dish_sweet_dish);
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

    //初始化对不同的菜进行修改的布局
    public void initDishLinear(View v){
        dish_detail =(LinearLayout)v.findViewById(R.id.dish_detail);
        grid_dish_name=(TextView)v.findViewById(R.id.grid_dish_name);
        grid_dish_price=(EditText) v.findViewById(R.id.grid_dish_price);
        grid_radio=(RadioGroup)v.findViewById(R.id.grid_radio);
        cancel_btn=(Button)v.findViewById(R.id.grid_cancel);
        modify_btn=(Button)v.findViewById(R.id.grid_OK);

    }

    //让数据显示出来
    public void initLinearData(){
        final int foodprice=f_detail.getFood_price();
        grid_dish_name.setText(f_detail.getFood_name());
        grid_dish_price.setText(""+foodprice);
        grid_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
                radioCheck= (RadioButton)getView().findViewById(group.getCheckedRadioButtonId());
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish_detail.setVisibility(View.GONE);
                //grid.getSelectedView().setBackgroundResource(R.color.grid_nochoose);
            }
        });
        modify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish_detail.setVisibility(View.GONE);
                //下面进行修改数据:
                //是不是修改了价格
               if(!grid_dish_price.getText().toString().equals(""+foodprice)){
                   Food_Res change_food=new Food_Res();
                   change_food.setFood_price(Integer.parseInt(grid_dish_price.getText().toString()));
                  // change_food.update()
                   change_food.update(judgeList().get(f_detail_posi), new UpdateListener() {
                       @Override
                       public void done(BmobException e) {
                           if(e==null){
                               Log.e("餐品价格修改","成功");
                           }else{
                               Log.e("餐品价格修改失败",""+e);
                           }
                       }
                   });
               }
                //进行修改上传，下载的操作
                if(radioCheck!=null){
                  if(radioCheck.getText().equals("上线")){

                      Food_Res change_food=new Food_Res();
                      change_food.setIfhave(true);
                      change_food.setFood_price(f_detail.getFood_price());
                      // change_food.update()
                      change_food.update(judgeList().get(f_detail_posi), new UpdateListener() {
                          @Override
                          public void done(BmobException e) {
                              if(e==null){
                                  judgeFoodList().get(f_detail_posi).setIfhave(true);
                                  adapter.notifyDataSetChanged();
                                  grid.setAdapter(adapter);
                                  Log.e("餐品上线修改","成功");
                              }else{
                                  Log.e("餐品上线修改失败",""+e);
                              }
                          }
                      });
                  }


                    if(radioCheck.getText().equals("下线")){
                        Food_Res change_food=new Food_Res();
                        change_food.setIfhave(false);
                        change_food.setFood_price(f_detail.getFood_price());
                        // change_food.update()
                        change_food.update(judgeList().get(f_detail_posi), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    judgeFoodList().get(f_detail_posi).setIfhave(false);
                                    Log.e("大小下线",""+dish_all.size()+dish_set_meal.size());
                                    adapter.notifyDataSetChanged();
                                    grid.setAdapter(adapter);
                                    Log.e("餐品下线修改","成功");
                                }else{
                                    Log.e("餐品下线修改失败",""+e);
                                }
                            }
                        });
                    }

                    if(radioCheck.getText().equals("删除")){
                        Food_Res change_food=new Food_Res();
                        change_food.setObjectId(judgeList().get(f_detail_posi));
                        change_food.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Log.e("餐品删除","成功");
                                    judgeFoodList().remove(f_detail);
                                    adapter.notifyDataSetChanged();
                                    grid.setAdapter(adapter);
                                }else{
                                    Log.e("餐品删除失败",""+e);
                                }
                            }
                        });
                    }


                }
            }
        });
    }

    //判断点的属于哪个list
    public List<String> judgeList(){
        if(dish_all.size()!=0){
            return dish_alls;
        }
        else if(dish_set_meal.size()!=0){
            return dish_set_meals;
        }else if(dish_special.size()!=0){
            return dish_specials;
        }else if(dish_west.size()!=0){
            return dish_wests;
        }else if(dish_east.size()!=0){
            return dish_easts;
        }else if(dish_drink.size()!=0){
            return dish_drinks;
        }else if(dish_main_food.size()!=0){
            return dish_main_foods;
        }else if(dish_today_good.size()!=0){
            return dish_today_goods;
        }else if(dish_health_dish.size()!=0){

            return dish_health_dishs;
        }
        return dish_sweet_dishs;
    }

    public List<Food_Res> judgeFoodList(){
        if(dish_all.size()!=0){
            return dish_all;
        }else if(dish_set_meal.size()!=0){
            return dish_set_meal;
        }
        else if(dish_special.size()!=0){
            return dish_special;
        }else if(dish_west.size()!=0){
            return dish_west;
        }else if(dish_east.size()!=0){
            return dish_east;
        }else if(dish_drink.size()!=0){
            return dish_drink;
        }else if(dish_main_food.size()!=0){
            return dish_main_food;
        }else if(dish_today_good.size()!=0){
            return dish_today_good;
        }else if(dish_health_dish.size()!=0){

            return dish_health_dish;
        }
        return dish_sweet_dish;
    }






    //根据点击，给按钮设置不同的颜色
    public void setColorOfBut(String table){
        switch (before_button){
            case "全部":
                seller_dish_all.setTextColor(getResources().getColor(R.color.seller_button_no));
                break;
            case "套餐":
                seller_set_meal.setTextColor(getResources().getColor(R.color.seller_button_no));
                break;
            case "特色菜":
                seller_special.setTextColor(getResources().getColor(R.color.seller_button_no));
                break;
            case "西餐":
                seller_west.setTextColor(getResources().getColor(R.color.seller_button_no));
                break;
            case "中餐":
                seller_east.setTextColor(getResources().getColor(R.color.seller_button_no));
                break;
            case "饮品":
                seller_drink.setTextColor(getResources().getColor(R.color.seller_button_no));
                break;
            case "主食":
                seller_main_food.setTextColor(getResources().getColor(R.color.seller_button_no));
                break;
            case "今日特色":
                seller_today_good.setTextColor(getResources().getColor(R.color.seller_button_no));
                break;
            case "养生菜":
                seller_health_dish.setTextColor(getResources().getColor(R.color.seller_button_no));
                break;
            case "甜点":
                seller_sweet_dish.setTextColor(getResources().getColor(R.color.seller_button_no));
                break;

        }
        before_button=table;
        switch (table){
            case "全部":
                seller_dish_all.setTextColor(getResources().getColor(R.color.seller_button_yes));
                break;
            case "套餐":
                seller_set_meal.setTextColor(getResources().getColor(R.color.seller_button_yes));
                break;
            case "特色菜":
                seller_special.setTextColor(getResources().getColor(R.color.seller_button_yes));
                break;
            case "西餐":
                seller_west.setTextColor(getResources().getColor(R.color.seller_button_yes));
                break;
            case "中餐":
                seller_east.setTextColor(getResources().getColor(R.color.seller_button_yes));
                break;
            case "饮品":
                seller_drink.setTextColor(getResources().getColor(R.color.seller_button_yes));
                break;
            case "主食":
                seller_main_food.setTextColor(getResources().getColor(R.color.seller_button_yes));
                break;
            case "今日特色":
                seller_today_good.setTextColor(getResources().getColor(R.color.seller_button_yes));
                break;
            case "养生菜":
                seller_health_dish.setTextColor(getResources().getColor(R.color.seller_button_yes));
                break;
            case "甜点":
                seller_sweet_dish.setTextColor(getResources().getColor(R.color.seller_button_yes));
                break;
        }
    }



}
