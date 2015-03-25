package com.example.dhruvil.parkmomo.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dhruvil.parkmomo.R;
import com.example.dhruvil.parkmomo.custom_components.CircularImageView;
import com.example.dhruvil.parkmomo.helpers.AppConstants;
import com.example.dhruvil.parkmomo.helpers.PrefUtils;
import com.example.dhruvil.parkmomo.model.ParkingList;

public class OfferDetailActivity extends ActionBarActivity {

    private TextView txtSecond,txtMinute,txtHour;
    private TextView verify;
    private TextView title,address,description;
    private CircularImageView categoryImage;
    private ParkingList parkingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);
        parkingList=PrefUtils.getSingleOffer(OfferDetailActivity.this);
        txtSecond= (TextView) findViewById(R.id.txtSecond);
        txtMinute= (TextView) findViewById(R.id.txtMinute);
        txtHour= (TextView) findViewById(R.id.txtHour);
        verify= (TextView) findViewById(R.id.verify);
        categoryImage=(CircularImageView) findViewById(R.id.categoryImage);
        title= (TextView) findViewById(R.id.title);
        address= (TextView) findViewById(R.id.address);
        description= (TextView) findViewById(R.id.description);
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
    }

}
