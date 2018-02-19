package com.example.safwat.carstation.Fragment;


import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safwat.carstation.Adapter.MergeAdapter;
import com.example.safwat.carstation.Database.Constant;
import com.example.safwat.carstation.Model.MergeModle;
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

import static com.example.safwat.carstation.Fragment.DailyFormFragment.setListViewHeightBasedOnChildren;

/**
 * A simple {@link Fragment} subclass.
 */
public class MergeDaily extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {


    private static final String LOG_CAT = MergeDaily.class.getSimpleName() ;
    private LinearLayout merge_date,carsOwner,progressBar,print_merge;
    private TextView merge_date_text_view;
    private EditText mergeNameOwner;
    private ListView listView;
    private ListView shiftListView;
    private Typeface typeface;
    private Font normal;
    private ArrayList<String >carOwenrId;
    private ArrayList<Integer>carIdList;
    private ArrayList<PersonData> listCarsData;
    private ArrayList<String >listCar;
    private MergeAdapter mergeAdapter;
    private Calendar calendar;
    private int Month,Year,Day;
    private String OwnerName,filepath;
    private String date_in_sql,date_in_sql_m_y,date_in_month,date_in_year;
    private Double[] finalcost,finalexpense,finaldaily;
    private String [] finaldecleration;
    private String idOfMergeData;
    private static int testLoader=0;
    private Document document;
    private Double total_cost_of_daily=0.0,total_cost_of_collection=0.0,total_cost_of_expense=0.0;
    private ArrayList<String> shiftListData;
    private ArrayAdapter<String> shiftAdapter;
    private LinearLayout shiftContainer;
    private TextView shiftTextView;
    private int shift=0;
    private String date_in_day;

