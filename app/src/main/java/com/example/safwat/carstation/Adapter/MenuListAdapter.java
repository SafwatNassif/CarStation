package com.example.safwat.carstation.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.safwat.carstation.Model.ModelOfMainMenu;
import com.example.safwat.carstation.R;

import java.util.ArrayList;

/**
 * Created by minato on 5/2/2017.
 */

public class MenuListAdapter extends ArrayAdapter<ModelOfMainMenu> {

    private  Context context;
    private  ImageView image;
    private  TextView name;

    public MenuListAdapter(@NonNull Context context, @LayoutRes int layout_list_item, @NonNull ArrayList<ModelOfMainMenu> list) {
        super(context, layout_list_item, list);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rootView =convertView;
        if(rootView == null){
            rootView = LayoutInflater.from(context).inflate(R.layout.grid_menu_item,parent,false);
        }
        image =(ImageView) rootView.findViewById(R.id.menu_item);
        name =(TextView) rootView.findViewById(R.id.grid_menu_name);
        image.setImageResource(getItem(position).getImage());
        name.setText(getItem(position).getName());
        return rootView;
    }
}
