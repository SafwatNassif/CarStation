package com.example.safwat.carstation.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.safwat.carstation.Model.OilObject;
import com.example.safwat.carstation.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by safwat on 02/10/17.
 */

public class OilAdapter extends ArrayAdapter<OilObject> {

    private Context context;
    private TextView carName,DateAnswer;
    private ArrayList<OilObject> arrayList;
    public OilAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<OilObject> list) {
        super(context, resource, list);
        this.context =context;
        this.arrayList = list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rootView = convertView;
        if(rootView == null){
            rootView = LayoutInflater.from(context).inflate(R.layout.oil_list_item,parent,false);
        }
        carName =(TextView) rootView.findViewById(R.id.car_name_answer);
        DateAnswer =(TextView) rootView.findViewById(R.id.date_answer);

        carName.setText(arrayList.get(position).getCarName());
        DateAnswer.setText(arrayList.get(position).getDate());

        return rootView;
    }


}
