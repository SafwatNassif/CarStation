package com.example.safwat.carstation.Adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;
import com.example.safwat.carstation.Activity.Menu;
import com.example.safwat.carstation.Database.Constant;
import com.example.safwat.carstation.Database.Operation;
import com.example.safwat.carstation.Interface.callbackResponse;
import com.example.safwat.carstation.Model.DailyDetailsObject;
import com.example.safwat.carstation.R;
import com.example.safwat.carstation.Utility;
import com.example.safwat.carstation.WebServer.VolleyConnectionRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by safwat on 12/07/17.
 */

public class DailyAdapter extends RecyclerSwipeAdapter<DailyAdapter.dailyHolder> {
    private String LOG_CAT = DailyAdapter.class.getSimpleName(),type;
    private Context context;
    private Menu menu;
    private Operation operation;
    private ArrayList<DailyDetailsObject> dailyDetails;
    private int Resource , position_of_item =0;
    private LinearLayout dailyProgressBarContainer;
    private VolleyConnectionRequest volleyConnectionRequest;
    protected SwipeItemRecyclerMangerImpl itemManger= new SwipeItemRecyclerMangerImpl(this);

    public DailyAdapter(Context c, int resource, ArrayList<DailyDetailsObject> dailyDetails
            , LinearLayout dailyProgressBarContainer,String type) {
        this.context = c;
        this.operation =new Operation(context);
        this.Resource =resource;
        this.dailyDetails = dailyDetails;
        menu = new Menu();
        this.type = type;
        this.dailyProgressBarContainer = dailyProgressBarContainer;
    }

    @Override
    public long getItemId(int position) {
        return dailyDetails.indexOf(position);
    }

    @Override
    public int getItemCount() {
        return dailyDetails.size();
    }

    @Override
    public dailyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(Resource,parent,false);
        dailyHolder holder =new dailyHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final dailyHolder holder, final int position) {

        if (type.equals("daily")) {
            holder.car_name.setText(dailyDetails.get(position).getCarName());
            holder.driver_name.setText(dailyDetails.get(position).getDriverName());
            holder.date.setText(dailyDetails.get(position).getDate());
            holder.total_cost.setText("" + dailyDetails.get(position).getTotal_cost());
        }else if (type.equals("search")){
            holder.driver_name.setText(dailyDetails.get(position).getDriverName());
            if (dailyDetails.get(position).getMorning_cost() == 0.0){
                holder.ShiftTime.setText(context.getResources().getString(R.string.night));
                holder.daily.setText(dailyDetails.get(position).getNight_cost()+"");
            }else if (dailyDetails.get(position).getNight_cost() == 0.0){
                holder.ShiftTime.setText(context.getResources().getString(R.string.morreing));
                holder.daily.setText(dailyDetails.get(position).getMorning_cost()+"");
            }
            holder.expense.setText(dailyDetails.get(position).getExpense_cost()+"   "+dailyDetails.get(position).getDescription());
            holder.total_cost.setText(dailyDetails.get(position).getTotal_cost()+"");
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Utility.visible(dailyProgressBarContainer);
                    position_of_item = holder.getLayoutPosition();
                    deleteFromSqLiteDatabase(position_of_item,holder);
            }
        });
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position_of_item=holder.getLayoutPosition();
                    Bundle bundle = new Bundle();
                    bundle.putString("type",type);
                    bundle.putInt(Constant.daily.Col_ID,dailyDetails.get(position_of_item).getDaily_id());
                    bundle.putString(Constant.daily.Col_Car_name,dailyDetails.get(position_of_item).getCarName());
                    bundle.putString(Constant.daily.Col_Driver_Name,dailyDetails.get(position_of_item).getDriverName());
                    bundle.putInt(Constant.daily.Col_carowners_id,dailyDetails.get(position_of_item).getId_car());
                    bundle.putInt(Constant.daily.Col_driver_id,dailyDetails.get(position_of_item).getId_driver());
                    bundle.putInt(Constant.daily.Col_change_oil,dailyDetails.get(position_of_item).getChange_oil());
                    bundle.putDouble(Constant.daily.Col_kilos,dailyDetails.get(position_of_item).getKilos());
                    bundle.putString(Constant.daily.Col_date,dailyDetails.get(position_of_item).getDate());
                    bundle.putString(Constant.daily.Col_decleration,dailyDetails.get(position_of_item).getDescription());
                    bundle.putDouble(Constant.daily.Col_expense,dailyDetails.get(position_of_item).getExpense_cost());
                    bundle.putDouble(Constant.daily.Col_Total_cost,dailyDetails.get(position_of_item).getTotal_cost());
                    bundle.putDouble(Constant.daily.Col_daily_cost_morning,dailyDetails.get(position_of_item).getMorning_cost());
                    bundle.putDouble(Constant.daily.Col_daily_cost_night,dailyDetails.get(position_of_item).getNight_cost());

                    ((Menu)context).beginTransaction(bundle);
                    }
                }) ;
    }

    private void deleteFromSqLiteDatabase(int position , final dailyHolder holder) {
            int response= operation.RemoveOperation(dailyDetails.get(position).getDaily_id()
                    , Constant.daily.Table_Uri);
            if (response ==1){
                deleteFromRecycleView(holder,position);
            }
        }

    private void deleteFromRecycleView(dailyHolder holder, int positions) {
        itemManger.removeShownLayouts(holder.swipeLayout);
        positions = holder.getLayoutPosition();
        dailyDetails.remove(positions);
        notifyItemRemoved(positions);
        notifyItemRangeChanged(0,dailyDetails.size());
        itemManger.closeAllItems();
        Utility.Gona(dailyProgressBarContainer);
        Toast.makeText(context,context.getResources().getString(R.string.deleteDailyDone)
                ,Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.single_details_item;
    }

    class dailyHolder extends RecyclerView.ViewHolder{
        private TextView ShiftTime,daily,expense;
        private TextView car_name,driver_name,total_cost,date;
        private ImageView delete , edit;
        private SwipeLayout swipeLayout;
        public dailyHolder(View rootView) {
            super(rootView);
            if (type.equals("daily")){
            swipeLayout =(SwipeLayout) itemView.findViewById(R.id.single_details_item);
            car_name = (TextView) rootView.findViewById(R.id.car_name_details_fragment);
            driver_name = (TextView) rootView.findViewById(R.id.driver_name_details_fragment);
            date = (TextView) rootView.findViewById(R.id.date_details_fragment);
            total_cost = (TextView) rootView.findViewById(R.id.cost_details_fragment);
            delete = (ImageView) rootView.findViewById(R.id.delete_in_details_daily);
            edit =(ImageView) rootView.findViewById(R.id.edit_in_details_daily);
            }else if (type.equals("search")){
                swipeLayout =(SwipeLayout) itemView.findViewById(R.id.single_details_item_search);
                driver_name = (TextView) rootView.findViewById(R.id.driver_name_of_search);
                ShiftTime =(TextView) rootView.findViewById(R.id.shift_time);
                daily =(TextView) rootView.findViewById(R.id.daily_search_result);
                expense =(TextView) rootView.findViewById(R.id.cost_search_fragment);
                total_cost =(TextView) rootView.findViewById(R.id.totalCost_search_fragment);
                delete = (ImageView) rootView.findViewById(R.id.delete_in_details_daily);
                edit =(ImageView) rootView.findViewById(R.id.edit_in_details_daily);
            }
        }

    }
}
