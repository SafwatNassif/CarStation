package com.example.safwat.carstation.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by safwat on 18/10/17.
 */

public class CreateDatabase extends SQLiteOpenHelper {

    private static final String LOG_CAT = CreateDatabase.class.getSimpleName();

    public CreateDatabase(Context context) {
        // this code used to save data in storage in mobile.
//          super(context,"/storage/sdcard0/Demain/"
//          +Constant.Database_Name,null,Constant.Database_Version);

        //this code for used to save data in storage in emulator.
        super(context,Environment.getExternalStorageDirectory()+ File.separator+"Demian"
            +File.separator+Constant.Database_Name,null,Constant.Database_Version);

////        this code for used to save data in emulator.
//        super(context,Constant.Database_Name,null,Constant.Database_Version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
            String  Create_Table_CarOwner="CREATE TABLE "+Constant.Table_carOwners +"( "
                    +Constant.carOwners.Col_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "
                    +Constant.carOwners.Col_name+" TEXT NOT NULL , "
                    +Constant.carOwners.Col_remove+" INTEGER DEFAULT 0  ); ";

            String  Create_Table_driver="CREATE TABLE "+Constant.Table_driver +"( "
                    +Constant.driver.Col_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "
                    +Constant.driver.Col_name+" TEXT NOT NULL , "
                    +Constant.driver.Col_remove+" INTEGER DEFAULT 0 ); ";

            String  Create_Table_daily="CREATE TABLE "+Constant.Table_daily +"( "
                    +Constant.daily.Col_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "
                    +Constant.daily.Col_carowners_id+" INTEGER  NOT NULL , "
                    +Constant.daily.Col_driver_id+" INTEGER NOT NULL , "
                    +Constant.daily.Col_Car_name+" TEXT NOT NULL ,"
                    +Constant.daily.Col_Driver_Name+" TEXT NOT NULL , "
                    +Constant.daily.Col_daily_cost_morning+" DOUBLE DEFAULT 0 ,"
                    +Constant.daily.Col_daily_cost_night+" DOUBLE DEFAULT 0 ,"
                    +Constant.daily.Col_kilos+" DOUBLE DEFAULT 0 ,"
                    +Constant.daily.Col_expense+" DOUBLE DEFAULT 0 ,"
                    +Constant.daily.Col_Total_cost+" DOUBLE DEFAULT 0 ,"
                    +Constant.daily.Col_date+" DATE NOT NULL ,"
                    +Constant.daily.Col_decleration+" TEXT , "
                    +Constant.daily.Col_change_oil+" INTEGER DEFAULT 0 ,"
                    +Constant.daily.Col_Remove+" INTEGER  DEFAULT 0 "+"); ";

            Log.v(LOG_CAT,"carOwner table is created : "+Create_Table_CarOwner);
            db.execSQL(Create_Table_CarOwner);
            Log.v(LOG_CAT,"carOwner table is created : "+Create_Table_CarOwner);


            db.execSQL(Create_Table_driver);
            Log.v(LOG_CAT,"carOwner table is created : "+Create_Table_driver);


            db.execSQL(Create_Table_daily);
            Log.v(LOG_CAT,"carOwner table is created : "+Create_Table_daily);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXIST "+Constant.Table_carOwners);
            Log.v(LOG_CAT,"carOwner table is drop : ");

            db.execSQL("DROP TABLE IF EXIST "+Constant.Table_driver);
            Log.v(LOG_CAT,"driver table is drop : ");

            db.execSQL("DROP TABLE IF EXIST "+Constant.Table_daily);
            Log.v(LOG_CAT,"daily table is drop : ");
    }
}
