package com.example.dhruvil.parkmomo.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.dhruvil.parkmomo.R;
import com.example.dhruvil.parkmomo.helpers.AppConstants;
import com.example.dhruvil.parkmomo.helpers.CallWebService;
import com.example.dhruvil.parkmomo.helpers.PrefUtils;
import com.example.dhruvil.parkmomo.model.Offer;
import com.example.dhruvil.parkmomo.model.ParkingList;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class ConfirmActivity extends ActionBarActivity {
    private ArrayList<ParkingList> parkingLists;
    private TextView freePark;
    private ProgressDialog pd;
    private double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        freePark= (TextView) findViewById(R.id.freePark);
        Intent i=getIntent();
        latitude=Double.parseDouble(i.getStringExtra("latitude"));
        longitude=Double.parseDouble(i.getStringExtra("longitude"));
        freePark.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                proessFetchOfferList(latitude+"",longitude+"");

            }
        });


    }


    private void proessFetchOfferList(String latitudeValue,String longitudeValue){
        try{

            pd = ProgressDialog.show(ConfirmActivity.this, "Please Wait", "Loading..", true, false);
            pd.show();

            new CallWebService(AppConstants.OFFER_LIST +latitudeValue+"/"+longitudeValue,CallWebService.TYPE_JSONOBJECT){

                @Override
                public void response(String response) {
                    Log.e("offer list response", response);

                    Offer offerlist = new GsonBuilder().create().fromJson(response,Offer.class);
                    parkingLists=offerlist.Parkinglst;
                    pd.dismiss();
                    PrefUtils.setOffers(offerlist, ConfirmActivity.this);



                    if(parkingLists ==null || parkingLists.isEmpty()) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmActivity.this);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.setMessage("There is no sponsored parking around you.");
                        dialog.show();
                    } else {

                        Intent intent = new Intent(ConfirmActivity.this, OfferlistActivity.class);
                        startActivity(intent);

                    }



                }

                @Override
                public void error(VolleyError error) {
                    Log.e("exc in volly",error.toString());
                    pd.dismiss();
                }
            }.start();

        }catch (Exception e1){
            Log.e("exception", e1.toString());
        }
    }

}
