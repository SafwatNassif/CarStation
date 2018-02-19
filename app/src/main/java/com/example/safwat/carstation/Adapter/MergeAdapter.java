package com.example.safwat.carstation.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.safwat.carstation.Model.PersonData;
import com.example.safwat.carstation.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by safwat on 29/08/17.
 */

public class MergeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PersonData> listCar;
    private int Resource;
    private CheckBox checkBox;
    private ArrayList<String > carOwener_id,carOwnerName ;
    private ArrayList<Integer> carIdList;
    private Boolean [] checkBoxState ;
    private String LOG_CAT =MergeAdapter.class.getSimpleName();

    public MergeAdapter(Context context, ArrayList<PersonData> listCar, int resource) {
        this.context = context;
        this.listCar = listCar;
        this.checkBoxState = new Boolean[listCar.size()];
        Arrays.fill(checkBoxState,false);
        this.Resource = resource;
        carOwener_id = new ArrayList<>();
        carOwnerName = new ArrayList<>();
        carIdList = new ArrayList<>();

    }

    @Override
    public int getCount() {
        return listCar.size();
    }

    @Override
    public Object getItem(int position) {
        return listCar.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(context).inflate(Resource,parent,false);
        checkBox = (CheckBox) rootView.findViewById(R.id.checkbox_merge);
        TextView name = (TextView) rootView.findViewById(R.id.carOwner_merge_textView_item);
        name.setText(listCar.get(position).getName());
        checkBox.setChecked(checkBoxState[position]);
        Log.v(LOG_CAT,"checkBox state Of "+position+" is : "+checkBoxState[position]);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    carOwener_id.add(listCar.get(position).getId()+"");
                    carIdList.add(listCar.get(position).getId());
                    carOwnerName.add(listCar.get(position).getName());
                    checkBoxState[position] = true;
                    Log.v(LOG_CAT,"carowner id that add is : "+listCar.get(position).getId());
                }else{
                    int positionOfitem =carOwener_id.indexOf(""+listCar.get(position).getId());
                    carOwener_id.remove(positionOfitem);
                    carOwnerName.remove(positionOfitem);
                    carIdList.remove(positionOfitem);
                    checkBoxState[position] = false;
                    Log.v(LOG_CAT,"carowner id that removed is : "+listCar.get(position).getId());
                }
            }
        });

        return rootView;
    }

    public ArrayList<String> getCarOwener_id(){
        return  carOwener_id;
    }
    public ArrayList<Integer> getCar_id(){
        return carIdList;
    }
    public ArrayList<String> getCarOwenerName(){
        return  carOwnerName;
    }


}
