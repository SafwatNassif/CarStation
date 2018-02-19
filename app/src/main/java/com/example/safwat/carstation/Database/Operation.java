package com.example.safwat.carstation.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.safwat.carstation.Model.PersonData;

/**
 * Created by safwat on 18/10/17.
 */

public class Operation extends ContentProvider {

    private static final String LOG_CAT = Operation.class.getSimpleName();
    private  Context context;
    private  CreateDatabase createDatabase;
    private SQLiteDatabase sqLiteDatabase;

    public Operation() {
    }

    public Operation(Context context) {
        this.context = context;
    }

    @Override
    public boolean onCreate() {
        createDatabase = new CreateDatabase(getContext());
        return false;
    }


    public  int insertOperation (ContentValues values ,Uri  TableName){
        try {
            Uri response = context.getContentResolver().insert(TableName,values);
            Log.v(LOG_CAT,"the last row number is : "+ response.getLastPathSegment());
            return  1;
        }catch (Exception e){
            Log.e(LOG_CAT,"the Value not inserted success and there is exception : "+ e.toString());
        }
        return  0;
    }

    public int RemoveOperation(int id, Uri TableName){
        try {
                ContentValues values = new ContentValues();
                values.put(Constant.carOwners.Col_remove,"1");
                String where = Constant.carOwners.Col_ID+" =? ";
                int response = context.getContentResolver().update(TableName,values,where,
                        new String[]{String.valueOf(id)});
                Log.v(LOG_CAT,"removed state is : "+ response);
                return  1;

        }catch (Exception e){
            Log.e(LOG_CAT,"the Value not inserted success and there is exception : "+ e.toString());
        }
        return  0;
    }

    public int UpdateCarORDriverOperation(String newName,String OldName,int id,Uri TableName){
       try {
            ContentValues values =new ContentValues();
            values.put(Constant.carOwners.Col_name,newName);
            String where = Constant.carOwners.Col_name+" = ? AND "
                    +Constant.carOwners.Col_ID+" = ? ";
            int response = context.getContentResolver().update(TableName,values,where,
                    new String []{OldName, String.valueOf(id)});
            Log.v(LOG_CAT,"the"+OldName+" updated to  : "+ newName +" and the response is : "+response);
            return 1;
       }catch (Exception e){
           Log.e(LOG_CAT,"the Value not updated success and there is exception : "+ e.toString());
       }
        return 0;
    }
    public int UpdateDailyOperation(ContentValues values,Uri TableName){
        try {
            String where = Constant.daily.Col_ID+" = ?" ;
            int response = context.getContentResolver().update(TableName,values,where,
                    new String []{ ""+values.get(Constant.daily.Col_ID)});
            Log.v(LOG_CAT,"the daily updated success : "+response);
            return 1;
        }catch (Exception e){
            Log.e(LOG_CAT,"the Value not updated success and there is exception : "+ e.toString());
        }
        return 0;
    }
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String LastParameter = uri.getLastPathSegment();
        Cursor responseOfQuery;
        String GroupBy =  null;
        sqLiteDatabase = createDatabase.getReadableDatabase();
        if (LastParameter.equals(Constant.daily.Col_date)){
            GroupBy = Constant.daily.Col_date;
            uri = Constant.daily.Table_Uri;
        }else if(LastParameter.equals(Constant.daily.Col_carowners_id)){
            GroupBy =  Constant.daily.Col_carowners_id;
            uri = Constant.daily.Table_Uri;
        }
        responseOfQuery = sqLiteDatabase.query(uri.getLastPathSegment(),projection,selection
                ,selectionArgs,GroupBy,null,sortOrder);
        responseOfQuery.setNotificationUri(getContext().getContentResolver(),uri);
        return responseOfQuery;
    }
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return uri.getLastPathSegment();
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        sqLiteDatabase= createDatabase.getWritableDatabase();
        try {
            long id = sqLiteDatabase.insert(uri.getLastPathSegment(),null,values);
            Log.v(LOG_CAT,"the values is inserted and the last id is : "+ id);
            getContext().getContentResolver().notifyChange(uri,null);
            return uri.buildUpon().appendPath(""+id).build();
        }catch (Exception e){
            Log.e(LOG_CAT,"there is problem in insert : " + e.toString());
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String whereStatement
            , @Nullable String[] whereArgs) {
        sqLiteDatabase =createDatabase.getWritableDatabase();
        int updateState = sqLiteDatabase.update(uri.getLastPathSegment(),values,whereStatement,whereArgs);
        // this line to notify cursor loader there is change done in data
        getContext().getContentResolver().notifyChange(uri,null);
        return updateState;
    }
}
