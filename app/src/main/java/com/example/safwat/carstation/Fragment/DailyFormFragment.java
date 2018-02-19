package com.example.safwat.carstation.Fragment;


import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safwat.carstation.Adapter.CarAndDriverAdapter;
import com.example.safwat.carstation.Database.Constant;
import com.example.safwat.carstation.Database.Operation;
import com.example.safwat.carstation.Interface.CarOrDriverNameSelected;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyFormFragment extends Fragment implements
        View.OnClickListener,LoaderManager.LoaderCallbacks<Cursor>  {

    private View rootView ;
    private TextView car_name , driver_name, shift_name, total_cost,date_textView,edit_daily_form
            ,save_daily_form;
    private LinearLayout save ,cars_container ,driver_container,shift_container,collection_container
            ,date_container,dailyContainer,dailyFormFragmentProgressBar;
    private CheckBox change_oil ;
    private ListView shift_list;
    private ArrayList<PersonData > listCarsData , listDriverData ;
    private ArrayList<String> shiftList;
    private ArrayList<String> listCar,listDriver;
    private ArrayList<Integer> listCarId,listDriverId;
    private ArrayAdapter <String > shiftAdapter , carNameAdapter ,driverNameAdapter;
    private EditText daily_editText,kilo_editText,description_editText,cost_editText;
    private static String LOG_CAT = CarFragment.class.getSimpleName(),dateFromBundle;
    private static String car_name_text,driver_name_text,shift_text,description_text,date_in_sql;
    private double daily_cost,daily_morning,daily_night, number_kilo,buy_cost;
    private static int car_id,driver_id,change_oil_state,shift;
    private VolleyConnectionRequest volleyConnectionRequest;
    private static Double morningCost,nightCost;
    private  Calendar calendar;
    private ScrollView scrollViewDaily;
    private Bundle bundle;
    private Double Totalcost;

    private Operation operation;
    private Date currentDate;

    public DailyFormFragment() {
//        /*
//         *Required empty public constructor
//         *check internet connection -- >done
//         *check all filed required -->done
//         *get Data from all filed -->done
//         *insert in sqLite -->done
//         *insert in sqlDatabase -->done
//         *add  new column in database server named it total_cost and insert total cost in it
//         *send notification
//         */
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =inflater.inflate(R.layout.fragment_form_daily, container, false);
        define();
        shiftListSetAdapter();
        getCarList();
        getDriverList();
        bundle = this.getArguments();
        if (bundle.getString("type").equals("daily")||bundle.getString("type").equals("search")){
                edit_daily_form.setVisibility(View.VISIBLE);
                save_daily_form.setVisibility(View.GONE);
                car_name.setText(bundle.getString(Constant.daily.Col_Car_name));
                car_id = bundle.getInt(Constant.daily.Col_carowners_id);
                driver_name.setText(bundle.getString(Constant.daily.Col_Driver_Name));
                driver_id = bundle.getInt(Constant.daily.Col_driver_id);
                change_oil_state= bundle.getInt(Constant.daily.Col_change_oil);
                kilo_editText.setText((int) bundle.getDouble(Constant.daily.Col_kilos)+"");
                date_textView.setText(bundle.getString(Constant.daily.Col_date));
                dateFromBundle = bundle.getString(Constant.daily.Col_date);
                description_editText.setText(bundle.getString(Constant.daily.Col_decleration));
                cost_editText.setText((int) bundle.getDouble(Constant.daily.Col_expense)+"");
                total_cost.setText((int) bundle.getDouble(Constant.daily.Col_Total_cost)+"");
                morningCost = bundle.getDouble(Constant.daily.Col_daily_cost_morning);
                nightCost= bundle.getDouble(Constant.daily.Col_daily_cost_night);

                if (morningCost == 0.0){
                    daily_editText.setText(""+nightCost);
                    shift_name.setText(shiftList.get(1));
                }else if (nightCost == 0.0){
                    daily_editText.setText(""+morningCost);
                    shift_name.setText(shiftList.get(0));
                }

                if (change_oil_state == 1){
                    change_oil.setChecked(true);
                }else{
                    change_oil.setChecked(false);
                }

        }else{
            edit_daily_form.setVisibility(View.GONE);
            save_daily_form.setVisibility(View.VISIBLE);
        }

        // to set morning and  night list
        save.setOnClickListener(this);
        cars_container.setOnClickListener(this);
        driver_container.setOnClickListener(this);
        shift_container.setOnClickListener(this);
        date_container.setOnClickListener(this);
        change_oil.setOnClickListener(this);
        collection_container.setOnClickListener(this);

        shift_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                shift_name.setText(shiftList.get(position).toString());
                shift_list.setVisibility(View.GONE);
            }
        });
        Utility.Gona(dailyFormFragmentProgressBar);
        return rootView;
    }


    // this for define all layout
       private void define() {
        currentDate = new Date();
        operation = new Operation(getContext());
        shift_list = (ListView) rootView.findViewById(R.id.shift_list_in_daily);


        change_oil = (CheckBox)rootView.findViewById(R.id.change_oil_check_box);
        save = (LinearLayout) rootView.findViewById(R.id.save_new_daily);
        cars_container = (LinearLayout) rootView.findViewById(R.id.cars_container);
        driver_container = (LinearLayout) rootView.findViewById(R.id.drivers_container);
        collection_container= (LinearLayout) rootView.findViewById(R.id.collection_container);
        shift_container= (LinearLayout) rootView.findViewById(R.id.shift_container);
        date_container= (LinearLayout) rootView.findViewById(R.id.date_container);
        dailyFormFragmentProgressBar= (LinearLayout) rootView.findViewById(R.id.dailyFormProgressDialog);
        dailyContainer= (LinearLayout) rootView.findViewById(R.id.daily_cost_container);

        car_name = (TextView) rootView.findViewById(R.id.car_name_text_view_in_daily);
        driver_name = (TextView) rootView.findViewById(R.id.driver_name_text_view_daily);
        shift_name = (TextView) rootView.findViewById(R.id.shift_name);
        total_cost = (TextView) rootView.findViewById(R.id.collection_tetView);
        date_textView =(TextView)rootView.findViewById(R.id.date_TextView);
        edit_daily_form= (TextView) rootView.findViewById(R.id.edit_daily_form);
        save_daily_form= (TextView) rootView.findViewById(R.id.save_daily_form);

        daily_editText = (EditText) rootView.findViewById(R.id.daily_EditText);
        kilo_editText = (EditText) rootView.findViewById(R.id.kilo_EditText);
        description_editText = (EditText) rootView.findViewById(R.id.description_EditText);
        cost_editText= (EditText) rootView.findViewById(R.id.cost_EditText);

        scrollViewDaily = (ScrollView) rootView.findViewById(R.id.scrollViewDaily_general);
        shift_list.setVisibility(View.GONE);
        dailyContainer.setOnClickListener(this);
        Utility.visible(dailyFormFragmentProgressBar);
    }

    // this method that called the Loader
       private void getCarList() {
        listCarsData = new ArrayList<PersonData>();
        listCarId = new ArrayList<Integer>();
        listDriverId = new ArrayList<Integer>();
        getActivity().getLoaderManager().initLoader(0,null,DailyFormFragment.this);
      }
       private void getDriverList() {
        listDriverData = new ArrayList<PersonData>();
        getActivity().getLoaderManager().initLoader(1,null,DailyFormFragment.this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cars_container:
                Utility.showDialog(getContext(),getResources().getString(R.string.car_name)
                        ,listCar,"car", new CarOrDriverNameSelected() {
                            @Override
                            public void carOrDriverNameSpecified(String name, String type, int position) {
                                if (type.equals("car")){
                                    car_name.setText(name);
                                    car_id=listCarId.get(position);
                                }else if (type.equals("driver")){
                                    driver_name.setText(name);
                                    driver_id= listDriverId.get(position);
                                }
                            }
                        });

                break;
            case R.id.drivers_container:
                    Utility.showDialog(getContext(),getResources().getString(R.string.driver_name)
                            ,listDriver,"driver", new CarOrDriverNameSelected() {
                                @Override
                                public void carOrDriverNameSpecified(String name, String type, int position) {
                                    if (type.equals("car")){
                                        car_name.setText(name);
                                        car_id = listCarId.get(position);
                                    }else if (type.equals("driver")){
                                        driver_name.setText(name);
                                        driver_id = listDriverId.get(position);
                                    }
                                }
                            });


                break;
            case R.id.shift_container:
                if (shift_list.getVisibility()== View.VISIBLE ){
                    shift_list.setVisibility(View.GONE);
                }else{
                    shift_list.setVisibility(View.VISIBLE);
                    setListViewHeightBasedOnChildren(shift_list);
                }
                break;
            case R.id.date_container:
                calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        updateTextViewOfDate();
                    }

                };
                new DatePickerDialog(getContext(),date,calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.change_oil_check_box:
                    if (change_oil.isChecked()){
                        change_oil_state=1;
                        Log.v(LOG_CAT,"change oil state after click : "+change_oil_state);
                    }else{
                        change_oil_state=0;
                        Log.v(LOG_CAT,"change oil state after click : "+change_oil_state);
                    }
                break;
            case R.id.save_new_daily:
                    Utility.visible(dailyFormFragmentProgressBar);
                    car_name_text = car_name.getText().toString();
                    driver_name_text = driver_name.getText().toString();
                    shift_text = shift_name.getText().toString();
                    try {
                        daily_cost = Double.parseDouble(daily_editText.getText().toString());
                    }catch (Exception e){
                        daily_cost=0.0;
                    }

                    try {
                        number_kilo = Double.parseDouble(kilo_editText.getText().toString());
                    }catch (Exception e){
                        number_kilo=0.0;
                    }

                    try {
                            buy_cost = Double.parseDouble(cost_editText.getText().toString());
                    }catch (Exception e){
                            buy_cost=0.0;
                    }

                    description_text = description_editText.getText().toString();
                    try {
                        Totalcost = calculateTotal(daily_cost, buy_cost);
                    }catch (Exception e){
                        Totalcost =Double.parseDouble(daily_editText.getText().toString());
                    }
                    if (shift_text.equals(getResources().getString(R.string.morreing))){
                          morningCost=daily_cost;
                          nightCost=0.0;
                    }else if(shift_text.equals(getResources().getString(R.string.night))){
                           nightCost =daily_cost;
                           morningCost=0.0;
                    }

                    if (edit_daily_form.getVisibility() == View.GONE &&
                            save_daily_form.getVisibility()==View.VISIBLE){
                        InsertIntoSqLiteDatabase(car_id,
                                driver_id,number_kilo, description_text, buy_cost, currentDate,
                                morningCost,nightCost,Totalcost, driver_name_text,
                                car_name_text,change_oil_state);


                    }else if ( edit_daily_form.getVisibility() == View.VISIBLE &&
                            save_daily_form.getVisibility()== View.GONE){
                        //update daily
                        Log.v(LOG_CAT,"driver id is : "+driver_id);
                        Log.v(LOG_CAT,"car id is :"+car_id);
                        Log.v(LOG_CAT,"change oil state is :"+change_oil_state);

                        UpdateSqLiteDataBase(bundle.getInt(Constant.daily.Col_ID)
                                , car_id, driver_id
                                , number_kilo
                                , description_text
                                , buy_cost
                                , dateFromBundle
                                , morningCost,nightCost
                                , Totalcost
                                , driver_name_text
                                , car_name_text
                                , change_oil_state);
                    }

                 break;

            case R.id.collection_container:
                if (daily_editText.getText().toString().equals("") ||
                        cost_editText.getText().toString().equals("")){
                    Toast.makeText(getContext(),getResources().getString(R.string.check_data),
                            Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        buy_cost = Double.parseDouble(cost_editText.getText().toString());
                    }catch (Exception e){
                        buy_cost=0.0;
                    }
                    try {
                        daily_cost = Double.parseDouble(daily_editText.getText().toString());
                    }catch (Exception e){
                        daily_cost=0.0;
                    }

                    try {
                        Totalcost = calculateTotal(daily_cost,buy_cost);
                    }catch (Exception e){
                        Totalcost =Double.parseDouble(daily_editText.getText().toString());
                    }

                    total_cost.setText(""+Totalcost);
                }
                break;
        }
    }

    private void UpdateSqLiteDataBase(int daily_id, int car_id, int driver_id,
                                      double number_kilo, String description_text, double buy_cost,
                                      String  currentDate, Double morningCost, Double nightCost,
                                      Double totalcost, String driver_name_text,
                                      String car_name_text, int change_oil_state) {
        ContentValues values = new ContentValues();
        values.put(Constant.daily.Col_ID,daily_id);
        values.put(Constant.daily.Col_carowners_id,car_id);
        values.put(Constant.daily.Col_driver_id,driver_id);
        values.put(Constant.daily.Col_daily_cost_night,nightCost);
        values.put(Constant.daily.Col_daily_cost_morning,morningCost);
        values.put(Constant.daily.Col_kilos,number_kilo);
        values.put(Constant.daily.Col_decleration,description_text);
        values.put(Constant.daily.Col_expense,buy_cost);
        values.put(Constant.daily.Col_date,currentDate);
        values.put(Constant.daily.Col_Total_cost,totalcost);
        values.put(Constant.daily.Col_Driver_Name,driver_name_text);
        values.put(Constant.daily.Col_Car_name,car_name_text);
        values.put(Constant.daily.Col_change_oil,change_oil_state);
        int response = operation.UpdateDailyOperation(values,Constant.daily.Table_Uri);

        if (response == 1 ){
            car_name.setText("");
            driver_name.setText("");
            shift_name.setText("");
            daily_editText.setText("");
            kilo_editText.setText("");
            date_textView.setText("");
            description_editText.setText("");
            change_oil.setChecked(false);
            cost_editText.setText("");
            total_cost.setText("");
            Toast.makeText(getContext(),getString(R.string.edit_daily_success)
                    , Toast.LENGTH_SHORT).show();
            scrollViewDaily.scrollTo(0,0);

        }else {
            Toast.makeText(getContext(),getString(R.string.check_data)
                    , Toast.LENGTH_SHORT).show();
        }
        Utility.Gona(dailyFormFragmentProgressBar);

    }

    private void InsertIntoSqLiteDatabase(int car_id, int driver_id,
                                          double number_kilo, String description_text,
                                          double buy_cost, Date  currentDate, Double morningCost,
                                          Double nightCost, Double totalcost, String driver_name_text,
                                          String car_name_text, int change_oil_state) {
        ContentValues values = new ContentValues();
        values.put(Constant.daily.Col_carowners_id,car_id);
        values.put(Constant.daily.Col_driver_id,driver_id);
        values.put(Constant.daily.Col_kilos,number_kilo);
        values.put(Constant.daily.Col_decleration,description_text);
        values.put(Constant.daily.Col_expense,buy_cost);
        values.put(Constant.daily.Col_date,FromDateToString(currentDate));
        values.put(Constant.daily.Col_daily_cost_morning,morningCost);
        values.put(Constant.daily.Col_daily_cost_night,nightCost);
        values.put(Constant.daily.Col_Total_cost,totalcost);
        values.put(Constant.daily.Col_Driver_Name,driver_name_text);
        values.put(Constant.daily.Col_Car_name,car_name_text);
        values.put(Constant.daily.Col_change_oil,change_oil_state);

        int response = operation.insertOperation(values,Constant.daily.Table_Uri);
        if (response == 1 ){
            car_name.setText("");
            driver_name.setText("");
            shift_name.setText("");
            daily_editText.setText("");
            kilo_editText.setText("");
            date_textView.setText("");
            description_editText.setText("");
            change_oil.setChecked(false);
            cost_editText.setText("");
            total_cost.setText("");
            Toast.makeText(getContext(),getString(R.string.add_daily_success)
                    , Toast.LENGTH_SHORT).show();
            scrollViewDaily.scrollTo(0,0);
        }else {
            Toast.makeText(getContext(),getString(R.string.check_data)
                    , Toast.LENGTH_SHORT).show();
        }
        Utility.Gona(dailyFormFragmentProgressBar);
    }



    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void updateTextViewOfDate() {
        String Format = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(Format);
        date_in_sql = sdf.format(calendar.getTime());
        date_textView.setText(date_in_sql);
        dateFromBundle = date_in_sql;
        currentDate = calendar.getTime();
    }

    private String FromDateToString(Date date) {
        String Format = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(Format);
        date_in_sql = sdf.format(date);
        return date_in_sql;
    }

    private double calculateTotal(double daily_editText, double cost_editText) {
        double Cost = daily_editText- cost_editText ;
        return Cost;
    }

    private void shiftListSetAdapter() {
        shiftList = new ArrayList<String>(Arrays.asList(new String[]{getResources().getString(R.string.morreing)
                ,getResources().getString(R.string.night)}));
        shiftAdapter =new ArrayAdapter<String>(getContext(),R.layout.car_driver_item,R.id.car_driver_item,shiftList);
        shift_list.setAdapter(shiftAdapter);
    }

    // carLoader is 0 and driverLoader is 1
    private void getCarOwnerFromCursor(Cursor data) {
        PersonData car;
        listCar = new ArrayList<>();
        if (data != null && data.getCount()>0){
            for (data.moveToFirst();!data.isAfterLast();data.moveToNext()){
                car = new PersonData(
                        data.getString(data.getColumnIndex(Constant.carOwners.Col_name)),
                        data.getInt(data.getColumnIndex(Constant.carOwners.Col_ID)));
                listCarsData.add(car);
                listCarId.add(car.getId());
                listCar.add(data.getString(data.getColumnIndex(Constant.carOwners.Col_name)));
            }

        }else{
            Toast.makeText(getContext(),getResources().getString(R.string.no_car_data)
                    , Toast.LENGTH_SHORT).show();
        }
    }
    private void getDriverFromCursor(Cursor data) {
        PersonData driver;
        listDriver = new ArrayList<>();
        if (data != null && data.getCount()>0){
            for (data.moveToFirst();!data.isAfterLast();data.moveToNext()){
                driver = new PersonData(
                        data.getString(data.getColumnIndex(Constant.driver.Col_name)),
                        data.getInt(data.getColumnIndex(Constant.driver.Col_ID)));
                listDriverData.add(driver);
                listDriverId.add(driver.getId());
                listDriver.add(data.getString(data.getColumnIndex(Constant.driver.Col_name)));
            }


        }else{
            Toast.makeText(getContext(),getResources().getString(R.string.no_driver_data)
                    , Toast.LENGTH_SHORT).show();
        }
    }

    // this method of Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id == 0){
            Uri table_uri=Constant.carOwners.Table_Uri;
            String whereStatement=Constant.carOwners.Col_remove +" = ? ";
            return new CursorLoader(getContext(),table_uri,
                    new String []{Constant.carOwners.Col_name,Constant.carOwners.Col_ID}
                    ,whereStatement,new String []{"0"},null);
        }else if(id == 1){
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
            case 0:
                getCarOwnerFromCursor(data);
                break;
            case 1:
                getDriverFromCursor(data);
                break;
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
