package com.example.safwat.carstation.Fragment;


import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safwat.carstation.Adapter.OilAdapter;
import com.example.safwat.carstation.Database.Constant;
import com.example.safwat.carstation.Interface.RefreshCallBackCarAndDriver;
import com.example.safwat.carstation.Model.OilObject;
import com.example.safwat.carstation.Model.PersonData;
import com.example.safwat.carstation.R;
import com.example.safwat.carstation.Refresh;
import com.example.safwat.carstation.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.safwat.carstation.Fragment.DailyFormFragment.setListViewHeightBasedOnChildren;

/**
 * A simple {@link Fragment} subclass.
 */
public class OilFragment extends Fragment implements
        View.OnClickListener,LoaderManager.LoaderCallbacks<Cursor> {

    private LinearLayout dateLayear,carLayear,searchLayear,progressBarLayear;
    private TextView date_textView , carNameLayear;
    private ListView listView,searchResult;
    private SharedPreferences sharedPreferences;
    private String car_name;
    private int user_id;
    private ArrayList<OilObject> carDataList;
    private ArrayList<PersonData> listCarsData;
    private ArrayList<String > listCar;
    private ArrayAdapter<String> carNameAdapter;
    private Calendar calendar;
    private int Month,Year;
    private String LOG_CAT = OilFragment.class.getSimpleName();
    private String date_in_sql;
    private int car_id;
    private static int TestLoader=0;
    private OilAdapter adpter;
    private String date_in_month,date_in_year;

    public OilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_oil, container, false);
        define(rootView);
        dateLayear.setOnClickListener(this);
        carLayear.setOnClickListener(this);
        searchLayear.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                car_name=listCarsData.get(position).getName();
                carNameLayear.setText(car_name);
                car_id= listCarsData.get(position).getId();
                listView.setVisibility(View.GONE);
            }
        });

