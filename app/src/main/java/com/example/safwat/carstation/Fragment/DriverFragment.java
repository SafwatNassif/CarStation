package com.example.safwat.carstation.Fragment;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.safwat.carstation.Adapter.CarAndDriverAdapter;
import com.example.safwat.carstation.Database.Constant;
import com.example.safwat.carstation.Database.Operation;
import com.example.safwat.carstation.Interface.RefreshCallBackCarAndDriver;
import com.example.safwat.carstation.Interface.callbackResponse;
import com.example.safwat.carstation.Model.OilObject;
import com.example.safwat.carstation.Model.PersonData;
import com.example.safwat.carstation.R;
import com.example.safwat.carstation.Refresh;
import com.example.safwat.carstation.Utility;
import com.example.safwat.carstation.WebServer.VolleyConnectionRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */

public class DriverFragment extends Fragment implements
        View.OnClickListener,LoaderManager.LoaderCallbacks<Cursor>{

    private View rootView ;
    private EditText driver_name;
    private LinearLayout save ,driverProgressContainer;
    private RecyclerView driver_list ;
    private CarAndDriverAdapter adapter;
    private Operation operation;
    private String LOG_CAT = DriverFragment.class.getSimpleName();


    public DriverFragment() {
        // Required empty public constructor
        //  check if name is empty before add
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =inflater.inflate(R.layout.fragment_driver, container, false);
        define(rootView);
        RefreshData();
        return rootView;
    }

    private void define(View rootView) {
        driver_name =(EditText) rootView.findViewById(R.id.driver_name_text);
        save = (LinearLayout) rootView.findViewById(R.id.save_new_driver);
        driver_list =(RecyclerView) rootView.findViewById(R.id.driver_list);
        driverProgressContainer= (LinearLayout) rootView.findViewById(R.id.driverProgressContainer);
        Utility.visible(driverProgressContainer);
        operation = new Operation(getContext());
        save.setOnClickListener(this);
        driver_list.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void RefreshData() {
        getActivity().getLoaderManager().initLoader(1,null,DriverFragment.this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case  R.id.save_new_driver:
                String  driverText = driver_name.getText().toString();
                if (driverText.equals("") || driverText == null){
                    Toast.makeText(getContext(), getResources().getString(R.string.check_driver),
                            Toast.LENGTH_LONG).show();
                }else {
                    Utility.visible(driverProgressContainer);
                    insertIntoSqLite(driverText);
                }
                break;
        }

    }

    private void insertIntoSqLite(final String newDriver) {
        ContentValues values = new ContentValues();
        values.put(Constant.driver.Col_name,newDriver);
        int response = operation.insertOperation(values,Constant.driver.Table_Uri);
        if (response ==1){
            Toast.makeText(getContext(),getResources().getString(R.string.addNewDriver)
                    ,Toast.LENGTH_SHORT).show();
            driver_name.setText("");
        }else{
            Toast.makeText(getContext(),getResources().getString(R.string.addNewDriver)
                    ,Toast.LENGTH_SHORT).show();
        }
        Utility.Gona(driverProgressContainer);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == 1){
            Uri table_uri=Constant.driver.Table_Uri;
            String whereStatement=Constant.driver.Col_remove +" = ? ";
            return new CursorLoader(getContext(),table_uri,
                    new String []{Constant.driver.Col_name,Constant.driver.Col_ID}
                    ,whereStatement,new String []{"0"},null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case 1:
                getDriverFromCursor(data);
                break;
        }
    }

    private void getDriverFromCursor(Cursor data) {
        ArrayList<PersonData> driverList = new ArrayList<>();
        PersonData driver;
        if (data != null && data.getCount()>0){
            for (data.moveToFirst();!data.isAfterLast();data.moveToNext()){
                driver = new PersonData(
                        data.getString(data.getColumnIndex(Constant.driver.Col_name)),
                        data.getInt(data.getColumnIndex(Constant.driver.Col_ID)));
                driverList.add(driver);
            }
            adapter = new CarAndDriverAdapter(getContext(),R.layout.car_list_name_item,driverList
                    ,Constant.driver.Table_Uri,driverProgressContainer);
            driver_list.setAdapter(adapter);

        }else{
            Toast.makeText(getContext(),getResources().getString(R.string.no_driver_data)
                    , Toast.LENGTH_SHORT).show();
        }
        Utility.Gona(driverProgressContainer);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
