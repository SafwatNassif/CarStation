package com.example.safwat.carstation.Fragment;


import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.example.safwat.carstation.Database.Constant;
import com.example.safwat.carstation.Model.CollectedMonyModel;
import com.example.safwat.carstation.Model.PersonData;
import com.example.safwat.carstation.R;
import com.example.safwat.carstation.Utility;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyCollected extends Fragment implements
        View.OnClickListener,LoaderManager.LoaderCallbacks<Cursor>  {


    private LinearLayout dateContainer,printContainer,shiftContainer,progressBar,carContainer;
    private TextView dateTextView,shiftTextView,carListName;
    private ListView shiftListView,carList;
    private ArrayList<String> shiftListData ,carListDataName;
    private ArrayList<PersonData> carListOfNameAndId;
    private ArrayAdapter<String> shiftAdapter;
    private Calendar calendar;
    private Document document ;
    private String filepath;
    private String date_in_sql,date_in_month,date_in_year,date_in_day;
    private int testLoader=0,shift=0;
    private ArrayAdapter<String> carNameAdapter;
    private Font normal;
    private int car_id;
    private String LOG_CAT = DailyCollected.class.getSimpleName();
    private double total_cost_of_expense=0.0, total_cost_of_collection =0.0,total_cost_of_daily=0.0;


    public DailyCollected() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_daily_collected, container, false);
        define(rootView);
//        getCarList();
        shiftListSetAdapter();
        return rootView;
    }

    private void define(View rootView) {
        dateContainer = (LinearLayout) rootView.findViewById(R.id.container_of_collected_daily);
        printContainer = (LinearLayout) rootView.findViewById(R.id.print_collected_daily);
        shiftContainer = (LinearLayout) rootView.findViewById(R.id.shift_container_of_daily);
        progressBar  = (LinearLayout) rootView.findViewById(R.id.daily_collected__progressContainer);
//        carContainer  = (LinearLayout) rootView.findViewById(R.id.collectedMony_car_name_of_daily);


        dateTextView = (TextView) rootView.findViewById(R.id.date_of_collected_daily_textView);
        shiftTextView= (TextView) rootView.findViewById(R.id.shift_name_of_collected_daily);
//        carListName =  (TextView) rootView.findViewById(R.id.collectedMony_car_name_text_daily);

        shiftListView =(ListView) rootView.findViewById(R.id.shift_list_in_collected_daily);
//        carList = (ListView) rootView.findViewById(R.id.list_view_of_collectedMoney_in_daily);

        shiftListView.setVisibility(View.GONE);
//        carList.setVisibility(View.GONE);
        Utility.Gona(progressBar);

        dateContainer.setOnClickListener(this);
        printContainer.setOnClickListener(this);
        shiftContainer.setOnClickListener(this);
//        carContainer.setOnClickListener(this);

        normal = FontFactory.getFont("assets/arabicbold.ttf", BaseFont.IDENTITY_H, 12);

        shiftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                shiftTextView.setText(shiftListData.get(position));
                shift = position;
                shiftListView.setVisibility(View.GONE);
            }
        });
//        carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                carListName.setText(carListOfNameAndId.get(position).getName());
//                carList.setVisibility(View.GONE);
//                car_id = carListOfNameAndId.get(position).getId();
//            }
//        });

    }

