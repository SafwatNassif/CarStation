package com.example.safwat.carstation.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.safwat.carstation.Adapter.MenuListAdapter;
import com.example.safwat.carstation.Database.Constant;
import com.example.safwat.carstation.Fragment.CarFragment;
import com.example.safwat.carstation.Fragment.CollectMoneyFragment;
import com.example.safwat.carstation.Fragment.DailyCollected;
import com.example.safwat.carstation.Fragment.DailyFormFragment;
import com.example.safwat.carstation.Fragment.DailyFragment;
import com.example.safwat.carstation.Fragment.DriverFragment;
import com.example.safwat.carstation.Fragment.MergeDaily;
import com.example.safwat.carstation.Fragment.MergeFragment;
import com.example.safwat.carstation.Fragment.OilFragment;
import com.example.safwat.carstation.Fragment.SearchFragment;
import com.example.safwat.carstation.Interface.StartTransition;
import com.example.safwat.carstation.Model.ModelOfMainMenu;
import com.example.safwat.carstation.R;

import java.util.ArrayList;
import java.util.Arrays;

public class Menu extends AppCompatActivity implements AdapterView.OnItemClickListener , StartTransition {

    GridView menu ;
    ModelOfMainMenu[] array_name;
    ArrayList listName;
    MenuListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        init();
        menu.setAdapter(adapter);
        menu.setOnItemClickListener(this);
    }


    private void init() {
        menu =(GridView) findViewById(R.id.grid_menu);
        array_name =new ModelOfMainMenu[9];
        array_name[0]= new ModelOfMainMenu(R.drawable.adddriver,getResources().getString(R.string.add_driver));
        array_name[1]= new ModelOfMainMenu(R.drawable.addcar,getResources().getString(R.string.add_car));
        array_name[2]= new ModelOfMainMenu(R.drawable.collectmony,getResources().getString(R.string.collect_money));
        array_name[3]= new ModelOfMainMenu(R.drawable.daily,getResources().getString(R.string.daily));
        array_name[4]= new ModelOfMainMenu(R.drawable.merge,getResources().getString(R.string.merge));
        array_name[5]= new ModelOfMainMenu(R.drawable.oil,getResources().getString(R.string.change_oil));
        array_name[6]= new ModelOfMainMenu(R.drawable.calc,getResources().getString(R.string.collected_daily));
        array_name[7]= new ModelOfMainMenu(R.drawable.mdaily,getResources().getString(R.string.merge_daily));
        array_name[8]= new ModelOfMainMenu(R.drawable.search,getResources().getString(R.string.search_daily));

        listName = new ArrayList(Arrays.asList(array_name));
        adapter = new MenuListAdapter(getBaseContext(),R.layout.grid_menu_item,listName);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0 :
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container,new DriverFragment()).addToBackStack("fragment").commit();
                menu.setVisibility(View.GONE);
                break;
            case 1 :
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container,new CarFragment()).addToBackStack("fragment").commit();
                menu.setVisibility(View.GONE);
                getWindow().setBackgroundDrawable(null);
                break;
            case 2 :
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container,new CollectMoneyFragment()).addToBackStack("fragment").commit();
                menu.setVisibility(View.GONE);
                break;
            case 3 :

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container,new DailyFragment()).addToBackStack("fragment").commit();
                menu.setVisibility(View.GONE);
                break;
            case 4 :
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container,new MergeFragment()).addToBackStack("fragment").commit();
                menu.setVisibility(View.GONE);
                break;

            case 5:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container,new OilFragment()).addToBackStack("fragment").commit();
                menu.setVisibility(View.GONE);
                break;
            case 6:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container,new DailyCollected()).addToBackStack("fragment").commit();
                menu.setVisibility(View.GONE);
                break;
            case 7:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container,new MergeDaily()).addToBackStack("fragment").commit();
                menu.setVisibility(View.GONE);
                break;
            case 8:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container,new SearchFragment()).addToBackStack("fragment").commit();
                menu.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
                getSupportFragmentManager().popBackStack();
         }
        menu.setVisibility(View.VISIBLE);

    }


    @Override
    public void beginTransaction(Bundle bundle) {

        DailyFormFragment dailyFormFragment = new DailyFormFragment();
        dailyFormFragment.setArguments(bundle);
        String  type = bundle.getString("type");
        if (type.equals("search")){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.search_fragment, dailyFormFragment)
                    .commit();
        }else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.daily_fragment_container, dailyFormFragment)
                    .commit();
        }
    }
}
