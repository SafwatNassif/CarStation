package com.example.safwat.carstation.Interface;

import com.example.safwat.carstation.Model.DailyDetailsObject;
import com.example.safwat.carstation.Model.OilObject;
import com.example.safwat.carstation.Model.PersonData;

import java.util.ArrayList;

/**
 * Created by safwat on 19/09/17.
 */

public interface RefreshCallBackDaily {

    void refresh(ArrayList<DailyDetailsObject> result);

}
