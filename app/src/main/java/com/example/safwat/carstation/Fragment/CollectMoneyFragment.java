package com.example.safwat.carstation.Fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.content.Loader;
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
import com.example.safwat.carstation.Interface.CarOrDriverNameSelected;
import com.example.safwat.carstation.Model.CollectedMonyModel;
import com.example.safwat.carstation.Model.PersonData;
import com.example.safwat.carstation.R;
import com.example.safwat.carstation.Utility;
import com.example.safwat.carstation.WebServer.VolleyConnectionRequest;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.VerticalText;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
/**
 * A simple {@link Fragment} subclass.
 */
public class CollectMoneyFragment extends Fragment
        implements View.OnClickListener,LoaderManager.LoaderCallbacks<Cursor> {
    private Calendar calendar;
    private LinearLayout collectedMoney_date,collectedMoney_car_name,print,Container_Progress;
    private TextView collectedMoney_date_textView,collectedMoney_car_name_textView;
    private String date_in_sql;
    private String LOG_CAT =CollectMoneyFragment.class.getSimpleName();
    private int car_id,Month=0,Year=0;
    private ArrayList<PersonData> listCarsData;
    private ArrayList<String> listCar;
    private ArrayAdapter<String> carNameAdapter;
    private Document document ;
    private String filepath;
    private Typeface typeface ;
    private Font normal;
    private Double total_cost_of_collection=0.0,total_cost_of_expense=0.0,total_cost_of_daily=0.0;
    private String date_in_month;
    private String date_in_year;
    private String date_in_day;
    private static int testLoader =0;
    private String nameSpecified="";

    public CollectMoneyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_collect_money, container, false);
        define(rootView);
        collectedMoney_car_name.setOnClickListener(this);
        collectedMoney_date.setOnClickListener(this);
        print.setOnClickListener(this);


        getCarList();
        return rootView;
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
    private void define(View rootView) {
        collectedMoney_car_name = (LinearLayout) rootView.findViewById(R.id.collectedMony_car_name);
        collectedMoney_date = (LinearLayout) rootView.findViewById(R.id.collectedMony_date);
        print =(LinearLayout) rootView.findViewById(R.id.printFileOfCollectMoney);
        Container_Progress =(LinearLayout) rootView.findViewById(R.id.container_of_progress_in_collected_money);

        collectedMoney_car_name_textView = (TextView) rootView.findViewById(R.id.collectedMony_car_name_text);
        collectedMoney_date_textView = (TextView) rootView.findViewById(R.id.collectedMony_date_textView);

        Utility.Gona(Container_Progress);

        typeface = Typeface.createFromAsset(getContext().getAssets(),"arabicbold.ttf");
        normal = FontFactory.getFont("assets/arabicbold.ttf", BaseFont.IDENTITY_H,12);
    }
    private void getCarList() {
        listCarsData = new ArrayList<PersonData>();
        listCar = new ArrayList<>();
        getActivity().getLoaderManager().initLoader(0,null,CollectMoneyFragment.this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.collectedMony_car_name:
                Utility.showDialog(getContext(),getResources().getString(R.string.car_name)
                        ,listCar,"car", new CarOrDriverNameSelected() {
                            @Override
                            public void carOrDriverNameSpecified(String name, String type, int position) {
                                if (type.equals("car")){
                                    collectedMoney_car_name_textView.setText(name);
                                    car_id= listCarsData.get(position).getId();
                                    nameSpecified = name ;

                                }
                            }
                        });

                break;
            case R.id.collectedMony_date:
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

            case R.id.printFileOfCollectMoney:
                if (collectedMoney_date_textView.getText().equals("")||
                        collectedMoney_car_name_textView.getText().equals("")){
                    Toast.makeText(getContext(),getContext().getResources()
                            .getString(R.string.check_data), Toast.LENGTH_SHORT).show();
                }else {
                    // print file into sdcard
                    Utility.visible(Container_Progress);
                    if (testLoader ==0) {
                        getActivity().getLoaderManager().initLoader(4, null, CollectMoneyFragment.this);
                        testLoader=1;
                    }else if (testLoader == 1){
                        getActivity().getLoaderManager().restartLoader(4, null, CollectMoneyFragment.this);
                    }
                        break;
        }
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

        collectedMoney_date_textView.setText(date_in_sql);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == 0){
            Uri table_uri= Constant.carOwners.Table_Uri;
            String whereStatement=Constant.carOwners.Col_remove +" = ? ";
            return new CursorLoader(getContext(),table_uri,
                    new String []{Constant.carOwners.Col_name,Constant.carOwners.Col_ID}
                    ,whereStatement,new String []{"0"},null);
        }else if (id == 4){
            Uri table_uri= Constant.daily.Table_Uri;
            table_uri = table_uri.buildUpon().appendPath(Constant.daily.Col_date).build();

            String whereStatement=Constant.carOwners.Col_remove +" = ? AND "+Constant.daily.Col_carowners_id
                    +" = ? ";

            String [] selectArg = {Constant.daily.Col_date,
                    "group_concat("+Constant.daily.Col_decleration+") as decleration",
                    "SUM("+Constant.daily.Col_daily_cost_morning+") as morning"
            ,"SUM("+Constant.daily.Col_daily_cost_night+") as night"
            ,"SUM("+Constant.daily.Col_expense+") as total_expense"
            ,"SUM("+Constant.daily.Col_Total_cost+") as finalTotal"};
            String [] ArgValues={"0", String.valueOf(car_id)};
            // print to check if its correct.
            Log.v(LOG_CAT,"Where statement is : "+whereStatement);

            return new CursorLoader(getContext(),table_uri,
                   selectArg,whereStatement,ArgValues,null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case 0:
                getCarOwnerFromCursor(data);
                getActivity().getLoaderManager().destroyLoader(0);
                break;
            case 4:
                getPrintedFile(data);
                getActivity().getLoaderManager().destroyLoader(4);
                break;

        }
    }

    private void getPrintedFile(Cursor data) {
        if (data.getCount() > 0 ){
             CollectedMonyModel[] object = new CollectedMonyModel[data.getCount()];
             filepath = Environment.getExternalStorageDirectory() + "/"+"حصر شهرى خاصة "+nameSpecified+".pdf";

            document = new Document(PageSize.A4, 10, 10, 10, 10);
            String date ;
            Log.v(LOG_CAT, "file path is : " + filepath);
            try {
                FileOutputStream file = new FileOutputStream(filepath);
                PdfWriter.getInstance(document, file);
                                            document.open();
                                            PdfPTable table = new PdfPTable(8);
                                            table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                                            table.addCell(createCell("حصر شهرى خاصة "+nameSpecified,8,Element.ALIGN_CENTER));
                                            table.addCell(createCell("م",1,Element.ALIGN_CENTER));
                                            table.addCell(createCell("التاريخ",1,Element.ALIGN_CENTER));
                                            table.addCell(createCell("اليومية صباحاً",1,Element.ALIGN_CENTER));
                                            table.addCell(createCell("اليومية مساءً",1,Element.ALIGN_CENTER));
                                            table.addCell(createCell("اليومية",1,Element.ALIGN_CENTER));
                                            table.addCell(createCell("المصروف",1,Element.ALIGN_CENTER));
                                            table.addCell(createCell("البيان",1,Element.ALIGN_CENTER));
                                            table.addCell(createCell("الصافى",1,Element.ALIGN_CENTER));

                int i=0 ;
                for (data.moveToFirst();!data.isAfterLast();data.moveToNext()){
                        date = data.getString(data.getColumnIndex(Constant.daily.Col_date));
                        if (date.contains(date_in_month) && date.contains(date_in_year)){
                            object[i]= new CollectedMonyModel(
                                         data.getString(data.getColumnIndex(Constant.daily.Col_date))
                                        ,data.getDouble(data.getColumnIndex("morning"))
                                        ,data.getDouble(data.getColumnIndex("night"))
                                        ,data.getDouble(data.getColumnIndex("total_expense"))
                                        ,data.getDouble(data.getColumnIndex("finalTotal"))
                                        ,data.getString(data.getColumnIndex("decleration")));

                                int x =i;
                                x++;
                                table.addCell(createCell(String.valueOf(x),1,Element.ALIGN_CENTER));
                                table.addCell(createCell(object[i].getDate(),1,Element.ALIGN_CENTER));
                                table.addCell(createCell(String.valueOf(object[i].getMoring()),1,Element.ALIGN_CENTER));
                                table.addCell(createCell(String.valueOf(object[i].getNight()),1,Element.ALIGN_CENTER));
                                table.addCell(createCell(String.valueOf(object[i].getMoring()+object[i].getNight()),1,Element.ALIGN_CENTER));
                                table.addCell(createCell(String.valueOf(object[i].getTotalExpense()),1,Element.ALIGN_CENTER));
                                table.addCell(createCell(String.valueOf(object[i].getDecleration()),1,Element.ALIGN_CENTER));
                                table.addCell(createCell(String.valueOf(object[i].getFinalTotal()),1,Element.ALIGN_CENTER));
                                table.completeRow();

                                total_cost_of_collection += object[i].getFinalTotal();
                                total_cost_of_expense += object[i].getTotalExpense();
                                total_cost_of_daily += object[i].getMoring()+object[i].getNight();
                                i++;
                    }

                }
                table.addCell(createCell("الإجمالى اليومية",2,Element.ALIGN_JUSTIFIED&Element.ALIGN_CENTER));
                table.addCell(createCell(String.valueOf(total_cost_of_daily),5,Element.ALIGN_CENTER));
                table.completeRow();

                table.addCell(createCell("الإجمالى المصروف",2,Element.ALIGN_JUSTIFIED&Element.ALIGN_CENTER));
                table.addCell(createCell(String.valueOf(total_cost_of_expense),5,Element.ALIGN_CENTER));
                table.completeRow();

                table.addCell(createCell("الإجمالى الصافى",2,Element.ALIGN_JUSTIFIED&Element.ALIGN_CENTER));
                table.addCell(createCell(String.valueOf(total_cost_of_collection),5,Element.ALIGN_CENTER));
                table.completeRow();

                table.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                table.setWidthPercentage(100);

                document.add(table);
                document.close();

                collectedMoney_car_name_textView.setText("");
                collectedMoney_date_textView.setText("");

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

        Utility.Gona(Container_Progress);
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

        }else{
            Toast.makeText(getContext(),getResources().getString(R.string.no_car_data)
                    , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public PdfPCell createCell(String content, int colspan, int alignment) {
        PdfPCell cell = new PdfPCell(new Paragraph(content,normal));
        cell.setUseAscender(true);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment&Element.ALIGN_MIDDLE);
        cell.setVerticalAlignment(alignment&Element.ALIGN_JUSTIFIED);
        cell.setPaddingBottom(5);

        return cell;
    }
}

