package com.example.safwat.carstation;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.safwat.carstation.Database.Constant;
import com.example.safwat.carstation.Interface.RefreshCallBackCarAndDriver;
import com.example.safwat.carstation.Interface.RefreshCallBackDaily;
import com.example.safwat.carstation.Interface.callbackResponse;
import com.example.safwat.carstation.Model.DailyDetailsObject;
import com.example.safwat.carstation.Model.OilObject;
import com.example.safwat.carstation.Model.PersonData;
import com.example.safwat.carstation.WebServer.VolleyConnectionRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by safwat on 19/09/17.
 */

public class Refresh {
    private static VolleyConnectionRequest volleyConnectionRequest;
    private static String LOG_CAT = Refresh.class.getSimpleName();

//    public static void RefreshAllCarOrDriver(String tableName, int user_id, String user_token
//            , Context context, final RefreshCallBackCarAndDriver callBack) {
//
//        final ArrayList<PersonData> carDataList = new ArrayList<PersonData>();
//        volleyConnectionRequest = new VolleyConnectionRequest(context);
//        Uri.Builder builderUri = new Uri.Builder();
//        builderUri.scheme("http")
//                .authority("safwat-android.000webhostapp.com")
//                .appendPath("cars")
//                .appendPath("selectAll.php")
//                .appendQueryParameter("table",tableName)
//                .appendQueryParameter(Constant.carOwners.Col_user_token,user_token)
//                .appendQueryParameter(Constant.carOwners.Col_user_id, String.valueOf(user_id));
//        String URI =builderUri.build().toString();
//        volleyConnectionRequest.getJsonArrayResponse(URI, new callbackResponse() {
//            @Override
//            public void stringResponse(String responseData) {
//
//            }
//
//            @Override
//            public void jsonObjectResponse(JSONObject jsonObject) {
//
//            }
//
//            @Override
//            public void jsonArrayResponse(JSONArray jsonArray) {
//                PersonData data;
//                if (jsonArray != null) {
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        try {
//                            JSONObject object = jsonArray.getJSONObject(i);
//                            data = new PersonData(object.getString(Constant.carOwners.Col_name),
//                                    object.getInt(Constant.carOwners.Col_ID));
//                            carDataList.add(data);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    callBack.refresh(carDataList);
//                }
//            }
//
//
//        });
//    }


//    public static void RefreshDaily(boolean search,int Month,int Year , int user_id,
//                                    String user_token, Context context,
//                                    final RefreshCallBackDaily callBackDaily){
//
//        final ArrayList<DailyDetailsObject> dailyDetailsObjects = new ArrayList<DailyDetailsObject>();
//        volleyConnectionRequest = new VolleyConnectionRequest(context);
//        Uri.Builder builderUri = new Uri.Builder();
//        if (search){
//            builderUri.scheme("http")
//                    .authority("safwat-android.000webhostapp.com")
//                    .appendPath("cars")
//                    .appendPath("selectAllDailySpecificMonth.php")
//                    .appendQueryParameter(Constant.carOwners.Col_user_token,user_token)
//                    .appendQueryParameter(Constant.carOwners.Col_user_id, String.valueOf(user_id))
//                    .appendQueryParameter("month", String.valueOf(Month))
//                    .appendQueryParameter("year", String.valueOf(Year));
//
//        }else{
//            builderUri.scheme("http")
//                    .authority("safwat-android.000webhostapp.com")
//                    .appendPath("cars")
//                    .appendPath("selectAllDaily.php")
//                    .appendQueryParameter(Constant.carOwners.Col_user_token,user_token)
//                    .appendQueryParameter(Constant.carOwners.Col_user_id, String.valueOf(user_id));
//        }
//        String URI =builderUri.build().toString();
//        Log.v(LOG_CAT,"URL of selectAllDaily is : "+URI);
//        volleyConnectionRequest.getJsonArrayResponse(URI, new callbackResponse() {
//            @Override
//            public void stringResponse(String responseData) {
//
//            }
//
//            @Override
//            public void jsonObjectResponse(JSONObject jsonObject) {
//
//            }
//
//            @Override
//            public void jsonArrayResponse(JSONArray jsonArray)  {
//                Log.v( LOG_CAT ,"response From SelectAllDaily.php is : "+jsonArray);
//                DailyDetailsObject dailyObject;
//                if (jsonArray != null){
//                    for (int i=0;i<jsonArray.length();i++){
//                        try {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            if (jsonObject.getDouble("daily_cost_morning") == 0.0){
//                                dailyObject = new DailyDetailsObject(jsonObject.getInt(Constant.daily.Col_ID),
//                                        jsonObject.getString(Constant.daily.Col_Car_name)
//                                        ,jsonObject.getString(Constant.daily.Col_Driver_Name)
//                                        ,jsonObject.getString(Constant.daily.Col_date)
//                                        ,jsonObject.getInt(Constant.daily.Col_carowners_id)
//                                        ,jsonObject.getInt(Constant.daily.Col_driver_id)
//                                        ,jsonObject.getDouble("daily_cost_night")
//                                        ,jsonObject.getDouble(Constant.daily.Col_Total_cost)
//                                        ,jsonObject.getDouble(Constant.daily.Col_expense)
//                                        ,jsonObject.getDouble(Constant.daily.Col_kilos)
//                                        ,jsonObject.getString(Constant.daily.Col_decleration)
//                                        ,1
//                                        ,jsonObject.getInt(Constant.daily.Col_user_id)
//                                        ,jsonObject.getString(Constant.daily.Col_user_token)
//                                        ,jsonObject.getInt("change_oil"));
//                                dailyDetailsObjects.add(dailyObject);
//                            }else{
//                                dailyObject = new DailyDetailsObject(jsonObject.getInt(Constant.daily.Col_ID),
//                                        jsonObject.getString(Constant.daily.Col_Car_name)
//                                        ,jsonObject.getString(Constant.daily.Col_Driver_Name)
//                                        ,jsonObject.getString(Constant.daily.Col_date)
//                                        ,jsonObject.getInt(Constant.daily.Col_carowners_id)
//                                        ,jsonObject.getInt(Constant.daily.Col_driver_id)
//                                        ,jsonObject.getDouble("daily_cost_morning")
//                                        ,jsonObject.getDouble(Constant.daily.Col_Total_cost)
//                                        ,jsonObject.getDouble(Constant.daily.Col_expense)
//                                        ,jsonObject.getDouble(Constant.daily.Col_kilos)
//                                        ,jsonObject.getString(Constant.daily.Col_decleration)
//                                        ,0
//                                        ,jsonObject.getInt(Constant.daily.Col_user_id)
//                                        ,jsonObject.getString(Constant.daily.Col_user_token)
//                                        ,jsonObject.getInt("change_oil"));
//                                dailyDetailsObjects.add(dailyObject);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    callBackDaily.refresh(dailyDetailsObjects);
//                }
//            }
//        });
//    }
//
//    public static void RefreshOil(int month, int year, int user_id, String user_token, String car_name, Context context
//            , final RefreshCallBackCarAndDriver callbackOfOil){
//
//        final ArrayList<OilObject> carDataList = new ArrayList<OilObject>();
//        volleyConnectionRequest = new VolleyConnectionRequest(context);
//        Uri.Builder builderUri = new Uri.Builder();
//        builderUri.scheme("http")
//                .authority("safwat-android.000webhostapp.com")
//                .appendPath("cars")
//                .appendPath("selectOilDate.php")
//                .appendQueryParameter(Constant.carOwners.Col_user_token,user_token)
//                .appendQueryParameter("month", String.valueOf(month))
//                .appendQueryParameter("year", String.valueOf(year))
//                .appendQueryParameter("car",car_name)
//                .appendQueryParameter(Constant.carOwners.Col_user_id, String.valueOf(user_id));
//        String URI =builderUri.build().toString();
//        Log.v(LOG_CAT,"url of  oil :" +URI);
//        volleyConnectionRequest.getJsonArrayResponse(URI, new callbackResponse() {
//            @Override
//            public void stringResponse(String responseData) {
//
//            }
//
//            @Override
//            public void jsonObjectResponse(JSONObject jsonObject) {
//
//            }
//
//            @Override
//            public void jsonArrayResponse(JSONArray jsonArray) {
//                OilObject data;
//                if (jsonArray != null) {
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        try {
//                            JSONObject object = jsonArray.getJSONObject(i);
//                            data = new OilObject(object.getString("driver_name"),
//                                    object.getString(Constant.daily.Col_date));
//                            carDataList.add(data);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    callbackOfOil.refreshOil(carDataList);
//                }
//            }
//
//
//        });
//
//    }

}
