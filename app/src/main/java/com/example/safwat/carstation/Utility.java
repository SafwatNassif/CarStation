package com.example.safwat.carstation;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.safwat.carstation.Interface.CarOrDriverNameSelected;

import java.util.ArrayList;

/**
 * Created by safwat on 12/07/17.
 */

public class Utility  {
    private static Context context;
    private static CarOrDriverNameSelected carOrDriverNameSelected;

    public Utility(Context c) {
    this.context =c;
    }
    public static  void visible(LinearLayout progress){
        progress.setBackgroundColor(R.color.activeProgress);
        progress.setVisibility(View.VISIBLE);
    }
    public static  void Gona(LinearLayout progress){
        progress.setBackgroundColor(R.color.Default);
        progress.setVisibility(View.GONE);
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static  void showDialog(Context context , final String title , final ArrayList<String> data,
                                   final String type, CarOrDriverNameSelected carOrDriver){
        carOrDriverNameSelected = carOrDriver;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View DialogRootView = LayoutInflater.from(context).inflate(R.layout.grid_view_dialog,null);
        TextView titleOfDialog = (TextView) DialogRootView.findViewById(R.id.titleOfDialog);
        GridView gridView = (GridView) DialogRootView.findViewById(R.id.grid_list_dialog);
        titleOfDialog.setText(title);
        ArrayAdapter adapter = new ArrayAdapter(context,R.layout.car_driver_grid_view_item
                ,R.id.car_driver_item_of_grid_view,data);
        gridView.setAdapter(adapter);

        builder.setView(DialogRootView);

        final AlertDialog dialog = builder.create();
        dialog.show();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (type.equals("car") || type.equals("driver") ){
                    carOrDriverNameSelected.carOrDriverNameSpecified(data.get(position),type,position);
                }
                dialog.dismiss();
            }
        });
    }



}
