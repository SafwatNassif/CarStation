package com.example.safwat.carstation.Database;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by safwat on 28/05/17.
 */

public class Constant {

    public static final String Database_Name="carStation.db";
    public static final int  Database_Version=1;
    public static final String PackageName = "com.example.safwat.carstation";
    public static final Uri BaseURI = Uri.parse("content://"+PackageName);
    public static final String Table_carOwners ="carowners";
    public static final String Table_daily ="daily";
    public static final String Table_driver ="driver";
    public static final String Table_user ="user" ;
    public static class carOwners implements BaseColumns{
        public static final Uri Table_Uri = BaseURI.buildUpon().appendPath(Table_carOwners).build();
        public static final String Col_ID = "id";
        public static final String Col_name="name";
        public static final String Col_remove = "remove";
        public static final String  [] All_Column ={Col_ID,Col_name,Col_remove};
    }

    public static class daily implements BaseColumns{
        public static final Uri Table_Uri = BaseURI.buildUpon().appendPath(Table_daily).build();
        public static final String Col_ID = "id";
        public static final String Col_carowners_id = "carowners_id";
        public static final String Col_driver_id ="driver_id";
        public static final String Col_daily_cost_morning = "daily_cost_morning";
        public static final String Col_daily_cost_night = "daily_cost_night";
        public static final String Col_kilos = "kilos";
        public static final String Col_decleration = "decleration";
        public static final String Col_expense = "expense";
        public static final String Col_date = "date";
        public static final String Col_Total_cost = "total_cost";
        public static final String Col_Driver_Name="driver_name";
        public static final String Col_Car_name="car_name";
        public static final String Col_Remove="remove";
        public static final String Col_change_oil="oil";
        public static final String  [] All_Column ={Col_ID,Col_carowners_id,Col_driver_id,
                Col_daily_cost_morning,Col_daily_cost_night,Col_kilos,Col_decleration,Col_expense,
                Col_date,Col_Total_cost,Col_Driver_Name,Col_Car_name,Col_change_oil};
    }

    public static class driver implements BaseColumns{
        public static final Uri Table_Uri = BaseURI.buildUpon().appendPath(Table_driver).build();
        public static final String Col_ID = "id";
        public static final String Col_name ="name";
        public static final String Col_remove = "remove";
        public static final String  [] All_Column ={Col_ID,Col_name,Col_remove};
    }
//    public static class  user implements BaseColumns{
//        public static final String Table_name = "user";
//        public static final Uri Table_Uri = BaseURI.buildUpon().appendPath(Table_user).build();
//        public static final String COL_ID = "id";
//        public static final String COL_NAME = "name";
//        public static final String COL_PASS="password";
//        public static final String COL_PHONE="phone";
//        public static final String COL_TOKEN = "token";
//        public static final String COL_IMPASS="impassword";
//    }
}
