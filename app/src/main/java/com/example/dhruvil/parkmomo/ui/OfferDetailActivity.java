package com.example.dhruvil.parkmomo.ui;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dhruvil.parkmomo.R;
import com.example.dhruvil.parkmomo.custom_components.CircularImageView;
import com.example.dhruvil.parkmomo.helpers.APIClass;
import com.example.dhruvil.parkmomo.helpers.AppConstants;
import com.example.dhruvil.parkmomo.helpers.PrefUtils;
import com.example.dhruvil.parkmomo.model.ParkingList;
import com.example.dhruvil.parkmomo.model.SaveUserOffer;
import com.example.dhruvil.parkmomo.model.UserClass;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

public class OfferDetailActivity extends ActionBarActivity {

    private TextView txtSecond,txtMinute,txtHour;
    private TextView verify;
    private TextView title,address,description;
    private CircularImageView categoryImage;
    ParkingList parkingList;
    UserClass user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

        parkingList=PrefUtils.getSingleOffer(OfferDetailActivity.this);
        user=PrefUtils.getCurrentUser(OfferDetailActivity.this);

        //find views
        verify= (TextView) findViewById(R.id.verify);
        txtSecond= (TextView) findViewById(R.id.txtSecond);
        txtMinute= (TextView) findViewById(R.id.txtMinute);
        txtHour= (TextView) findViewById(R.id.txtHour);
        categoryImage=(CircularImageView) findViewById(R.id.categoryImage);
        title= (TextView) findViewById(R.id.title);
        address= (TextView) findViewById(R.id.address);
        description= (TextView) findViewById(R.id.description);

        //set values
        Glide.with(OfferDetailActivity.this)
                .load(AppConstants.IMAGE_PATH + parkingList.CampaignArtworks.ImageArt)
                .placeholder(R.drawable.logo)
                .centerCrop()
                .into(categoryImage);

        title.setText(parkingList.CampaignDetails.CampaignTitle);
        address.setText("( "+parkingList.CampaignLocations.Address+" )");
        description.setText(parkingList.CampaignDetails.CampaignDescription);


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(OfferDetailActivity.this, OfferScanActivity.class);
                startActivity(i);
            }
        });
        new CountDownTimer(3600000, 1000) {

            public void onTick(long millisUntilFinished) {
                txtSecond.setText(String.format("%02d", ((millisUntilFinished/1000) % 60)));
                txtMinute.setText(String.format("%02d", ((millisUntilFinished/1000) % 3600) / 60));
                txtHour.setText(String.format("%02d", (millisUntilFinished/1000) / 3600));
            }
            public void onFinish() {

            }
        }.start();
        makeOneImpression();
        showFirstNotification();
        showSecondNotification();

    }


    private void showFirstNotification(){// five minutes
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);
        intent.putExtra("minutes","55 minutes remaining for merchant validation");
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 20000000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000,
                alarmIntent);
    }

    private void showSecondNotification(){// ten minutes
         AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);
        intent.putExtra("minutes","your parking session will end in  10 Minutes. would you like to broadcast?");
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 30000000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 600000,
                alarmIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(PrefUtils.getValidatedOffer(OfferDetailActivity.this)){
            verify.setText("VERIFYING...");
            validatedQRCode();

        } else {
            verify.setEnabled(true);
            verify.setClickable(true);
            verify.setText("VERIFY QR CODE");
        }
    }

    private void validatedQRCode() {
        SaveUserOffer saveUserOffer=PrefUtils.getSaveUserOffer(OfferDetailActivity.this);
        JSONObject offerUser=null;
        JSONObject jsonObject=null;
        try {
            offerUser=new JSONObject();
            offerUser.put("CampaignId", parkingList.CampaignDetails.CampaignId);
            offerUser.put("CustomerId", user.userID);
            offerUser.put("QRCodeMetaString", parkingList.qRcode.QRMetadata+"");
            offerUser.put("OfferUserId", saveUserOffer.offerUser.OfferUserId);
            offerUser.put("VehicleId", "123456");

            jsonObject=new JSONObject();
            jsonObject.put("Latitide", PrefUtils.getLatLng(OfferDetailActivity.this).latitude+"");
            jsonObject.put("Longitude", PrefUtils.getLatLng(OfferDetailActivity.this).longitude+"");
            jsonObject.put("StatusTypeValueId", AppConstants.OPEN);
            jsonObject.put("offerUser", offerUser);
            jsonObject.put("ResponseMsg", "");
            jsonObject.put("ResponseCode", "");
            Log.e("jsonObject", jsonObject + "");


        }catch (Exception e){
            e.printStackTrace();
        }

        new APIClass(AppConstants.SAVE_OFFER,jsonObject){

            @Override
            public void response(String response) {
//                Toast.makeText(OfferDetailActivity.this,response,Toast.LENGTH_LONG).show();
                SaveUserOffer saveUserOffer = new GsonBuilder().create().fromJson(response, SaveUserOffer.class);
                Log.e("response", response + "");
                if(saveUserOffer.ResponseCode.equalsIgnoreCase("SUCCESS")){
                    verify.setText("VERIFIED");
                    verify.setBackgroundColor(getResources().getColor(R.color.green));
                    verify.setClickable(false);
                    verify.setEnabled(false);

                } else {
                    verify.setEnabled(true);
                    verify.setClickable(true);
                    verify.setText("VERIFY QR CODE");
                    Toast.makeText(OfferDetailActivity.this,saveUserOffer.ResponseMsg,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void error(String error) {

                Log.e("response", error+"");
            }
        }.start();
    }

    private void makeOneImpression() {

        JSONObject offerUser=null;
        JSONObject jsonObject=null;
        try {
            offerUser=new JSONObject();
            offerUser.put("CampaignId", parkingList.CampaignDetails.CampaignId);
            offerUser.put("CustomerId", user.userID);
            offerUser.put("QRCodeMetaString", parkingList.qRcode.QRMetadata+"");
            offerUser.put("OfferUserId", 0);
            offerUser.put("VehicleId", "123456");

            jsonObject=new JSONObject();
            jsonObject.put("Latitide", PrefUtils.getLatLng(OfferDetailActivity.this).latitude+"");
            jsonObject.put("Longitude", PrefUtils.getLatLng(OfferDetailActivity.this).longitude+"");
            jsonObject.put("StatusTypeValueId", AppConstants.VALIDATED);
            jsonObject.put("offerUser", offerUser);
            jsonObject.put("ResponseMsg", "");
            jsonObject.put("ResponseCode", "");
            Log.e("jsonObject", jsonObject + "");


        }catch (Exception e){
            e.printStackTrace();
        }

        new APIClass(AppConstants.SAVE_OFFER,jsonObject){

            @Override
            public void response(String response) {
//Toast.makeText(OfferDetailActivity.this,response,Toast.LENGTH_LONG).show();
                Log.e("response", response + "");
                SaveUserOffer saveUserOffer = new GsonBuilder().create().fromJson(response, SaveUserOffer.class);
                PrefUtils.setSaveUserOffer(saveUserOffer,OfferDetailActivity.this);
            }

            @Override
            public void error(String error) {

                Log.e("response", error+"");
            }
        }.start();
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
