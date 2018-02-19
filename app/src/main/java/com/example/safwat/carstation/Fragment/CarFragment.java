package com.example.safwat.carstation.Fragment;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.safwat.carstation.Adapter.CarAndDriverAdapter;
import com.example.safwat.carstation.Database.Constant;
import com.example.safwat.carstation.Database.Operation;
import com.example.safwat.carstation.Model.PersonData;
import com.example.safwat.carstation.R;
import com.example.safwat.carstation.Utility;
import com.example.safwat.carstation.WebServer.VolleyConnectionRequest;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarFragment extends Fragment implements
        View.OnClickListener,LoaderManager.LoaderCallbacks<Cursor> {

    private View rootView ;
    private Operation operation;
    private EditText car_name;
    private LinearLayout save,carProgressContainer;
    private RecyclerView car_RecyclerView ;
    private CarAndDriverAdapter adapter;
    private String LOG_CAT = CarFragment.class.getSimpleName();

    public CarFragment() {
        // Required empty public constructor
        /*
        * required in this fragment
        *
        * 1- add delete button ---- done
        * 2- add update method in operation class --- done
        * 4- access on hold press appear two  button delete and update ----- done
        * 5- request data from server
        * 6- send notification
        * 7- edit name ---- > done
        * 8- check if name is empty before add --> done
        * */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_car, container, false);
        getActivity().getWindow().setBackgroundDrawable(null);
        define(rootView);
        RefreshData();
        return rootView ;
    }

    private void define(View rootView) {
        car_name =(EditText) rootView.findViewById(R.id.car_name_text);
        save = (LinearLayout) rootView.findViewById(R.id.save_new_car);
        car_RecyclerView =(RecyclerView) rootView.findViewById(R.id.cars_RecycleViewList);
        car_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        carProgressContainer = (LinearLayout) rootView.findViewById(R.id.carProgressContainer);
        Utility.visible(carProgressContainer);
        operation = new Operation(getContext());
        save.setOnClickListener(this);
    }

    private void RefreshData() {
        getActivity().getLoaderManager().initLoader(0,null,CarFragment.this);
    }



    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
                case  R.id.save_new_car:
                      String  newCar = car_name.getText().toString();
                        if (newCar.equals("") || newCar == null){
                            Toast.makeText(getContext(), getResources().getString(R.string.check_car), Toast.LENGTH_LONG).show();
                        }else {
                                Utility.visible(carProgressContainer);
                                insertIntoSqLite(newCar);
                        }
                    break;
        }


    }

    private void insertIntoSqLite(String newCar) {
        ContentValues values = new ContentValues();
        values.put(Constant.carOwners.Col_name,newCar);
        int response = operation.insertOperation(values,Constant.carOwners.Table_Uri);
        if (response ==1){
            Toast.makeText(getContext(),getResources().getString(R.string.addNewCar)
                    ,Toast.LENGTH_SHORT).show();
            car_name.setText("");
        }else{
            Toast.makeText(getContext(),getResources().getString(R.string.addNewCar)
                    ,Toast.LENGTH_SHORT).show();

        }
        Utility.Gona(carProgressContainer);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == 0){
            Uri table_uri=Constant.carOwners.Table_Uri;
            String whereStatement=Constant.carOwners.Col_remove +" = ? ";
            return new CursorLoader(getContext(),table_uri,
                    new String []{Constant.carOwners.Col_name,Constant.carOwners.Col_ID}
                    ,whereStatement,new String []{"0"},null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case 0:
                getCarOwnerFromCursor(data);
                break;
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void getCarOwnerFromCursor(Cursor data) {
        ArrayList<PersonData> carList = new ArrayList<>();
        PersonData car;
        if (data != null && data.getCount()>0){
            for (data.moveToFirst();!data.isAfterLast();data.moveToNext()){
                car = new PersonData(
                        data.getString(data.getColumnIndex(Constant.carOwners.Col_name)),
                        data.getInt(data.getColumnIndex(Constant.carOwners.Col_ID)));
                carList.add(car);
            }
            adapter = new CarAndDriverAdapter(getContext(),R.layout.car_list_name_item,carList
                    ,Constant.carOwners.Table_Uri,carProgressContainer);
            car_RecyclerView.setAdapter(adapter);

        }else{
            Toast.makeText(getContext(),getResources().getString(R.string.no_car_data)
                    , Toast.LENGTH_SHORT).show();
        }
        Utility.Gona(carProgressContainer);
    }
}