//        getCarList();
        return rootView;
    }

    private void define(View rootView) {
        dateLayear = (LinearLayout) rootView.findViewById(R.id.oil_date);
        carLayear= (LinearLayout) rootView.findViewById(R.id.carOwner_oil);
        searchLayear= (LinearLayout) rootView.findViewById(R.id.search_option);
        progressBarLayear= (LinearLayout) rootView.findViewById(R.id.oilProgressPar);

        progressBarLayear.setVisibility(View.GONE);

        listView =(ListView)rootView.findViewById(R.id.car_that_changed_oil);
        listView.setVisibility(View.GONE);


        date_textView=(TextView) rootView.findViewById(R.id.oil_date_textView);
        carNameLayear=(TextView) rootView.findViewById(R.id.oil_textView);

//        sharedPreferences = getActivity().getSharedPreferences(Constant.Table_user, Context.MODE_PRIVATE);
//        user_token = sharedPreferences.getString(Constant.user.COL_TOKEN,null);
//        user_id=sharedPreferences.getInt(Constant.user.COL_ID,0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.oil_date:
                calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month++);
                        Month=month; Year = year;
                        Log.v(LOG_CAT,"month in onDataSet : "+Month);
                        Log.v(LOG_CAT,"year in onDataSet : "+Year);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateTextViewOfDate();

                    }
                };
                new DatePickerDialog(getContext(),date,calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.carOwner_oil:

                if (listView.getVisibility()==0){
                    listView.setVisibility(View.GONE);
                }else{
                    Utility.visible(progressBarLayear);
                    getCarList();
                    setListViewHeightBasedOnChildren(listView);
                    listView.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.search_option:
                    if (date_textView.getText().equals("")||date_textView.getText() == null
                            || carNameLayear.getText().equals("")||carNameLayear.getText() == null){
                        Toast.makeText(getContext(),getContext().getResources().getString(R.string.check_data)
                                , Toast.LENGTH_SHORT).show();
                    }else{
                        Utility.visible(progressBarLayear);
                        if (TestLoader == 0) {
                            getActivity().getLoaderManager().initLoader(5, null, OilFragment.this);
                            TestLoader=1;
                        }else  if (TestLoader == 1){
                            getActivity().getLoaderManager().restartLoader(5, null, OilFragment.this);

                        }
                    }
                break;

        }
    }

    private void updateTextViewOfDate() {
        String Format = "dd-MM-yyyy";
        String FormatMonth="MM";
        String FormatYear="yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat(Format);
        date_in_sql = sdf.format(calendar.getTime());
        Log.v(LOG_CAT,"date is : "+date_in_sql);

        SimpleDateFormat sdfm = new SimpleDateFormat(FormatMonth);
        date_in_month =sdfm.format(calendar.getTime());
        Log.v(LOG_CAT,"dateMonth is : "+date_in_month);

        SimpleDateFormat sdfy = new SimpleDateFormat(FormatYear);
        date_in_year =sdfy.format(calendar.getTime());
        Log.v(LOG_CAT,"dateYear is : "+date_in_year);
        date_textView.setText(date_in_sql);
    }

    private void getCarList() {
        listCarsData = new ArrayList<PersonData>();
        listCar = new ArrayList<>();
        getActivity().getLoaderManager().initLoader(0,null,OilFragment.this);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == 0){
            Uri table_uri= Constant.carOwners.Table_Uri;
            String whereStatement=Constant.carOwners.Col_remove +" = ? ";
            return new CursorLoader(getContext(),table_uri,
                    new String []{Constant.carOwners.Col_name,Constant.carOwners.Col_ID}
                    ,whereStatement,new String []{"0"},null);
        }else if (id == 5) {

            Uri table_uri= Constant.daily.Table_Uri;
            String whereStatement =Constant.daily.Col_Remove + " = ? AND "+Constant.daily.Col_Car_name
                    +" =? AND "+Constant.daily.Col_change_oil+" = ? ";

//            String whereStatement= Constant.carOwners.Col_remove +" = ? AND "
//                    + Constant.daily.Col_Car_name
//                    + " = ? AND strftime('%m',date) = '"+date_in_month+"' AND strftime('%Y',date) = '"
//                    + date_in_year+"' AND "
//                    + Constant.daily.Col_change_oil+" = ? ";

            String []SelectArg={Constant.daily.Col_Driver_Name,Constant.daily.Col_date};
            String []ArgValues={"0",car_name,"1"};
            Log.v(LOG_CAT,"where statement of oil is: "+whereStatement);

            return new CursorLoader(getContext(),table_uri,
                    SelectArg,whereStatement,ArgValues,null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case 0:
                getCarOwnerFromCursor(data);
                Utility.Gona(progressBarLayear);
                break;
            case 5:
                getOilData(data);
                // because when edit change oil in daily form fragment it come to this loader  so
                // we need  to  destroy this cursor after finish
                getActivity().getLoaderManager().destroyLoader(5);
                break;
        }
    }

    private void getOilData(Cursor data) {
        if (data.getCount()>0){
            OilObject oildata;
            carDataList = new ArrayList<OilObject>();
            String date ;
            for (data.moveToFirst();!data.isAfterLast();data.moveToNext()){
                 date = data.getString(data.getColumnIndex(Constant.daily.Col_date));
                 if (date.contains(date_in_month)&& date.contains(date_in_year)) {
                    oildata = new OilObject(
                            data.getString(data.getColumnIndex("driver_name"))
                            , data.getString(data.getColumnIndex("date")));
                    carDataList.add(oildata);
                }
            }
            adpter = new OilAdapter(getContext(),R.layout.oil_list_item,carDataList);
            listView.setAdapter(adpter);
            listView.setVisibility(View.VISIBLE);
            setListViewHeightBasedOnChildren(listView);

        }else{
            Toast.makeText(getContext(),
                    getContext().getResources().getString(R.string.no_oil_data)
                    , Toast.LENGTH_SHORT).show();
        }
        Utility.Gona(progressBarLayear);

    }

    private void getCarOwnerFromCursor(Cursor data) {
        PersonData car;
        listCar = new ArrayList<>();
        if (data != null && data.getCount()>0){
            for (data.moveToFirst();!data.isAfterLast();data.moveToNext()){
                car = new PersonData(
                        data.getString(data.getColumnIndex(Constant.carOwners.Col_name)),
                        data.getInt(data.getColumnIndex(Constant.carOwners.Col_ID)));
                listCarsData.add(car);
                listCar.add(data.getString(data.getColumnIndex(Constant.carOwners.Col_name)));
            }
            carNameAdapter = new ArrayAdapter<String>(getContext(),R.layout.car_driver_item
                    ,R.id.car_driver_item,listCar);
            listView.setAdapter(carNameAdapter);
        }else{
            Toast.makeText(getContext(),getResources().getString(R.string.no_car_data)
                    , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