    public MergeDaily() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_merge_daily, container, false);
        define(rootView);
        shiftListSetAdapter();
        getCarList();

        merge_date.setOnClickListener(this);
        carsOwner.setOnClickListener(this);
        shiftContainer.setOnClickListener(this);
        print_merge.setOnClickListener(this);

        shiftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                shiftTextView.setText(shiftListData.get(position));
                shift = position;
                shiftListView.setVisibility(View.GONE);
            }
        });
        return rootView;
    }

    private void getCarList() {
        listCarsData = new ArrayList<PersonData>();
        Utility.visible(progressBar);
        getActivity().getLoaderManager().initLoader(0,null,MergeDaily.this);
    }

    private void define(View rootView) {
        merge_date= (LinearLayout) rootView.findViewById(R.id.merge_date_daily);
        carsOwner = (LinearLayout) rootView.findViewById(R.id.carOwner_merge_daily);
        progressBar = (LinearLayout) rootView.findViewById(R.id.mergeDailyProgressPar);
        print_merge = (LinearLayout) rootView.findViewById(R.id.printMergeDaily);
        shiftContainer = (LinearLayout) rootView.findViewById(R.id.shift_merge_daily);

        merge_date_text_view= (TextView) rootView.findViewById(R.id.merge_date_daily_textView);
        shiftTextView= (TextView) rootView.findViewById(R.id.shift_merge_daily_textView);

        mergeNameOwner=(EditText) rootView.findViewById(R.id.merge_name_daily_EditText);


        listView = (ListView) rootView.findViewById(R.id.carOwner_merge_daily_list);
        shiftListView =(ListView) rootView.findViewById(R.id.shift_merge_daily_list);

        listView.setVisibility(View.GONE);
        shiftListView.setVisibility(View.GONE);
        Utility.Gona(progressBar);

        typeface = Typeface.createFromAsset(getContext().getAssets(),"arabicbold.ttf");
        normal = FontFactory.getFont("assets/arabicbold.ttf", BaseFont.IDENTITY_H,12);
        carOwenrId = new ArrayList<>();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == 0){
            Uri table_uri= Constant.carOwners.Table_Uri;
            String whereStatement=Constant.carOwners.Col_remove +" = ? ";
            return new CursorLoader(getContext(),table_uri,
                    new String []{Constant.carOwners.Col_name,Constant.carOwners.Col_ID}
                    ,whereStatement,new String []{"0"},null);
        }
        else if(id == 9)
        {
            String shiftCase="";
            if (shift==0){
                shiftCase= Constant.daily.Col_daily_cost_night;
            }else {
                shiftCase=Constant.daily.Col_daily_cost_morning;
            }

            Uri table_uri = Constant.daily.Table_Uri;
            String []selectArg= new String[]{
                    Constant.daily.Col_carowners_id,
                    Constant.daily.Col_Car_name,
                    Constant.daily.Col_expense,
                    Constant.daily.Col_decleration,
                    Constant.daily.Col_daily_cost_morning,
                    Constant.daily.Col_daily_cost_night,
                    Constant.daily.Col_Total_cost
            };

            String whereStatement = Constant.daily.Col_Remove +" = ? AND "
                    +shiftCase+" = ? AND "+Constant.daily.Col_date+" = ? AND "
                    +Constant.daily.Col_carowners_id+" IN( "+idOfMergeData+" )";

            String [] SelectArgs=new String[]{"0","0.0",date_in_sql};

            Log.v(LOG_CAT,"Where statement is : "+whereStatement);
            return new CursorLoader(getContext(),table_uri,
                    selectArg,whereStatement,SelectArgs,null);
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
            case 9:
                getPrintedFile(data);
                getActivity().getLoaderManager().destroyLoader(9);
                break;

        }
        Utility.Gona(progressBar);
    }
    private void getPrintedFile(Cursor data) {
        if (data.getCount()>0){
            try {
                MergeModle[] object = new MergeModle[finalcost.length];
                filepath = Environment.getExternalStorageDirectory() + "/"+"دمج يومى خاصة "+OwnerName+".pdf";
                document = new Document(PageSize.A4, 10, 10, 10, 10);
                Log.v(LOG_CAT, "file path is : " + filepath);
                FileOutputStream file = new FileOutputStream(filepath);
                PdfWriter.getInstance(document, file);
                document.open();

                PdfPTable table = new PdfPTable(6);
                table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

                table.addCell(createCell("دمج يومى خاصة "+OwnerName,6,Element.ALIGN_CENTER));
                table.addCell(createCell("م",1,Element.ALIGN_CENTER));
                table.addCell(createCell("أسم السيارة",1,Element.ALIGN_CENTER));
                table.addCell(createCell("اليومية", 1,Element.ALIGN_CENTER));
                table.addCell(createCell("المصروف ", 1,Element.ALIGN_CENTER));
                table.addCell(createCell("البيان ", 1,Element.ALIGN_CENTER));
                table.addCell(createCell("ألصافى ", 1,Element.ALIGN_CENTER));
                Log.v(LOG_CAT,"carListId size is : "+carIdList.size());
                Log.v(LOG_CAT,"data retrieve  size is : "+data.getCount());

                for (data.moveToFirst();!data.isAfterLast();data.moveToNext()){
                    Integer id = data.getInt(data.getColumnIndex(Constant.daily.Col_carowners_id));
                    for (int i=0 ;i<finalcost.length;i++){
                        if (carIdList.get(i) == id){
                            object[i] = new MergeModle(
                                      data.getString(data.getColumnIndex(Constant.daily.Col_Car_name))
                                    , data.getDouble(data.getColumnIndex(Constant.daily.Col_Total_cost))
                                    , data.getDouble(data.getColumnIndex(Constant.daily.Col_daily_cost_morning))
                                    , data.getDouble(data.getColumnIndex(Constant.daily.Col_daily_cost_night))
                                    , data.getDouble(data.getColumnIndex(Constant.daily.Col_expense))
                                    , data.getString(data.getColumnIndex(Constant.daily.Col_decleration)));
                            finalcost[i] += object[i].getTotal();
                            finaldaily[i] += object[i].getDaily_morning() + object[i].getDaily_night();
                            finalexpense[i] += object[i].getExpense();
                            finaldecleration[i]+="+"+object[i].getDeclearation();
                            break;
                        }
                    }
                }

                ArrayList<String> carOwnerName = mergeAdapter.getCarOwenerName();

                for (int y = 0; y<finalcost.length ;y++){
                    table.addCell(createCell(String.valueOf(y+1),1,Element.ALIGN_CENTER));
                    table.addCell(createCell(carOwnerName.get(y),1,Element.ALIGN_CENTER));
                    table.addCell(createCell(String.valueOf(finaldaily[y]),1,Element.ALIGN_CENTER));
                    table.addCell(createCell(String.valueOf(finalexpense[y]),1,Element.ALIGN_CENTER));
                    table.addCell(createCell(finaldecleration[y].substring(1,finaldecleration[y].length()),1,Element.ALIGN_CENTER));
                    table.addCell(createCell(String.valueOf(finalcost[y]),1,Element.ALIGN_CENTER));

                    total_cost_of_daily+=finaldaily[y];
                    total_cost_of_collection +=finalcost[y];
                    total_cost_of_expense+=finalexpense[y];
                }

                table.addCell(createCell("الإجمالى اليومية ",1,Element.ALIGN_JUSTIFIED&Element.ALIGN_CENTER));
                table.addCell(createCell(String.valueOf(total_cost_of_daily),5,Element.ALIGN_CENTER));

                table.addCell(createCell("الإجمالى المصروف ",1,Element.ALIGN_JUSTIFIED&Element.ALIGN_CENTER));
                table.addCell(createCell(String.valueOf(total_cost_of_expense),5,Element.ALIGN_CENTER));

                table.addCell(createCell("الإجمالى الصافى ",1,Element.ALIGN_JUSTIFIED&Element.ALIGN_CENTER));
                table.addCell(createCell(String.valueOf(total_cost_of_collection),5,Element.ALIGN_CENTER));

                document.add(table);
                document.close();

                merge_date_text_view.setText("");
                mergeNameOwner.setText("");
                shiftTextView.setText("");

                total_cost_of_collection=0.0;
                total_cost_of_daily=0.0;
                total_cost_of_expense=0.0;
                Arrays.fill(finaldaily,0.0);
                Arrays.fill(finalcost,0.0);
                Arrays.fill(finalexpense,0.0);
                Arrays.fill(finaldecleration,"");
                listView.setVisibility(View.GONE);
                Utility.Gona(progressBar);
                Toast.makeText(getContext(), getContext().getResources()
                        .getString(R.string.print_done), Toast.LENGTH_SHORT).show();

            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else{
            Toast.makeText(getContext(),
                    getContext().getResources().getString(R.string.no_printed_data)
                    , Toast.LENGTH_SHORT).show();
        }
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
            mergeAdapter = new MergeAdapter(getContext(),listCarsData,R.layout.merge_dialog_item);
            listView.setAdapter(mergeAdapter);
            setListViewHeightBasedOnChildren(listView);
            Utility.Gona(progressBar);
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
        switch (v.getId()){

            case R.id.merge_date_daily:
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

            case R.id.shift_merge_daily:
                if (shiftListView.getVisibility()==View.VISIBLE){
                    shiftListView.setVisibility(View.GONE);
                }else{
                    shiftListView.setVisibility(View.VISIBLE);
                    setListViewHeightBasedOnChildren(shiftListView);
                }
                break;

            case R.id.carOwner_merge_daily:
                if (listView.getVisibility() == View.VISIBLE){
                    listView.setVisibility(View.GONE);
                }else{
                    listView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.printMergeDaily :
                if (mergeAdapter == null ){
                    Toast.makeText(getContext(), getContext().getResources()
                            .getString(R.string.check_data), Toast.LENGTH_SHORT).show();
                }else {
                    carOwenrId = mergeAdapter.getCarOwener_id();
                    carIdList = mergeAdapter.getCar_id();
                    OwnerName = mergeNameOwner.getText().toString();
                    if (carOwenrId.size() == 0 || merge_date_text_view.getText().toString().equals("")
                            || OwnerName.equals("") || OwnerName ==null) {
                        Toast.makeText(getContext(), getContext().getResources()
                                .getString(R.string.check_data), Toast.LENGTH_SHORT).show();
                    } else {
                        Utility.visible(progressBar);
                        String id = carOwenrId.toString();

                        finalcost = new Double[carOwenrId.size()];
                        finalexpense = new Double[carOwenrId.size()];
                        finaldaily = new Double[carOwenrId.size()];
                        finaldecleration = new String [carOwenrId.size()];
                        Arrays.fill(finalcost,0.0);
                        Arrays.fill(finalexpense,0.0);
                        Arrays.fill(finaldaily,0.0);
                        Arrays.fill(finaldecleration,"");
                        SelectAllMergeData(id);
                    }
                }
                break;

        }
    }

    private void SelectAllMergeData(String id) {
        idOfMergeData = id.substring(1,id.length()-1);
        Log.v(LOG_CAT,"id is : "+id);
        if (testLoader ==0 ) {
            getActivity().getLoaderManager().initLoader(9, null, MergeDaily.this);
            testLoader=1;
        }else if (testLoader == 1){
            getActivity().getLoaderManager().restartLoader(9, null, MergeDaily.this);
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
        merge_date_text_view.setText(date_in_sql);
    }

    private void shiftListSetAdapter() {
        shiftListData = new ArrayList<String>(Arrays.asList(new String[]{getResources().getString(R.string.morreing)
                , getResources().getString(R.string.night)}));

        shiftAdapter =new ArrayAdapter<String>(getContext(),R.layout.car_driver_item,R.id.car_driver_item,shiftListData);
        shiftListView.setAdapter(shiftAdapter);
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
}
