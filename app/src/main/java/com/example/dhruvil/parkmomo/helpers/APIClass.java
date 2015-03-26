package com.example.dhruvil.parkmomo.helpers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dhruvil.parkmomo.app.MyApplication;

import org.json.JSONObject;

/**
 * Created by dhruvil on 20-03-2015.
 */
public abstract class APIClass implements IService2 {

    String response = null;
    private String url;
    private JSONObject object;

    public abstract void response(String response);

    public abstract void error(String error);

    public synchronized final APIClass start() {
        call();
        return this;

    }

    public APIClass(String url, JSONObject object) {
        this.url = url;
        this.object = object;
    }

    public void call() {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jobj) {

                try{
//                    if(jobj.getString("ResponseCode").equalsIgnoreCase("SUCCESS")){
                        response(jobj.toString());
//                    }else{
//                        error(jobj.getString("ResponseMsg"));
//                    }
                }catch(Exception e){
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                        error(error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(5000,0,0));
        MyApplication.getInstance().addToRequestQueue(req);
    }
}