//    private void getCarList() {
//        carListOfNameAndId = new ArrayList<PersonData>();
//        getActivity().getLoaderManager().initLoader(0,null,DailyCollected.this);
//    }

    private void shiftListSetAdapter() {
        shiftListData = new ArrayList<String>(Arrays.asList(new String[]{getResources().getString(R.string.morreing)
                , getResources().getString(R.string.night)}));

        shiftAdapter =new ArrayAdapter<String>(getContext(),R.layout.car_driver_item,R.id.car_driver_item,shiftListData);
        shiftListView.setAdapter(shiftAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // case Of datePicker
            case R.id.container_of_collected_daily:
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
            // case  of Shift container
            case  R.id.shift_container_of_daily:
                if (shiftListView.getVisibility()==View.VISIBLE){
                    shiftListView.setVisibility(View.GONE);
                }else{
                    shiftListView.setVisibility(View.VISIBLE);
                    setListViewHeightBasedOnChildren(shiftListView);
                }
                break;
            // case listOfCar
//            case R.id.collectedMony_car_name_of_daily:
//                if (carList.getVisibility()==0){
//                    carList.setVisibility(View.GONE);
//                }else{
//                    carList.setVisibility(View.VISIBLE);
//                    setListViewHeightBasedOnChildren(carList);
//                }
//                break;
            // case of print
            case R.id.print_collected_daily:
                    if (dateTextView.getText().equals("")
                            || shiftTextView.getText().equals("")){
                        Toast.makeText(getContext(),getContext().getResources()
                                .getString(R.string.check_data), Toast.LENGTH_SHORT).show();

                    }else{
                        Utility.visible(progressBar);
                        if (testLoader == 0) {
                            getActivity().getLoaderManager().initLoader(8, null, DailyCollected.this);
                            testLoader=1;
                        }else if (testLoader == 1){
                            getActivity().getLoaderManager().restartLoader(8, null, DailyCollected.this);
                        }
                    }
                break;
        }
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == 8){
            Uri table_uri= Constant.daily.Table_Uri;
            String  shiftcase;
            if (shift == 0){
                shiftcase = Constant.daily.Col_daily_cost_night;
            }else{
                shiftcase= Constant.daily.Col_daily_cost_morning;
            }
            String whereStatement=Constant.carOwners.Col_remove +" = ? AND  "+ Constant.daily.Col_date+" = ? AND "+
                    shiftcase+" = ?";

            String [] selectArg = {Constant.daily.Col_date,
                     Constant.daily.Col_decleration
                    ,Constant.daily.Col_daily_cost_morning
                    ,Constant.daily.Col_daily_cost_night
                    ,Constant.daily.Col_expense
                    ,Constant.daily.Col_Car_name
                    ,Constant.daily.Col_Driver_Name
                    ,Constant.daily.Col_Total_cost};
            // need linearLayout for car
            String [] ArgValues={"0",date_in_sql,"0.0"};
            // print to check if its correct.
            Log.v(LOG_CAT,"Where statement is : "+whereStatement);

            return new CursorLoader(getContext(),table_uri,
                    selectArg,whereStatement,ArgValues,Constant.daily.Col_Car_name);

        }
