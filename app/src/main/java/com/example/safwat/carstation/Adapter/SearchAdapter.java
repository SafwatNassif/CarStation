package com.example.safwat.carstation.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.safwat.carstation.Model.DailyDetailsObject;
import com.example.safwat.carstation.Model.OilObject;
import com.example.safwat.carstation.R;

import java.util.ArrayList;

/**
 * Created by safwat on 07/12/17.
 */

public class SearchAdapter extends ArrayAdapter<DailyDetailsObject> {

    private Context context;
    private  ArrayList<DailyDetailsObject> arrayList;
    private TextView driverName,ShiftTime,daily,expense,totalCost;
    public SearchAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<DailyDetailsObject> list) {
        super(context, resource, list);
        this.context =context;
        this.arrayList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rootView = convertView;
        if(rootView == null){
            rootView = LayoutInflater.from(context).inflate(R.layout.search_result_item,parent,false);
        }
        driverName = (TextView) rootView.findViewById(R.id.driver_name_of_search);
        ShiftTime =(TextView) rootView.findViewById(R.id.shift_time);
        daily =(TextView) rootView.findViewById(R.id.daily_search_result);
        expense =(TextView) rootView.findViewById(R.id.cost_search_fragment);
        totalCost =(TextView) rootView.findViewById(R.id.totalCost_search_fragment);

        driverName.setText(arrayList.get(position).getDriverName());
        if (arrayList.get(position).getMorning_cost() == 0.0){
            ShiftTime.setText(context.getResources().getString(R.string.night));
            daily.setText(arrayList.get(position).getNight_cost()+"");
        }else if (arrayList.get(position).getNight_cost() == 0.0){
            ShiftTime.setText(context.getResources().getString(R.string.morreing));
            daily.setText(arrayList.get(position).getMorning_cost()+"");
        }
        expense.setText(arrayList.get(position).getExpense_cost()+"   "+arrayList.get(position).getDescription());
        totalCost.setText(arrayList.get(position).getTotal_cost()+"");

        return rootView;
    }
}
