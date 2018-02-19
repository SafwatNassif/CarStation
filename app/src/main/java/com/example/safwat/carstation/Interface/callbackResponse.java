package com.example.safwat.carstation.Interface;

import com.example.safwat.carstation.Model.PersonData;
import com.itextpdf.text.DocumentException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by bishoy on 11/17/16.
 */
public interface callbackResponse {
    public void stringResponse(String responseData);
    public void jsonObjectResponse(JSONObject jsonObject);
    public void jsonArrayResponse(JSONArray jsonArray);

}
