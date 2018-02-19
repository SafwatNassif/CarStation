package com.example.safwat.carstation.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;
import com.example.safwat.carstation.Database.Operation;
import com.example.safwat.carstation.Model.PersonData;
import com.example.safwat.carstation.R;
import com.example.safwat.carstation.Utility;
import com.example.safwat.carstation.WebServer.VolleyConnectionRequest;

import java.util.ArrayList;

/**
 * Created by safwat on 31/05/17.
 */

public class CarAndDriverAdapter extends RecyclerSwipeAdapter<CarAndDriverAdapter.CarViewHolder>  {

    private String LOG_CAT = CarAndDriverAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<PersonData > data;
    private static  int Resource ,positionOfitem;
    private Uri Table_name;
    private Operation operation;
    private VolleyConnectionRequest volleyConnectionRequest;
    private SharedPreferences sharedPreferences ;
    private LinearLayout ProgressBarContainer;
    protected SwipeItemRecyclerMangerImpl itemManger= new SwipeItemRecyclerMangerImpl(this);

    public CarAndDriverAdapter(@NonNull Context context, @LayoutRes int resource ,
                               ArrayList<PersonData> data, Uri table_name,
                               LinearLayout ProgressParContainer) {
        this.context =context;
        this.operation = new Operation(context);
        this.data =data;
        this.Resource =resource;
        this.Table_name =table_name;
        this.ProgressBarContainer = ProgressParContainer;
    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(Resource, parent ,false);
        CarViewHolder holder = new CarViewHolder(rootView);
        volleyConnectionRequest = new VolleyConnectionRequest(context);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CarViewHolder holder, int position) {
//        sharedPreferences = context.getSharedPreferences(Constant.Table_user, Context.MODE_PRIVATE);
//        final String Token = sharedPreferences.getString(Constant.user.COL_TOKEN,null);
//        final int user_id =sharedPreferences.getInt(Constant.user.COL_ID,0);
        Log.v(LOG_CAT,"position of item is : "+positionOfitem);
        holder.car_name.setText(data.get(position).getName());
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Utility.visible(ProgressBarContainer);
                   positionOfitem = holder.getLayoutPosition();
                   RemoveFromSqLiteDatabase(holder, positionOfitem);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * 1- show dialog to change name --- done
                * 2- update textView with new name --- done
                * 3- update name in specific table --- done
                * 4- make update and delete in background thread -- >done
                * */
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View DialogRootView = LayoutInflater.from(context).inflate(R.layout.dialog_layout,null);
                        final EditText name = (EditText) DialogRootView.findViewById(R.id.edit_name);
                        Button save = (Button) DialogRootView.findViewById(R.id.dialog_save);
                        Button cancel = (Button) DialogRootView.findViewById(R.id.dialog_cancel);
                        builder.setView(DialogRootView);

                        final AlertDialog dialog = builder.create();
                        positionOfitem = holder.getLayoutPosition();
                        dialog.show();
                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String  OldName = data.get(positionOfitem).getName();
                                int car_id =data.get(positionOfitem).getId();
                                String  newName = name.getText().toString();
                                if (newName == null || newName.equals("")){
                                    dialog.dismiss();
                                }else{
                                    updateNameServerDatabase(holder,newName,OldName,car_id);
                                    dialog.dismiss();
                                }
                             }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });


            }
        });

    }

    private void updateNameServerDatabase(CarViewHolder holder, final String newName, final String oldname, final int id) {
//        Uri.Builder builderUri = new Uri.Builder();
//        builderUri.scheme("http")
//                .authority("safwat-android.000webhostapp.com")
//                .appendPath("cars")
//                .appendPath("updateName.php")
//                .appendQueryParameter("old",oldname)
//                .appendQueryParameter("new",newName)
//                .appendQueryParameter(Constant.carOwners.Col_ID, String.valueOf(data.get(position).getId()))
//                .appendQueryParameter("table",Table_name)
//                .appendQueryParameter(Constant.carOwners.Col_user_id ,String.valueOf(user_id))
//                .appendQueryParameter(Constant.carOwners.Col_user_token,Token);
//        String URI = builderUri.build().toString();
//        Log.v(LOG_CAT,"URI of edit is : "+URI );
//        Log.v(LOG_CAT,"id of  item is : "+data.get(position).getId() );
//        volleyConnectionRequest.getStringResponse(URI, new callbackResponse() {
//            @Override
//            public void stringResponse(String responseData) {
//                if (responseData.equals("1")){
//                    Toast.makeText(context,context.getResources().getString(R.string.editNameDone),
//                            Toast.LENGTH_SHORT).show();                }
//            }
//            @Override
//            public void jsonObjectResponse(JSONObject jsonObject) {
//
//            }
//            @Override
//            public void jsonArrayResponse(JSONArray jsonArray) {
//
//             }
//        });
        int response = operation.UpdateCarORDriverOperation(newName,oldname,id,Table_name);
        if (response == 1){
            holder.car_name.setText(newName);
            Toast.makeText(context,context.getResources().getString(R.string.editNameDone),
                    Toast.LENGTH_SHORT).show();
            Utility.Gona(ProgressBarContainer);
        }
    }


    private void RemoveFromSqLiteDatabase(final CarViewHolder holder, final int position) {
       int response= operation.RemoveOperation(data.get(position).getId(),Table_name);
        if (response ==1){
            RemoveFromRecyclerView(holder,position);
        }
    }

    private void RemoveFromRecyclerView(CarViewHolder holder, int position) {
        itemManger.removeShownLayouts(holder.swipeLayout);
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0,data.size());
        itemManger.closeAllItems();
        Utility.Gona(ProgressBarContainer);
        Toast.makeText(context,context.getResources().getString(R.string.deleteNameDone)
                , Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.single_item;
    }


    class CarViewHolder extends RecyclerView.ViewHolder{

        SwipeLayout swipeLayout ;
        ImageView edit , delete ;
        TextView car_name;

        public CarViewHolder(View itemView) {
            super(itemView);
            swipeLayout =(SwipeLayout) itemView.findViewById(R.id.single_item);
            edit =(ImageView) itemView.findViewById(R.id.edit);
            delete =(ImageView) itemView.findViewById(R.id.delete);
            car_name =(TextView) itemView.findViewById(R.id.car_name);
        }
    }

}