//        else if (id == 0){
//            Uri table_uri= Constant.carOwners.Table_Uri;
//            String whereStatement=Constant.carOwners.Col_remove +" = ? ";
//            return new CursorLoader(getContext(),table_uri,
//                    new String []{Constant.carOwners.Col_name,Constant.carOwners.Col_ID}
//                    ,whereStatement,new String []{"0"},null);
//        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case 0:
//                getCarOwnerFromCursor(data);
                break;
            case 8:
                getPrintedFile(data);
                getActivity().getLoaderManager().destroyLoader(8);
                break;

        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public PdfPCell createCell(String content, int colspan, int alignment) {
        PdfPCell cell = new PdfPCell(new Paragraph(content,normal));
        cell.setUseAscender(true);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment& Element.ALIGN_MIDDLE);
        cell.setVerticalAlignment(alignment&Element.ALIGN_JUSTIFIED);
        cell.setPaddingBottom(5);

        return cell;
    }

    private void getCarOwnerFromCursor(Cursor data) {
        PersonData car;
        carListDataName = new ArrayList<>();
        if (data != null && data.getCount() > 0){
            for (data.moveToFirst();!data.isAfterLast();data.moveToNext()){
                car = new PersonData(
                        data.getString(data.getColumnIndex(Constant.carOwners.Col_name)),
                        data.getInt(data.getColumnIndex(Constant.carOwners.Col_ID)));
                carListOfNameAndId.add(car);
                carListDataName.add(carListOfNameAndId.get(data.getPosition()).getName());
            }
            carNameAdapter = new ArrayAdapter<String>(getContext(), R.layout.car_driver_item
                    , R.id.car_driver_item, carListDataName);
            carList.setAdapter(carNameAdapter);
        }else{
            Toast.makeText(getContext(),getResources().getString(R.string.no_car_data)
                    , Toast.LENGTH_SHORT).show();
        }
    }

    private void getPrintedFile(Cursor data) {
        if (data.getCount() > 0 ){
            CollectedMonyModel[] object = new CollectedMonyModel[data.getCount()];
            if (shift==0){
                filepath = Environment.getExternalStorageDirectory() + "/"+"حصر يومى للفترة الصباحية"+date_in_sql+".pdf";
            }else {
                filepath = Environment.getExternalStorageDirectory() + "/"+"حصر يومى للفترة المسائية"+date_in_sql+".pdf";
            }
            Double daily=0.0;
            document = new Document(PageSize.A4, 10, 10, 10, 10);
            String date ;
            Log.v(LOG_CAT, "file path is : " + filepath);
            try {
                FileOutputStream file = new FileOutputStream(filepath);
                PdfWriter.getInstance(document, file);
                document.open();
                PdfPTable table = new PdfPTable(8);
                table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                table.addCell(createCell("م",1,Element.ALIGN_CENTER));
                table.addCell(createCell("السيارة",1,Element.ALIGN_CENTER));
                table.addCell(createCell("السائق",1,Element.ALIGN_CENTER));
                table.addCell(createCell("التاريخ",1,Element.ALIGN_CENTER));
                table.addCell(createCell("اليومية",1,Element.ALIGN_CENTER));
                table.addCell(createCell("المصروف",1,Element.ALIGN_CENTER));
                table.addCell(createCell("البيان",1,Element.ALIGN_CENTER));
                table.addCell(createCell("الصافى",1,Element.ALIGN_CENTER));

                int i=0 ;
                for (data.moveToFirst();!data.isAfterLast();data.moveToNext()){
                    if (data.getDouble(data.getColumnIndex(Constant.daily.Col_daily_cost_morning))== 0.0){
                        daily= data.getDouble(data.getColumnIndex(Constant.daily.Col_daily_cost_night));
                    }else{
                        daily=data.getDouble(data.getColumnIndex(Constant.daily.Col_daily_cost_morning));
                    }
                    object[i]= new CollectedMonyModel(
                            data.getString(data.getColumnIndex(Constant.daily.Col_date))
                            ,data.getString(data.getColumnIndex(Constant.daily.Col_Car_name))
                            ,data.getString(data.getColumnIndex(Constant.daily.Col_Driver_Name))
                            ,data.getDouble(data.getColumnIndex(Constant.daily.Col_expense))
                            ,data.getDouble(data.getColumnIndex(Constant.daily.Col_Total_cost))
                            ,daily
                            ,data.getString(data.getColumnIndex(Constant.daily.Col_decleration)));

                    table.addCell(createCell(String.valueOf(i+1),1,Element.ALIGN_CENTER));
                    table.addCell(createCell(object[i].getName(),1,Element.ALIGN_CENTER));
                    table.addCell(createCell(object[i].getDriver(),1,Element.ALIGN_CENTER));
                    table.addCell(createCell(object[i].getDate(),1,Element.ALIGN_CENTER));
                    table.addCell(createCell(object[i].getDaily()+"",1,Element.ALIGN_CENTER));
                    table.addCell(createCell(String.valueOf(object[i].getTotalExpense()),1,Element.ALIGN_CENTER));
                    table.addCell(createCell(object[i].getDecleration(),1,Element.ALIGN_CENTER));
                    table.addCell(createCell(String.valueOf(object[i].getFinalTotal()),1,Element.ALIGN_CENTER));
                    table.completeRow();

                    total_cost_of_collection += object[i].getFinalTotal();
                    total_cost_of_expense += object[i].getTotalExpense();
                    total_cost_of_daily += object[i].getDaily();
                    i++;
                }

                table.addCell(createCell("الإجمالى اليومية",2,Element.ALIGN_JUSTIFIED&Element.ALIGN_CENTER));
                table.addCell(createCell(String.valueOf(total_cost_of_daily),4,Element.ALIGN_CENTER));
                table.completeRow();

                table.addCell(createCell("الإجمالى المصروف",2,Element.ALIGN_JUSTIFIED&Element.ALIGN_CENTER));
                table.addCell(createCell(String.valueOf(total_cost_of_expense),4,Element.ALIGN_CENTER));
                table.completeRow();

                table.addCell(createCell("الإجمالى الصافى",2,Element.ALIGN_JUSTIFIED&Element.ALIGN_CENTER));
                table.addCell(createCell(String.valueOf(total_cost_of_collection),4,Element.ALIGN_CENTER));
                table.completeRow();

                table.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                table.setWidthPercentage(100);

                document.add(table);
                document.close();

//                carListName.setText("");
                shiftTextView.setText("");
                dateTextView.setText("");

                total_cost_of_expense=0.0;
                total_cost_of_collection=0.0;
                total_cost_of_daily=0.0;

                Toast.makeText(getContext(), getContext().getResources()
                        .getString(R.string.print_done), Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getContext(),
                    getContext().getResources().getString(R.string.no_printed_data)
                    , Toast.LENGTH_SHORT).show();
        }

        Utility.Gona(progressBar);

    }


}
