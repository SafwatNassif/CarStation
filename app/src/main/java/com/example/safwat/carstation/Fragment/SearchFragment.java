package com.example.safwat.carstation.Fragment;


import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safwat.carstation.Adapter.CarAndDriverAdapter;
import com.example.safwat.carstation.Adapter.DailyAdapter;
import com.example.safwat.carstation.Adapter.OilAdapter;
import com.example.safwat.carstation.Adapter.SearchAdapter;
import com.example.safwat.carstation.Database.Constant;
import com.example.safwat.carstation.Interface.CarOrDriverNameSelected;
import com.example.safwat.carstation.Model.DailyDetailsObject;
import com.example.safwat.carstation.Model.OilObject;
import com.example.safwat.carstation.Model.PersonData;
import com.example.safwat.carstation.R;
import com.example.safwat.carstation.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final String LOG_CAT = SearchFragment.class.getSimpleName();
    private LinearLayout DateLinear,CarNameLinear,ProgressBarLinear,SearchLinear;
    private TextView dateTextView,CarNameTextView;
    private RecyclerView recyclerView;
    private ArrayList<String > carList;
    private int testCarLoader=0,testdailyLoader=0;
    private ArrayList<PersonData> listCarsData;
    private int car_id;
    private Calendar calendar;
    private int Month=0,Year=0;
    private String date_in_sql,date_in_month,date_in_year,date_in_day;
    private ArrayList<DailyDetailsObject> carDataList;
    private DailyAdapter adapter;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_search, container, false);
        define(rootView);
        getCarData();

        return rootView;
    }



    private void define(View rootView) {
        DateLinear = (LinearLayout) rootView.findViewById(R.id.search_view_for_daily_date);
        CarNameLinear = (LinearLayout) rootView.findViewById(R.id.carOwner_name);
        ProgressBarLinear = (LinearLayout) rootView.findViewById(R.id.search_daily_progressContainer);
        SearchLinear =(LinearLayout) rootView.findViewById(R.id.search_option_daily_button);

        dateTextView= (TextView) rootView.findViewById(R.id.search_view_text_date_daily);
        CarNameTextView= (TextView) rootView.findViewById(R.id.carName);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.daily_details_RecycleViewList_daily);

        Utility.visible(ProgressBarLinear);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        CarNameLinear.setOnClickListener(this);
        DateLinear.setOnClickListener(this);
        SearchLinear.setOnClickListener(this);

    }

    private void getCarData() {
        listCarsData = new ArrayList<PersonData>();
        carList = new ArrayList<>();
        if (testCarLoader == 0) {
            getActivity().getLoaderManager().initLoader(0, null, SearchFragment.this);
            testCarLoader =1;
        }else{
            getActivity().getLoaderManager().restartLoader(0, null, SearchFragment.this);

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == 0){
            Uri table_uri= Constant.carOwners.Table_Uri;
            String whereStatement=Constant.carOwners.Col_remove +" = ? ";
            return new CursorLoader(getContext(),table_uri,
                    new String []{Constant.carOwners.Col_name,Constant.carOwners.Col_ID}
                    ,whereStatement,new String []{"0"},null);
        }else if (id == 10 ){
            Uri table_uri= Constant.daily.Table_Uri;

            String whereStatement = "remove = ?  AND "+Constant.daily.Col_date+" = ? AND "
                    +Constant.daily.Col_carowners_id +" = ? ";

//            String []SelectArgs= new String []{
//                            Constant.daily.Col_Driver_Name,
//                            Constant.daily.Col_driver_id,
//                            Constant.daily.Col_ID,
//                            Constant.daily.Col_daily_cost_morning,Constant.daily.Col_daily_cost_night,
//                            Constant.daily.Col_expense,Constant.daily.Col_Total_cost,Constant.daily.Col_decleration};

            String [] WhereValues= new String []{"0",date_in_sql, String.valueOf(car_id)};
            Log.v(LOG_CAT,"where statement is : "+whereStatement);
            return  new CursorLoader(getContext(),table_uri,Constant.daily.All_Column
                    ,whereStatement,WhereValues,null);

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case 0:
                getCarOwnerFromCursor(data);
                Utility.Gona(ProgressBarLinear);
                getActivity().getLoaderManager().destroyLoader(0);
                break;
            case 10 :
                getDailyDataFromSearch(data);
                getActivity().getLoaderManager().destroyLoader(10);
                break;
        }
    }

    private void getDailyDataFromSearch(Cursor data) {
        if (data.getCount()>0){
            DailyDetailsObject searchResult;
            carDataList = new ArrayList<DailyDetailsObject>();
            for (data.moveToFirst();!data.isAfterLast();data.moveToNext()){

                searchResult = new DailyDetailsObject(
                        data.getString(data.getColumnIndex(Constant.daily.Col_Car_name)),
                        data.getString(data.getColumnIndex(Constant.daily.Col_Driver_Name)),
                        data.getString(data.getColumnIndex(Constant.daily.Col_decleration)),
                        data.getString(data.getColumnIndex(Constant.daily.Col_date)),
                        data.getInt(data.getColumnIndex(Constant.daily.Col_carowners_id)),
                        data.getInt(data.getColumnIndex(Constant.daily.Col_driver_id)),
                        data.getInt(data.getColumnIndex(Constant.daily.Col_ID)),
                        data.getInt(data.getColumnIndex(Constant.daily.Col_change_oil)),
                        data.getDouble(data.getColumnIndex(Constant.daily.Col_daily_cost_morning)),
                        data.getDouble(data.getColumnIndex(Constant.daily.Col_daily_cost_night)),
                        data.getDouble(data.getColumnIndex(Constant.daily.Col_Total_cost)),
                        data.getDouble(data.getColumnIndex(Constant.daily.Col_expense)),
                        data.getDouble(data.getColumnIndex(Constant.daily.Col_kilos))
                        );
                carDataList.add(searchResult);
            }

        }else{
            Toast.makeText(getContext(),
                    getContext().getResources().getString(R.string.NoDaily)
                    , Toast.LENGTH_SHORT).show();
            carDataList.clear();
        }
        adapter = new DailyAdapter(getContext(), R.layout.search_result_item, carDataList,
                ProgressBarLinear,"search");
        recyclerView.setAdapter(adapter);
        Utility.Gona(ProgressBarLinear);

    }

    private void getCarOwnerFromCursor(Cursor data) {
        PersonData car;
        carList = new ArrayList<>();
        if (data != null && data.getCount()>0){
            for (data.moveToFirst();!data.isAfterLast();data.moveToNext()){
                car = new PersonData(
                        data.getString(data.getColumnIndex(Constant.carOwners.Col_name)),
                        data.getInt(data.getColumnIndex(Constant.carOwners.Col_ID)));
                listCarsData.add(car);
                carList.add(data.getString(data.getColumnIndex(Constant.carOwners.Col_name)));
            }

        }else{
            Toast.makeText(getContext(),getResources().getString(R.string.no_car_data)
                    , Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.carOwner_name:
                Utility.showDialog(getContext(), getResources().getString(R.string.car_name)
                        , carList, "car", new CarOrDriverNameSelected() {
                            @Override
                            public void carOrDriverNameSpecified(String name, String type, int position) {
                                if (type.equals("car")) {
                                    CarNameTextView.setText(name);
                                    car_id = listCarsData.get(position).getId();
                                 }
                            }
                        });
                break;
            case R.id.search_view_for_daily_date:
                calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month++);
                        Month = month;
                        Year = year;
                        Log.v(LOG_CAT, "month in onDataSet : " + Month);
                        Log.v(LOG_CAT, "year in onDataSet : " + Year);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateTextViewOfDate();
                    }
                };
                new DatePickerDialog(getContext(),date,calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.search_option_daily_button:
                if (dateTextView.getText().equals("")||dateTextView.getText() == null
                        || CarNameTextView.getText().equals("")||CarNameTextView.getText() == null){
                    Toast.makeText(getContext(),getContext().getResources().getString(R.string.check_data)
                            , Toast.LENGTH_SHORT).show();
                }else{
                    Utility.visible(ProgressBarLinear);
                    if (testdailyLoader == 0) {
                        getActivity().getLoaderManager().initLoader(10, null, SearchFragment.this);
                        testdailyLoader=1;
                    }else  if (testdailyLoader == 1){
                        getActivity().getLoaderManager().restartLoader(10, null, SearchFragment.this);
                    }
                }
                break;

        }
    }

    private void updateTextViewOfDate() {
        String Format = "dd-MM-yyyy";
        String FormatMonth="MM";
        String FormatYear="yyyy";
        String FormatDay="dd";
        SimpleDateFormat sdf = new SimpleDateFormat(Format);
        date_in_sql = sdf.format(calendar.getTime());
        Log.v(LOG_CAT,"date is : "+date_in_sql);

        SimpleDateFormat sdfd = new SimpleDateFormat(FormatDay);
        date_in_day =sdfd.format(calendar.getTime());
        Log.v(LOG_CAT,"dateDaily is : "+date_in_day);

        SimpleDateFormat sdfm = new SimpleDateFormat(FormatMonth);
        date_in_month =sdfm.format(calendar.getTime());
        Log.v(LOG_CAT,"dateMonthly is : "+date_in_month);

        SimpleDateFormat sdfy = new SimpleDateFormat(FormatYear);
        date_in_year =sdfy.format(calendar.getTime());
        Log.v(LOG_CAT,"dateYear is : "+date_in_year);
        dateTextView.setText(date_in_sql);
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

}
