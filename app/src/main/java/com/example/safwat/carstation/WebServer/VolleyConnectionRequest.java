package com.example.safwat.carstation.WebServer;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.safwat.carstation.Interface.callbackResponse;
import com.itextpdf.text.DocumentException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;

/**
 * Created by safwat on 20/08/17.
 */

public class VolleyConnectionRequest {
    private static String  responses="";
    private static JSONObject jsonObjectResponse = null;
    private static JSONArray jsonArrayResponse = null;
    private Context context ;
    private String LOG_CAT= VolleyConnectionRequest.class.getSimpleName();
    public VolleyConnectionRequest(Context c){
        this.context= c;
    }

    public void getStringResponse(String  url, final callbackResponse callback){
        StringRequest request = new StringRequest( Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responses=response;
                Log.v("VolleyRequest",responses);
                callback.stringResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responses=null;
            }
        });

        request.setTag("get_users");
        Volley.newRequestQueue(context).add(request);
    }

    public void getJsonObjectResponse(String url,final callbackResponse jsonCallback){

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                jsonObjectResponse =response;
                jsonCallback.jsonObjectResponse(jsonObjectResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("VolleyRequests",jsonObjectResponse.toString()+"\n"+error.toString());
            }
        });

        jsonRequest.setTag("JsonObjectRequest");
        Volley.newRequestQueue(context).add(jsonRequest);
    }

    public void getJsonArrayResponse(String url,final callbackResponse jsonCallback){

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                jsonArrayResponse=response;
                Log.w("VolleyRequests","jsonArray request is : "+ jsonArrayResponse.toString());
                jsonCallback.jsonArrayResponse(jsonArrayResponse);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                jsonArrayResponse = null;
                jsonCallback.jsonArrayResponse(jsonArrayResponse);
                Log.w(LOG_CAT," there is error in jsonArrayResponse is : " + error.toString());
            }
        });
        jsonArrayRequest.setTag("JsonArrayRequest");
        Volley.newRequestQueue(context).add(jsonArrayRequest);
    }
}
