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
import android.widget.TextView;
import android.widget.Toast;

import com.example.safwat.carstation.Adapter.DailyAdapter;
import com.example.safwat.carstation.Database.Constant;
import com.example.safwat.carstation.Model.DailyDetailsObject;
import com.example.safwat.carstation.R;
import com.example.safwat.carstation.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyFragment extends Fragment implements
        View.OnClickListener,LoaderManager.LoaderCallbacks<Cursor> {

    private LinearLayout addButton;
    private TextView date_textView;
    private RecyclerView recyclerView ;
    private ArrayList<DailyDetailsObject> DailyList;
    private ArrayList<String> carList ,driverList;
    private String [] id_car,id_drivers;
    private DailyAdapter adapter;
    private LinearLayout dailyProgressBarContainer,search_view;
    private String LOG_CAT = DailyFragment.class.getSimpleName();
    private String date_in_sql,date_in_month,date_in_year;
    private static int Month,Year,testLoader=0 ;
    private Calendar calendar;

    public DailyFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // need fetch daily data , car data , driver data ;
        View rootView = inflater.inflate(R.layout.fragment_daily, container, false);
        define(rootView);
        RefreshDaily();
        addButton.setOnClickListener(this);
        search_view.setOnClickListener(this);
        return rootView;

    }

    private void define(View rootView) {
        addButton = (LinearLayout) rootView.findViewById(R.id.new_daily);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.daily_details_RecycleViewList);
        date_textView=(TextView)rootView.findViewById(R.id.search_view_text);

        dailyProgressBarContainer = (LinearLayout) rootView.findViewById(R.id.daily_progressContainer);
        search_view=(LinearLayout) rootView.findViewById(R.id.search_view);

        Utility.visible(dailyProgressBarContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        carList = new ArrayList<>();
        driverList = new ArrayList<>();
    }

    private  void RefreshDaily(){
        getActivity().getLoaderManager().initLoader(2,null,DailyFragment.this);
    }
    private void getDailyData(Cursor data) {
        DailyList = new ArrayList<DailyDetailsObject>();
        DailyDetailsObject object;
        Log.v(LOG_CAT, "Daily data is : " + data);
        String date;
        for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
            date = data.getString(data.getColumnIndex(Constant.daily.Col_date));
            if (date.contains(date_in_month) && date.contains(date_in_year)) {
                object = new DailyDetailsObject(
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
                Log.v(LOG_CAT, "date that retrive is : " +
                        data.getString(data.getColumnIndex(Constant.daily.Col_date)));
                DailyList.add(object);

            }
        }
        if (DailyList.size() > 0) {
            adapter = new DailyAdapter(getContext(), R.layout.daily_details_item, DailyList,
                    dailyProgressBarContainer,"daily");
            recyclerView.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(),"لايوجد لديك يوميات",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void SeperateListID() {
        if (DailyList.size()>0){
            id_car = new String[DailyList.size()];
            id_drivers = new String[DailyList.size()];
            for (int i=0; i< DailyList.size() ; i++){
                id_car[i] = ""+DailyList.get(i).getId_car();
                id_drivers[i]= ""+ DailyList.get(i).getId_driver();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_daily:
                Bundle bundle = new Bundle();
                bundle.putString("type","new");
                DailyFormFragment fragment = new DailyFormFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .add(R.id.daily_fragment_container,fragment)
                        .commit();
                 break;

            case R.id.search_view:
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
        Log.v(LOG_CAT,"dateMonth is : "+date_in_year);

        date_textView.setText(date_in_sql);
        RefreshListWithNewDate();
    }

    private void RefreshListWithNewDate() {
        Utility.visible(dailyProgressBarContainer);
        DailyList = new ArrayList<DailyDetailsObject>();

        if (testLoader == 1){
            getActivity().getLoaderManager().restartLoader(3,null,DailyFragment.this);
            Log.v(LOG_CAT,"loader 3 is restarted");
        }else {
            getActivity().getLoaderManager().initLoader(3, null, DailyFragment.this);
            testLoader = 1;
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri table_name = Constant.daily.Table_Uri;
        String whereStatement;
        if (id ==2) {
            // select all daily data
            whereStatement = Constant.daily.Col_Remove + " = ? ";
            return new CursorLoader(getContext(), table_name, Constant.daily.All_Column
                    , whereStatement, new String[]{"0"}, Constant.daily.Col_date);
        }else if(id == 3){
            // select all daily data in specific date
            whereStatement = "remove = ?  AND "+Constant.daily.Col_date+" = ? ";
            Log.v(LOG_CAT,"where statement is : "+whereStatement);
            return  new CursorLoader(getContext(),table_name,Constant.daily.All_Column
                    ,whereStatement, new String []{"0",date_in_sql},Constant.daily.Col_date);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == 3){
            if (data.getCount() >0 ){
                getDailyData(data);
            }else {
                Toast.makeText(getContext(),"لايوجد لديك يوميات",
                        Toast.LENGTH_SHORT).show();
            }
        }else if(loader.getId() == 2){
            if (data.getCount() >0 ){
                getDailyDataWithoutDate(data);
                getActivity().getLoaderManager().destroyLoader(2);

            }else {
                Toast.makeText(getContext(),"لايوجد لديك يوميات",
                        Toast.LENGTH_SHORT).show();
            }

        }
        Utility.Gona(dailyProgressBarContainer);
    }

    private void getDailyDataWithoutDate(Cursor data) {
        DailyList = new ArrayList<DailyDetailsObject>();
        DailyDetailsObject object;
        Log.v(LOG_CAT, "Daily data is : " + data);
        for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
                object = new DailyDetailsObject(
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
                Log.v(LOG_CAT, "date that retrive is : " +
                        data.getString(data.getColumnIndex(Constant.daily.Col_date)));
                DailyList.add(object);


        }
        if (DailyList.size() > 0) {
            adapter = new DailyAdapter(getContext(), R.layout.daily_details_item, DailyList,
                    dailyProgressBarContainer,"daily");
            recyclerView.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(),"لايوجد لديك يوميات",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
